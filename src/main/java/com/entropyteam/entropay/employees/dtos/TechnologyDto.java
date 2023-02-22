package com.entropyteam.entropay.employees.dtos;

import java.util.UUID;
import com.entropyteam.entropay.employees.models.Technology;

import javax.validation.constraints.NotNull;

public record TechnologyDto(UUID id,
                            @NotNull(message = "Name is mandatory")
                            String name) {

    public TechnologyDto(Technology technology) {
        this(technology.getId(), technology.getName());
    }
}