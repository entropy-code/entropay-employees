package com.entropyteam.entropay.notifications;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.entropyteam.entropay.notifications.services.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/slack/test")
public class SlackTestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SlackTestController.class);
    private final NotificationService notificationService;

    @Autowired
    public SlackTestController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/{status}")
    public ResponseEntity<String> testSlackNotification(@PathVariable String status) {
        String returnMessage;
        try {
            SlackAlertStatus alertStatus = SlackAlertStatus.valueOf(status.toUpperCase());
            String alertMessage = String.format("Slack test message for status: [%s]", alertStatus.name());

            notificationService.sendSlackNotification(new AlertMessageDto(
                    "Slack Test Feature",
                    alertMessage,
                    alertStatus
            ));
            returnMessage = String.format("Slack notification sent successfully with status: %s", alertStatus.name());
            LOGGER.info(returnMessage);
            return ResponseEntity.ok(returnMessage);
        } catch (IllegalArgumentException e) {
            returnMessage = "Invalid status received: " + status + ". Use: SUCCESS, WARNING, ERROR, INFO";
            LOGGER.error(returnMessage);
            return ResponseEntity.badRequest().body(returnMessage);
        }
    }
}