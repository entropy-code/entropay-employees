package com.entropyteam.entropay.employees.services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import com.entropyteam.entropay.employees.dtos.FeedbackDto;

@Service
public class EmployeeAIService {

    private final ChatModel chatModel;
    private final Resource promptTemplate;

    public EmployeeAIService(
            ChatModel chatModel,
            @Value("classpath:/prompts/feedback-summary.st") Resource promptTemplate) {
        this.chatModel = chatModel;
        this.promptTemplate = promptTemplate;
    }

    /**
     * Generate a comprehensive summary of employee feedbacks using AI.
     * Uses Spring AI's PromptTemplate pattern for maintainable, testable prompts.
     *
     * @param feedbacks List of feedback DTOs for an employee
     * @param employeeName Name of the employee
     * @return AI-generated summary of all feedbacks
     * @throws IllegalArgumentException if feedbacks list is null or empty
     */
    public String generateFeedbackSummary(List<FeedbackDto> feedbacks, String employeeName) {
        if (feedbacks == null || feedbacks.isEmpty()) {
            throw new IllegalArgumentException("Feedbacks list cannot be null or empty");
        }

        PromptTemplate template = new PromptTemplate(promptTemplate);

        Map<String, Object> model = Map.of(
            "employeeName", employeeName,
            "feedbackCount", feedbacks.size(),
            "feedbacks", formatFeedbacks(feedbacks)
        );

        Prompt prompt = template.create(model);
        return chatModel.call(prompt).getResult().getOutput().getText();
    }

    /**
     * Formats feedbacks into a readable string for the AI prompt.
     *
     * @param feedbacks List of feedback DTOs
     * @return Formatted string with all feedbacks
     */
    private String formatFeedbacks(List<FeedbackDto> feedbacks) {
        return feedbacks.stream()
            .map(feedback -> String.format("""
                --- Feedback ---
                Date: %s
                Source: %s
                Title: %s
                Feedback: %s

                """,
                feedback.feedbackDate(),
                feedback.source(),
                feedback.title(),
                feedback.text()))
            .collect(Collectors.joining("\n"));
    }
}