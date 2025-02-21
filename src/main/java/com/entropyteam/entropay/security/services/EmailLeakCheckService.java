package com.entropyteam.entropay.security.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.entropyteam.entropay.employees.models.Employee;
import com.entropyteam.entropay.employees.repositories.EmployeeRepository;
import com.entropyteam.entropay.notifications.AlertMessageDto;
import com.entropyteam.entropay.notifications.SlackAlertStatus;
import com.entropyteam.entropay.notifications.services.NotificationService;
import com.entropyteam.entropay.security.dtos.LeakDto;
import com.entropyteam.entropay.security.dtos.LeakResponseDto;
import com.entropyteam.entropay.security.enums.LeakType;
import com.entropyteam.entropay.security.enums.VulnerabilityStatus;
import com.entropyteam.entropay.security.models.EmailLeakHistory;
import com.entropyteam.entropay.security.models.EmailVulnerability;
import com.entropyteam.entropay.security.repositories.EmailLeakHistoryRepository;
import com.entropyteam.entropay.security.repositories.EmailVulnerabilityRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class EmailLeakCheckService {

    public static final String STEALER_LOGS = "Stealer Logs";
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String EMAIL_LEAK_CHECKER = "Email Leak Checker";
    private final EmployeeRepository employeeRepository;
    private final EmailLeakHistoryRepository emailLeakHistoryRepository;
    private final EmailVulnerabilityRepository emailVulnerabilityRepository;
    private final RestTemplate restTemplate;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final NotificationService notificationService;

    @Value("${leakcheck.api.key:DEFAULT_KEY}")
    private String leakCheckApiKey;

    @Value("${leakcheck.api.url:DEFAULT_URL}")
    private String leakCheckApiUrl;

    public EmailLeakCheckService(EmployeeRepository employeeRepository,
            EmailLeakHistoryRepository emailLeakHistoryRepository,
            EmailVulnerabilityRepository emailVulnerabilityRepository,
            NotificationService notificationService,
            RestTemplate restTemplate,
            @Value("${leakcheck.api.url:DEFAULT_KEY}") String leakCheckApiUrl,
            @Value("${leakcheck.api.key:DEFAULT_URL}") String leakCheckApiKey) {
        this.employeeRepository = employeeRepository;
        this.emailLeakHistoryRepository = emailLeakHistoryRepository;
        this.emailVulnerabilityRepository = emailVulnerabilityRepository;
        this.restTemplate = restTemplate;

        this.notificationService = notificationService;
        this.leakCheckApiUrl = leakCheckApiUrl;
        this.leakCheckApiKey = leakCheckApiKey;
    }

    @Scheduled(cron = "0 30 9 * * ?")
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

        employees.forEach(employee -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            int newLabourLeaks = 0;
            int newPersonalLeaks = 0;

            // Labour email
            String labourEmail = employee.getLabourEmail();
            if (isValidEmail(labourEmail)) {
                LOGGER.info("Checking email leaks for employee: {}, email: {}",
                        employee.getFullName(), obfuscateEmail(labourEmail));
                newLabourLeaks = processEmailLeak(employee, labourEmail, vulnerabilityStats);
            } else {
                LOGGER.warn("Invalid or null labour email for employee: {} -> {}",
                        employee.getFullName(), labourEmail);
            }

            // Personal email
            String personalEmail = employee.getPersonalEmail();
            if (isValidEmail(personalEmail)) {
                LOGGER.info("Checking email leaks for employee: {}, email: {}",
                        employee.getFullName(), obfuscateEmail(personalEmail));
                newPersonalLeaks = processEmailLeak(employee, personalEmail, vulnerabilityStats);
            } else {
                LOGGER.warn("Invalid or null personal email for employee: {} -> {}",
                        employee.getFullName(), personalEmail);
            }

            // Notify
            if (hasNewLeaks(newLabourLeaks, newPersonalLeaks)) {
                notifyEmployeeLeaks(employee, newLabourLeaks, newPersonalLeaks);
            } else {
                LOGGER.info("No new vulnerabilities found for employee: {}", employee.getFullName());
            }
        });

        sendFinalReport(employees.size(), vulnerabilityStats);
    }

    private void notifyEmployeeLeaks(Employee employee, int newLabourLeaks, int newPersonalLeaks) {
        StringBuilder notificationMessage = new StringBuilder()
                .append("*New Leaks Found!*\n")
                .append("👤 *User:* ").append(employee.getFullName()).append("\n")
                .append("*Total New Vulnerabilities:* ").append(newLabourLeaks + newPersonalLeaks).append("\n");

        if (newLabourLeaks > 0) {
            notificationMessage.append("📧 *Labour Email Affected:* ")
                    .append(obfuscateEmail(employee.getLabourEmail()))
                    .append(" | *New Vulnerabilities:* ").append(newLabourLeaks).append("\n");
        }
        if (newPersonalLeaks > 0) {
            notificationMessage.append("🏠 *Personal Email Affected:* ")
                    .append(obfuscateEmail(employee.getPersonalEmail()))
                    .append(" | *New Vulnerabilities:* ").append(newPersonalLeaks).append("\n");
        }

        notificationService.sendSlackNotification(new AlertMessageDto(
                EMAIL_LEAK_CHECKER,
                notificationMessage.toString(),
                SlackAlertStatus.WARNING
        ));
    }

    private boolean hasNewLeaks(int newLabourLeaks, int newPersonalLeaks) {
        return newLabourLeaks > 0 || newPersonalLeaks > 0;
    }

    private int processEmailLeak(Employee employee, String email, Map<LeakType, Integer> vulnerabilityStats) {
        LOGGER.info("Checking leaks for email: {}", obfuscateEmail(email));

        HttpEntity<String> entity = new HttpEntity<>(createHeaders());
        String url = leakCheckApiUrl + "/query/" + email;

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        saveEmailLeakHistory(employee, email, response.getBody());

        return handleApiResponse(employee, email, response, vulnerabilityStats);
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-Key", leakCheckApiKey);
        return headers;
    }

    private void saveEmailLeakHistory(Employee employee, String email, String apiResponse) {
        EmailLeakHistory history = new EmailLeakHistory();
        history.setEmail(email);
        history.setEmployee(employee);
        history.setCreatedAt(LocalDateTime.now());
        history.setModifiedAt(LocalDateTime.now());
        history.setApiResponse(apiResponse);
        emailLeakHistoryRepository.save(history);
    }

    private int handleApiResponse(Employee employee, String email, ResponseEntity<String> response,
            Map<LeakType, Integer> vulnerabilityStats) {
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return processLeakResponse(employee, email, response.getBody(), vulnerabilityStats);
        }

        LOGGER.error("Failed to check email: {}", email);
        return 0;
    }

    int processLeakResponse(Employee employee, String email, String responseBody,
            Map<LeakType, Integer> vulnerabilityStats) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            LeakResponseDto leakResponse = objectMapper.readValue(responseBody, LeakResponseDto.class);

            List<EmailVulnerability> existingLeaks =
                    emailVulnerabilityRepository.findAllByEmployeeAndDeletedFalse(employee);

            Map<String, EmailVulnerability> existingLeaksMap = existingLeaks.stream()
                    .collect(Collectors.toMap(this::generateLeakKey, v -> v));

            int newLeaks = 0;

            if (leakResponse.success() && leakResponse.result() != null) {
                for (LeakDto leak : leakResponse.result()) {
                    LeakType leakType = determineLeakType(leak);
                    String key = generateLeakKey(leak, leakType);

                    if (!existingLeaksMap.containsKey(key)) {
                        saveNewLeak(employee, leak, leakType, email);
                        vulnerabilityStats.merge(leakType, 1, Integer::sum);
                        newLeaks++;
                    }
                }
            }
            return newLeaks;
        } catch (Exception e) {
            LOGGER.error("Error parsing leak response: {}", e.getMessage());
            return 0;
        }
    }

    private void saveNewLeak(Employee employee, LeakDto leak, LeakType leakType, String email) {
        EmailVulnerability vulnerability = new EmailVulnerability();
        vulnerability.setEmployee(employee);
        vulnerability.setEmail(email);
        vulnerability.setSourceName(leak.source().name());
        vulnerability.setDetectedAt(LocalDateTime.now());
        vulnerability.setStatus(VulnerabilityStatus.DETECTED);
        vulnerability.setLeakType(leakType);

        String password = leak.password();
        vulnerability.setPassword(password);

        if (leakType == LeakType.STEALER_LOGS_LEAK) {
            String origin = Optional.ofNullable(leak.origin())
                    .map(o -> String.join(", ", o))
                    .orElse("");
            vulnerability.setOrigin(origin);
        }

        if (leakType == LeakType.SITE_LEAK) {
            LocalDate breachDate = parseBreachDate(leak.source().breachDate());
            vulnerability.setBreachDate(breachDate);
        }

        emailVulnerabilityRepository.save(vulnerability);
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

    private String generateLeakKey(EmailVulnerability vulnerability) {
        List<String> keyValues = switch (vulnerability.getLeakType()) {
            case STEALER_LOGS_LEAK -> List.of(
                    vulnerability.getEmail(),
                    vulnerability.getSourceName(),
                    Optional.ofNullable(vulnerability.getPassword()).orElse(""),
                    Optional.ofNullable(vulnerability.getOrigin()).orElse("")
            );

            case SITE_LEAK -> List.of(
                    vulnerability.getEmail(),
                    vulnerability.getSourceName(),
                    Optional.ofNullable(vulnerability.getBreachDate())
                            .map(LocalDate::toString).orElse(""),
                    Optional.ofNullable(vulnerability.getPassword()).orElse("")
            );

            case COMMON_LEAK -> List.of(
                    vulnerability.getEmail(),
                    vulnerability.getSourceName(),
                    Optional.ofNullable(vulnerability.getPassword()).orElse("")
            );

            default -> throw new IllegalArgumentException("Unrecognized LeakType: " + vulnerability.getLeakType());
        };

        return buildKey(keyValues);
    }

    private String generateLeakKey(LeakDto leak, LeakType leakType) {
        List<String> keyValues = switch (leakType) {
            case STEALER_LOGS_LEAK -> List.of(
                    leak.email(),
                    leak.source().name(),
                    Optional.ofNullable(leak.password()).orElse(""),
                    String.join(", ", Optional.ofNullable(leak.origin()).orElse(Collections.emptyList()))
            );

            case SITE_LEAK -> List.of(
                    leak.email(),
                    leak.source().name(),
                    Optional.ofNullable(leak.source().breachDate()).filter(StringUtils::isNotBlank)
                            .map(date -> date + "-01").orElse(""),
                    Optional.ofNullable(leak.password()).orElse("")
            );

            case COMMON_LEAK -> List.of(
                    leak.email(),
                    leak.source().name(),
                    Optional.ofNullable(leak.password()).orElse("")
            );

            default -> throw new IllegalArgumentException("Unrecognized LeakType: " + leakType);
        };

        return buildKey(keyValues);
    }

    private String buildKey(List<String> values) {
        return values.stream()
                .map(v -> v == null ? "" : v)
                .collect(Collectors.joining("-"));
    }

    private LeakType determineLeakType(LeakDto leak) {
        if (STEALER_LOGS.equalsIgnoreCase(leak.source().name()) &&
                StringUtils.isNotBlank(leak.password()) &&
                CollectionUtils.isNotEmpty(leak.origin())) {
            return LeakType.STEALER_LOGS_LEAK;
        } else if (StringUtils.isNotBlank(leak.source().breachDate())) {
            return LeakType.SITE_LEAK;
        } else {
            return LeakType.COMMON_LEAK;
        }
    }

    private LocalDate parseBreachDate(String breachDate) {
        if (StringUtils.isBlank(breachDate)) {
            return null;
        }
        return LocalDate.parse(breachDate + "-01", FORMATTER);
    }

    public String obfuscateEmail(String email) {
        if (email == null || !email.contains("@")) {
            return email;
        }

        String[] parts = email.split("@");
        String local = parts[0];
        String domain = parts[1];

        if (local.length() > 2) {
            local = local.charAt(0) + "****" + local.charAt(local.length() - 1);
        } else {
            local = local.charAt(0) + "*";
        }

        String[] domainParts = domain.split("\\.");
        String domainName = domainParts[0];
        String tld = domainParts.length > 1 ? domainParts[1] : "";

        if (domainName.length() > 1) {
            domainName = domainName.charAt(0) + "******";
        }

        return local + "@" + domainName + (tld.isEmpty() ? "" : "." + tld);
    }

    public boolean isValidEmail(String email) {
        if (StringUtils.isBlank(email)) {
            return false;
        }

        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return Pattern.compile(emailRegex).matcher(email).matches();
    }



}