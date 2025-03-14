package com.entropyteam.entropay.notifications.services;

import com.entropyteam.entropay.config.NotificationConfig;
import com.entropyteam.entropay.notifications.AlertMessageDto;
import com.entropyteam.entropay.notifications.SlackAlertStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;

import org.junit.jupiter.api.Assertions;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class SlackNotifierTest {

    @Mock
    private NotificationConfig config;

    @Mock
    private HttpClient httpClient;

    @Mock
    private HttpResponse<String> mockResponse;

    @InjectMocks
    private SlackNotifier slackNotifier;

    @Test
    void shouldNotSendMessageWhenWebhookUrlIsMissing() {
        // Given
        when(config.getSlackWebhookUrl()).thenReturn(null);
        AlertMessageDto alert = new AlertMessageDto("Test Feature",
                "This is a test", SlackAlertStatus.INFO);

        // When
        slackNotifier.sendNotification(alert);

        // Then
        verifyNoInteractions(httpClient);
    }

    @Test
    void shouldSendMessageToSlack() throws Exception {
        // Given
        String webhookUrl = "https://hooks.slack.com/services/test-webhook";
        when(config.getSlackWebhookUrl()).thenReturn(webhookUrl);
        when(mockResponse.statusCode()).thenReturn(200);

        when(httpClient.send(Mockito.any(HttpRequest.class), Mockito.any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        AlertMessageDto alert = new AlertMessageDto("Test Feature",
                "This is a test", SlackAlertStatus.INFO);

        // When
        slackNotifier.sendNotification(alert);

        // Then
        ArgumentCaptor<HttpRequest> requestCaptor = ArgumentCaptor.forClass(HttpRequest.class);
        verify(httpClient, times(1)).send(requestCaptor.capture(),
                Mockito.any(HttpResponse.BodyHandler.class));

        HttpRequest capturedRequest = requestCaptor.getValue();
        Assertions.assertEquals(URI.create(webhookUrl), capturedRequest.uri());
    }
}