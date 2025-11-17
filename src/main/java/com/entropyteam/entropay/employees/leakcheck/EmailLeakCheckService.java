package com.entropyteam.entropay.employees.leakcheck;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.entropyteam.entropay.employees.models.Employee;
import com.entropyteam.entropay.employees.repositories.EmployeeRepository;
import com.entropyteam.entropay.notifications.MessageDto;
import com.entropyteam.entropay.notifications.MessageType;
import com.entropyteam.entropay.notifications.NotificationService;

@Service
class EmailLeakCheckService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailLeakCheckService.class);
    private static final String EMAIL_LEAK_CHECKER = "Email Leak Checker";
    private final EmployeeRepository employeeRepository;
    private final NotificationService notificationService;
    private final EmailLeakProcessor emailLeakProcessor;
    private final boolean leakCheckEnabled;

    EmailLeakCheckService(EmployeeRepository employeeRepository,
            NotificationService notificationService,
            EmailLeakProcessor emailLeakProcessor,
            @Value("${leakcheck.enabled:false}") boolean leakCheckEnabled) {
        this.employeeRepository = employeeRepository;
        this.notificationService = notificationService;
        this.emailLeakProcessor = emailLeakProcessor;
        this.leakCheckEnabled = leakCheckEnabled;
    }

    @Scheduled(cron = "0 30 9 * * ?", zone = "GMT-3")
    public void executeEmailLeakScan() {
        if (!leakCheckEnabled) {
            LOGGER.debug("Leak check is disabled via configuration");
            return;
        }
        LOGGER.info("{} process started.", EMAIL_LEAK_CHECKER);

        notificationService.sendNotification(new MessageDto(
                EMAIL_LEAK_CHECKER,
                "The daily email leak scan has started. Checking active employees for potential leaks.",
                MessageType.INFO
        ));

        List<Employee> employees = employeeRepository.findAllByDeletedIsFalseAndActiveIsTrue();

        // Submit all tasks to the async executor
        List<CompletableFuture<LeakCheckResult>> futures = employees.stream()
                .map(emailLeakProcessor::processEmployeeLeaksAsync)
                .toList();

        // Wait for all tasks to complete
        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        try {
            allOf.join(); // Wait for all tasks to complete

            // Aggregate results from all futures
            Map<LeakType, Integer> vulnerabilityStats = aggregateResults(futures);

            sendFinalReport(employees.size(), vulnerabilityStats);
        } catch (Exception e) {
            LOGGER.error("Error during email leak check: {}", e.getMessage(), e);
            notificationService.sendNotification(new MessageDto(
                    EMAIL_LEAK_CHECKER,
                    "Error occurred during email leak check: " + e.getMessage(),
                    MessageType.ERROR
            ));
        }
    }

    private Map<LeakType, Integer> aggregateResults(List<CompletableFuture<LeakCheckResult>> futures) {
        Map<LeakType, Integer> aggregated = new EnumMap<>(LeakType.class);

        futures.stream()
                .filter(future -> !future.isCompletedExceptionally())
                .map(CompletableFuture::join)
                .flatMap(result -> result.leaksByType().entrySet().stream())
                .forEach(entry -> aggregated.merge(entry.getKey(), entry.getValue(), Integer::sum));

        return aggregated;
    }

    /**
     * Checks a single email address for leaks without saving to database.
     * Used for ad-hoc email verification via API endpoint.
     *
     * @param email the email address to check
     * @return structured leak check result
     * @throws IllegalArgumentException if email format is invalid
     */
    public SingleEmailLeakCheckDto checkSingleEmail(String email) {
        if (!emailLeakProcessor.isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format: " + email);
        }

        LOGGER.info("Single email leak check requested for: {}", emailLeakProcessor.obfuscateEmail(email));

        String responseBody = emailLeakProcessor.checkEmailWithApi(email);
        SingleEmailLeakCheckDto result = emailLeakProcessor.parseSingleEmailResponse(email, responseBody);

        if (result.hasLeaks()) {
            LOGGER.warn("Found {} leak(s) for email: {}", result.totalLeaksFound(),
                    emailLeakProcessor.obfuscateEmail(email));
        } else {
            LOGGER.info("No leaks found for email: {}", emailLeakProcessor.obfuscateEmail(email));
        }

        return result;
    }

    private void sendFinalReport(int totalChecked, Map<LeakType, Integer> vulnerabilityStats) {
        String finalMessage;
        MessageType finalStatus;

        if (!vulnerabilityStats.isEmpty()) {
            StringBuilder statsMessage = new StringBuilder(" *Email Leak Check Completed* \n")
                    .append("Total Employees Checked: ").append(totalChecked).append("\n");

            vulnerabilityStats.forEach((type, count) ->
                    statsMessage.append(":warning: ")
                            .append(type.name())
                            .append(": ")
                            .append(count).append("\n")
            );

            finalMessage = statsMessage.toString();
            finalStatus = MessageType.WARNING;
        } else {
            finalMessage = String.format(" *Email Leak Check Completed Successfully* " +
                            "\nTotal Employees Checked: %d\nNo new vulnerabilities found.",
                    totalChecked);
            finalStatus = MessageType.SUCCESS;
        }

        LOGGER.info(finalMessage);
        notificationService.sendNotification(new MessageDto("Email Leak Checker", finalMessage, finalStatus));
    }

}