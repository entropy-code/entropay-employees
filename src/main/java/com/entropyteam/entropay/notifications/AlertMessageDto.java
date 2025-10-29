package com.entropyteam.entropay.notifications;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

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
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode payload = mapper.createObjectNode();
        payload.put("text", toSlackFormat());
        try {
            return mapper.writeValueAsString(payload);
        } catch (Exception e) {
            throw new RuntimeException("Error creating JSON payload", e);
        }
    }
}