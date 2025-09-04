package com.entropyteam.entropay.employees.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.entropyteam.entropay.common.ReactAdminMapper;
import com.entropyteam.entropay.employees.models.Assignment;
import com.entropyteam.entropay.employees.models.Country;
import com.entropyteam.entropay.employees.models.Employee;
import com.entropyteam.entropay.employees.models.Project;
import com.entropyteam.entropay.employees.repositories.AssignmentRepository;
import com.entropyteam.entropay.employees.repositories.EmployeeRepository;
import com.entropyteam.entropay.employees.repositories.ProjectRepository;
import com.entropyteam.entropay.employees.repositories.RoleRepository;
import com.entropyteam.entropay.employees.repositories.SeniorityRepository;
import com.entropyteam.entropay.employees.timetracking.AssignmentTimeEntry;
import com.entropyteam.entropay.employees.timetracking.TimeTrackingEntry;

@ExtendWith(MockitoExtension.class)
class AssignmentServiceTest {

    @Mock
    private AssignmentRepository assignmentRepository;
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private SeniorityRepository seniorityRepository;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private HolidayService holidayService;
    @Mock
    private ReactAdminMapper reactAdminMapper;

    @InjectMocks
    private AssignmentService assignmentService;

    private LocalDate startDate;
    private LocalDate endDate;
    private Employee employee;
    private Project project;
    private Assignment assignment;
    private Country country;

    @BeforeEach
    void setUp() {
        startDate = LocalDate.of(2025, 1, 1); // Wednesday
        endDate = LocalDate.of(2025, 1, 31); // Friday

        country = new Country();
        country.setId(UUID.randomUUID());
        country.setName("Test Country");

        employee = new Employee();
        employee.setId(UUID.randomUUID());
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setCountry(country);

        project = new Project();
        project.setId(UUID.randomUUID());
        project.setName("Test Project");
        project.setPaidPto(false);

        assignment = new Assignment();
        assignment.setId(UUID.randomUUID());
        assignment.setEmployee(employee);
        assignment.setProject(project);
        assignment.setStartDate(startDate);
        assignment.setEndDate(endDate);
    }

    @Test
    @DisplayName("Should calculate activity for full month assignment")
    void shouldCalculateActivityForFullMonthAssignment() {
        // Given
        List<Assignment> assignments = List.of(assignment);
        Map<Country, Set<LocalDate>> holidaysByCountry = Map.of();

        when(assignmentRepository.findAllBetweenPeriod(startDate, endDate)).thenReturn(assignments);
        when(holidayService.getHolidaysByCountry(startDate, endDate)).thenReturn(holidaysByCountry);

        // When
        List<AssignmentTimeEntry> result = assignmentService.findActivityForAssignments(startDate, endDate);

        // Then
        // January 2025 has 23 weekdays (excluding weekends)
        assertEquals(23, result.size());

        // All entries should be for the same assignment and have 8.0 hours
        result.forEach(entry -> {
            assertEquals(assignment, entry.getAssignment());
            assertEquals(8.0, entry.getHours());
        });

        // Verify all dates are weekdays
        Set<LocalDate> resultDates = result.stream()
                .map(TimeTrackingEntry::getDate)
                .collect(Collectors.toSet());

        resultDates.forEach(date ->
            assertTrue(date.getDayOfWeek().getValue() < 6, "Date should be weekday: " + date)
        );
    }

    @Test
    @DisplayName("Should exclude holidays for non-PTO projects")
    void shouldExcludeHolidaysForNonPtoProjects() {
        // Given
        project.setPaidPto(false);
        List<Assignment> assignments = List.of(assignment);

        LocalDate holiday1 = LocalDate.of(2025, 1, 2); // Thursday
        LocalDate holiday2 = LocalDate.of(2025, 1, 3); // Friday
        Set<LocalDate> holidays = Set.of(holiday1, holiday2);
        Map<Country, Set<LocalDate>> holidaysByCountry = Map.of(country, holidays);

        when(assignmentRepository.findAllBetweenPeriod(startDate, endDate)).thenReturn(assignments);
        when(holidayService.getHolidaysByCountry(startDate, endDate)).thenReturn(holidaysByCountry);

        // When
        List<AssignmentTimeEntry> result = assignmentService.findActivityForAssignments(startDate, endDate);

        // Then
        // 23 weekdays - 2 holidays = 21 working days
        assertEquals(21, result.size());

        // Verify holidays are not included
        Set<LocalDate> resultDates = result.stream()
                .map(TimeTrackingEntry::getDate)
                .collect(Collectors.toSet());

        assertFalse(resultDates.contains(holiday1));
        assertFalse(resultDates.contains(holiday2));
    }

    @Test
    @DisplayName("Should include holidays for PTO projects")
    void shouldIncludeHolidaysForPtoProjects() {
        // Given
        project.setPaidPto(true);
        List<Assignment> assignments = List.of(assignment);

        LocalDate holiday = LocalDate.of(2025, 1, 2); // Thursday
        Set<LocalDate> holidays = Set.of(holiday);
        Map<Country, Set<LocalDate>> holidaysByCountry = Map.of(country, holidays);

        when(assignmentRepository.findAllBetweenPeriod(startDate, endDate)).thenReturn(assignments);
        when(holidayService.getHolidaysByCountry(startDate, endDate)).thenReturn(holidaysByCountry);

        // When
        List<AssignmentTimeEntry> result = assignmentService.findActivityForAssignments(startDate, endDate);

        // Then
        // All 23 weekdays should be included (holidays not excluded for PTO projects)
        assertEquals(23, result.size());

        // Verify holiday is included
        Set<LocalDate> resultDates = result.stream()
                .map(TimeTrackingEntry::getDate)
                .collect(Collectors.toSet());

        assertTrue(resultDates.contains(holiday));
    }

    @Test
    @DisplayName("Should adjust working days when assignment starts mid-period")
    void shouldAdjustWorkingDaysWhenAssignmentStartsMidPeriod() {
        // Given
        LocalDate assignmentStartDate = LocalDate.of(2025, 1, 15); // Wednesday mid-month
        assignment.setStartDate(assignmentStartDate);

        List<Assignment> assignments = List.of(assignment);
        Map<Country, Set<LocalDate>> holidaysByCountry = Map.of();

        when(assignmentRepository.findAllBetweenPeriod(startDate, endDate)).thenReturn(assignments);
        when(holidayService.getHolidaysByCountry(startDate, endDate)).thenReturn(holidaysByCountry);

        // When
        List<AssignmentTimeEntry> result = assignmentService.findActivityForAssignments(startDate, endDate);

        // Then
        // Should only include days from Jan 15-31 (13 weekdays)
        assertEquals(13, result.size());

        // Verify all dates are on or after assignment start date
        result.forEach(entry ->
            assertFalse(entry.getDate().isBefore(assignmentStartDate),
                    "Date should be on or after assignment start: " + entry.getDate())
        );
    }

    @Test
    @DisplayName("Should adjust working days when assignment ends mid-period")
    void shouldAdjustWorkingDaysWhenAssignmentEndsMidPeriod() {
        // Given
        LocalDate assignmentEndDate = LocalDate.of(2025, 1, 15); // Wednesday mid-month
        assignment.setEndDate(assignmentEndDate);

        List<Assignment> assignments = List.of(assignment);
        Map<Country, Set<LocalDate>> holidaysByCountry = Map.of();

        when(assignmentRepository.findAllBetweenPeriod(startDate, endDate)).thenReturn(assignments);
        when(holidayService.getHolidaysByCountry(startDate, endDate)).thenReturn(holidaysByCountry);

        // When
        List<AssignmentTimeEntry> result = assignmentService.findActivityForAssignments(startDate, endDate);

        // Then
        // Should only include days from Jan 1-15 (11 weekdays)
        assertEquals(11, result.size());

        // Verify all dates are on or before assignment end date
        result.forEach(entry ->
            assertFalse(entry.getDate().isAfter(assignmentEndDate),
                    "Date should be on or before assignment end: " + entry.getDate())
        );
    }

    @Test
    @DisplayName("Should handle assignment that starts and ends within period")
    void shouldHandleAssignmentThatStartsAndEndsWithinPeriod() {
        // Given
        LocalDate assignmentStartDate = LocalDate.of(2025, 1, 10); // Friday
        LocalDate assignmentEndDate = LocalDate.of(2025, 1, 20); // Monday
        assignment.setStartDate(assignmentStartDate);
        assignment.setEndDate(assignmentEndDate);

        List<Assignment> assignments = List.of(assignment);
        Map<Country, Set<LocalDate>> holidaysByCountry = Map.of();

        when(assignmentRepository.findAllBetweenPeriod(startDate, endDate)).thenReturn(assignments);
        when(holidayService.getHolidaysByCountry(startDate, endDate)).thenReturn(holidaysByCountry);

        // When
        List<AssignmentTimeEntry> result = assignmentService.findActivityForAssignments(startDate, endDate);

        // Then
        // Should only include weekdays from Jan 10-20 (7 weekdays)
        assertEquals(7, result.size());

        // Verify all dates are within assignment period
        result.forEach(entry -> {
            LocalDate date = entry.getDate();
            assertFalse(date.isBefore(assignmentStartDate),
                    "Date should be on or after assignment start: " + date);
            assertFalse(date.isAfter(assignmentEndDate),
                    "Date should be on or before assignment end: " + date);
        });
    }

    @Test
    @DisplayName("Should handle multiple assignments for different employees")
    void shouldHandleMultipleAssignmentsForDifferentEmployees() {
        // Given
        Employee employee2 = new Employee();
        employee2.setId(UUID.randomUUID());
        employee2.setFirstName("Jane");
        employee2.setLastName("Smith");
        employee2.setCountry(country);

        Assignment assignment2 = new Assignment();
        assignment2.setId(UUID.randomUUID());
        assignment2.setEmployee(employee2);
        assignment2.setProject(project);
        assignment2.setStartDate(startDate);
        assignment2.setEndDate(endDate);

        List<Assignment> assignments = List.of(assignment, assignment2);
        Map<Country, Set<LocalDate>> holidaysByCountry = Map.of();

        when(assignmentRepository.findAllBetweenPeriod(startDate, endDate)).thenReturn(assignments);
        when(holidayService.getHolidaysByCountry(startDate, endDate)).thenReturn(holidaysByCountry);

        // When
        List<AssignmentTimeEntry> result = assignmentService.findActivityForAssignments(startDate, endDate);

        // Then
        // Should have entries for both assignments (23 weekdays * 2 assignments = 46 entries)
        assertEquals(46, result.size());

        // Verify we have entries for both employees
        Set<UUID> employeeIds = result.stream()
                .map(entry -> entry.getEmployee().getId())
                .collect(Collectors.toSet());

        assertEquals(2, employeeIds.size());
        assertTrue(employeeIds.contains(employee.getId()));
        assertTrue(employeeIds.contains(employee2.getId()));
    }

    @Test
    @DisplayName("Should handle assignment with null end date")
    void shouldHandleAssignmentWithNullEndDate() {
        // Given
        assignment.setEndDate(null); // Open-ended assignment

        List<Assignment> assignments = List.of(assignment);
        Map<Country, Set<LocalDate>> holidaysByCountry = Map.of();

        when(assignmentRepository.findAllBetweenPeriod(startDate, endDate)).thenReturn(assignments);
        when(holidayService.getHolidaysByCountry(startDate, endDate)).thenReturn(holidaysByCountry);

        // When
        List<AssignmentTimeEntry> result = assignmentService.findActivityForAssignments(startDate, endDate);

        // Then
        // Should include all weekdays in the period (23 weekdays)
        assertEquals(23, result.size());

        // All entries should be for the assignment
        result.forEach(entry -> assertEquals(assignment, entry.getAssignment()));
    }

    @Test
    @DisplayName("Should handle empty assignment list")
    void shouldHandleEmptyAssignmentList() {
        // Given
        List<Assignment> assignments = List.of();
        Map<Country, Set<LocalDate>> holidaysByCountry = Map.of();

        when(assignmentRepository.findAllBetweenPeriod(startDate, endDate)).thenReturn(assignments);
        when(holidayService.getHolidaysByCountry(startDate, endDate)).thenReturn(holidaysByCountry);

        // When
        List<AssignmentTimeEntry> result = assignmentService.findActivityForAssignments(startDate, endDate);

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should handle single day period")
    void shouldHandleSingleDayPeriod() {
        // Given
        LocalDate singleDay = LocalDate.of(2025, 1, 8); // Wednesday
        assignment.setStartDate(singleDay);
        assignment.setEndDate(singleDay);

        List<Assignment> assignments = List.of(assignment);
        Map<Country, Set<LocalDate>> holidaysByCountry = Map.of();

        when(assignmentRepository.findAllBetweenPeriod(singleDay, singleDay)).thenReturn(assignments);
        when(holidayService.getHolidaysByCountry(singleDay, singleDay)).thenReturn(holidaysByCountry);

        // When
        List<AssignmentTimeEntry> result = assignmentService.findActivityForAssignments(singleDay, singleDay);

        // Then
        assertEquals(1, result.size());
        assertEquals(singleDay, result.getFirst().getDate());
        assertEquals(8.0, result.getFirst().getHours());
    }

    @Test
    @DisplayName("Should handle weekend-only period")
    void shouldHandleWeekendOnlyPeriod() {
        // Given
        LocalDate saturday = LocalDate.of(2025, 1, 4);
        LocalDate sunday = LocalDate.of(2025, 1, 5);

        List<Assignment> assignments = List.of(assignment);
        Map<Country, Set<LocalDate>> holidaysByCountry = Map.of();

        when(assignmentRepository.findAllBetweenPeriod(saturday, sunday)).thenReturn(assignments);
        when(holidayService.getHolidaysByCountry(saturday, sunday)).thenReturn(holidaysByCountry);

        // When
        List<AssignmentTimeEntry> result = assignmentService.findActivityForAssignments(saturday, sunday);

        // Then
        assertTrue(result.isEmpty(), "Should not include weekend days");
    }
}
