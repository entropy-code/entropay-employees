package com.entropyteam.entropay.notifications;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import com.entropyteam.entropay.notifications.slack.SlackNotificationService;


public class NotificationServiceTest {

    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        notificationService = Mockito.mock(SlackNotificationService.class);
    }

    @Test
    void shouldSendNotificationToSlack() {
        //Given
        MessageDto alert = new MessageDto("Test Feature",
                "Test message", MessageType.INFO);

        //When
        notificationService.sendNotification(alert);

        //Then
        verify(notificationService, times(1)).sendNotification(alert);
    }
}