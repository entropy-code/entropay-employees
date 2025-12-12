package com.entropyteam.entropay.summary.dtos;

import java.time.LocalDateTime;
import java.util.UUID;
import com.entropyteam.entropay.summary.model.EmployeeSummary;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;


public record EmployeeSummaryDto (

        UUID id,

        @NotNull(message = "Prompt is mandatory")
    String prompt,

        @NotNull(message = "Summary text is mandatory")
    String summaryText,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    String createdBy,


        LocalDateTime createdAt, String employeeName) {

    public EmployeeSummaryDto(EmployeeSummary summary) {
            this(
                    summary.getId(),
                    summary.getPrompt(),
                    summary.getSummaryText(),
                    summary.getCreatedBy(),
                    summary.getCreatedAt(),
                    summary.getEmployee() != null ? summary.getEmployee().getFullName() : null
            );
        }

}

