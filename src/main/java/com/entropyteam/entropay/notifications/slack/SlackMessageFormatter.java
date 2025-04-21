package com.entropyteam.entropay.notifications.slack;

import com.entropyteam.entropay.notifications.MessageDto;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Formats messages for Slack.
 */
public class SlackMessageFormatter {

    private static final Gson GSON = new Gson();

    /**
     * Formats a MessageDto for Slack.
     *
     * @param messageDto the message to format
     * @return the formatted message
     */
    public static String formatMessage(MessageDto messageDto) {
        String emoji = SlackEmojiMapper.getEmojiForMessageType(messageDto.messageType());
        return String.format(
                "*%s %s*\n%s\n\n",
                emoji,
                messageDto.title(),
                messageDto.message()
        );
    }

    /**
     * Converts a MessageDto directly to a Slack JSON payload.
     *
     * @param messageDto the message to convert
     * @return the JSON payload
     */
    public static String toJsonPayload(MessageDto messageDto) {
        String formattedMessage = formatMessage(messageDto);
        JsonObject jsonPayload = new JsonObject();
        jsonPayload.addProperty("text", formattedMessage);
        return GSON.toJson(jsonPayload);
    }
}