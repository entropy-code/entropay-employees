package com.entropyteam.entropay.employees.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import com.entropyteam.entropay.common.ReactAdminMapper;
import com.entropyteam.entropay.common.ReactAdminParams;
import com.entropyteam.entropay.employees.calendar.CalendarService;
import com.entropyteam.entropay.employees.dtos.ReportDto;
import com.entropyteam.entropay.employees.models.Assignment;
import com.entropyteam.entropay.employees.models.Client;
import com.entropyteam.entropay.employees.models.Company;
import com.entropyteam.entropay.employees.models.Contract;
import com.entropyteam.entropay.employees.models.Country;
import com.entropyteam.entropay.employees.models.Currency;
import com.entropyteam.entropay.employees.models.Employee;
import com.entropyteam.entropay.employees.models.Modality;
import com.entropyteam.entropay.employees.models.Overtime;
import com.entropyteam.entropay.employees.models.PaymentSettlement;
import com.entropyteam.entropay.employees.models.Project;
import com.entropyteam.entropay.employees.models.Pto;
import com.entropyteam.entropay.employees.repositories.AssignmentRepository;
import com.entropyteam.entropay.employees.repositories.EmployeeRepository;
import com.entropyteam.entropay.employees.repositories.HolidayRepository;
import com.entropyteam.entropay.employees.repositories.LeaveTypeRepository;
import com.entropyteam.entropay.employees.repositories.ProjectRepository;
import com.entropyteam.entropay.employees.repositories.PtoRepository;
import com.entropyteam.entropay.employees.repositories.RoleRepository;
import com.entropyteam.entropay.employees.repositories.SeniorityRepository;
import com.entropyteam.entropay.employees.services.MarginService.MarginDto;
import com.fasterxml.jackson.databind.ObjectMapper;


@ExtendWith(MockitoExtension.class)
class MarginServiceTest {

    private static final String START_DATE = "2024-01-01";
    private static final String END_DATE = "2025-04-30";
    private static final String FILTER_JSON = """
            {"startDate":"%s", "endDate":"%s"}""";
    private static final String RANGE_JSON = """
            [%d,%d]""";
    private static final String SORT_JSON = """
            ["%s","%s"]""";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Company COMPANY = buildCompany();
    private static final Country ARGENTINA = buildCountry("Argentina");

    @Mock
    private OvertimeService overtimeService;
    @Mock
    private ContractService contractService;
    @Mock
    AssignmentRepository assignmentRepository;
    @Mock
    EmployeeRepository employeeRepository;
    @Mock
    HolidayService holidayService;
    @Mock
    PtoRepository ptoRepository;
    private MarginService marginService;

    @BeforeEach
    void setUp() {
        ReactAdminMapper mapper = new ReactAdminMapper(OBJECT_MAPPER);

        AssignmentService assignmentService =
                new AssignmentService(assignmentRepository, employeeRepository, Mockito.mock(RoleRepository.class),
                        Mockito.mock(SeniorityRepository.class), Mockito.mock(ProjectRepository.class), mapper,
                        holidayService);

        PtoService ptoService =
                new PtoService(mapper, ptoRepository, employeeRepository, Mockito.mock(LeaveTypeRepository.class),
                        Mockito.mock(HolidayRepository.class), holidayService, Mockito.mock(VacationService.class),
                        Mockito.mock(CalendarService.class));

        marginService = new MarginService(ptoService, overtimeService, assignmentService, contractService, mapper);
    }

    @Test
    void testGenerateMarginReport_WithValidDateRange() {
        ReactAdminParams params =
                new ReactAdminParams(FILTER_JSON.formatted(START_DATE, END_DATE), RANGE_JSON.formatted(0, 20),
                        SORT_JSON.formatted("internalId", "ASC"));

        Employee employee = buildEmployee(UUID.randomUUID(), "E001");
        LocalDate startDate = LocalDate.parse(START_DATE);
        LocalDate endDate = LocalDate.parse(END_DATE);

        Mockito.when(holidayService.getHolidaysByCountry(LocalDate.parse(START_DATE), LocalDate.parse(END_DATE)))
                .thenReturn(argentinaHolidays());

        Mockito.when(contractService.findByDateBetween(startDate, endDate)).thenReturn(
                List.of(buildContract(employee, LocalDate.of(2023, 1, 23), LocalDate.of(2024, 1, 31),
                                BigDecimal.valueOf(2300)),
                        buildContract(employee, LocalDate.of(2024, 2, 1), LocalDate.of(2025, 3, 31),
                                BigDecimal.valueOf(2530)),
                        buildContract(employee, LocalDate.of(2025, 4, 1), null, BigDecimal.valueOf(2800))));

        Mockito.when(ptoRepository.findAllBetweenPeriod(startDate, endDate)).thenReturn(
                List.of(buildPto(employee, LocalDate.of(2024, 2, 23), LocalDate.of(2024, 2, 23), 1),
                        buildPto(employee, LocalDate.of(2024, 8, 12), LocalDate.of(2024, 8, 14), 3),
                        buildPto(employee, LocalDate.of(2025, 3, 7), LocalDate.of(2025, 3, 7), 1),
                        buildPto(employee, LocalDate.of(2025, 4, 3), LocalDate.of(2025, 4, 11), 7)));

        BigDecimal rate2024 = BigDecimal.valueOf(28.56);
        BigDecimal rate2025 = BigDecimal.valueOf(26);
        Assignment assignment2024 = buildAssignment(employee, startDate, startDate.plusYears(1).minusDays(1), rate2024);
        Assignment assignment2025 = buildAssignment(employee, startDate.plusYears(1), null, rate2025);
        Mockito.when(assignmentRepository.findAllBetweenPeriod(startDate, endDate))
                .thenReturn(List.of(assignment2024, assignment2025));

        Mockito.when(overtimeService.findByDateBetween(startDate, endDate)).thenReturn(
                List.of(buildOvertime(employee, assignment2024, LocalDate.of(2024, 11, 15), 4f),
                        buildOvertime(employee, assignment2024, LocalDate.of(2024, 11, 18), 11f),
                        buildOvertime(employee, assignment2024, LocalDate.of(2024, 12, 31), 4f),
                        buildOvertime(employee, assignment2025, LocalDate.of(2025, 1, 8), 3f),
                        buildOvertime(employee, assignment2025, LocalDate.of(2025, 2, 4), 2f),
                        buildOvertime(employee, assignment2025, LocalDate.of(2025, 2, 10), 2f),
                        buildOvertime(employee, assignment2025, LocalDate.of(2025, 2, 28), 4f),
                        buildOvertime(employee, assignment2025, LocalDate.of(2025, 3, 3), 2f)));

        ReportDto<MarginDto> marginDtoReportDto = marginService.generateMarginReport(params);

        List<MarginDto> data =
                marginDtoReportDto.data().stream().sorted(Comparator.comparing(MarginDto::yearMonth)).toList();

        Assertions.assertEquals(16, data.size());
        validateResult(data.get(0), YearMonth.of(2024, 1), rate2024, 176.0, 0d, BigDecimal.valueOf(2300));
        validateResult(data.get(1), YearMonth.of(2024, 2), rate2024, 152.0, 4d, BigDecimal.valueOf(2530));
        validateResult(data.get(2), YearMonth.of(2024, 3), rate2024, 160.0, 0d, BigDecimal.valueOf(2530));
        validateResult(data.get(3), YearMonth.of(2024, 4), rate2024, 160.0, 0d, BigDecimal.valueOf(2530));
        validateResult(data.get(4), YearMonth.of(2024, 5), rate2024, 176.0, 0d, BigDecimal.valueOf(2530));
        validateResult(data.get(5), YearMonth.of(2024, 6), rate2024, 136.0, 0d, BigDecimal.valueOf(2530));
        validateResult(data.get(6), YearMonth.of(2024, 7), rate2024, 176.0, 0d, BigDecimal.valueOf(2530));
        validateResult(data.get(7), YearMonth.of(2024, 8), rate2024, 176.0, 24d, BigDecimal.valueOf(2530));
        validateResult(data.get(8), YearMonth.of(2024, 9), rate2024, 168.0, 0d, BigDecimal.valueOf(2530));
        validateResult(data.get(9), YearMonth.of(2024, 10), rate2024, 176.0, 0d, BigDecimal.valueOf(2530));
        validateResult(data.get(10), YearMonth.of(2024, 11), rate2024, 175.0, 0d, BigDecimal.valueOf(2530));
        validateResult(data.get(11), YearMonth.of(2024, 12), rate2024, 156.0, 0d, BigDecimal.valueOf(2530));
        validateResult(data.get(12), YearMonth.of(2025, 1), rate2025, 179.0, 0d, BigDecimal.valueOf(2530));
        validateResult(data.get(13), YearMonth.of(2025, 2), rate2025, 168.0, 0d, BigDecimal.valueOf(2530));
        validateResult(data.get(14), YearMonth.of(2025, 3), rate2025, 146.0, 4d, BigDecimal.valueOf(2530));
        validateResult(data.get(15), YearMonth.of(2025, 4), rate2025, 160.0, 56d, BigDecimal.valueOf(2800));
    }

    private static void validateResult(MarginDto marginDto, YearMonth yearMonth, BigDecimal rate, double hours,
            double ptoHours, BigDecimal paid) {
        Assertions.assertEquals(yearMonth.toString(), marginDto.yearMonth());
        BigDecimal total = BigDecimal.valueOf(hours - ptoHours).multiply(rate);
        Assertions.assertEquals(rate, marginDto.rate());
        Assertions.assertEquals(hours, marginDto.hours());
        Assertions.assertEquals(ptoHours, marginDto.ptoHours());
        Assertions.assertEquals(total, marginDto.total());
        Assertions.assertEquals(paid, marginDto.paid());
        Assertions.assertEquals(total.subtract(paid), marginDto.margin());
    }

    private static Assignment buildAssignment(Employee employee, LocalDate startDate, LocalDate endDate,
            BigDecimal rate) {
        Client client = new Client();
        client.setId(UUID.randomUUID());
        client.setName("Test client");

        Project project = new Project();
        project.setId(UUID.randomUUID());
        project.setName("Test project");
        project.setClient(client);

        Assignment assignment = new Assignment();
        assignment.setId(UUID.randomUUID());
        assignment.setEmployee(employee);
        assignment.setStartDate(startDate);
        assignment.setEndDate(endDate);
        assignment.setBillableRate(rate);
        assignment.setProject(project);
        return assignment;
    }

    private static Overtime buildOvertime(Employee employee, Assignment assignment, LocalDate date, float hours) {
        Overtime overtime = new Overtime();
        overtime.setId(UUID.randomUUID());
        overtime.setEmployee(employee);
        overtime.setAssignment(assignment);
        overtime.setDate(date);
        overtime.setHours(hours);
        overtime.setDescription("Test overtime");
        return overtime;
    }

    private static Contract buildContract(Employee employee, LocalDate startDate, LocalDate endDate,
            BigDecimal salary) {
        Contract contract = new Contract();
        contract.setId(UUID.randomUUID());
        contract.setCompany(COMPANY);
        contract.setEmployee(employee);
        contract.setStartDate(startDate);
        contract.setEndDate(endDate);
        contract.setActive(true);
        PaymentSettlement paymentSettlement = new PaymentSettlement();
        paymentSettlement.setCurrency(Currency.USD);
        paymentSettlement.setSalary(salary);
        paymentSettlement.setModality(Modality.MONTHLY);
        contract.addPaymentSettlement(paymentSettlement);
        return contract;
    }

    private static Pto buildPto(Employee employee, LocalDate startDate, LocalDate endDate, double days) {
        Pto pto = new Pto();
        pto.setId(UUID.randomUUID());
        pto.setEmployee(employee);
        pto.setStartDate(startDate);
        pto.setEndDate(endDate);
        pto.setDays(days);
        return pto;
    }

    private static Employee buildEmployee(UUID id, String internalId) {
        Employee employee = new Employee();
        employee.setId(id);
        employee.setInternalId(internalId);
        employee.setActive(true);
        employee.setCountry(ARGENTINA);
        return employee;
    }

    private static Company buildCompany() {
        Company company = new Company();
        company.setName("Entropy");
        company.setId(UUID.randomUUID());
        return company;
    }

    private static Country buildCountry(String name) {
        Country country = new Country();
        country.setId(UUID.randomUUID());
        country.setName(name);
        return country;
    }

    private static Map<Country, Set<LocalDate>> argentinaHolidays() {
        return Map.of(ARGENTINA, Set.of(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 2, 12), LocalDate.of(2024, 2, 13),
                LocalDate.of(2024, 3, 29), LocalDate.of(2024, 4, 1), LocalDate.of(2024, 4, 2), LocalDate.of(2024, 5, 1),
                LocalDate.of(2024, 5, 25), LocalDate.of(2024, 6, 17), LocalDate.of(2024, 6, 20),
                LocalDate.of(2024, 6, 21), LocalDate.of(2024, 7, 9), LocalDate.of(2024, 8, 17),
                LocalDate.of(2024, 10, 11), LocalDate.of(2024, 11, 18), LocalDate.of(2024, 12, 24),
                LocalDate.of(2024, 12, 25), LocalDate.of(2024, 12, 31), LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 3, 3), LocalDate.of(2025, 3, 4), LocalDate.of(2025, 3, 24), LocalDate.of(2025, 4, 2),
                LocalDate.of(2025, 4, 18)));
    }
}