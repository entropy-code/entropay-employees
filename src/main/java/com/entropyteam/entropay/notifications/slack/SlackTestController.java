package com.entropyteam.entropay.notifications.slack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.entropyteam.entropay.notifications.MessageDto;
import com.entropyteam.entropay.notifications.MessageType;
import com.entropyteam.entropay.notifications.NotificationService;

@RestController
@RequestMapping("/slack/test")
public class SlackTestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SlackTestController.class);
    private final NotificationService notificationService;

    @Autowired
    public SlackTestController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/{messageType}")
    public ResponseEntity<String> testSlackNotification(@PathVariable MessageType messageType) {
        String returnMessage;
        try {
            String message = String.format("Slack test message for type: [%s]", messageType.name());

            notificationService.sendNotification(new MessageDto(
                    "Slack Test Feature",
                    message,
                    messageType
            ));
            returnMessage = String.format("Slack notification sent successfully with messageType: %s", messageType.name());
            LOGGER.info(returnMessage);
            return ResponseEntity.ok(returnMessage);
        } catch (IllegalArgumentException e) {
            returnMessage = "Invalid messageType received: " + messageType + ". Use: SUCCESS, WARNING, ERROR, INFO";
            LOGGER.error(returnMessage);
            return ResponseEntity.badRequest().body(returnMessage);
        }
    }
}