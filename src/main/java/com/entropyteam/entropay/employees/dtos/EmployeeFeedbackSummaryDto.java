package com.entropyteam.entropay.employees.dtos;

import java.time.LocalDateTime;
import java.util.UUID;
import com.entropyteam.entropay.employees.models.EmployeeFeedbackSummary;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

public record EmployeeFeedbackSummaryDto(
        UUID id,

        @NotNull(message = "Employee ID is mandatory")
        UUID employeeId,

        @NotNull(message = "Summary is mandatory")
        String summary,

        String createdBy,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdAt,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime modifiedAt,

        boolean deleted) {

    public EmployeeFeedbackSummaryDto(EmployeeFeedbackSummary summary) {
        this(
                summary.getId(),
                summary.getEmployee().getId(),
                summary.getSummary(),
                summary.getCreatedBy(),
                summary.getCreatedAt(),
                summary.getModifiedAt(),
                summary.isDeleted()
        );
    }
}
