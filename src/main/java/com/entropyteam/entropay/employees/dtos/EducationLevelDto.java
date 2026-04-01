package com.entropyteam.entropay.employees.dtos;

import java.util.UUID;
import com.entropyteam.entropay.employees.models.EducationLevel;

import jakarta.validation.constraints.NotNull;

public record EducationLevelDto(
        UUID id,
        @NotNull(message = "Education Level name is mandatory")
        String name) {

    public EducationLevelDto(EducationLevel educationLevel) {
        this(educationLevel.getId(), educationLevel.getName());
    }
}
