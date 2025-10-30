package com.entropyteam.entropay.security.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import com.entropyteam.entropay.employees.models.Employee;
import com.entropyteam.entropay.employees.repositories.EmployeeRepository;
import com.entropyteam.entropay.notifications.MessageDto;
import com.entropyteam.entropay.notifications.MessageType;
import com.entropyteam.entropay.notifications.NotificationService;

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

        ArgumentCaptor<MessageDto> messageCaptor = ArgumentCaptor.forClass(MessageDto.class);
        verify(notificationService, times(2)).sendNotification(messageCaptor.capture());

        List<MessageDto> sentMessages = messageCaptor.getAllValues();

        MessageDto startMessage = sentMessages.get(0);
        assertEquals("Email Leak Checker", startMessage.title());
        assertTrue(startMessage.message().contains("The daily email leak scan has started."));
        assertEquals(MessageType.INFO, startMessage.messageType());

        MessageDto finalMessage = sentMessages.get(1);
        assertEquals("Email Leak Checker", finalMessage.title());
        assertTrue(finalMessage.message().contains("Email Leak Check Completed"));
        assertNotNull(finalMessage.messageType());

        verify(notificationService, times(2)).sendNotification(any(MessageDto.class));
    }

}