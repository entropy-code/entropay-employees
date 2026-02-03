package com.entropyteam.entropay.employees.dtos;

import java.util.UUID;
import com.entropyteam.entropay.employees.models.Benefit;

import jakarta.validation.constraints.NotNull;

public record BenefitDto(UUID id,
                         @NotNull(message = "Name is mandatory")
                         String name) {

    public BenefitDto(Benefit benefit) {
        this(benefit.getId(), benefit.getName());
    }
}
