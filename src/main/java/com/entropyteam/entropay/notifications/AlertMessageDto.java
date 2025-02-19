package com.entropyteam.entropay.notifications;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record AlertMessageDto(
        String feature,
        String message,
        SlackAlertStatus status,
        LocalDateTime timestamp) {
    public AlertMessageDto(String feature, String message, SlackAlertStatus status) {
        this(feature, message, status, LocalDateTime.now());
    }

    public String toSlackFormat() {
        return String.format(
                "*%s %s*\n%s\n\n*Timestamp:* %s",
                status.getSlackEmoji(),
                feature,
                message,
                timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );
    }

    public String toJsonPayload() {
        return "{ \"text\": \""
                + toSlackFormat().replace("\"", "\\\"") + "\" }";
    }
}