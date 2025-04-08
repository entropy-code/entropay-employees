package com.entropyteam.entropay.employees.services;

import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.entropyteam.entropay.auth.SecureObjectService;
import com.entropyteam.entropay.common.ReactAdminMapper;
import com.entropyteam.entropay.common.ReactAdminParams;
import com.entropyteam.entropay.common.ReactAdminSqlMapper;
import com.entropyteam.entropay.employees.calendar.CalendarService;
import com.entropyteam.entropay.employees.dtos.ReportDto;
import com.entropyteam.entropay.employees.models.Assignment;
import com.entropyteam.entropay.employees.models.Client;
import com.entropyteam.entropay.employees.models.Country;
import com.entropyteam.entropay.employees.models.Employee;
import com.entropyteam.entropay.employees.models.Holiday;
import com.entropyteam.entropay.employees.models.LeaveType;
import com.entropyteam.entropay.employees.models.Project;
import com.entropyteam.entropay.employees.models.Pto;
import com.entropyteam.entropay.employees.models.Status;
import com.entropyteam.entropay.employees.repositories.AssignmentRepository;
import com.entropyteam.entropay.employees.repositories.CountryRepository;
import com.entropyteam.entropay.employees.repositories.EmployeeRepository;
import com.entropyteam.entropay.employees.repositories.HolidayRepository;
import com.entropyteam.entropay.employees.repositories.LeaveTypeRepository;
import com.entropyteam.entropay.employees.repositories.ProjectRepository;
import com.entropyteam.entropay.employees.repositories.PtoRepository;
import com.entropyteam.entropay.employees.repositories.RoleRepository;
import com.entropyteam.entropay.employees.repositories.SeniorityRepository;
import com.entropyteam.entropay.employees.services.BillingService.BillingDto;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class BillingServiceTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final ReactAdminSqlMapper REACT_ADMIN_SQL_MAPPER = new ReactAdminSqlMapper(OBJECT_MAPPER);
    private static final UUID ARG_COUNTRY_ID = UUID.fromString("e7cccdc7-b9b8-4017-b0c9-541abc1d6f9d");
    private static final UUID EMPLOYEE_ID = UUID.fromString("123e4567-e89b-12d3-a456-426655440000");
    private static final Country ARG = getArgentina();

    private static final LeaveType LEAVE_TYPE = new LeaveType("PTO");
    private static final BigDecimal RATE = BigDecimal.valueOf(20);

    @Mock
    private AssignmentRepository assignmentRepository;
    @Mock
    private PtoRepository ptoRepository;
    @Mock
    private HolidayRepository holidayRepository;
    @Mock
    private CountryRepository countryRepository;
    @Mock
    private ReactAdminMapper mapper;
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private LeaveTypeRepository leaveTypeRepository;
    @Mock
    private VacationService vacationRepository;
    @Mock
    private CalendarService calendarService;
    @Mock
    private OvertimeService overtimeService;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private SeniorityRepository seniorityRepository;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private SecureObjectService secureObjectService;
    private BillingService billingService;

    @BeforeEach
    void setup() {
        LocalDate startDate = LocalDate.of(2025, 2, 1);
        LocalDate endDate = LocalDate.of(2025, 2, 28);

        when(holidayRepository.findAllBetweenPeriod(startDate, endDate)).thenReturn(createSampleHolidays());
        when(ptoRepository.findAllBetweenPeriod(startDate, endDate)).thenReturn(createSamplePto());
        when(assignmentRepository.findAllBetweenPeriod(startDate, endDate)).thenReturn(createSampleAssignments());

        HolidayService holidayService =
                new HolidayService(holidayRepository, countryRepository, mapper, calendarService);

        PtoService ptoService =
                new PtoService(mapper, ptoRepository, employeeRepository, leaveTypeRepository, holidayRepository,
                        holidayService, vacationRepository, calendarService);

        AssignmentService assignmentService = new AssignmentService(assignmentRepository, employeeRepository,
                roleRepository, seniorityRepository, projectRepository, secureObjectService, mapper, holidayService);

        billingService = new BillingService(assignmentService, ptoService, overtimeService, REACT_ADMIN_SQL_MAPPER);
    }

    @Test
    void testGenerateBillingReportForEmployeeInFebruary() {
        ReactAdminParams params = new ReactAdminParams();
        params.setFilter("{\"startDate\":\"2025-02-01\",\"endDate\":\"2025-02-28\"}\n");
        params.setRange("[0,10]");
        params.setSort("[\"internalId\",\"ASC\"]");

        ReportDto<BillingDto> generatedReport = billingService.generateBillingReport(params);

        List<BillingDto> data = generatedReport.data();

        Assertions.assertEquals(1, data.size());
        BillingDto billingDto = data.getFirst();
        // In February, he is on PTO until 10/02/2025. 6 Weekdays
        Assertions.assertEquals(6 * 8, billingDto.ptoHours());
        // February has 20 week-days, minus 6 PTO, so this employee worked 14 days
        Assertions.assertEquals(14 * 8, billingDto.hours());
        // The rate is 20, so the total is:
        Assertions.assertEquals(BigDecimal.valueOf(14 * 8 * 20.0), billingDto.total());
    }

    private static Employee getEmployee() {
        Employee employee = new Employee();
        employee.setId(EMPLOYEE_ID);
        employee.setCountry(ARG);
        employee.setFirstName("Test");
        employee.setLastName("Employee");
        employee.setInternalId("E007");
        return employee;
    }

    private static List<Pto> createSamplePto() {
        Pto pto = new Pto();
        pto.setStartDate(LocalDate.of(2025, 1, 29));
        pto.setEndDate(LocalDate.of(2025, 2, 10));
        pto.setStatus(Status.APPROVED);
        pto.setDetails("Test intra month pto");
        pto.setDays(9.0);
        pto.setLabourHours(0);
        pto.setEmployee(getEmployee());
        pto.setLeaveType(LEAVE_TYPE);

        return List.of(pto);
    }


    private static List<Holiday> createSampleHolidays() {
        return List.of(
                new Holiday(LocalDate.of(2025, 1, 1), "New Year", ARG),
                new Holiday(LocalDate.of(2025, 3, 4), "Carnival 1", ARG),
                new Holiday(LocalDate.of(2025, 3, 5), "Carnival 2", ARG),
                new Holiday(LocalDate.of(2025, 3, 24), "Dia de la Memoria", ARG)
        );
    }

    private static List<Assignment> createSampleAssignments() {
        Client client = new Client();
        client.setId(UUID.randomUUID());
        client.setName("Test client");

        Project project = new Project();
        project.setId(UUID.randomUUID());
        project.setName("Test project");
        project.setClient(client);

        Assignment assignment = new Assignment();
        assignment.setId(UUID.randomUUID());
        assignment.setStartDate(LocalDate.of(2025, 1, 1));
        assignment.setEmployee(getEmployee());
        assignment.setBillableRate(RATE);
        assignment.setProject(project);

        return List.of(assignment);
    }

    private static Country getArgentina() {
        Country country = new Country();
        country.setId(ARG_COUNTRY_ID);
        country.setName("Argentina");
        return country;
    }
}