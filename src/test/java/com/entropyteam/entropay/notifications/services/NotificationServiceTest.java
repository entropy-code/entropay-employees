package com.entropyteam.entropay.notifications.services;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.entropyteam.entropay.notifications.AlertMessageDto;
import com.entropyteam.entropay.notifications.SlackAlertStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


public class NotificationServiceTest {

    private SlackNotifier slackNotifier;
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        slackNotifier = Mockito.mock(SlackNotifier.class);
        notificationService = new NotificationService(slackNotifier);
    }

    @Test
    void shouldSendNotificationToSlack() {
        //Given
        AlertMessageDto alert = new AlertMessageDto("Test Feature",
                "Test message", SlackAlertStatus.INFO);

        //When
        notificationService.sendSlackNotification(alert);

        //Then
        verify(slackNotifier, times(1)).sendNotification(alert);
    }
}