package com.entropyteam.entropay.mcp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import com.entropyteam.entropay.employees.dtos.EmployeeDto;
import com.entropyteam.entropay.employees.repositories.PtoRepository;
import com.entropyteam.entropay.employees.repositories.ReimbursementRepository;
import com.entropyteam.entropay.employees.repositories.VacationRepository;
import com.entropyteam.entropay.employees.services.EmployeeService;
import com.entropyteam.entropay.mcp.dtos.PayrollSummary;
import com.entropyteam.entropay.mcp.dtos.RosterEntry;
import com.entropyteam.entropay.mcp.dtos.VacationBalance;

@ExtendWith(MockitoExtension.class)
class PayrollQueryServiceTest {

    @Mock
    private EmployeeService employeeService;
    @Mock
    private PtoRepository ptoRepository;
    @Mock
    private ReimbursementRepository reimbursementRepository;
    @Mock
    private VacationRepository vacationRepository;

    @InjectMocks
    private PayrollQueryService service;

    @Test
    void listPayrollRosterFiltersBySearchTerm() {
        // Given
        when(employeeService.findAllActive(any())).thenReturn(new PageImpl<>(List.of(
                activeEmployee("Alice", "Anderson", "E-001"),
                activeEmployee("Bob", "Brown", "E-002"))));

        // When
        List<RosterEntry> result = service.listPayrollRoster("anders", null);

        // Then
        assertEquals(1, result.size());
        assertEquals("Alice", result.get(0).firstName());
    }

    @Test
    void getVacationBalanceTreatsNullTotalAsZero() {
        // Given
        UUID employeeId = UUID.randomUUID();
        when(vacationRepository.getVacationByYear(employeeId)).thenReturn(List.of());
        when(vacationRepository.getAvailableDays(employeeId)).thenReturn(null);

        // When
        VacationBalance balance = service.getVacationBalance(employeeId);

        // Then
        assertEquals(0, balance.totalAvailableDays());
        assertTrue(balance.byYear().isEmpty());
    }

    @Test
    void getPayrollSummaryAggregatesSalaryByCountry() {
        // Given
        when(employeeService.findAllActive(any())).thenReturn(new PageImpl<>(List.of(
                activeEmployeeInCountry("Argentina", new BigDecimal("3000")),
                activeEmployeeInCountry("Argentina", new BigDecimal("2000")),
                activeEmployeeInCountry("Brazil", new BigDecimal("4000")))));

        // When
        PayrollSummary summary = service.getPayrollSummary(null, null);

        // Then
        assertEquals(3, summary.headcount());
        assertEquals(0, new BigDecimal("9000").compareTo(summary.totalMonthlySalaryUsd()));
        assertEquals(2, summary.byCountry().size());
    }

    private EmployeeDto activeEmployee(String firstName, String lastName, String internalId) {
        EmployeeDto dto = new EmployeeDto();
        dto.setId(UUID.randomUUID());
        dto.setFirstName(firstName);
        dto.setLastName(lastName);
        dto.setInternalId(internalId);
        dto.setActive(true);
        return dto;
    }

    private EmployeeDto activeEmployeeInCountry(String country, BigDecimal salary) {
        EmployeeDto dto = new EmployeeDto();
        dto.setId(UUID.randomUUID());
        dto.setFirstName("First");
        dto.setLastName("Last");
        dto.setActive(true);
        dto.setCountryName(country);
        dto.setSalary(salary);
        return dto;
    }
}
