package com.entropyteam.entropay.security.services;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.entropyteam.entropay.employees.models.Employee;
import com.entropyteam.entropay.employees.repositories.EmployeeRepository;
import com.entropyteam.entropay.notifications.AlertMessageDto;
import com.entropyteam.entropay.notifications.SlackAlertStatus;
import com.entropyteam.entropay.notifications.services.NotificationService;
import com.entropyteam.entropay.security.enums.LeakType;

@Service
public class EmailLeakCheckService {

    private static final Logger LOGGER = LogManager.getLogger();
    public static final String EMAIL_LEAK_CHECKER = "Email Leak Checker";
    private final EmployeeRepository employeeRepository;
    private final NotificationService notificationService;
    private final EmailLeakProcessor emailLeakProcessor;

    public EmailLeakCheckService(EmployeeRepository employeeRepository,
            NotificationService notificationService,
            EmailLeakProcessor emailLeakProcessor) {
        this.employeeRepository = employeeRepository;
        this.notificationService = notificationService;
        this.emailLeakProcessor = emailLeakProcessor;
    }

    @Scheduled(cron = "0 30 9 * * ?", zone = "GMT-3")
    @Async
    public void runAsyncEmailCheck() {
        LOGGER.info("{} process started.", EMAIL_LEAK_CHECKER);

        notificationService.sendSlackNotification(new AlertMessageDto(
                EMAIL_LEAK_CHECKER,
                "The daily email leak scan has started.\nChecking active employees for potential leaks.",
                SlackAlertStatus.INFO
        ));

        List<Employee> employees = employeeRepository.findAllByDeletedIsFalseAndActiveIsTrue();
        Map<LeakType, Integer> vulnerabilityStats = new EnumMap<>(LeakType.class);

        employees.forEach(employee -> emailLeakProcessor.processEmployeeLeaks(employee, vulnerabilityStats));

        sendFinalReport(employees.size(), vulnerabilityStats);
    }

    private void sendFinalReport(int totalChecked, Map<LeakType, Integer> vulnerabilityStats) {
        String finalMessage;
        SlackAlertStatus finalStatus;

        if (!vulnerabilityStats.isEmpty()) {
            StringBuilder statsMessage = new StringBuilder(" *Email Leak Check Completed* \n")
                    .append("Total Employees Checked: ").append(totalChecked).append("\n");

            vulnerabilityStats.forEach((type, count) ->
                    statsMessage.append(SlackAlertStatus.WARNING.
                            getSlackEmoji())
                            .append(" ")
                            .append(type.name())
                            .append(": ")
                            .append(count).append("\n")
            );

            finalMessage = statsMessage.toString();
            finalStatus = SlackAlertStatus.WARNING;
        } else {
            finalMessage = String.format(" *Email Leak Check Completed Successfully* " +
                            "\nTotal Employees Checked: %d\nNo new vulnerabilities found.",
                    totalChecked);
            finalStatus = SlackAlertStatus.SUCCESS;
        }

        LOGGER.info(finalMessage);
        notificationService.sendSlackNotification(new AlertMessageDto("Email Leak Checker", finalMessage, finalStatus));
    }

}