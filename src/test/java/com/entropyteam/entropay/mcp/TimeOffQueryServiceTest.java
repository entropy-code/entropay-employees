package com.entropyteam.entropay.mcp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
import com.entropyteam.entropay.employees.dtos.PtoDto;
import com.entropyteam.entropay.employees.models.Employee;
import com.entropyteam.entropay.employees.models.LeaveType;
import com.entropyteam.entropay.employees.models.Pto;
import com.entropyteam.entropay.employees.models.Status;
import com.entropyteam.entropay.employees.repositories.PtoRepository;
import com.entropyteam.entropay.employees.repositories.VacationRepository;
import com.entropyteam.entropay.employees.repositories.projections.VacationBalanceByYear;
import com.entropyteam.entropay.employees.services.EmployeeService;
import com.entropyteam.entropay.mcp.dtos.VacationBalance;

@ExtendWith(MockitoExtension.class)
class TimeOffQueryServiceTest {

    @Mock
    private PtoRepository ptoRepository;
    @Mock
    private VacationRepository vacationRepository;
    @Mock
    private EmployeeService employeeService;

    private TimeOffQueryService service() {
        return new TimeOffQueryService(ptoRepository, vacationRepository, employeeService);
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
    @DisplayName("get_vacation_balance resolves the internal ID and returns total and per-year breakdown")
    void getVacationBalance() {
        UUID id = UUID.randomUUID();
        stubResolution("E042", id);
        VacationBalanceByYear y2024 = mockYearBalance("2024", 5);
        VacationBalanceByYear y2025 = mockYearBalance("2025", 10);
        when(vacationRepository.getVacationByYear(id)).thenReturn(List.of(y2024, y2025));
        when(vacationRepository.getAvailableDays(id)).thenReturn(15);

        VacationBalance result = service().getVacationBalance("E042");

        assertEquals("E042", result.internalId());
        assertEquals(15, result.totalAvailableDays());
        assertEquals(2, result.byYear().size());
        assertEquals("2024", result.byYear().get(0).year());
        assertEquals(5, result.byYear().get(0).balance());
        assertEquals(10, result.byYear().get(1).balance());
    }

    @Test
    @DisplayName("get_vacation_balance returns 0 when getAvailableDays is null")
    void getVacationBalanceNullTotal() {
        UUID id = UUID.randomUUID();
        stubResolution("E042", id);
        when(vacationRepository.getVacationByYear(id)).thenReturn(List.of());
        when(vacationRepository.getAvailableDays(id)).thenReturn(null);

        VacationBalance result = service().getVacationBalance("E042");

        assertEquals(0, result.totalAvailableDays());
        assertTrue(result.byYear().isEmpty());
    }

    @Test
    @DisplayName("get_vacation_balance rejects a blank internal ID")
    void getVacationBalanceBlankId() {
        assertThrows(IllegalArgumentException.class, () -> service().getVacationBalance(null));
        assertThrows(IllegalArgumentException.class, () -> service().getVacationBalance("  "));
    }

    @Test
    @DisplayName("get_vacation_balance rejects an internal ID that matches no active employee")
    void getVacationBalanceUnknownId() {
        when(employeeService.findAllActive(any())).thenReturn(new PageImpl<>(List.of()));

        assertThrows(IllegalArgumentException.class, () -> service().getVacationBalance("E404"));
    }

    @Test
    @DisplayName("list_employee_ptos returns all PTOs sorted by start date desc when no filters")
    void listEmployeePtosNoFilters() {
        UUID id = UUID.randomUUID();
        stubResolution("E042", id);
        Pto older = newPto(id, LocalDate.of(2023, 6, 1), LocalDate.of(2023, 6, 5), "Vacation");
        Pto newer = newPto(id, LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 3), "Sick");
        when(ptoRepository.findAllByEmployee_IdAndDeletedIsFalse(id)).thenReturn(List.of(older, newer));

        List<PtoDto> result = service().listEmployeePtos("E042", null, null, null);

        assertEquals(2, result.size());
        assertEquals(LocalDate.of(2025, 1, 1), result.get(0).ptoStartDate());
        assertEquals(LocalDate.of(2023, 6, 1), result.get(1).ptoStartDate());
    }

    @Test
    @DisplayName("list_employee_ptos filters by leave type case-insensitive")
    void listEmployeePtosFiltersByLeaveType() {
        UUID id = UUID.randomUUID();
        stubResolution("E042", id);
        Pto vacation = newPto(id, LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 5), "Vacation");
        Pto sick = newPto(id, LocalDate.of(2025, 2, 1), LocalDate.of(2025, 2, 3), "Sick");
        when(ptoRepository.findAllByEmployee_IdAndDeletedIsFalse(id)).thenReturn(List.of(vacation, sick));

        List<PtoDto> result = service().listEmployeePtos("E042", null, null, "vacation");

        assertEquals(1, result.size());
        assertEquals(LocalDate.of(2025, 1, 1), result.get(0).ptoStartDate());
    }

    @Test
    @DisplayName("list_employee_ptos filters by overlapping date range")
    void listEmployeePtosFiltersByDateRange() {
        UUID id = UUID.randomUUID();
        stubResolution("E042", id);
        Pto before = newPto(id, LocalDate.of(2024, 12, 1), LocalDate.of(2024, 12, 5), "Vacation");
        Pto inWindow = newPto(id, LocalDate.of(2025, 3, 1), LocalDate.of(2025, 3, 5), "Vacation");
        Pto after = newPto(id, LocalDate.of(2025, 7, 1), LocalDate.of(2025, 7, 5), "Vacation");
        when(ptoRepository.findAllByEmployee_IdAndDeletedIsFalse(id))
                .thenReturn(List.of(before, inWindow, after));

        List<PtoDto> result = service().listEmployeePtos("E042",
                LocalDate.of(2025, 1, 1), LocalDate.of(2025, 6, 30), null);

        assertEquals(1, result.size());
        assertEquals(LocalDate.of(2025, 3, 1), result.get(0).ptoStartDate());
    }

    @Test
    @DisplayName("list_employee_ptos rejects a blank internal ID")
    void listEmployeePtosBlankId() {
        assertThrows(IllegalArgumentException.class,
                () -> service().listEmployeePtos(null, null, null, null));
        assertThrows(IllegalArgumentException.class,
                () -> service().listEmployeePtos("  ", null, null, null));
    }

    @Test
    @DisplayName("list_employee_ptos rejects an internal ID that matches no active employee")
    void listEmployeePtosUnknownId() {
        when(employeeService.findAllActive(any())).thenReturn(new PageImpl<>(List.of()));

        assertThrows(IllegalArgumentException.class,
                () -> service().listEmployeePtos("E404", null, null, null));
    }

    @Test
    @DisplayName("list_upcoming_ptos defaults to today + 30 days when no range provided")
    void listUpcomingPtosDefaultsWindow() {
        when(ptoRepository.findAllBetweenPeriod(any(), any())).thenReturn(List.of());

        List<PtoDto> result = service().listUpcomingPtos(null, null);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("list_upcoming_ptos returns approved PTOs sorted by start date asc")
    void listUpcomingPtosSorted() {
        UUID id = UUID.randomUUID();
        Pto later = newPto(id, LocalDate.now().plusDays(20), LocalDate.now().plusDays(22), "Vacation");
        Pto sooner = newPto(id, LocalDate.now().plusDays(2), LocalDate.now().plusDays(4), "Vacation");
        when(ptoRepository.findAllBetweenPeriod(any(), any())).thenReturn(List.of(later, sooner));

        List<PtoDto> result = service().listUpcomingPtos(null, null);

        assertEquals(2, result.size());
        assertEquals(sooner.getStartDate(), result.get(0).ptoStartDate());
        assertEquals(later.getStartDate(), result.get(1).ptoStartDate());
    }

    @Test
    @DisplayName("list_upcoming_ptos rejects an end date before the start date")
    void listUpcomingPtosInvertedRange() {
        assertThrows(IllegalArgumentException.class, () -> service().listUpcomingPtos(
                LocalDate.of(2025, 6, 1), LocalDate.of(2025, 5, 1)));
    }

    private VacationBalanceByYear mockYearBalance(String year, int balance) {
        VacationBalanceByYear b = mock(VacationBalanceByYear.class);
        lenient().when(b.getYear()).thenReturn(year);
        lenient().when(b.getBalance()).thenReturn(balance);
        return b;
    }

    private Pto newPto(UUID employeeId, LocalDate startDate, LocalDate endDate, String leaveType) {
        Employee employee = mock(Employee.class);
        lenient().when(employee.getId()).thenReturn(employeeId);
        lenient().when(employee.getFullName()).thenReturn("Test Employee");
        LeaveType type = new LeaveType();
        type.setId(UUID.randomUUID());
        type.setName(leaveType);
        Pto pto = new Pto();
        pto.setEmployee(employee);
        pto.setLeaveType(type);
        pto.setStartDate(startDate);
        pto.setEndDate(endDate);
        pto.setStatus(Status.APPROVED);
        pto.setDays(1.0);
        pto.setLabourHours(8);
        return pto;
    }
}
