package com.entropyteam.entropay.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationConfig {
    @Value("${notifications.slack.webhook.url}")
    private String slackWebhookUrl;

    public String getSlackWebhookUrl() {
        return slackWebhookUrl;
    }

}