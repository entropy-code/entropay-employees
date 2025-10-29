package com.entropyteam.entropay.security.services;

import com.entropyteam.entropay.employees.models.Employee;
import com.entropyteam.entropay.employees.repositories.EmployeeRepository;
import com.entropyteam.entropay.notifications.AlertMessageDto;
import com.entropyteam.entropay.notifications.SlackAlertStatus;
import com.entropyteam.entropay.notifications.services.NotificationService;
import com.entropyteam.entropay.security.enums.LeakType;
import com.entropyteam.entropay.security.models.EmailLeakHistory;
import com.entropyteam.entropay.security.repositories.EmailLeakHistoryRepository;
import com.entropyteam.entropay.security.repositories.EmailVulnerabilityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

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
    private NotificationService notificationService;

    @Mock
    private EmailLeakProcessor emailLeakProcessor;

    @Mock
    private RestTemplate restTemplate;

    private EmailLeakCheckService emailLeakCheckService;

    private Employee employee;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        emailLeakCheckService = new EmailLeakCheckService(
                employeeRepository,
                notificationService,
                emailLeakProcessor
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

        verify(notificationService, times(2)).sendSlackNotification(any(AlertMessageDto.class));
    }

}