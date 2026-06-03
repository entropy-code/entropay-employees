package com.entropyteam.entropay.mcp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import com.entropyteam.entropay.employees.dtos.EmployeeDto;
import com.entropyteam.entropay.employees.models.Employee;
import com.entropyteam.entropay.employees.models.Reimbursement;
import com.entropyteam.entropay.employees.models.ReimbursementCategory;
import com.entropyteam.entropay.employees.repositories.ReimbursementRepository;
import com.entropyteam.entropay.employees.services.EmployeeService;
import com.entropyteam.entropay.mcp.dtos.ReimbursementEntry;

@ExtendWith(MockitoExtension.class)
class ReimbursementQueryServiceTest {

    @Mock
    private ReimbursementRepository reimbursementRepository;
    @Mock
    private EmployeeService employeeService;

    private ReimbursementQueryService service() {
        return new ReimbursementQueryService(reimbursementRepository, employeeService);
    }

    /** Stubs the platform search so {@code internalId} resolves to {@code id}. */
    private void stubResolution(String internalId, UUID id) {
        EmployeeDto dto = new EmployeeDto();
        dto.setId(id);
        dto.setInternalId(internalId);
        dto.setActive(true);
        when(employeeService.findAllActive(any())).thenReturn(new PageImpl<>(List.of(dto)));
    }

    @Test
    @DisplayName("list_reimbursements with internalId calls the per-employee finder")
    void listForEmployee() {
        UUID employeeId = UUID.randomUUID();
        stubResolution("E042", employeeId);
        Reimbursement r = newReimbursement(employeeId, LocalDate.of(2025, 3, 1), "Equipment",
                new BigDecimal("250"), "Monitor");
        when(reimbursementRepository.findAllByEmployeeIdAndDateBetweenAndDeletedIsFalse(
                eq(employeeId), any(), any())).thenReturn(List.of(r));

        List<ReimbursementEntry> result = service().listReimbursements("E042",
                LocalDate.of(2025, 1, 1), LocalDate.of(2025, 12, 31));

        assertEquals(1, result.size());
        assertEquals("Equipment", result.get(0).category());
        assertEquals(new BigDecimal("250"), result.get(0).amount());
        verify(reimbursementRepository, never()).findAllBetweenPeriod(any(), any());
    }

    @Test
    @DisplayName("list_reimbursements without internalId returns company-wide entries")
    void listCompanyWide() {
        Reimbursement r = newReimbursement(UUID.randomUUID(), LocalDate.of(2025, 4, 1), "Travel",
                new BigDecimal("500"), "Flight");
        when(reimbursementRepository.findAllBetweenPeriod(any(), any())).thenReturn(List.of(r));

        List<ReimbursementEntry> result = service().listReimbursements(null, null, null);

        assertEquals(1, result.size());
        assertEquals("Travel", result.get(0).category());
        verify(reimbursementRepository, never()).findAllByEmployeeIdAndDateBetweenAndDeletedIsFalse(any(), any(), any());
    }

    @Test
    @DisplayName("list_reimbursements rejects an unknown internalId")
    void rejectsUnknownInternalId() {
        when(employeeService.findAllActive(any())).thenReturn(new PageImpl<>(List.of()));

        assertThrows(IllegalArgumentException.class,
                () -> service().listReimbursements("E999", null, null));
        verify(reimbursementRepository, never()).findAllByEmployeeIdAndDateBetweenAndDeletedIsFalse(any(), any(), any());
        verify(reimbursementRepository, never()).findAllBetweenPeriod(any(), any());
    }

    @Test
    @DisplayName("list_reimbursements sorts results by date desc")
    void listSortedByDateDesc() {
        UUID id = UUID.randomUUID();
        stubResolution("E042", id);
        Reimbursement older = newReimbursement(id, LocalDate.of(2025, 2, 1), "Equipment",
                new BigDecimal("100"), "Older");
        Reimbursement newer = newReimbursement(id, LocalDate.of(2025, 5, 1), "Travel",
                new BigDecimal("300"), "Newer");
        when(reimbursementRepository.findAllByEmployeeIdAndDateBetweenAndDeletedIsFalse(
                eq(id), any(), any())).thenReturn(List.of(older, newer));

        List<ReimbursementEntry> result = service().listReimbursements("E042", null, null);

        assertEquals(2, result.size());
        assertEquals("Newer", result.get(0).comment());
        assertEquals("Older", result.get(1).comment());
    }

    @Test
    @DisplayName("list_reimbursements defaults date range to first-of-year through today")
    void defaultDateRange() {
        when(reimbursementRepository.findAllBetweenPeriod(any(), any())).thenReturn(List.of());

        List<ReimbursementEntry> result = service().listReimbursements(null, null, null);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("list_reimbursements rejects an inverted date range")
    void rejectsInvertedRange() {
        assertThrows(IllegalArgumentException.class, () -> service().listReimbursements(null,
                LocalDate.of(2025, 6, 1), LocalDate.of(2025, 5, 1)));
    }

    @Test
    @DisplayName("list_reimbursements gracefully maps a reimbursement with no category")
    void handlesMissingCategory() {
        UUID id = UUID.randomUUID();
        stubResolution("E042", id);
        Employee employee = new Employee();
        employee.setId(id);
        Reimbursement r = new Reimbursement();
        r.setEmployee(employee);
        r.setAmount(new BigDecimal("50"));
        r.setDate(LocalDate.now());
        r.setComment("No category");
        when(reimbursementRepository.findAllByEmployeeIdAndDateBetweenAndDeletedIsFalse(
                eq(id), any(), any())).thenReturn(List.of(r));

        List<ReimbursementEntry> result = service().listReimbursements("E042", null, null);

        assertEquals(1, result.size());
        assertEquals(null, result.get(0).category());
        assertEquals("No category", result.get(0).comment());
    }

    private Reimbursement newReimbursement(UUID employeeId, LocalDate date, String category,
            BigDecimal amount, String comment) {
        Employee employee = mock(Employee.class);
        lenient().when(employee.getId()).thenReturn(employeeId);
        lenient().when(employee.getFullName()).thenReturn("Employee " + employeeId);
        ReimbursementCategory cat = new ReimbursementCategory();
        cat.setName(category);
        Reimbursement r = new Reimbursement();
        r.setEmployee(employee);
        r.setCategory(cat);
        r.setAmount(amount);
        r.setDate(date);
        r.setComment(comment);
        return r;
    }
}
