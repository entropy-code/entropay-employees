package com.entropyteam.entropay.summary.services;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class SummaryService {

    private final ChatClient chatClient;

    public SummaryService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public String generateSummary(String employeeName, List<String> feedbacks) {
        String feedbackText = String.join("\n", feedbacks);
        String prompt = String.format(
                "Resume el siguiente feedback para el empleado %s en un párrafo claro y profesional:\n%s",
                employeeName, feedbackText
        );

        return chatClient.prompt(prompt).call().content();
    }
}
