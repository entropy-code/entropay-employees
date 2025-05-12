package com.entropyteam.entropay.employees.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import com.entropyteam.entropay.employees.models.ClientFeedback;
import com.entropyteam.entropay.employees.models.FeedbackSource;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotNull;

public record ClientFeedbackDto(
        UUID id,

        @NotNull(message = "Client ID is mandatory")
        UUID clientId,

        String createdBy,

        @NotNull(message = "Feedback date is mandatory")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate feedbackDate,

        @NotNull(message = "Source is mandatory")
        FeedbackSource source,

        @NotNull(message = "Title is mandatory")
        String title,

        @NotNull(message = "Text is mandatory")
        String text,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdAt,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime modifiedAt,

        boolean deleted) {

    public ClientFeedbackDto(ClientFeedback clientFeedback) {
        this(
                clientFeedback.getId(),
                clientFeedback.getClient().getId(),
                clientFeedback.getCreatedBy(),
                clientFeedback.getFeedbackDate(),
                clientFeedback.getSource(),
                clientFeedback.getTitle(),
                clientFeedback.getText(),
                clientFeedback.getCreatedAt(),
                clientFeedback.getModifiedAt(),
                clientFeedback.isDeleted()
        );
    }
}