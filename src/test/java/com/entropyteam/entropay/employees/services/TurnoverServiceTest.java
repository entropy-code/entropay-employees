package com.entropyteam.entropay.employees.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.entropyteam.entropay.common.ReactAdminParams;
import com.entropyteam.entropay.common.ReactAdminSqlMapper;
import com.entropyteam.entropay.common.ReactAdminSqlParams;
import com.entropyteam.entropay.employees.dtos.TurnoverReportDto;
import com.entropyteam.entropay.employees.models.Client;
import com.entropyteam.entropay.employees.models.Employee;
import com.entropyteam.entropay.employees.models.Project;
import com.entropyteam.entropay.employees.repositories.AssignmentRepository;
import com.entropyteam.entropay.employees.repositories.projections.MonthlyAssignment;

@ExtendWith(MockitoExtension.class)
public class TurnoverServiceTest {

    private static final LocalDate TEST_START_DATE = LocalDate.of(2023, 1, 1);
    private static final LocalDate TEST_END_DATE = LocalDate.of(2023, 3, 31);

    @Mock
    private AssignmentRepository assignmentRepository;

    @Mock
    private ReactAdminSqlMapper sqlMapper;

    @InjectMocks
    private TurnoverService turnoverService;

    private TurnoverReportDto report;
    private ReactAdminParams params;

    @BeforeEach
    void setUp() {
        setupMocks();
        params = ReactAdminParams.createTestInstance(Map.of("startDate", "2023-01-01", "endDate", "2023-3-31"), 0, 100,
                Map.of("id", "ASC"));
        report = turnoverService.generateHierarchicalTurnoverReport(params);
    }

    private void setupMocks() {
        Map<String, String> queryParams = createQueryParams();
        ReactAdminSqlParams sqlParams = new ReactAdminSqlParams(queryParams, 100, 0, "id", "ASC");

        List<Client> clients = createTestClients();
        List<Project> projects = createTestProjects(clients);
        List<Employee> employees = createTestEmployees();
        List<MonthlyAssignment> monthlyAssignments = createTestMonthlyAssignments(clients, projects, employees);

        when(sqlMapper.map(any())).thenReturn(sqlParams);
        when(assignmentRepository.findMonthlyAssignmentBetweenPeriod(any(), any())).thenReturn(monthlyAssignments);
    }

    private Map<String, String> createQueryParams() {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("startDate", TEST_START_DATE.toString());
        queryParams.put("endDate", TEST_END_DATE.toString());
        return queryParams;
    }

    @Test
    void testGenerateHierarchicalTurnoverReport_BasicStructure() {
        assertNotNull(report);
        assertNotNull(report.yearMonths());
        assertEquals(3, report.yearMonths().size());
        assertNotNull(report.clients());
        assertEquals(2, report.clients().size());
    }

    @Test
    void testOverallTurnoverMetrics() {
        // Overall: 7 employees at start, 2 left (Employee 2 and 4), 6 at end including Employee 6 who joined in Feb
        TurnoverReportDto.TurnoverMetrics overall = report.overall();
        assertTurnoverMetrics(overall, 7, 2, 6, new BigDecimal("30.77"));
    }

    @ParameterizedTest
    @MethodSource("provideMonthlyMetricsTestData")
    void testMonthlyTurnoverMetrics(String month, int expectedStart, int expectedLeft, int expectedEnd,
            BigDecimal expectedRate) {
        TurnoverReportDto.TurnoverMetrics monthlyMetrics = report.yearMonths().get(month);
        assertNotNull(monthlyMetrics, "Monthly metrics for " + month + " should not be null");
        assertTurnoverMetrics(monthlyMetrics, expectedStart, expectedLeft, expectedEnd, expectedRate);
    }

    private static Stream<Arguments> provideMonthlyMetricsTestData() {
        return Stream.of(Arguments.of("2023-01", 7, 1, 6, new BigDecimal("15.38")), // January: Employee 4 left
                Arguments.of("2023-02", 7, 1, 6, new BigDecimal("15.38")),
                // February: Employee 2 left, Employee 6 joined
                Arguments.of("2023-03", 6, 0, 6, BigDecimal.ZERO)          // March: No departures
        );
    }

    @ParameterizedTest
    @MethodSource("provideClientMetricsTestData")
    void testClientTurnoverMetrics(String clientName, int expectedStart, int expectedLeft, int expectedEnd,
            BigDecimal expectedRate) {
        TurnoverReportDto.ClientTurnoverDto client = findClientByName(clientName);
        assertNotNull(client, "Client " + clientName + " should exist");
        assertTurnoverMetrics(client.overall(), expectedStart, expectedLeft, expectedEnd, expectedRate);
    }

    private static Stream<Arguments> provideClientMetricsTestData() {
        return Stream.of(Arguments.of("Client 1", 5, 2, 3, new BigDecimal("50.00")),
                // Employee 2 left, Employee 7 moved to Client 2
                Arguments.of("Client 2", 2, 1, 3, new BigDecimal("40.00"))
                // Employee 4 left, Employee 6 joined, Employee 7 moved in
        );
    }

    @ParameterizedTest
    @MethodSource("provideProjectMetricsTestData")
    void testProjectTurnoverMetrics(String clientName, String projectName, int expectedStart, int expectedLeft,
            int expectedEnd, BigDecimal expectedRate) {
        TurnoverReportDto.ClientTurnoverDto client = findClientByName(clientName);
        TurnoverReportDto.ProjectTurnoverDto project = findProjectByName(client, projectName);
        assertNotNull(project, "Project " + projectName + " should exist");
        assertTurnoverMetrics(project.overall(), expectedStart, expectedLeft, expectedEnd, expectedRate);
    }

    private static Stream<Arguments> provideProjectMetricsTestData() {
        return Stream.of(Arguments.of("Client 1", "Project 1", 3, 2, 1, new BigDecimal("100.00")),
                // Employee 2 left, Employee 8 moved to Project 2
                Arguments.of("Client 1", "Project 2", 2, 1, 2, new BigDecimal("50.00")),
                // Employee 7 moved to Client 2, Employee 8 moved in
                Arguments.of("Client 2", "Project 3", 2, 1, 3, new BigDecimal("40.00"))
                // Employee 4 left, Employee 6 joined, Employee 7 moved in
        );
    }

    @ParameterizedTest
    @MethodSource("provideClientMonthlyMetricsTestData")
    void testClientMonthlyMetrics(String clientName, String month, int expectedStart, int expectedLeft, int expectedEnd,
            BigDecimal expectedRate) {
        TurnoverReportDto.ClientTurnoverDto client = findClientByName(clientName);
        TurnoverReportDto.TurnoverMetrics monthlyMetrics = client.yearMonths().get(month);
        assertNotNull(monthlyMetrics, "Monthly metrics for " + clientName + " in " + month + " should not be null");
        assertTurnoverMetrics(monthlyMetrics, expectedStart, expectedLeft, expectedEnd, expectedRate);
    }

    private static Stream<Arguments> provideClientMonthlyMetricsTestData() {
        return Stream.of(
                // Client 1 monthly metrics
                Arguments.of("Client 1", "2023-01", 5, 0, 5, BigDecimal.ZERO),
                Arguments.of("Client 1", "2023-02", 5, 2, 3, new BigDecimal("50.00")),
                // Employee 2 left, Employee 7 moved to Client 2
                Arguments.of("Client 1", "2023-03", 3, 0, 3, BigDecimal.ZERO),

                // Client 2 monthly metrics
                Arguments.of("Client 2", "2023-01", 2, 1, 1, new BigDecimal("66.67")), // Employee 4 left
                Arguments.of("Client 2", "2023-02", 2, 0, 2, BigDecimal.ZERO),         // Employee 6 joined
                Arguments.of("Client 2", "2023-03", 3, 0, 3, BigDecimal.ZERO)          // Employee 7 moved in
        );
    }

    @ParameterizedTest
    @MethodSource("provideProjectMonthlyMetricsTestData")
    void testProjectMonthlyMetrics(String clientName, String projectName, String month, int expectedStart,
            int expectedLeft, int expectedEnd, BigDecimal expectedRate) {
        TurnoverReportDto.ClientTurnoverDto client = findClientByName(clientName);
        TurnoverReportDto.ProjectTurnoverDto project = findProjectByName(client, projectName);
        TurnoverReportDto.TurnoverMetrics monthlyMetrics = project.yearMonths().get(month);
        assertNotNull(monthlyMetrics, "Monthly metrics for " + projectName + " in " + month + " should not be null");
        assertTurnoverMetrics(monthlyMetrics, expectedStart, expectedLeft, expectedEnd, expectedRate);
    }

    private static Stream<Arguments> provideProjectMonthlyMetricsTestData() {
        return Stream.of(
                // Project 1 monthly metrics
                Arguments.of("Client 1", "Project 1", "2023-01", 3, 0, 3, BigDecimal.ZERO),
                Arguments.of("Client 1", "Project 1", "2023-02", 3, 2, 1, new BigDecimal("100.00")),
                // Employee 2 left, Employee 8 moved to Project 2
                Arguments.of("Client 1", "Project 1", "2023-03", 1, 0, 1, BigDecimal.ZERO),

                // Project 2 monthly metrics
                Arguments.of("Client 1", "Project 2", "2023-01", 2, 0, 2, BigDecimal.ZERO),
                Arguments.of("Client 1", "Project 2", "2023-02", 2, 1, 1, new BigDecimal("66.67")),
                // Employee 7 moved to Client 2
                Arguments.of("Client 1", "Project 2", "2023-03", 2, 0, 2, BigDecimal.ZERO),
                // Employee 8 moved in

                // Project 3 monthly metrics
                Arguments.of("Client 2", "Project 3", "2023-01", 2, 1, 1, new BigDecimal("66.67")), // Employee 4 left
                Arguments.of("Client 2", "Project 3", "2023-02", 2, 0, 2, BigDecimal.ZERO),         // Employee 6 joined
                Arguments.of("Client 2", "Project 3", "2023-03", 3, 0, 3, BigDecimal.ZERO)
                // Employee 7 moved in
        );
    }

    // Helper methods

    private void assertTurnoverMetrics(TurnoverReportDto.TurnoverMetrics metrics, int expectedStart, int expectedLeft,
            int expectedEnd, BigDecimal expectedRate) {
        assertEquals(expectedStart, metrics.employeesAtStart(), "Employees at start mismatch");
        assertEquals(expectedLeft, metrics.employeesLeft(), "Employees left mismatch");
        assertEquals(expectedEnd, metrics.employeesAtEnd(), "Employees at end mismatch");
        assertEquals(0, expectedRate.compareTo(metrics.turnoverRate()), "Turnover rate mismatch");
    }

    private TurnoverReportDto.ClientTurnoverDto findClientByName(String clientName) {
        return report.clients().stream().filter(c -> c.name().equals(clientName)).findFirst().orElse(null);
    }

    private TurnoverReportDto.ProjectTurnoverDto findProjectByName(TurnoverReportDto.ClientTurnoverDto client,
            String projectName) {
        return client.projects().stream().filter(p -> p.name().equals(projectName)).findFirst().orElse(null);
    }

    // Test data creation methods (unchanged for brevity)

    private List<Client> createTestClients() {
        List<Client> clients = new ArrayList<>();

        Client client1 = new Client();
        client1.setId(UUID.randomUUID());
        client1.setName("Client 1");

        Client client2 = new Client();
        client2.setId(UUID.randomUUID());
        client2.setName("Client 2");

        clients.add(client1);
        clients.add(client2);

        return clients;
    }

    private List<Project> createTestProjects(List<Client> clients) {
        List<Project> projects = new ArrayList<>();

        // Projects for Client 1
        Project project1 = new Project();
        project1.setId(UUID.randomUUID());
        project1.setName("Project 1");
        project1.setClient(clients.get(0));

        Project project2 = new Project();
        project2.setId(UUID.randomUUID());
        project2.setName("Project 2");
        project2.setClient(clients.get(0));

        // Projects for Client 2
        Project project3 = new Project();
        project3.setId(UUID.randomUUID());
        project3.setName("Project 3");
        project3.setClient(clients.get(1));

        projects.add(project1);
        projects.add(project2);
        projects.add(project3);

        return projects;
    }

    private List<Employee> createTestEmployees() {
        List<Employee> employees = new ArrayList<>();

        for (int i = 1; i <= 8; i++) {
            Employee employee = new Employee();
            employee.setId(UUID.randomUUID());
            employee.setFirstName("Employee");
            employee.setLastName(String.valueOf(i));
            employees.add(employee);
        }

        return employees;
    }

    /**
     * Creates test monthly assignments for the period January 2023 to March 2023.
     * <pre>
     * Test data structure:
     *
     * | Client   | Project  | Employee | Jan 2023 | Feb 2023 | Mar 2023 |
     * |----------|----------|----------|----------|----------|----------|
     * | Client 1 | Project 1| Employee1| Present  | Present  | Present  |
     * | Client 1 | Project 1| Employee2| Present  | Present  | Left     |
     * | Client 1 | Project 2| Employee3| Present  | Present  | Present  |
     * | Client 2 | Project 3| Employee4| Present  | Left     | Left     |
     * | Client 2 | Project 3| Employee5| Present  | Present  | Present  |
     * | Client 2 | Project 3| Employee6| -        | Present  | Present  |
     * | Client 1 | Project 2| Employee7| Present  | Present  | Left     |
     * | Client 2 | Project 3| Employee7| -        | -        | Present  |
     * | Client 1 | Project 1| Employee8| Present  | Present  | Left     |
     * | Client 1 | Project 2| Employee8| -        | -        | Present  |
     * </pre>
     *
     * @param clients List of test clients
     * @param projects List of test projects
     * @param employees List of test employees
     * @return List of MonthlyAssignment test data
     */
    private List<MonthlyAssignment> createTestMonthlyAssignments(List<Client> clients, List<Project> projects,
            List<Employee> employees) {
        List<MonthlyAssignment> monthlyAssignments = new ArrayList<>();

        // Client 1, Project 1
        UUID client1Id = clients.get(0).getId();
        String client1Name = clients.get(0).getName();
        UUID project1Id = projects.get(0).getId();
        String project1Name = projects.get(0).getName();

        // Employee 1 - Present throughout the period (Jan, Feb, Mar)
        UUID employee1Id = employees.get(0).getId();
        monthlyAssignments.add(
                new MonthlyAssignment("2023-01", employee1Id, "EMP001", "Employee", "1", project1Id, project1Name,
                        client1Id, client1Name));
        monthlyAssignments.add(
                new MonthlyAssignment("2023-02", employee1Id, "EMP001", "Employee", "1", project1Id, project1Name,
                        client1Id, client1Name));
        monthlyAssignments.add(
                new MonthlyAssignment("2023-03", employee1Id, "EMP001", "Employee", "1", project1Id, project1Name,
                        client1Id, client1Name));

        // Employee 2 - Left during February
        UUID employee2Id = employees.get(1).getId();
        monthlyAssignments.add(
                new MonthlyAssignment("2023-01", employee2Id, "EMP002", "Employee", "2", project1Id, project1Name,
                        client1Id, client1Name));
        monthlyAssignments.add(
                new MonthlyAssignment("2023-02", employee2Id, "EMP002", "Employee", "2", project1Id, project1Name,
                        client1Id, client1Name));
        // No March assignment for Employee 2 (left)

        // Client 1, Project 2
        UUID project2Id = projects.get(1).getId();
        String project2Name = projects.get(1).getName();

        // Employee 3 - Present throughout the period
        UUID employee3Id = employees.get(2).getId();
        monthlyAssignments.add(
                new MonthlyAssignment("2023-01", employee3Id, "EMP003", "Employee", "3", project2Id, project2Name,
                        client1Id, client1Name));
        monthlyAssignments.add(
                new MonthlyAssignment("2023-02", employee3Id, "EMP003", "Employee", "3", project2Id, project2Name,
                        client1Id, client1Name));
        monthlyAssignments.add(
                new MonthlyAssignment("2023-03", employee3Id, "EMP003", "Employee", "3", project2Id, project2Name,
                        client1Id, client1Name));

        // Client 2, Project 3
        UUID client2Id = clients.get(1).getId();
        String client2Name = clients.get(1).getName();
        UUID project3Id = projects.get(2).getId();
        String project3Name = projects.get(2).getName();

        // Employee 4 - Left during January
        UUID employee4Id = employees.get(3).getId();
        monthlyAssignments.add(
                new MonthlyAssignment("2023-01", employee4Id, "EMP004", "Employee", "4", project3Id, project3Name,
                        client2Id, client2Name));
        // No February or March assignment for Employee 4 (left)

        // Employee 5 - Joined during January
        UUID employee5Id = employees.get(4).getId();
        monthlyAssignments.add(
                new MonthlyAssignment("2023-01", employee5Id, "EMP005", "Employee", "5", project3Id, project3Name,
                        client2Id, client2Name));
        monthlyAssignments.add(
                new MonthlyAssignment("2023-02", employee5Id, "EMP005", "Employee", "5", project3Id, project3Name,
                        client2Id, client2Name));
        monthlyAssignments.add(
                new MonthlyAssignment("2023-03", employee5Id, "EMP005", "Employee", "5", project3Id, project3Name,
                        client2Id, client2Name));

        // Employee 6 - Joined during February
        UUID employee6Id = employees.get(5).getId();
        monthlyAssignments.add(
                new MonthlyAssignment("2023-02", employee6Id, "EMP006", "Employee", "6", project3Id, project3Name,
                        client2Id, client2Name));
        monthlyAssignments.add(
                new MonthlyAssignment("2023-03", employee6Id, "EMP006", "Employee", "6", project3Id, project3Name,
                        client2Id, client2Name));

        // Employee 7 - Moved from Client 1/Project 2 to Client 2/Project 3
        UUID employee7Id = employees.get(6).getId();
        monthlyAssignments.add(
                new MonthlyAssignment("2023-01", employee7Id, "EMP007", "Employee", "7", project2Id, project2Name,
                        client1Id, client1Name));
        monthlyAssignments.add(
                new MonthlyAssignment("2023-02", employee7Id, "EMP007", "Employee", "7", project2Id, project2Name,
                        client1Id, client1Name));
        monthlyAssignments.add(
                new MonthlyAssignment("2023-03", employee7Id, "EMP007", "Employee", "7", project3Id, project3Name,
                        client2Id, client2Name));

        // Employee 8 - Moved from Client 1/Project 1 to Client 1/Project 2
        UUID employee8Id = employees.get(7).getId();
        monthlyAssignments.add(
                new MonthlyAssignment("2023-01", employee8Id, "EMP008", "Employee", "8", project1Id, project1Name,
                        client1Id, client1Name));
        monthlyAssignments.add(
                new MonthlyAssignment("2023-02", employee8Id, "EMP008", "Employee", "8", project1Id, project1Name,
                        client1Id, client1Name));
        monthlyAssignments.add(
                new MonthlyAssignment("2023-03", employee8Id, "EMP008", "Employee", "8", project2Id, project2Name,
                        client1Id, client1Name));

        return monthlyAssignments;
    }
}
