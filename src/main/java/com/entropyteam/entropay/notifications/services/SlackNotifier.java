package com.entropyteam.entropay.notifications.services;

import com.entropyteam.entropay.config.NotificationConfig;
import com.entropyteam.entropay.notifications.AlertMessageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.nio.charset.StandardCharsets;

@Service
public class SlackNotifier {

    private static final Logger LOGGER = LoggerFactory.getLogger(SlackNotifier.class);
    private final NotificationConfig config;
    private final HttpClient httpClient;

    @Autowired
    public SlackNotifier(NotificationConfig config, HttpClient httpClient) {
        this.config = config;
        this.httpClient = httpClient;
    }

    public void sendNotification(AlertMessageDto alert) {
        String webhookUrl = config.getSlackWebhookUrl();
        if (webhookUrl == null || webhookUrl.isBlank()) {
            LOGGER.info("Missing Slack webhook URL");
            return;
        }

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(webhookUrl))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(alert.toJsonPayload(), StandardCharsets.UTF_8))
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