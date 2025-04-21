package com.entropyteam.entropay.notifications.slack;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.entropyteam.entropay.notifications.MessageDto;
import com.entropyteam.entropay.notifications.NotificationService;
import com.google.common.base.Preconditions;

@Service
public class SlackNotificationService implements NotificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SlackNotificationService.class);
    private final String webhookUrl;
    private final HttpClient httpClient;

    @Autowired
    public SlackNotificationService(@Value("${notifications.slack.webhook.url}") String webhookUrl,
            HttpClient httpClient) {
        this.webhookUrl = Preconditions.checkNotNull(webhookUrl);
        this.httpClient = httpClient;
    }

    public void sendNotification(MessageDto messageDto) {
        try {
            String jsonPayload = SlackMessageFormatter.toJsonPayload(messageDto);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(webhookUrl))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload, StandardCharsets.UTF_8))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                LOGGER.info("Slack message sent successfully");
            } else {
                LOGGER.error("Failed to send Slack message. Response code: {}", response.statusCode());
            }
        } catch (Exception e) {
            LOGGER.error("Error sending Slack message", e);
        }
    }
}
