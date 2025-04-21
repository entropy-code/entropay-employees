package com.entropyteam.entropay.notifications;

/**
 * Data transfer object for notification messages.
 */
public record MessageDto(String title, String message, MessageType messageType) {

}
