package com.entropyteam.entropay.notifications;

public enum SlackAlertStatus {
    SUCCESS(":white_check_mark:"),
    WARNING(":warning:"),
    ERROR(":x:"),
    INFO(":information_source:");

    private final String slackEmoji;

    SlackAlertStatus(String slackEmoji) {
        this.slackEmoji = slackEmoji;
    }

    public String getSlackEmoji() {
        return slackEmoji;
    }
}