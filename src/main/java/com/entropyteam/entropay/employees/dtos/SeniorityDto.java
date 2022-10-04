package com.entropyteam.entropay.employees.dtos;

import java.util.UUID;
import com.entropyteam.entropay.employees.models.Seniority;

public record SeniorityDto(UUID id, String name) {

    public SeniorityDto(Seniority seniority) {
        this(seniority.getId(), seniority.getName());
    }
}
