package com.entropyteam.entropay.employees.leakcheck;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import com.entropyteam.entropay.employees.models.Employee;
import com.entropyteam.entropay.notifications.MessageDto;
import com.entropyteam.entropay.notifications.MessageType;
import com.entropyteam.entropay.notifications.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.resilience4j.ratelimiter.RateLimiter;

@Service
class EmailLeakProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailLeakProcessor.class);
    private static final String STEALER_LOGS = "Stealer Logs";
    private static final String EMAIL_LEAK_CHECKER = "Email Leak Checker";

    private final EmailLeakHistoryRepository emailLeakHistoryRepository;
    private final EmailVulnerabilityRepository emailVulnerabilityRepository;
    private final RestTemplate restTemplate;
    private final NotificationService notificationService;
    private final RateLimiter rateLimiter;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final ObjectMapper objectMapper;

    @Value("${leakcheck.api.key:DEFAULT_KEY}")
    private String leakCheckApiKey;

    @Value("${leakcheck.api.url:DEFAULT_URL}")
    private String leakCheckApiUrl;

    EmailLeakProcessor(EmailLeakHistoryRepository emailLeakHistoryRepository,
            EmailVulnerabilityRepository emailVulnerabilityRepository, RestTemplate restTemplate,
            NotificationService notificationService,
            @Qualifier("leakCheckRateLimiter") RateLimiter rateLimiter, ObjectMapper objectMapper) {
        this.emailLeakHistoryRepository = emailLeakHistoryRepository;
        this.emailVulnerabilityRepository = emailVulnerabilityRepository;
        this.restTemplate = restTemplate;
        this.notificationService = notificationService;
        this.rateLimiter = rateLimiter;
        this.objectMapper = objectMapper;
    }

    @Async("leakCheckExecutor")
    @Transactional
    CompletableFuture<LeakCheckResult> processEmployeeLeaksAsync(Employee employee) {
        return CompletableFuture.supplyAsync(() -> {
            List<String> emails = getEmployeeEmails(employee);

            Map<String, EmailLeakResult> resultsByEmail = emails.stream()
                    .filter(this::isValidEmail)
                    .peek(email -> logEmailCheck(employee, email))
                    .collect(Collectors.toMap(
                            email -> email,
                            email -> processEmailLeak(employee, email),
                            (existing, replacement) -> existing
                    ));

            Map<LeakType, Integer> aggregatedLeaks = resultsByEmail.values().stream()
                    .flatMap(result -> result.leaksByType.entrySet().stream())
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            Integer::sum,
                            () -> new EnumMap<>(LeakType.class)
                    ));

            int totalNewLeaks = aggregatedLeaks.values().stream()
                    .mapToInt(Integer::intValue)
                    .sum();

            if (totalNewLeaks > 0) {
                List<String> affectedEmails = resultsByEmail.entrySet().stream()
                        .filter(entry -> entry.getValue().newLeaksCount > 0)
                        .map(Map.Entry::getKey)
                        .toList();

                notifyEmployeeLeaks(employee, totalNewLeaks, affectedEmails);
            } else {
                LOGGER.info("No new vulnerabilities found for employee: {}", employee.getFullName());
            }

            return new LeakCheckResult(employee.getFullName(), Map.copyOf(aggregatedLeaks));
        });
    }

    /**
     * Extracts all non-null email addresses from an employee.
     */
    private List<String> getEmployeeEmails(Employee employee) {
        return Stream.of(employee.getLabourEmail(), employee.getPersonalEmail())
                .filter(Objects::nonNull)
                .toList();
    }

    private void logEmailCheck(Employee employee, String email) {
        LOGGER.info("Checking email leaks for employee: {}, email: {}", employee.getFullName(), obfuscateEmail(email));
    }

    private void notifyEmployeeLeaks(Employee employee, int totalNewLeaks, List<String> affectedEmails) {
        StringBuilder notificationMessage = new StringBuilder()
                .append("*New Leaks Found!*\n")
                .append("👤 *User:* ").append(employee.getFullName()).append("\n")
                .append("*Total New Vulnerabilities:* ").append(totalNewLeaks).append("\n");

        if (!affectedEmails.isEmpty()) {
            notificationMessage.append("📧 *Affected Emails:* ")
                    .append(String.join(", ", affectedEmails))
                    .append("\n");
        }

        notificationService.sendNotification(new MessageDto(
                EMAIL_LEAK_CHECKER,
                notificationMessage.toString(),
                MessageType.WARNING
        ));
    }

    /**
     * Internal result holder for processing a single email.
     */
    private record EmailLeakResult(int newLeaksCount, Map<LeakType, Integer> leaksByType) {}

    private EmailLeakResult processEmailLeak(Employee employee, String email) {
        String responseBody = checkEmailWithApi(email);
        saveEmailLeakHistory(employee, email, responseBody);

        Map<LeakType, Integer> leaksByType = new EnumMap<>(LeakType.class);
        int newLeaks = processLeakResponseForEmployee(employee, email, responseBody, leaksByType);

        return new EmailLeakResult(newLeaks, leaksByType);
    }

    /**
     * Checks an email against the leak API without saving to database.
     * Used for ad-hoc email checks via API endpoint.
     *
     * @param email the email address to check
     * @return the raw API response body
     */
    String checkEmailWithApi(String email) {
        LOGGER.info("Checking leaks for email: {}", obfuscateEmail(email));

        // Acquire rate limiter permission (blocks if necessary to enforce rate limit)
        rateLimiter.acquirePermission();

        HttpEntity<String> entity = new HttpEntity<>(createHeaders());
        String url = leakCheckApiUrl + "/query/" + email;

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody();
        }

        LOGGER.error("Failed to check email: {}", email);
        return "{}";
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-Key", leakCheckApiKey);
        return headers;
    }

    private int processLeakResponseForEmployee(Employee employee, String email, String responseBody,
            Map<LeakType, Integer> vulnerabilityStats) {
        return processLeakResponse(employee, email, responseBody, vulnerabilityStats);
    }

    int processLeakResponse(Employee employee, String email, String responseBody,
            Map<LeakType, Integer> vulnerabilityStats) {
        try {
            ObjectMapper objectMapper = this.objectMapper;
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

    private LocalDate parseBreachDate(String breachDate) {
        if (StringUtils.isBlank(breachDate)) {
            return null;
        }
        return LocalDate.parse(breachDate + "-01", FORMATTER);
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

    private String buildKey(List<String> values) {
        return values.stream()
                .map(v -> v == null ? "" : v)
                .collect(Collectors.joining("-"));
    }

    private void saveEmailLeakHistory(Employee employee, String email, String apiResponse) {
        EmailLeakHistory history = new EmailLeakHistory();
        history.setEmployee(employee);
        history.setEmail(email);
        history.setCreatedAt(LocalDateTime.now());
        history.setModifiedAt(LocalDateTime.now());
        history.setApiResponse(apiResponse);
        emailLeakHistoryRepository.save(history);
    }

    String obfuscateEmail(String email) {
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

    boolean isValidEmail(String email) {
        if (StringUtils.isBlank(email)) {
            return false;
        }

        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return Pattern.compile(emailRegex).matcher(email).matches();
    }

    /**
     * Parses leak check API response into a structured DTO for single email checks.
     * Does not save anything to database.
     *
     * @param email the email that was checked
     * @param responseBody the API response JSON
     * @return structured leak check result
     */
    SingleEmailLeakCheckDto parseSingleEmailResponse(String email, String responseBody) {
        try {
            LeakResponseDto leakResponse = objectMapper.readValue(responseBody, LeakResponseDto.class);

            if (!leakResponse.success() || leakResponse.result() == null || leakResponse.result().isEmpty()) {
                return SingleEmailLeakCheckDto.noLeaks(email);
            }

            List<SingleEmailLeakCheckDto.LeakDetailDto> leakDetails = leakResponse.result().stream()
                    .map(this::mapToLeakDetail)
                    .toList();

            return new SingleEmailLeakCheckDto(
                    email,
                    true,
                    leakDetails.size(),
                    leakDetails
            );
        } catch (Exception e) {
            LOGGER.error("Error parsing leak response for email {}: {}", obfuscateEmail(email), e.getMessage());
            return SingleEmailLeakCheckDto.noLeaks(email);
        }
    }

    private SingleEmailLeakCheckDto.LeakDetailDto mapToLeakDetail(LeakDto leak) {
        LeakType leakType = determineLeakType(leak);
        String breachDate = leak.source().breachDate();
        boolean hasPassword = StringUtils.isNotBlank(leak.password());
        List<String> origins = leak.origin() != null ? leak.origin() : List.of();

        return new SingleEmailLeakCheckDto.LeakDetailDto(
                leak.source().name(),
                breachDate,
                leakType,
                hasPassword,
                origins
        );
    }
}