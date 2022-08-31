package com.entropyteam.entropay.employees.dtos;

import java.util.UUID;
import com.entropyteam.entropay.employees.models.ProjectType;

public record ProjectTypeDto(
        UUID id,
        String name) {

    public ProjectTypeDto(ProjectType projectType) {
        this(projectType.getId(), projectType.getName());
    }
}
