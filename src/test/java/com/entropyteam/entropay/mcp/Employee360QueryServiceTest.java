package com.entropyteam.entropay.mcp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import com.entropyteam.entropay.employees.dtos.AssignmentDto;
import com.entropyteam.entropay.employees.dtos.EmployeeDto;
import com.entropyteam.entropay.employees.models.Assignment;
import com.entropyteam.entropay.employees.models.Client;
import com.entropyteam.entropay.employees.models.Employee;
import com.entropyteam.entropay.employees.models.EmployeeFeedback;
import com.entropyteam.entropay.employees.models.FeedbackSource;
import com.entropyteam.entropay.employees.models.Project;
import com.entropyteam.entropay.employees.models.Reimbursement;
import com.entropyteam.entropay.employees.models.ReimbursementCategory;
import com.entropyteam.entropay.employees.models.Role;
import com.entropyteam.entropay.employees.models.Seniority;
import com.entropyteam.entropay.employees.repositories.AssignmentRepository;
import com.entropyteam.entropay.employees.repositories.EmployeeFeedbackRepository;
import com.entropyteam.entropay.employees.repositories.ReimbursementRepository;
import com.entropyteam.entropay.employees.repositories.VacationRepository;
import com.entropyteam.entropay.employees.services.EmployeeService;
import com.entropyteam.entropay.mcp.dtos.EmployeeSummary;

@ExtendWith(MockitoExtension.class)
class Employee360QueryServiceTest {

    @Mock
    private EmployeeService employeeService;
    @Mock
    private AssignmentRepository assignmentRepository;
    @Mock
    private EmployeeFeedbackRepository employeeFeedbackRepository;
    @Mock
    private VacationRepository vacationRepository;
    @Mock
    private ReimbursementRepository reimbursementRepository;

    private Employee360QueryService service() {
        return new Employee360QueryService(employeeService, assignmentRepository, employeeFeedbackRepository,
                vacationRepository, reimbursementRepository);
    }

    @Test
    @DisplayName("get_employee resolves by UUID directly")
    void getEmployeeByUuid() {
        UUID id = UUID.randomUUID();
        EmployeeDto dto = newEmployee(id, "INT-1", "Jane", "Doe");
        when(employeeService.findOne(id)).thenReturn(Optional.of(dto));

        EmployeeDto result = service().getEmployee(id.toString());

        assertEquals(id, result.getId());
    }

    @Test
    @DisplayName("get_employee resolves by internal ID when UUID parse fails")
    void getEmployeeByInternalId() {
        UUID id = UUID.randomUUID();
        EmployeeDto dto = newEmployee(id, "INT-7", "Jane", "Doe");
        when(employeeService.findAllActive(any())).thenReturn(new PageImpl<>(List.of(dto)));

        EmployeeDto result = service().getEmployee("int-7");

        assertEquals(id, result.getId());
    }

    @Test
    @DisplayName("get_employee resolves by name via the platform search (single DB match)")
    void getEmployeeByName() {
        EmployeeDto match = newEmployee(UUID.randomUUID(), "INT-1", "Maria", "Lopez");
        when(employeeService.findAllActive(any())).thenReturn(new PageImpl<>(List.of(match)));

        EmployeeDto result = service().getEmployee("lopez");

        assertEquals(match.getId(), result.getId());
    }

    @Test
    @DisplayName("get_employee throws an ambiguity error when the search returns several matches")
    void getEmployeeAmbiguous() {
        EmployeeDto first = newEmployee(UUID.randomUUID(), "INT-1", "Maria", "Lopez");
        EmployeeDto second = newEmployee(UUID.randomUUID(), "INT-2", "Mario", "Lopez");
        when(employeeService.findAllActive(any())).thenReturn(new PageImpl<>(List.of(first, second)));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service().getEmployee("lopez"));
        assertTrue(ex.getMessage().contains("Multiple employees match"));
    }

    @Test
    @DisplayName("get_employee prefers an exact internal-ID hit when the search returns several matches")
    void getEmployeePrefersExactInternalId() {
        EmployeeDto exact = newEmployee(UUID.randomUUID(), "INT-2", "Mario", "Lopez");
        EmployeeDto partial = newEmployee(UUID.randomUUID(), "INT-20", "Maria", "Lopez");
        when(employeeService.findAllActive(any())).thenReturn(new PageImpl<>(List.of(partial, exact)));

        EmployeeDto result = service().getEmployee("int-2");

        assertEquals(exact.getId(), result.getId());
    }

    @Test
    @DisplayName("get_employee throws when no match is found")
    void getEmployeeMissing() {
        when(employeeService.findAllActive(any())).thenReturn(new PageImpl<>(List.of()));

        assertThrows(IllegalArgumentException.class, () -> service().getEmployee("nobody"));
    }

    @Test
    @DisplayName("get_employee rejects blank query")
    void getEmployeeBlank() {
        assertThrows(IllegalArgumentException.class, () -> service().getEmployee("   "));
    }

    @Test
    @DisplayName("list_employee_assignments returns assignments sorted by start date desc")
    void listEmployeeAssignments() {
        UUID employeeId = UUID.randomUUID();
        Assignment older = newAssignment(employeeId, LocalDate.of(2022, 1, 1));
        Assignment newer = newAssignment(employeeId, LocalDate.of(2024, 1, 1));
        when(assignmentRepository.findAssignmentByEmployee_IdAndDeletedIsFalse(employeeId))
                .thenReturn(List.of(older, newer));

        List<AssignmentDto> result = service().listEmployeeAssignments(employeeId);

        assertEquals(2, result.size());
        assertEquals(LocalDate.of(2024, 1, 1), result.get(0).startDate());
        assertEquals(LocalDate.of(2022, 1, 1), result.get(1).startDate());
    }

    @Test
    @DisplayName("list_employee_assignments rejects null id")
    void listEmployeeAssignmentsNullId() {
        assertThrows(IllegalArgumentException.class, () -> service().listEmployeeAssignments(null));
    }

    @Test
    @DisplayName("list_employee_feedbacks returns feedbacks sorted by feedback date desc")
    void listEmployeeFeedbacks() {
        UUID employeeId = UUID.randomUUID();
        EmployeeFeedback older = newFeedback(employeeId, LocalDate.of(2023, 1, 1), "Older");
        EmployeeFeedback newer = newFeedback(employeeId, LocalDate.of(2024, 6, 1), "Newer");
        when(employeeFeedbackRepository.findAllByEmployee_IdAndDeletedIsFalse(employeeId))
                .thenReturn(List.of(older, newer));

        List<com.entropyteam.entropay.employees.dtos.FeedbackDto> result = service().listEmployeeFeedbacks(employeeId);

        assertEquals(2, result.size());
        assertEquals("Newer", result.get(0).title());
        assertEquals("Older", result.get(1).title());
    }

    @Test
    @DisplayName("list_employee_feedbacks rejects null id")
    void listEmployeeFeedbacksNullId() {
        assertThrows(IllegalArgumentException.class, () -> service().listEmployeeFeedbacks(null));
    }

    @Test
    @DisplayName("get_employee_summary assembles profile, active engagements, vacation, feedbacks and reimbursements")
    void getEmployeeSummaryAssemblesAll() {
        UUID id = UUID.randomUUID();
        EmployeeDto dto = newEmployee(id, "INT-9", "Sam", "Rivera");
        dto.setSalary(new BigDecimal("3000"));
        dto.setActive(true);
        dto.setCountryName("Argentina");
        dto.setLabourEmail("sam@entropy.com");
        when(employeeService.findOne(id)).thenReturn(Optional.of(dto));

        Assignment assignment = newAssignment(id, LocalDate.of(2024, 1, 1), "Atlas", "ClientCo",
                "Senior Engineer", new BigDecimal("80"));
        when(assignmentRepository.findAssignmentByEmployee_IdAndDeletedIsFalse(id))
                .thenReturn(List.of(assignment));

        EmployeeFeedback feedback = newFeedback(id, LocalDate.of(2025, 1, 1), "Great work");
        Reimbursement reimbursement = newReimbursement(id, LocalDate.now().minusDays(10),
                "Equipment", new BigDecimal("250"), "Monitor");
        when(vacationRepository.getAvailableDays(id)).thenReturn(12);
        when(employeeFeedbackRepository.findAllByEmployee_IdAndDeletedIsFalse(id))
                .thenReturn(List.of(feedback));
        lenient().when(reimbursementRepository
                        .findAllByEmployeeIdAndDateBetweenAndDeletedIsFalse(eq(id), any(), any()))
                .thenReturn(List.of(reimbursement));

        EmployeeSummary summary = service().getEmployeeSummary(id.toString());

        assertEquals(id, summary.id());
        assertEquals("INT-9", summary.internalId());
        assertEquals(new BigDecimal("3000"), summary.currentSalary());
        assertEquals(1, summary.activeEngagements().size());
        EmployeeSummary.ActiveEngagement engagement = summary.activeEngagements().get(0);
        assertEquals("Atlas", engagement.project());
        assertEquals("ClientCo", engagement.client());
        assertEquals("Senior Engineer", engagement.role());
        assertEquals(new BigDecimal("80"), engagement.rate());
        assertEquals(12, summary.vacationBalance());
        assertEquals(1, summary.recentFeedbacks().size());
        assertEquals("Great work", summary.recentFeedbacks().get(0).title());
        assertEquals(1, summary.latestReimbursements().size());
        assertEquals(new BigDecimal("250"), summary.latestReimbursements().get(0).amount());
    }

    @Test
    @DisplayName("get_employee_summary lists every active engagement for a multi-project employee")
    void getEmployeeSummaryListsMultipleActiveEngagements() {
        UUID id = UUID.randomUUID();
        EmployeeDto dto = newEmployee(id, "INT-9", "Sam", "Rivera");
        dto.setSalary(new BigDecimal("3000"));
        when(employeeService.findOne(id)).thenReturn(Optional.of(dto));

        Assignment newer = newAssignment(id, LocalDate.of(2024, 6, 1), "Atlas", "ClientCo",
                "Senior Engineer", new BigDecimal("80"));
        Assignment older = newAssignment(id, LocalDate.of(2023, 1, 1), "Orion", "OtherCo",
                "Tech Lead", new BigDecimal("90"));
        when(assignmentRepository.findAssignmentByEmployee_IdAndDeletedIsFalse(id))
                .thenReturn(List.of(older, newer));
        when(vacationRepository.getAvailableDays(id)).thenReturn(0);
        when(employeeFeedbackRepository.findAllByEmployee_IdAndDeletedIsFalse(id)).thenReturn(List.of());
        lenient().when(reimbursementRepository
                        .findAllByEmployeeIdAndDateBetweenAndDeletedIsFalse(eq(id), any(), any()))
                .thenReturn(List.of());

        EmployeeSummary summary = service().getEmployeeSummary(id.toString());

        assertEquals(2, summary.activeEngagements().size());
        // Sorted by start date desc: the most recent engagement comes first.
        assertEquals("Atlas", summary.activeEngagements().get(0).project());
        assertEquals("Orion", summary.activeEngagements().get(1).project());
    }

    @Test
    @DisplayName("get_employee_summary excludes inactive assignments from active engagements")
    void getEmployeeSummaryExcludesInactiveAssignments() {
        UUID id = UUID.randomUUID();
        EmployeeDto dto = newEmployee(id, "INT-9", "Sam", "Rivera");
        when(employeeService.findOne(id)).thenReturn(Optional.of(dto));

        Assignment active = newAssignment(id, LocalDate.of(2024, 1, 1), "Atlas", "ClientCo",
                "Senior Engineer", new BigDecimal("80"));
        Assignment inactive = newAssignment(id, LocalDate.of(2022, 1, 1), "Legacy", "OldCo",
                "Engineer", new BigDecimal("70"));
        inactive.setActive(false);
        when(assignmentRepository.findAssignmentByEmployee_IdAndDeletedIsFalse(id))
                .thenReturn(List.of(active, inactive));
        when(vacationRepository.getAvailableDays(id)).thenReturn(0);
        when(employeeFeedbackRepository.findAllByEmployee_IdAndDeletedIsFalse(id)).thenReturn(List.of());
        lenient().when(reimbursementRepository
                        .findAllByEmployeeIdAndDateBetweenAndDeletedIsFalse(eq(id), any(), any()))
                .thenReturn(List.of());

        EmployeeSummary summary = service().getEmployeeSummary(id.toString());

        assertEquals(1, summary.activeEngagements().size());
        assertEquals("Atlas", summary.activeEngagements().get(0).project());
    }

    @Test
    @DisplayName("get_employee_summary returns 0 vacation balance when repository yields null")
    void getEmployeeSummaryHandlesNullVacationBalance() {
        UUID id = UUID.randomUUID();
        EmployeeDto dto = newEmployee(id, "INT-1", "A", "B");
        when(employeeService.findOne(id)).thenReturn(Optional.of(dto));
        when(vacationRepository.getAvailableDays(id)).thenReturn(null);
        when(employeeFeedbackRepository.findAllByEmployee_IdAndDeletedIsFalse(id)).thenReturn(List.of());
        lenient().when(reimbursementRepository
                        .findAllByEmployeeIdAndDateBetweenAndDeletedIsFalse(eq(id), any(), any()))
                .thenReturn(List.of());

        EmployeeSummary summary = service().getEmployeeSummary(id.toString());

        assertEquals(0, summary.vacationBalance());
        assertNotNull(summary.recentFeedbacks());
        assertNotNull(summary.latestReimbursements());
    }

    private EmployeeDto newEmployee(UUID id, String internalId, String firstName, String lastName) {
        EmployeeDto dto = new EmployeeDto();
        dto.setId(id);
        dto.setInternalId(internalId);
        dto.setFirstName(firstName);
        dto.setLastName(lastName);
        dto.setActive(true);
        return dto;
    }

    private Assignment newAssignment(UUID employeeId, LocalDate startDate) {
        return newAssignment(employeeId, startDate, null, null, "Engineer", new BigDecimal("100"));
    }

    private Assignment newAssignment(UUID employeeId, LocalDate startDate, String projectName,
            String clientName, String roleName, BigDecimal rate) {
        Employee employee = new Employee();
        employee.setId(employeeId);
        Project project = new Project();
        project.setId(UUID.randomUUID());
        project.setName(projectName);
        if (clientName != null) {
            Client client = new Client();
            client.setName(clientName);
            project.setClient(client);
        }
        Role role = new Role();
        role.setId(UUID.randomUUID());
        role.setName(roleName);
        Seniority seniority = new Seniority();
        seniority.setId(UUID.randomUUID());

        Assignment a = new Assignment();
        a.setId(UUID.randomUUID());
        a.setEmployee(employee);
        a.setProject(project);
        a.setRole(role);
        a.setSeniority(seniority);
        a.setBillableRate(rate);
        a.setStartDate(startDate);
        a.setActive(true);
        return a;
    }

    private EmployeeFeedback newFeedback(UUID employeeId, LocalDate date, String title) {
        Employee employee = mock(Employee.class);
        lenient().when(employee.getId()).thenReturn(employeeId);
        lenient().when(employee.getFullName()).thenReturn("Sam Rivera");
        EmployeeFeedback f = new EmployeeFeedback();
        f.setEmployee(employee);
        f.setFeedbackDate(date);
        f.setSource(FeedbackSource.LEADER);
        f.setTitle(title);
        f.setText("Body of " + title);
        f.setCreatedBy("Manager McManager");
        return f;
    }

    private Reimbursement newReimbursement(UUID employeeId, LocalDate date, String category,
            BigDecimal amount, String comment) {
        Employee employee = new Employee();
        employee.setId(employeeId);
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
