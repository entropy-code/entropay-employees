package com.entropyteam.entropay.notifications.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.entropyteam.entropay.notifications.AlertMessageDto;

@Service
public class NotificationService {

    private final SlackNotifier slackNotifier;

    @Autowired
    public NotificationService(SlackNotifier slackNotifier) {
        this.slackNotifier = slackNotifier;
    }

    public void sendSlackNotification(AlertMessageDto alert) {
        slackNotifier.sendNotification(alert);
    }
}
