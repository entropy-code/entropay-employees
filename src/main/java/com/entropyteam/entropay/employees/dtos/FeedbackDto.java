package com.entropyteam.entropay.employees.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import com.entropyteam.entropay.employees.models.EmployeeFeedback;
import com.entropyteam.entropay.employees.models.FeedbackSource;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotNull;

public record FeedbackDto(
        UUID id,

        @NotNull(message = "Employee ID is mandatory")
        UUID employeeId,

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

        boolean deleted,

        String employeeName) {

    public FeedbackDto(EmployeeFeedback employeeFeedback) {
        this(
                employeeFeedback.getId(),
                employeeFeedback.getEmployee().getId(),
                employeeFeedback.getCreatedBy(),
                employeeFeedback.getFeedbackDate(),
                employeeFeedback.getSource(),
                employeeFeedback.getTitle(),
                employeeFeedback.getText(),
                employeeFeedback.getCreatedAt(),
                employeeFeedback.getModifiedAt(),
                employeeFeedback.isDeleted(),
                employeeFeedback.getEmployee().getFullName()
        );
    }
}