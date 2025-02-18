package com.entropyteam.entropay.security.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.entropyteam.entropay.employees.models.Employee;
import com.entropyteam.entropay.employees.repositories.EmployeeRepository;
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
    private final EmployeeRepository employeeRepository;
    private final EmailLeakHistoryRepository emailLeakHistoryRepository;
    private final EmailVulnerabilityRepository emailVulnerabilityRepository;
    private final RestTemplate restTemplate;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Value("${leakcheck.api.key}")
    private String leakCheckApiKey;

    @Value("${leakcheck.api.url}")
    private String leakCheckApiUrl;

    public EmailLeakCheckService(EmployeeRepository employeeRepository,
            EmailLeakHistoryRepository emailLeakHistoryRepository,
            EmailVulnerabilityRepository emailVulnerabilityRepository,
            RestTemplate restTemplate) {
        this.employeeRepository = employeeRepository;
        this.emailLeakHistoryRepository = emailLeakHistoryRepository;
        this.emailVulnerabilityRepository = emailVulnerabilityRepository;
        this.restTemplate = restTemplate;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void checkEmailsForLeaks() {
        LOGGER.info("Starting Email Leak Checker process");
        List<Employee> employees = employeeRepository.findAllByDeletedIsFalseAndActiveIsTrue();

        for (Employee e : employees) {
            if ("juanjoalbo@gmail.com".equalsIgnoreCase(e.getLabourEmail())) {
                checkEmailLeak(e);
                break;
            }
        }
    }

    private void checkEmailLeak(Employee employee) {
        String email = employee.getLabourEmail();
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-Key", leakCheckApiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = leakCheckApiUrl + "/query/" + email;
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        EmailLeakHistory history = new EmailLeakHistory();
        history.setEmail(email);
        history.setEmployee(employee);
        history.setCreatedAt(LocalDateTime.now());
        history.setModifiedAt(LocalDateTime.now());
        history.setApiResponse(response.getBody());
        emailLeakHistoryRepository.save(history);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            processLeakResponse(employee, response.getBody());
        } else {
            System.err.println("Failed to check email: " + email);
        }
    }

    private void processLeakResponse(Employee employee, String responseBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            LeakResponseDto leakResponse = objectMapper.readValue(responseBody, LeakResponseDto.class);

            List<EmailVulnerability> existingLeaks =
                    emailVulnerabilityRepository.findAllByEmployeeAndDeletedFalse(employee);

            Map<String, EmailVulnerability> existingLeaksMap = existingLeaks.stream()
                    .collect(Collectors.toMap(
                            this::generateLeakKey,
                            v -> v
                    ));

            if (leakResponse.isSuccess() && leakResponse.getResult() != null) {
                List<LeakDto> leaks = leakResponse.getResult();

                for (LeakDto leak : leaks) {
                    LeakType leakType = determineLeakType(leak);

                    String key = generateLeakKey(leak, leakType);

                    if (!existingLeaksMap.containsKey(key)) {
                        switch (leakType) {
                            case STEALER_LOGS_LEAK:
                                handleStealerLogsLeak(employee, leak, existingLeaksMap);
                                break;

                            case SITE_LEAK:
                                handleSiteLeak(employee, leak, existingLeaksMap);
                                break;

                            case UNKNOWN_LEAK:
                                handleUnknownLeak(employee, leak, existingLeaksMap);
                                break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error parsing leak response: " + e.getMessage());
        }
    }
    private String generateLeakKey(EmailVulnerability vulnerability) {
        switch (vulnerability.getLeakType()) {
            case STEALER_LOGS_LEAK:
                return String.join("-",
                        vulnerability.getEmail(),
                        vulnerability.getSourceName(),
                        Optional.ofNullable(vulnerability.getPassword()).orElse(""),
                        Optional.ofNullable(vulnerability.getOrigin()).orElse("")
                );

            case SITE_LEAK:
                return String.join("-",
                        vulnerability.getEmail(),
                        vulnerability.getSourceName(),
                        Optional.ofNullable(vulnerability.getBreachDate())
                                .map(LocalDate::toString)
                                .orElse("")
                );

            case UNKNOWN_LEAK:
                return String.join("-",
                        vulnerability.getEmail(),
                        vulnerability.getSourceName(),
                        Optional.ofNullable(vulnerability.getPassword()).orElse("")
                );

            default:
                throw new IllegalArgumentException("Unrecognized LeakType: " + vulnerability.getLeakType());
        }
    }


    private String generateLeakKey(LeakDto leak, LeakType leakType) {
        switch (leakType) {
            case STEALER_LOGS_LEAK:
                return String.join("-",
                        leak.getEmail(),
                        leak.getSource().getName(),
                        Optional.ofNullable(leak.getPassword()).orElse(""),
                        String.join(", ", Optional.ofNullable(leak.getOrigin()).orElse(Collections.emptyList()))
                );

            case SITE_LEAK:
                return String.join("-",
                        leak.getEmail(),
                        leak.getSource().getName(),
                        StringUtils.isNotBlank(leak.getSource().getBreachDate())
                                ? leak.getSource().getBreachDate() + "-01" : "");

            case UNKNOWN_LEAK:
                return String.join("-",
                        leak.getEmail(),
                        leak.getSource().getName(),
                        Optional.ofNullable(leak.getPassword()).orElse("")
                );

            default:
                throw new IllegalArgumentException("Unrecognized LeakType: " + leakType);
        }
    }

    private LeakType determineLeakType(LeakDto leak) {
        if (STEALER_LOGS.equalsIgnoreCase(leak.getSource().getName()) &&
                StringUtils.isNotBlank(leak.getPassword()) &&
                CollectionUtils.isNotEmpty(leak.getOrigin())) {
            return LeakType.STEALER_LOGS_LEAK;
        } else if (StringUtils.isNotBlank(leak.getSource().getBreachDate())) {
            return LeakType.SITE_LEAK;
        } else {
            return LeakType.UNKNOWN_LEAK;
        }
    }

    private void handleStealerLogsLeak(Employee employee, LeakDto leak, Map<String, EmailVulnerability> existingLeaksMap) {
        String key = generateLeakKey(leak, LeakType.STEALER_LOGS_LEAK);

        if (!existingLeaksMap.containsKey(key)) {
            EmailVulnerability vulnerability = new EmailVulnerability();
            vulnerability.setEmployee(employee);
            vulnerability.setEmail(leak.getEmail());
            vulnerability.setSourceName(STEALER_LOGS);
            vulnerability.setPassword(leak.getPassword());
            vulnerability.setOrigin(String.join(", ", leak.getOrigin()));
            vulnerability.setDetectedAt(LocalDateTime.now());
            vulnerability.setStatus(VulnerabilityStatus.DETECTED);
            vulnerability.setLeakType(LeakType.STEALER_LOGS_LEAK);

            emailVulnerabilityRepository.save(vulnerability);
            existingLeaksMap.put(key, vulnerability);
        }
    }

    private void handleSiteLeak(Employee employee, LeakDto leak, Map<String, EmailVulnerability> existingLeaksMap) {
        String key = generateLeakKey(leak, LeakType.SITE_LEAK);

        if (!existingLeaksMap.containsKey(key)) {
            EmailVulnerability vulnerability = new EmailVulnerability();
            vulnerability.setEmployee(employee);
            vulnerability.setEmail(leak.getEmail());
            vulnerability.setSourceName(leak.getSource().getName());
            vulnerability.setBreachDate(parseBreachDate(leak.getSource().getBreachDate()));
            vulnerability.setDetectedAt(LocalDateTime.now());
            vulnerability.setStatus(VulnerabilityStatus.DETECTED);
            vulnerability.setLeakType(LeakType.SITE_LEAK);

            emailVulnerabilityRepository.save(vulnerability);
            existingLeaksMap.put(key, vulnerability);
        }
    }

    private void handleUnknownLeak(Employee employee, LeakDto leak, Map<String, EmailVulnerability> existingLeaksMap) {
        String key = generateLeakKey(leak, LeakType.UNKNOWN_LEAK);

        if (!existingLeaksMap.containsKey(key)) {
            EmailVulnerability vulnerability = new EmailVulnerability();
            vulnerability.setEmployee(employee);
            vulnerability.setEmail(leak.getEmail());
            vulnerability.setSourceName(leak.getSource().getName());
            vulnerability.setPassword(leak.getPassword());
            vulnerability.setDetectedAt(LocalDateTime.now());
            vulnerability.setStatus(VulnerabilityStatus.DETECTED);
            vulnerability.setLeakType(LeakType.UNKNOWN_LEAK);

            emailVulnerabilityRepository.save(vulnerability);
            existingLeaksMap.put(key, vulnerability);
        }
    }


    public static LocalDate parseBreachDate(String breachDate) {
        if (StringUtils.isBlank(breachDate)) {
            return null;
        }
        return LocalDate.parse(breachDate + "-01", FORMATTER);
    }

}