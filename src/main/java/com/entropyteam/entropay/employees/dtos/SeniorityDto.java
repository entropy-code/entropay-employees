package com.entropyteam.entropay.employees.dtos;

import java.util.UUID;

import com.entropyteam.entropay.employees.models.Seniority;

import jakarta.validation.constraints.NotNull;

public record SeniorityDto(UUID id,
                           @NotNull(message = "Name is mandatory")
                           String name,
                           @NotNull(message = "Vacation days is mandatory")
                           Integer vacationDays) {

    public SeniorityDto(Seniority seniority) {
        this(seniority.getId(), seniority.getName(), seniority.getVacationDays());
    }
}
