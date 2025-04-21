package com.entropyteam.entropay.employees.jobs;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.entropyteam.entropay.employees.models.Employee;
import com.entropyteam.entropay.employees.repositories.EmployeeRepository;
import com.entropyteam.entropay.notifications.MessageDto;
import com.entropyteam.entropay.notifications.MessageType;
import com.entropyteam.entropay.notifications.NotificationService;

/**
 * Job that sends notifications about employee birthdays.
 * Runs Monday-Friday at 9:00 AM GMT-3.
 * On Mondays, it also checks for birthdays from the weekend.
 */
@Component
public class BirthdayJob {

    private static final Logger LOGGER = LoggerFactory.getLogger(BirthdayJob.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final EmployeeRepository employeeRepository;
    private final NotificationService notificationService;

    @Autowired
    public BirthdayJob(EmployeeRepository employeeRepository, NotificationService notificationService) {
        this.employeeRepository = employeeRepository;
        this.notificationService = notificationService;
    }

    /**
     * Scheduled job that runs Monday-Friday at 9:00 AM GMT-3.
     * Sends notifications about employee birthdays.
     */
    @Scheduled(cron = "0 0 9 ? * MON-FRI", zone = "GMT-3")
    @Transactional(readOnly = true)
    public void notifyBirthdays() {
        LOGGER.info("Starting birthday notification job");

        List<LocalDate> datesToCheck = getDatesToCheck();

        // Get all active and non-deleted employees
        List<Employee> allEmployees = employeeRepository.findAllByDeletedIsFalseAndActiveIsTrue();

        // Filter employees with birthdays on the dates to check
        List<Employee> birthdayEmployees = allEmployees.stream()
                .filter(employee -> employee.getBirthDate() != null)
                .filter(employee -> datesToCheck.stream()
                        .anyMatch(date -> isSameDayAndMonth(employee.getBirthDate(), date)))
                .collect(Collectors.toList());

        if (!birthdayEmployees.isEmpty()) {
            LOGGER.info("Found {} employees with birthdays", birthdayEmployees.size());
            sendBirthdayNotification(birthdayEmployees);
        } else {
            LOGGER.info("No birthdays found for today");
        }
    }

    /**
     * Checks if two dates have the same day and month (ignoring year).
     *
     * @param date1 First date to compare
     * @param date2 Second date to compare
     * @return true if the day and month are the same, false otherwise
     */
    private boolean isSameDayAndMonth(LocalDate date1, LocalDate date2) {
        return date1.getDayOfMonth() == date2.getDayOfMonth() 
                && date1.getMonth() == date2.getMonth();
    }


    /**
     * Gets the list of dates to check for birthdays.
     * If today is Monday, it includes the weekend days (Saturday and Sunday).
     * Otherwise, it only includes today.
     *
     * @return List of dates to check for birthdays
     */
    private List<LocalDate> getDatesToCheck() {
        LocalDate today = LocalDate.now();

        if (today.getDayOfWeek() == DayOfWeek.MONDAY) {
            LOGGER.info("Today is Monday, checking weekend birthdays as well");

            List<LocalDate> datesToCheck = List.of(
                    today.minusDays(2), // Saturday
                    today.minusDays(1), // Sunday
                    today               // Monday
            );

            LOGGER.info("Checked for birthdays on weekend and today: {}", datesToCheck);
            return datesToCheck;
        } else {
            LOGGER.info("Checked for birthdays on: {}", today);
            return List.of(today);
        }
    }

    /**
     * Sends a notification with the list of employees having birthdays.
     *
     * @param employees List of employees with birthdays
     */
    private void sendBirthdayNotification(List<Employee> employees) {
        StringBuilder messageBuilder = new StringBuilder();

        for (Employee employee : employees) {
            messageBuilder
                    .append(employee.getInternalId())
                    .append(" - ")
                    .append(employee.getFullName())
                    .append(" - ")
                    .append(employee.getBirthDate().format(DATE_FORMATTER))
                    .append("\n");
        }

        // Create and send the notification
        MessageDto birthdayMessage = new MessageDto(
                "Employee Birthdays",
                messageBuilder.toString(),
                MessageType.BIRTHDAY
        );

        notificationService.sendNotification(birthdayMessage);
        LOGGER.info("Birthday notification sent for {} employees", employees.size());
    }
}
