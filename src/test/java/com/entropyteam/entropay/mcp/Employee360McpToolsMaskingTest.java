package com.entropyteam.entropay.mcp;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.ApplicationContext;
import com.entropyteam.entropay.auth.AuthConstants;
import com.entropyteam.entropay.common.SpringContext;
import com.entropyteam.entropay.common.sensitiveInformation.SensitiveInformationService;
import com.entropyteam.entropay.employees.dtos.EmployeeDto;
import com.entropyteam.entropay.employees.models.Assignment;
import com.entropyteam.entropay.employees.models.Employee;
import com.entropyteam.entropay.employees.models.Project;
import com.entropyteam.entropay.employees.models.Role;
import com.entropyteam.entropay.employees.models.Seniority;
import com.entropyteam.entropay.employees.repositories.AssignmentRepository;
import com.entropyteam.entropay.employees.repositories.EmployeeFeedbackRepository;
import com.entropyteam.entropay.employees.repositories.ReimbursementRepository;
import com.entropyteam.entropay.employees.repositories.VacationRepository;
import com.entropyteam.entropay.employees.services.EmployeeService;
import com.entropyteam.entropay.mcp.testUtils.McpTestSecurityContext;

/**
 * End-to-end masking checks for the Employee 360 tools. Invokes each tool through Spring AI's
 * {@code MethodToolCallback} (exactly the path an MCP client triggers) and asserts that
 * sensitive numeric fields are masked when the caller is a non-admin viewing an internal
 * employee, and visible otherwise. Parameterized over all four non-admin platform roles.
 */
@ExtendWith(MockitoExtension.class)
class Employee360McpToolsMaskingTest {

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

    private SensitiveInformationService sensitiveInformationService;
    private ToolCallback assignmentsCallback;
    private ToolCallback summaryCallback;

    private static Field springContextField;

    static {
        try {
            springContextField = SpringContext.class.getDeclaredField("context");
            springContextField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    void setUp() throws Exception {
        sensitiveInformationService = new SensitiveInformationService(employeeService);
        ApplicationContext mockCtx = mock(ApplicationContext.class);
        lenient().when(mockCtx.getBean(SensitiveInformationService.class)).thenReturn(sensitiveInformationService);
        springContextField.set(null, mockCtx);

        Employee360QueryService queryService = new Employee360QueryService(employeeService, assignmentRepository,
                employeeFeedbackRepository, vacationRepository, reimbursementRepository);
        Employee360McpTools tools = new Employee360McpTools(queryService);
        ToolCallback[] callbacks = MethodToolCallbackProvider.builder()
                .toolObjects(tools)
                .build()
                .getToolCallbacks();
        assignmentsCallback = pick(callbacks, "list_employee_assignments");
        summaryCallback = pick(callbacks, "get_employee_summary");
    }

    @AfterEach
    void tearDown() throws Exception {
        McpTestSecurityContext.clear();
        springContextField.set(null, null);
    }

    @ParameterizedTest(name = "{0} sees masked billableRate on list_employee_assignments")
    @ValueSource(strings = {
            AuthConstants.ROLE_HR_DIRECTOR,
            AuthConstants.ROLE_MANAGER_HR,
            AuthConstants.ROLE_ANALYST,
            AuthConstants.ROLE_DEVELOPMENT})
    @DisplayName("Non-admin roles see billableRate masked when listing assignments of an internal employee")
    void nonAdminRolesSeeMaskedAssignmentRate(String role) {
        UUID employeeId = UUID.randomUUID();
        when(employeeService.getInternalEmployeeIds()).thenReturn(Set.of(employeeId));
        when(assignmentRepository.findAssignmentByEmployee_IdAndDeletedIsFalse(employeeId))
                .thenReturn(List.of(newAssignment(employeeId, new BigDecimal("100"))));
        McpTestSecurityContext.authenticateWithRoles(role);

        String json = assignmentsCallback.call("{\"employeeId\":\"" + employeeId + "\"}");

        assertTrue(json.contains("\"billableRate\":null"),
                role + " should see masked billableRate. JSON: " + json);
    }

    @Test
    @DisplayName("Admin sees billableRate unmasked when listing assignments of an internal employee")
    void adminSeesUnmaskedAssignmentRate() {
        UUID employeeId = UUID.randomUUID();
        when(assignmentRepository.findAssignmentByEmployee_IdAndDeletedIsFalse(employeeId))
                .thenReturn(List.of(newAssignment(employeeId, new BigDecimal("100"))));
        McpTestSecurityContext.authenticateWithRoles(AuthConstants.ROLE_ADMIN);

        String json = assignmentsCallback.call("{\"employeeId\":\"" + employeeId + "\"}");

        assertTrue(json.contains("\"billableRate\":100"),
                "Admin should see raw billableRate. JSON: " + json);
    }

    @ParameterizedTest(name = "{0} sees masked rate+salary on get_employee_summary")
    @ValueSource(strings = {
            AuthConstants.ROLE_HR_DIRECTOR,
            AuthConstants.ROLE_MANAGER_HR,
            AuthConstants.ROLE_ANALYST,
            AuthConstants.ROLE_DEVELOPMENT})
    @DisplayName("Non-admin roles see currentRate and currentSalary masked on the employee summary")
    void nonAdminRolesSeeMaskedSummary(String role) {
        UUID employeeId = UUID.randomUUID();
        EmployeeDto employee = newEmployee(employeeId, new BigDecimal("80"), new BigDecimal("3000"));
        when(employeeService.findOne(employeeId)).thenReturn(Optional.of(employee));
        when(employeeService.getInternalEmployeeIds()).thenReturn(Set.of(employeeId));
        when(assignmentRepository.findAssignmentByEmployee_IdAndDeletedIsFalse(employeeId))
                .thenReturn(List.of(newAssignment(employeeId, new BigDecimal("80"))));
        when(vacationRepository.getAvailableDays(employeeId)).thenReturn(10);
        when(employeeFeedbackRepository.findAllByEmployee_IdAndDeletedIsFalse(employeeId)).thenReturn(List.of());
        lenient().when(reimbursementRepository
                        .findAllByEmployeeIdAndDateBetweenAndDeletedIsFalse(eq(employeeId), any(), any()))
                .thenReturn(List.of());
        McpTestSecurityContext.authenticateWithRoles(role);

        String json = summaryCallback.call("{\"query\":\"" + employeeId + "\"}");

        assertTrue(json.contains("\"rate\":null"),
                role + " should see masked engagement rate. JSON: " + json);
        assertTrue(json.contains("\"currentSalary\":null"),
                role + " should see masked currentSalary. JSON: " + json);
    }

    @Test
    @DisplayName("Admin sees rate and salary unmasked on the employee summary")
    void adminSeesUnmaskedSummary() {
        UUID employeeId = UUID.randomUUID();
        EmployeeDto employee = newEmployee(employeeId, new BigDecimal("80"), new BigDecimal("3000"));
        when(employeeService.findOne(employeeId)).thenReturn(Optional.of(employee));
        when(assignmentRepository.findAssignmentByEmployee_IdAndDeletedIsFalse(employeeId))
                .thenReturn(List.of(newAssignment(employeeId, new BigDecimal("80"))));
        when(vacationRepository.getAvailableDays(employeeId)).thenReturn(10);
        when(employeeFeedbackRepository.findAllByEmployee_IdAndDeletedIsFalse(employeeId)).thenReturn(List.of());
        lenient().when(reimbursementRepository
                        .findAllByEmployeeIdAndDateBetweenAndDeletedIsFalse(eq(employeeId), any(), any()))
                .thenReturn(List.of());
        McpTestSecurityContext.authenticateWithRoles(AuthConstants.ROLE_ADMIN);

        String json = summaryCallback.call("{\"query\":\"" + employeeId + "\"}");

        assertTrue(json.contains("\"rate\":80"), "Admin should see raw engagement rate. JSON: " + json);
        assertTrue(json.contains("\"currentSalary\":3000"), "Admin should see raw salary. JSON: " + json);
    }

    @Test
    @DisplayName("Non-admin viewing an external employee sees rate and salary unmasked on the summary")
    void nonAdminOnExternalEmployeeUnmaskedSummary() {
        UUID employeeId = UUID.randomUUID();
        EmployeeDto employee = newEmployee(employeeId, new BigDecimal("80"), new BigDecimal("3000"));
        when(employeeService.findOne(employeeId)).thenReturn(Optional.of(employee));
        when(employeeService.getInternalEmployeeIds()).thenReturn(Set.of(UUID.randomUUID()));
        when(assignmentRepository.findAssignmentByEmployee_IdAndDeletedIsFalse(employeeId))
                .thenReturn(List.of(newAssignment(employeeId, new BigDecimal("80"))));
        when(vacationRepository.getAvailableDays(employeeId)).thenReturn(10);
        when(employeeFeedbackRepository.findAllByEmployee_IdAndDeletedIsFalse(employeeId)).thenReturn(List.of());
        lenient().when(reimbursementRepository
                        .findAllByEmployeeIdAndDateBetweenAndDeletedIsFalse(eq(employeeId), any(), any()))
                .thenReturn(List.of());
        McpTestSecurityContext.authenticateWithRoles(AuthConstants.ROLE_ANALYST);

        String json = summaryCallback.call("{\"query\":\"" + employeeId + "\"}");

        assertTrue(json.contains("\"rate\":80"),
                "External-employee engagement rate should not be masked for non-admin. JSON: " + json);
        assertTrue(json.contains("\"currentSalary\":3000"),
                "External-employee salary should not be masked for non-admin. JSON: " + json);
    }

    private ToolCallback pick(ToolCallback[] callbacks, String name) {
        return Arrays.stream(callbacks)
                .filter(cb -> name.equals(cb.getToolDefinition().name()))
                .findFirst()
                .orElseThrow(() -> new AssertionError(name + " callback not advertised"));
    }

    private EmployeeDto newEmployee(UUID id, BigDecimal rate, BigDecimal salary) {
        EmployeeDto dto = new EmployeeDto();
        dto.setId(id);
        dto.setInternalId("INT-1");
        dto.setFirstName("Jane");
        dto.setLastName("Doe");
        dto.setActive(true);
        dto.setRate(rate);
        dto.setSalary(salary);
        return dto;
    }

    private Assignment newAssignment(UUID employeeId, BigDecimal rate) {
        Employee employee = new Employee();
        employee.setId(employeeId);
        Project project = new Project();
        project.setId(UUID.randomUUID());
        Role role = new Role();
        role.setId(UUID.randomUUID());
        role.setName("Engineer");
        Seniority seniority = new Seniority();
        seniority.setId(UUID.randomUUID());
        Assignment a = new Assignment();
        a.setId(UUID.randomUUID());
        a.setEmployee(employee);
        a.setProject(project);
        a.setRole(role);
        a.setSeniority(seniority);
        a.setBillableRate(rate);
        a.setStartDate(LocalDate.now().minusDays(30));
        a.setActive(true);
        return a;
    }
}
