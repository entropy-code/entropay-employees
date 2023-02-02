package com.entropyteam.entropay.employees.dtos;

import java.util.UUID;
import com.entropyteam.entropay.employees.models.Technology;

public record TechnologyDto(UUID id, String name) {

    public TechnologyDto(Technology technology) {
        this(technology.getId(), technology.getName());
    }
}