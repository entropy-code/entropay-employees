package com.entropyteam.entropay.employees.dtos;

import com.entropyteam.entropay.employees.models.Country;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CountryDto(UUID id,
                         @NotNull(message = "Name is mandatory")
                         String name) {

    public CountryDto(Country country) {
        this(country.getId(), country.getName());
    }
}
