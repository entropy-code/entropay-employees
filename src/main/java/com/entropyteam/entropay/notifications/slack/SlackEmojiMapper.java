package com.entropyteam.entropay.notifications.slack;

import com.entropyteam.entropay.notifications.MessageType;

/**
 * Maps MessageType to Slack emojis.
 */
public class SlackEmojiMapper {

    /**
     * Returns the Slack emoji for the given MessageType.
     *
     * @param messageType the message type
     * @return the Slack emoji
     */
    public static String getEmojiForMessageType(MessageType messageType) {
        return switch (messageType) {
            case SUCCESS -> ":white_check_mark:";
            case WARNING -> ":warning:";
            case ERROR -> ":x:";
            case INFO -> ":information_source:";
            case BIRTHDAY -> ":birthday:";
        };
    }
}