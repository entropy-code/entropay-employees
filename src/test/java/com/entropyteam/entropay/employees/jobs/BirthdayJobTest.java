package com.entropyteam.entropay.employees.jobs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.entropyteam.entropay.employees.models.Employee;
import com.entropyteam.entropay.employees.repositories.EmployeeRepository;
import com.entropyteam.entropay.notifications.MessageDto;
import com.entropyteam.entropay.notifications.MessageType;
import com.entropyteam.entropay.notifications.NotificationService;

@ExtendWith(MockitoExtension.class)
class BirthdayJobTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private BirthdayJob birthdayJob;

    @Captor
    private ArgumentCaptor<MessageDto> messageCaptor;

    private Employee employee1;
    private Employee employee2;

    @BeforeEach
    void setUp() {
        // Create test employees
        employee1 = new Employee();
        employee1.setId(UUID.randomUUID());
        employee1.setFirstName("John");
        employee1.setLastName("Doe");
        employee1.setBirthDate(LocalDate.of(1990, Month.JANUARY, 15));
        employee1.setActive(true);
        employee1.setInternalId("E001");

        employee2 = new Employee();
        employee2.setId(UUID.randomUUID());
        employee2.setFirstName("Jane");
        employee2.setLastName("Smith");
        employee2.setBirthDate(LocalDate.of(1985, Month.JANUARY, 16));
        employee2.setActive(true);
        employee2.setInternalId("E002");

        Employee employee3 = new Employee();
        employee3.setId(UUID.randomUUID());
        employee3.setFirstName("Bob");
        employee3.setLastName("Johnson");
        employee3.setBirthDate(LocalDate.of(1995, Month.JANUARY, 17));
        employee3.setActive(true);
        employee3.setInternalId("E003");
    }

    @Test
    void shouldSendNotificationForEmployeesWithBirthdayToday() {
        // Given
        LocalDate today = LocalDate.of(2023, Month.JANUARY, 15); // Same day as employee1's birthday

        // Create another employee with a different birthday
        Employee anotherEmployee = new Employee();
        anotherEmployee.setId(UUID.randomUUID());
        anotherEmployee.setFirstName("Another");
        anotherEmployee.setLastName("Person");
        anotherEmployee.setBirthDate(LocalDate.of(1990, Month.FEBRUARY, 15)); // Different month
        anotherEmployee.setActive(true);
        anotherEmployee.setInternalId("E004");

        // Mock the repository to return all active employees
        when(employeeRepository.findAllByDeletedIsFalseAndActiveIsTrue())
                .thenReturn(List.of(employee1, anotherEmployee));

        // When
        // Mock LocalDate.now() to return our test date
        try (var mockedStatic = mockStatic(LocalDate.class)) {
            mockedStatic.when(LocalDate::now).thenReturn(today);
            birthdayJob.notifyBirthdays();
        }

        // Then
        verify(notificationService).sendNotification(messageCaptor.capture());
        MessageDto capturedMessage = messageCaptor.getValue();

        assertEquals(MessageType.BIRTHDAY, capturedMessage.messageType());
        assertEquals("Employee Birthdays", capturedMessage.title());
        assertTrue(capturedMessage.message().contains("John Doe"));
        assertTrue(capturedMessage.message().contains("15/01/1990"));
        assertTrue(capturedMessage.message().contains("E001"));
        assertFalse(capturedMessage.message().contains("Another Person"));
        assertFalse(capturedMessage.message().contains("Jane Smith"));
        assertFalse(capturedMessage.message().contains("Bob Johnson"));
    }

    @Test
    void shouldIncludeWeekendBirthdaysOnMonday() {
        // Given
        LocalDate monday = LocalDate.of(2023, Month.JANUARY, 16); // Monday

        // Create employees with weekend birthdays
        Employee saturdayEmployee = new Employee();
        saturdayEmployee.setId(UUID.randomUUID());
        saturdayEmployee.setFirstName("Saturday");
        saturdayEmployee.setLastName("Person");
        saturdayEmployee.setBirthDate(LocalDate.of(1980, Month.JANUARY, 14)); // Birthday on Saturday
        saturdayEmployee.setActive(true);
        saturdayEmployee.setInternalId("E004");

        Employee sundayEmployee = employee1; // Birthday on Sunday (January 15)
        sundayEmployee.setBirthDate(LocalDate.of(1990, Month.JANUARY, 15)); // Ensure correct date

        Employee mondayEmployee = employee2; // Birthday on Monday (January 16)
        mondayEmployee.setBirthDate(LocalDate.of(1985, Month.JANUARY, 16)); // Ensure correct date

        // Create another employee with a different birthday
        Employee anotherEmployee = new Employee();
        anotherEmployee.setId(UUID.randomUUID());
        anotherEmployee.setFirstName("Another");
        anotherEmployee.setLastName("Person");
        anotherEmployee.setBirthDate(LocalDate.of(1990, Month.FEBRUARY, 15)); // Different month
        anotherEmployee.setActive(true);
        anotherEmployee.setInternalId("E005");

        // Mock the repository to return all active employees
        when(employeeRepository.findAllByDeletedIsFalseAndActiveIsTrue())
                .thenReturn(List.of(saturdayEmployee, sundayEmployee, mondayEmployee, anotherEmployee));

        // When
        try (var mockedStatic = mockStatic(LocalDate.class)) {
            mockedStatic.when(LocalDate::now).thenReturn(monday);
            birthdayJob.notifyBirthdays();
        }

        // Then
        verify(notificationService).sendNotification(messageCaptor.capture());
        MessageDto capturedMessage = messageCaptor.getValue();

        assertTrue(capturedMessage.message().contains("Saturday Person"));
        assertTrue(capturedMessage.message().contains("John Doe"));
        assertTrue(capturedMessage.message().contains("Jane Smith"));
        assertFalse(capturedMessage.message().contains("Another Person"));
        assertFalse(capturedMessage.message().contains("Bob Johnson"));
    }

    @Test
    void shouldNotSendNotificationWhenNoBirthdays() {
        // Given
        LocalDate today = LocalDate.of(2023, Month.FEBRUARY, 1); // No birthdays on this date

        // Create employees with birthdays on different dates
        Employee employee = new Employee();
        employee.setId(UUID.randomUUID());
        employee.setFirstName("No");
        employee.setLastName("Match");
        employee.setBirthDate(LocalDate.of(1990, Month.MARCH, 15)); // Different month and day
        employee.setActive(true);
        employee.setInternalId("E006");

        // Mock the repository to return employees with no birthdays on today's date
        when(employeeRepository.findAllByDeletedIsFalseAndActiveIsTrue())
                .thenReturn(List.of(employee));

        // When
        try (var mockedStatic = mockStatic(LocalDate.class)) {
            mockedStatic.when(LocalDate::now).thenReturn(today);
            birthdayJob.notifyBirthdays();
        }

        // Then
        verify(notificationService, never()).sendNotification(any());
    }

    @Test
    void shouldNotSendNotificationWhenNoBirthdaysOnMonday() {
        // Given
        LocalDate monday = LocalDate.of(2023, Month.FEBRUARY, 6); // Monday with no birthdays

        // Create employees with birthdays on different dates (not on weekend or Monday)
        Employee employee1 = new Employee();
        employee1.setId(UUID.randomUUID());
        employee1.setFirstName("No");
        employee1.setLastName("Match1");
        employee1.setBirthDate(LocalDate.of(1990, Month.MARCH, 15)); // Different month and day
        employee1.setActive(true);
        employee1.setInternalId("E007");

        Employee employee2 = new Employee();
        employee2.setId(UUID.randomUUID());
        employee2.setFirstName("No");
        employee2.setLastName("Match2");
        employee2.setBirthDate(LocalDate.of(1990, Month.FEBRUARY, 7)); // Day after Monday
        employee2.setActive(true);
        employee2.setInternalId("E008");

        // Mock the repository to return employees with no birthdays on weekend or Monday
        when(employeeRepository.findAllByDeletedIsFalseAndActiveIsTrue())
                .thenReturn(List.of(employee1, employee2));

        // When
        try (var mockedStatic = mockStatic(LocalDate.class)) {
            mockedStatic.when(LocalDate::now).thenReturn(monday);
            birthdayJob.notifyBirthdays();
        }

        // Then
        verify(notificationService, never()).sendNotification(any());
    }
}
