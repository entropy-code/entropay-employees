package com.entropyteam.entropay.employees.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import com.entropyteam.entropay.employees.models.Project;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;

public record ProjectDto(
        UUID id,
        @NotNull(message = "Client is mandatory")
        UUID clientId,
        @NotNull(message = "Name is mandatory")
        String name,
        @NotNull(message = "Project TypeP is mandatory")
        UUID projectTypeId,
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
        String notes,
        boolean deleted,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime createdAt,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime modifiedAt) {

    public ProjectDto(Project project) {
        this(project.getId(), project.getClient().getId(), project.getName(), project.getProjectType().getId(),
                project.getStartDate(), project.getEndDate(), project.getNotes(),
                project.isDeleted(), project.getCreatedAt(), project.getModifiedAt());
    }
}
