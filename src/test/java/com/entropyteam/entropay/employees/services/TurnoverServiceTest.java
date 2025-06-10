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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

    @Mock
    private AssignmentRepository assignmentRepository;

    @Mock
    private ReactAdminSqlMapper sqlMapper;

    @InjectMocks
    private TurnoverService turnoverService;

    @BeforeEach
    void setUp() {
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 3, 31);

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("startDate", startDate.toString());
        queryParams.put("endDate", endDate.toString());

        // Create ReactAdminSqlParams with the necessary fields
        ReactAdminSqlParams sqlParams = new ReactAdminSqlParams(queryParams, 10, 0, "id", "ASC");

        // Initialize test data
        List<Client> clients = createTestClients();
        List<Project> projects = createTestProjects(clients);
        List<Employee> employees = createTestEmployees();

        // Create monthly assignments for testing
        List<MonthlyAssignment> monthlyAssignments = createTestMonthlyAssignments(clients, projects, employees);

        // Mock the sqlMapper to return the sqlParams
        when(sqlMapper.map(any())).thenReturn(sqlParams);

        // Mock the assignmentRepository to return the monthly assignments
        when(assignmentRepository.findMonthlyAssignmentBetweenPeriod(any(), any()))
                .thenReturn(monthlyAssignments);
    }

    // Helper methods to create test data

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

        for (int i = 1; i <= 6; i++) {
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
        monthlyAssignments.add(new MonthlyAssignment(
                "2023-01", employee1Id, "EMP001", "Employee", "1",
                project1Id, project1Name, client1Id, client1Name));
        monthlyAssignments.add(new MonthlyAssignment(
                "2023-02", employee1Id, "EMP001", "Employee", "1",
                project1Id, project1Name, client1Id, client1Name));
        monthlyAssignments.add(new MonthlyAssignment(
                "2023-03", employee1Id, "EMP001", "Employee", "1",
                project1Id, project1Name, client1Id, client1Name));

        // Employee 2 - Left during February
        UUID employee2Id = employees.get(1).getId();
        monthlyAssignments.add(new MonthlyAssignment(
                "2023-01", employee2Id, "EMP002", "Employee", "2",
                project1Id, project1Name, client1Id, client1Name));
        monthlyAssignments.add(new MonthlyAssignment(
                "2023-02", employee2Id, "EMP002", "Employee", "2",
                project1Id, project1Name, client1Id, client1Name));
        // No March assignment for Employee 2 (left)

        // Client 1, Project 2
        UUID project2Id = projects.get(1).getId();
        String project2Name = projects.get(1).getName();

        // Employee 3 - Present throughout the period
        UUID employee3Id = employees.get(2).getId();
        monthlyAssignments.add(new MonthlyAssignment(
                "2023-01", employee3Id, "EMP003", "Employee", "3",
                project2Id, project2Name, client1Id, client1Name));
        monthlyAssignments.add(new MonthlyAssignment(
                "2023-02", employee3Id, "EMP003", "Employee", "3",
                project2Id, project2Name, client1Id, client1Name));
        monthlyAssignments.add(new MonthlyAssignment(
                "2023-03", employee3Id, "EMP003", "Employee", "3",
                project2Id, project2Name, client1Id, client1Name));

        // Client 2, Project 3
        UUID client2Id = clients.get(1).getId();
        String client2Name = clients.get(1).getName();
        UUID project3Id = projects.get(2).getId();
        String project3Name = projects.get(2).getName();

        // Employee 4 - Left during January
        UUID employee4Id = employees.get(3).getId();
        monthlyAssignments.add(new MonthlyAssignment(
                "2023-01", employee4Id, "EMP004", "Employee", "4",
                project3Id, project3Name, client2Id, client2Name));
        // No February or March assignment for Employee 4 (left)

        // Employee 5 - Joined during January
        UUID employee5Id = employees.get(4).getId();
        monthlyAssignments.add(new MonthlyAssignment(
                "2023-01", employee5Id, "EMP005", "Employee", "5",
                project3Id, project3Name, client2Id, client2Name));
        monthlyAssignments.add(new MonthlyAssignment(
                "2023-02", employee5Id, "EMP005", "Employee", "5",
                project3Id, project3Name, client2Id, client2Name));
        monthlyAssignments.add(new MonthlyAssignment(
                "2023-03", employee5Id, "EMP005", "Employee", "5",
                project3Id, project3Name, client2Id, client2Name));

        // Employee 6 - Joined during February
        UUID employee6Id = employees.get(5).getId();
        monthlyAssignments.add(new MonthlyAssignment(
                "2023-02", employee6Id, "EMP006", "Employee", "6",
                project3Id, project3Name, client2Id, client2Name));
        monthlyAssignments.add(new MonthlyAssignment(
                "2023-03", employee6Id, "EMP006", "Employee", "6",
                project3Id, project3Name, client2Id, client2Name));

        return monthlyAssignments;
    }

    @Test
    void testGenerateHierarchicalTurnoverReport() {
        // Call the method under test
        TurnoverReportDto report = turnoverService.generateHierarchicalTurnoverReport(new ReactAdminParams());

        // Verify the report is not null
        assertNotNull(report);

        // Verify overall metrics
        // 5 employees at start (Jan), 2 left (Employee 2 and 4), 4 at end (Mar) including Employee 6 who joined in Feb
        assertEquals(5, report.overall().employeesAtStart());
        assertEquals(2, report.overall().employeesLeft());
        assertEquals(4, report.overall().employeesAtEnd());
        assertEquals(new BigDecimal("44.44"), report.overall().turnoverRate());

        // Verify monthly metrics
        Map<String, TurnoverReportDto.TurnoverMetrics> monthlyMetrics = report.yearMonths();
        assertNotNull(monthlyMetrics);
        assertEquals(3, monthlyMetrics.size());

        // January: 5 employees, 1 left (Employee 4), 4 at end
        TurnoverReportDto.TurnoverMetrics janMetrics = monthlyMetrics.get("2023-01");
        assertNotNull(janMetrics);
        assertEquals(5, janMetrics.employeesAtStart());
        assertEquals(1, janMetrics.employeesLeft());
        assertEquals(4, janMetrics.employeesAtEnd());
        assertEquals(new BigDecimal("22.22"), janMetrics.turnoverRate());

        // February: 5 employees (4 + Employee 6), 1 left (Employee 2), 4 at end
        TurnoverReportDto.TurnoverMetrics febMetrics = monthlyMetrics.get("2023-02");
        assertNotNull(febMetrics);
        assertEquals(5, febMetrics.employeesAtStart());
        assertEquals(1, febMetrics.employeesLeft());
        assertEquals(4, febMetrics.employeesAtEnd());
        assertEquals(new BigDecimal("22.22"), febMetrics.turnoverRate());

        // March: 4 employees, 0 left, 4 at end
        TurnoverReportDto.TurnoverMetrics marMetrics = monthlyMetrics.get("2023-03");
        assertNotNull(marMetrics);
        assertEquals(4, marMetrics.employeesAtStart());
        assertEquals(0, marMetrics.employeesLeft());
        assertEquals(4, marMetrics.employeesAtEnd());
        assertEquals(BigDecimal.ZERO, marMetrics.turnoverRate());

        // Verify client metrics
        List<TurnoverReportDto.ClientTurnoverDto> clients = report.clients();
        assertNotNull(clients);
        assertEquals(2, clients.size());

        // Client 1: 3 employees at start, 1 left (Employee 2), 2 at end
        TurnoverReportDto.ClientTurnoverDto client1 = clients.stream()
                .filter(c -> c.name().equals("Client 1"))
                .findFirst()
                .orElse(null);
        assertNotNull(client1);
        assertEquals(3, client1.overall().employeesAtStart());
        assertEquals(1, client1.overall().employeesLeft());
        assertEquals(2, client1.overall().employeesAtEnd());
        assertEquals(new BigDecimal("40.00"), client1.overall().turnoverRate());

        // Client 2: 2 employees at start, 1 left (Employee 4), 2 at end (including Employee 6 who joined in Feb)
        TurnoverReportDto.ClientTurnoverDto client2 = clients.stream()
                .filter(c -> c.name().equals("Client 2"))
                .findFirst()
                .orElse(null);
        assertNotNull(client2);
        assertEquals(2, client2.overall().employeesAtStart());
        assertEquals(1, client2.overall().employeesLeft());
        assertEquals(2, client2.overall().employeesAtEnd());
        assertEquals(new BigDecimal("50.00"), client2.overall().turnoverRate());

        // Verify project metrics
        // Project 1: 2 employees at start, 1 left (Employee 2), 1 at end
        TurnoverReportDto.ProjectTurnoverDto project1 = client1.projects().stream()
                .filter(p -> p.name().equals("Project 1"))
                .findFirst()
                .orElse(null);
        assertNotNull(project1);
        assertEquals(2, project1.overall().employeesAtStart());
        assertEquals(1, project1.overall().employeesLeft());
        assertEquals(1, project1.overall().employeesAtEnd());
        assertEquals(new BigDecimal("66.67"), project1.overall().turnoverRate());

        // Project 2: 1 employee at start, 0 left, 1 at end
        TurnoverReportDto.ProjectTurnoverDto project2 = client1.projects().stream()
                .filter(p -> p.name().equals("Project 2"))
                .findFirst()
                .orElse(null);
        assertNotNull(project2);
        assertEquals(1, project2.overall().employeesAtStart());
        assertEquals(0, project2.overall().employeesLeft());
        assertEquals(1, project2.overall().employeesAtEnd());
        assertEquals(0, project2.overall().turnoverRate().compareTo(BigDecimal.ZERO));

        // Project 3: 2 employees at start, 1 left (Employee 4), 2 at end (including Employee 6 who joined in Feb)
        TurnoverReportDto.ProjectTurnoverDto project3 = client2.projects().stream()
                .filter(p -> p.name().equals("Project 3"))
                .findFirst()
                .orElse(null);
        assertNotNull(project3);
        assertEquals(2, project3.overall().employeesAtStart());
        assertEquals(1, project3.overall().employeesLeft());
        assertEquals(2, project3.overall().employeesAtEnd());
        assertEquals(new BigDecimal("50.00"), project3.overall().turnoverRate());

        // Verify client yearMonths metrics
        // Client 1
        Map<String, TurnoverReportDto.TurnoverMetrics> client1MonthlyMetrics = client1.yearMonths();
        assertNotNull(client1MonthlyMetrics);
        assertEquals(3, client1MonthlyMetrics.size());

        // January: 3 employees, 0 left, 3 at end
        TurnoverReportDto.TurnoverMetrics client1JanMetrics = client1MonthlyMetrics.get("2023-01");
        assertNotNull(client1JanMetrics);
        assertEquals(3, client1JanMetrics.employeesAtStart());
        assertEquals(0, client1JanMetrics.employeesLeft());
        assertEquals(3, client1JanMetrics.employeesAtEnd());
        assertEquals(0, client1JanMetrics.turnoverRate().compareTo(BigDecimal.ZERO));

        // February: 3 employees, 1 left (Employee 2), 2 at end
        TurnoverReportDto.TurnoverMetrics client1FebMetrics = client1MonthlyMetrics.get("2023-02");
        assertNotNull(client1FebMetrics);
        assertEquals(3, client1FebMetrics.employeesAtStart());
        assertEquals(1, client1FebMetrics.employeesLeft());
        assertEquals(2, client1FebMetrics.employeesAtEnd());
        assertEquals(new BigDecimal("40.00"), client1FebMetrics.turnoverRate());

        // March: 2 employees, 0 left, 2 at end
        TurnoverReportDto.TurnoverMetrics client1MarMetrics = client1MonthlyMetrics.get("2023-03");
        assertNotNull(client1MarMetrics);
        assertEquals(2, client1MarMetrics.employeesAtStart());
        assertEquals(0, client1MarMetrics.employeesLeft());
        assertEquals(2, client1MarMetrics.employeesAtEnd());
        assertEquals(0, client1MarMetrics.turnoverRate().compareTo(BigDecimal.ZERO));

        // Client 2
        Map<String, TurnoverReportDto.TurnoverMetrics> client2MonthlyMetrics = client2.yearMonths();
        assertNotNull(client2MonthlyMetrics);
        assertEquals(3, client2MonthlyMetrics.size());

        // January: 2 employees, 1 left (Employee 4), 1 at end
        TurnoverReportDto.TurnoverMetrics client2JanMetrics = client2MonthlyMetrics.get("2023-01");
        assertNotNull(client2JanMetrics);
        assertEquals(2, client2JanMetrics.employeesAtStart());
        assertEquals(1, client2JanMetrics.employeesLeft());
        assertEquals(1, client2JanMetrics.employeesAtEnd());
        assertEquals(new BigDecimal("66.67"), client2JanMetrics.turnoverRate());

        // February: 2 employees (1 + Employee 6), 0 left, 2 at end
        TurnoverReportDto.TurnoverMetrics client2FebMetrics = client2MonthlyMetrics.get("2023-02");
        assertNotNull(client2FebMetrics);
        assertEquals(2, client2FebMetrics.employeesAtStart());
        assertEquals(0, client2FebMetrics.employeesLeft());
        assertEquals(2, client2FebMetrics.employeesAtEnd());
        assertEquals(0, client2FebMetrics.turnoverRate().compareTo(BigDecimal.ZERO));

        // March: 2 employees, 0 left, 2 at end
        TurnoverReportDto.TurnoverMetrics client2MarMetrics = client2MonthlyMetrics.get("2023-03");
        assertNotNull(client2MarMetrics);
        assertEquals(2, client2MarMetrics.employeesAtStart());
        assertEquals(0, client2MarMetrics.employeesLeft());
        assertEquals(2, client2MarMetrics.employeesAtEnd());
        assertEquals(0, client2MarMetrics.turnoverRate().compareTo(BigDecimal.ZERO));

        // Verify project yearMonths metrics
        // Project 1
        Map<String, TurnoverReportDto.TurnoverMetrics> project1MonthlyMetrics = project1.yearMonths();
        assertNotNull(project1MonthlyMetrics);
        assertEquals(3, project1MonthlyMetrics.size());

        // January: 2 employees, 0 left, 2 at end
        TurnoverReportDto.TurnoverMetrics project1JanMetrics = project1MonthlyMetrics.get("2023-01");
        assertNotNull(project1JanMetrics);
        assertEquals(2, project1JanMetrics.employeesAtStart());
        assertEquals(0, project1JanMetrics.employeesLeft());
        assertEquals(2, project1JanMetrics.employeesAtEnd());
        assertEquals(0, project1JanMetrics.turnoverRate().compareTo(BigDecimal.ZERO));

        // February: 2 employees, 1 left (Employee 2), 1 at end
        TurnoverReportDto.TurnoverMetrics project1FebMetrics = project1MonthlyMetrics.get("2023-02");
        assertNotNull(project1FebMetrics);
        assertEquals(2, project1FebMetrics.employeesAtStart());
        assertEquals(1, project1FebMetrics.employeesLeft());
        assertEquals(1, project1FebMetrics.employeesAtEnd());
        assertEquals(new BigDecimal("66.67"), project1FebMetrics.turnoverRate());

        // March: 1 employee, 0 left, 1 at end
        TurnoverReportDto.TurnoverMetrics project1MarMetrics = project1MonthlyMetrics.get("2023-03");
        assertNotNull(project1MarMetrics);
        assertEquals(1, project1MarMetrics.employeesAtStart());
        assertEquals(0, project1MarMetrics.employeesLeft());
        assertEquals(1, project1MarMetrics.employeesAtEnd());
        assertEquals(0, project1MarMetrics.turnoverRate().compareTo(BigDecimal.ZERO));

        // Project 2
        Map<String, TurnoverReportDto.TurnoverMetrics> project2MonthlyMetrics = project2.yearMonths();
        assertNotNull(project2MonthlyMetrics);
        assertEquals(3, project2MonthlyMetrics.size());

        // January: 1 employee, 0 left, 1 at end
        TurnoverReportDto.TurnoverMetrics project2JanMetrics = project2MonthlyMetrics.get("2023-01");
        assertNotNull(project2JanMetrics);
        assertEquals(1, project2JanMetrics.employeesAtStart());
        assertEquals(0, project2JanMetrics.employeesLeft());
        assertEquals(1, project2JanMetrics.employeesAtEnd());
        assertEquals(0, project2JanMetrics.turnoverRate().compareTo(BigDecimal.ZERO));

        // February: 1 employee, 0 left, 1 at end
        TurnoverReportDto.TurnoverMetrics project2FebMetrics = project2MonthlyMetrics.get("2023-02");
        assertNotNull(project2FebMetrics);
        assertEquals(1, project2FebMetrics.employeesAtStart());
        assertEquals(0, project2FebMetrics.employeesLeft());
        assertEquals(1, project2FebMetrics.employeesAtEnd());
        assertEquals(0, project2FebMetrics.turnoverRate().compareTo(BigDecimal.ZERO));

        // March: 1 employee, 0 left, 1 at end
        TurnoverReportDto.TurnoverMetrics project2MarMetrics = project2MonthlyMetrics.get("2023-03");
        assertNotNull(project2MarMetrics);
        assertEquals(1, project2MarMetrics.employeesAtStart());
        assertEquals(0, project2MarMetrics.employeesLeft());
        assertEquals(1, project2MarMetrics.employeesAtEnd());
        assertEquals(0, project2MarMetrics.turnoverRate().compareTo(BigDecimal.ZERO));

        // Project 3
        Map<String, TurnoverReportDto.TurnoverMetrics> project3MonthlyMetrics = project3.yearMonths();
        assertNotNull(project3MonthlyMetrics);
        assertEquals(3, project3MonthlyMetrics.size());

        // January: 2 employees, 1 left (Employee 4), 1 at end
        TurnoverReportDto.TurnoverMetrics project3JanMetrics = project3MonthlyMetrics.get("2023-01");
        assertNotNull(project3JanMetrics);
        assertEquals(2, project3JanMetrics.employeesAtStart());
        assertEquals(1, project3JanMetrics.employeesLeft());
        assertEquals(1, project3JanMetrics.employeesAtEnd());
        assertEquals(new BigDecimal("66.67"), project3JanMetrics.turnoverRate());

        // February: 2 employees (1 + Employee 6), 0 left, 2 at end
        TurnoverReportDto.TurnoverMetrics project3FebMetrics = project3MonthlyMetrics.get("2023-02");
        assertNotNull(project3FebMetrics);
        assertEquals(2, project3FebMetrics.employeesAtStart());
        assertEquals(0, project3FebMetrics.employeesLeft());
        assertEquals(2, project3FebMetrics.employeesAtEnd());
        assertEquals(0, project3FebMetrics.turnoverRate().compareTo(BigDecimal.ZERO));

        // March: 2 employees, 0 left, 2 at end
        TurnoverReportDto.TurnoverMetrics project3MarMetrics = project3MonthlyMetrics.get("2023-03");
        assertNotNull(project3MarMetrics);
        assertEquals(2, project3MarMetrics.employeesAtStart());
        assertEquals(0, project3MarMetrics.employeesLeft());
        assertEquals(2, project3MarMetrics.employeesAtEnd());
        assertEquals(0, project3MarMetrics.turnoverRate().compareTo(BigDecimal.ZERO));
    }
}
