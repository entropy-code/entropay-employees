package com.entropyteam.entropay.employees.project.dtos;

import java.util.UUID;
import com.entropyteam.entropay.employees.project.models.ProjectType;

public record ProjectTypeDto(
        UUID id,
        String name) {

    public ProjectTypeDto(ProjectType projectType) {
        this(projectType.getId(), projectType.getName());
    }
}
