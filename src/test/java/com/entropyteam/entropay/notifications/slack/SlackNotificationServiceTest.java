package com.entropyteam.entropay.notifications.slack;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import com.entropyteam.entropay.notifications.MessageDto;
import com.entropyteam.entropay.notifications.MessageType;
import com.entropyteam.entropay.notifications.slack.SlackNotificationService;

@ExtendWith(MockitoExtension.class)
public class SlackNotificationServiceTest {

    @Mock
    private HttpClient httpClient;

    @Mock
    private HttpResponse<String> mockResponse;


    @Test
    void shouldSendMessageToSlack() throws Exception {
        // Given
        String webhookUrl = "https://hooks.slack.com/services/test-webhook";
        SlackNotificationService slackNotificationService = new SlackNotificationService(webhookUrl, httpClient);
        when(mockResponse.statusCode()).thenReturn(200);

        when(httpClient.send(Mockito.any(HttpRequest.class), Mockito.any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        MessageDto alert = new MessageDto("Test Feature",
                "This is a test", MessageType.INFO);

        // When
        slackNotificationService.sendNotification(alert);

        // Then
        ArgumentCaptor<HttpRequest> requestCaptor = ArgumentCaptor.forClass(HttpRequest.class);
        verify(httpClient, times(1)).send(requestCaptor.capture(),
                Mockito.any(HttpResponse.BodyHandler.class));

        HttpRequest capturedRequest = requestCaptor.getValue();
        Assertions.assertEquals(URI.create(webhookUrl), capturedRequest.uri());
    }
}