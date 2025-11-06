package com.entropyteam.entropay.employees.leakcheck;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import com.entropyteam.entropay.employees.models.Employee;
import com.entropyteam.entropay.employees.repositories.EmployeeRepository;
import com.entropyteam.entropay.notifications.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.resilience4j.ratelimiter.RateLimiter;

class EmailLeakProcessorTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmailLeakHistoryRepository emailLeakHistoryRepository;

    @Mock
    private EmailVulnerabilityRepository emailVulnerabilityRepository;

    @Mock
    private NotificationService notificationService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private RateLimiter rateLimiter;

    private EmailLeakProcessor emailLeakProcessor;

    private Employee employee;
    private final String testApiUrl = "https://api.leakcheck.com";
    private final String testApiKey = "test-api-key";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock rate limiter to not block during tests - acquirePermission returns boolean
        when(rateLimiter.acquirePermission()).thenReturn(true);

        emailLeakProcessor = new EmailLeakProcessor(
                emailLeakHistoryRepository,
                emailVulnerabilityRepository,
                restTemplate,
                notificationService,
                rateLimiter
        );

        employee = new Employee();
        employee.setFirstName("Juanjo");
        employee.setLastName("Munoz");
        employee.setLabourEmail("juanjo.munoz@company.com");
        employee.setPersonalEmail("juanjo.munoz@home.com");
    }

    @Test
    void shouldPerformEmailCheckProcessForBothEmployeeEmail() {
        when(employeeRepository.findAllByDeletedIsFalseAndActiveIsTrue()).thenReturn(List.of(employee));

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(String.class)))
                .thenReturn(ResponseEntity.ok("{}"));

        when(emailVulnerabilityRepository.findAllByEmployeeAndDeletedFalse(any(Employee.class)))
                .thenReturn(Collections.emptyList());

        LeakCheckResult result = emailLeakProcessor.processEmployeeLeaksAsync(employee).join();

        verify(restTemplate, times(2)).exchange(anyString(), eq(HttpMethod.GET), any(), eq(String.class));
        verify(emailLeakHistoryRepository, times(2)).save(any(EmailLeakHistory.class));
        verify(rateLimiter, times(2)).acquirePermission();

        // Verify result
        assertEquals(employee.getFullName(), result.employeeName());
        assertEquals(0, result.totalLeaks());
    }

    @Test
    void shouldProcessLeakResponseAndSaveVulnerability() throws Exception {
        LeakDto mockLeak = new LeakDto("juanjo.munoz@company.com",
                new SourceDto("TestSource", "2024-01"),
                "compromised123",
                List.of("Origin1"));

        LeakResponseDto mockResponse = new LeakResponseDto(true, List.of(mockLeak));

        String jsonResponse = new ObjectMapper().writeValueAsString(mockResponse);
        when(restTemplate.exchange(anyString(), any(), any(), eq(String.class)))
                .thenReturn(ResponseEntity.ok(jsonResponse));

        when(emailVulnerabilityRepository.findAllByEmployeeAndDeletedFalse(any(Employee.class)))
                .thenReturn(Collections.emptyList());

        Map<LeakType, Integer> vulnerabilityStats = new EnumMap<>(LeakType.class);
        int newLeaks = emailLeakProcessor.processLeakResponse(employee,
                employee.getLabourEmail(), jsonResponse, vulnerabilityStats);

        verify(emailVulnerabilityRepository, times(1)).save(any(EmailVulnerability.class));

        assertEquals(1, vulnerabilityStats.get(LeakType.SITE_LEAK));
        assertEquals(1, newLeaks);
    }

    @Test
    void shouldNotSaveDuplicateVulnerability() throws Exception {
        EmailVulnerability existingVulnerability = new EmailVulnerability();
        existingVulnerability.setEmail(employee.getLabourEmail());
        existingVulnerability.setSourceName("TestSource");

        when(emailVulnerabilityRepository.findAllByEmployeeAndDeletedFalse(any(Employee.class)))
                .thenReturn(List.of(existingVulnerability));

        LeakDto mockLeak = new LeakDto("juanjo.munoz@company.com",
                new SourceDto("TestSource", "2024-01"),
                "compromised123",
                List.of("Origin1"));

        LeakResponseDto mockResponse = new LeakResponseDto(true, List.of(mockLeak));

        String jsonResponse = new ObjectMapper().writeValueAsString(mockResponse);

        Map<LeakType, Integer> vulnerabilityStats = new EnumMap<>(LeakType.class);
        int newLeaks = emailLeakProcessor.processLeakResponse(employee,
                employee.getLabourEmail(), jsonResponse, vulnerabilityStats);

        verify(emailVulnerabilityRepository, never()).save(any(EmailVulnerability.class));
        assertEquals(0, newLeaks);
    }

    @Test
    void shouldHandleEmptyOrInvalidResponseCorrectly() {
        String emptyResponse = "{}";

        Map<LeakType, Integer> vulnerabilityStats = new EnumMap<>(LeakType.class);
        int newLeaks = emailLeakProcessor.processLeakResponse(employee,
                employee.getLabourEmail(), emptyResponse, vulnerabilityStats);

        verify(emailVulnerabilityRepository, never()).save(any(EmailVulnerability.class));
        assertEquals(0, newLeaks);
    }

    @Test
    void shouldReturnTrueForValidEmails() {
        Assertions.assertTrue(emailLeakProcessor.isValidEmail("juan.perez@example.com"));
        Assertions.assertTrue(emailLeakProcessor.isValidEmail("test@domain.co"));
        Assertions.assertTrue(emailLeakProcessor.isValidEmail("user123@sub.domain.com"));
        Assertions.assertTrue(emailLeakProcessor.isValidEmail("u.ser+alias@mail.org"));
    }

    @Test
    void shouldReturnFalseForInvalidEmails() {
        Assertions.assertFalse(emailLeakProcessor.isValidEmail("juan.perez@"));
        Assertions.assertFalse(emailLeakProcessor.isValidEmail("@example.com"));
        Assertions.assertFalse(emailLeakProcessor.isValidEmail("juan.perezexample.com"));
        Assertions.assertFalse(emailLeakProcessor.isValidEmail("juan@@example.com"));
        Assertions.assertFalse(emailLeakProcessor.isValidEmail("juan@.com"));
        Assertions.assertFalse(emailLeakProcessor.isValidEmail("juan @ test.com"));
        Assertions.assertFalse(emailLeakProcessor.isValidEmail("juan@domain.c"));
        Assertions.assertFalse(emailLeakProcessor.isValidEmail("juan@domain."));
    }

    @Test
    void shouldReturnFalseForNullOrBlank() {
        Assertions.assertFalse(emailLeakProcessor.isValidEmail(null));
        Assertions.assertFalse(emailLeakProcessor.isValidEmail(""));
        Assertions.assertFalse(emailLeakProcessor.isValidEmail("   "));
    }

}