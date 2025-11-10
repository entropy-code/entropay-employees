package com.entropyteam.entropay.summary.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

public class EmployeeSummaryDto {
    private UUID id;
    private String prompt;
    private String summaryText;
    private String createdBy;
    private LocalDateTime createdAt;
    private String employeeName;

    public EmployeeSummaryDto(UUID id, String prompt, String summaryText, String createdBy, LocalDateTime createdAt, String employeeName) {
        this.id = id;
        this.prompt = prompt;
        this.summaryText = summaryText;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.employeeName = employeeName;
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public String getPrompt() {
        return prompt;
    }

    public String getSummaryText() {
        return summaryText;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getEmployeeName() {
        return employeeName;
    }
}
