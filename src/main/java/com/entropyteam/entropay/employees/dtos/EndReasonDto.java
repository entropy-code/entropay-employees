package com.entropyteam.entropay.employees.dtos;

import java.util.UUID;

import com.entropyteam.entropay.employees.models.EndReason;

import jakarta.validation.constraints.NotNull;

public record EndReasonDto(UUID id,
                           @NotNull(message = "Name is mandatory")
                           String name) {

    public EndReasonDto(EndReason endReason) {
        this(endReason.getId(), endReason.getName());
    }

}
