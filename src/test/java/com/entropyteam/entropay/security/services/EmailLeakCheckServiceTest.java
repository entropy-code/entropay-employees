package com.entropyteam.entropay.security.services;

import com.entropyteam.entropay.employees.models.Employee;
import com.entropyteam.entropay.employees.repositories.EmployeeRepository;
import com.entropyteam.entropay.notifications.AlertMessageDto;
import com.entropyteam.entropay.notifications.SlackAlertStatus;
import com.entropyteam.entropay.notifications.services.NotificationService;
import com.entropyteam.entropay.security.dtos.LeakDto;
import com.entropyteam.entropay.security.dtos.LeakResponseDto;
import com.entropyteam.entropay.security.dtos.SourceDto;
import com.entropyteam.entropay.security.enums.LeakType;
import com.entropyteam.entropay.security.models.EmailLeakHistory;
import com.entropyteam.entropay.security.models.EmailVulnerability;
import com.entropyteam.entropay.security.repositories.EmailLeakHistoryRepository;
import com.entropyteam.entropay.security.repositories.EmailVulnerabilityRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class EmailLeakCheckServiceTest {

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

    private EmailLeakCheckService emailLeakCheckService;

    private Employee employee;
    private final String testApiUrl = "https://api.leakcheck.com";
    private final String testApiKey = "test-api-key";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        emailLeakCheckService = new EmailLeakCheckService(
                employeeRepository,
                emailLeakHistoryRepository,
                emailVulnerabilityRepository,
                notificationService,
                restTemplate,
                testApiUrl,
                testApiKey
        );

        employee = new Employee();
        employee.setFirstName("Juanjo");
        employee.setLastName("Munoz");
        employee.setLabourEmail("juanjo.munoz@company.com");
        employee.setPersonalEmail("juanjo.munoz@home.com");
    }

    @Test
    void shouldStartEmailCheckProcessAndSendSlackNotification() {
        when(employeeRepository.findAllByDeletedIsFalseAndActiveIsTrue()).thenReturn(List.of(employee));

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(String.class)))
                .thenReturn(ResponseEntity.ok("{}"));

        emailLeakCheckService.runAsyncEmailCheck();

        ArgumentCaptor<AlertMessageDto> messageCaptor = ArgumentCaptor.forClass(AlertMessageDto.class);
        verify(notificationService, times(2)).sendSlackNotification(messageCaptor.capture());

        List<AlertMessageDto> sentMessages = messageCaptor.getAllValues();

        AlertMessageDto startMessage = sentMessages.get(0);
        assertEquals("Email Leak Checker", startMessage.feature());
        assertTrue(startMessage.message().contains("The daily email leak scan has started."));
        assertEquals(SlackAlertStatus.INFO, startMessage.status());

        AlertMessageDto finalMessage = sentMessages.get(1);
        assertEquals("Email Leak Checker", finalMessage.feature());
        assertTrue(finalMessage.message().contains("Email Leak Check Completed"));
        assertNotNull(finalMessage.status());

        verify(restTemplate, times(2)).exchange(anyString(), eq(HttpMethod.GET), any(), eq(String.class));
        verify(emailLeakHistoryRepository, times(2)).save(any(EmailLeakHistory.class));
        verify(notificationService, times(2)).sendSlackNotification(any(AlertMessageDto.class));
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
        int newLeaks = emailLeakCheckService.processLeakResponse(employee,
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
        int newLeaks = emailLeakCheckService.processLeakResponse(employee,
                employee.getLabourEmail(), jsonResponse, vulnerabilityStats);

        verify(emailVulnerabilityRepository, never()).save(any(EmailVulnerability.class));
        assertEquals(0, newLeaks);
    }

    @Test
    void shouldHandleEmptyOrInvalidResponseCorrectly() {
        String emptyResponse = "{}";

        Map<LeakType, Integer> vulnerabilityStats = new EnumMap<>(LeakType.class);
        int newLeaks = emailLeakCheckService.processLeakResponse(employee,
                employee.getLabourEmail(), emptyResponse, vulnerabilityStats);

        verify(emailVulnerabilityRepository, never()).save(any(EmailVulnerability.class));
        assertEquals(0, newLeaks);
    }

    @Test
    void shouldReturnTrueForValidEmails() {
        Assertions.assertTrue(emailLeakCheckService.isValidEmail("juan.perez@example.com"));
        Assertions.assertTrue(emailLeakCheckService.isValidEmail("test@domain.co"));
        Assertions.assertTrue(emailLeakCheckService.isValidEmail("user123@sub.domain.com"));
        Assertions.assertTrue(emailLeakCheckService.isValidEmail("u.ser+alias@mail.org"));
    }

    @Test
    void shouldReturnFalseForInvalidEmails() {
        Assertions.assertFalse(emailLeakCheckService.isValidEmail("juan.perez@"));
        Assertions.assertFalse(emailLeakCheckService.isValidEmail("@example.com"));
        Assertions.assertFalse(emailLeakCheckService.isValidEmail("juan.perezexample.com"));
        Assertions.assertFalse(emailLeakCheckService.isValidEmail("juan@@example.com"));
        Assertions.assertFalse(emailLeakCheckService.isValidEmail("juan@.com"));
        Assertions.assertFalse(emailLeakCheckService.isValidEmail("juan @ test.com"));
        Assertions.assertFalse(emailLeakCheckService.isValidEmail("juan@domain.c"));
        Assertions.assertFalse(emailLeakCheckService.isValidEmail("juan@domain."));
    }

    @Test
    void shouldReturnFalseForNullOrBlank() {
        Assertions.assertFalse(emailLeakCheckService.isValidEmail(null));
        Assertions.assertFalse(emailLeakCheckService.isValidEmail(""));
        Assertions.assertFalse(emailLeakCheckService.isValidEmail("   "));
    }

}