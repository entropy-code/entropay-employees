package com.entropyteam.entropay.employees.dtos;

import java.util.UUID;
import com.entropyteam.entropay.employees.models.ProjectType;

import javax.validation.constraints.NotNull;

public record ProjectTypeDto(UUID id,
                            @NotNull(message = "Name is mandatory")
                            String name) {

    public ProjectTypeDto(ProjectType projectType) {
        this(projectType.getId(), projectType.getName());
    }
}
