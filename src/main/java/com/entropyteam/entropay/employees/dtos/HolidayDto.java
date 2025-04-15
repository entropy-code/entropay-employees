package com.entropyteam.entropay.employees.dtos;

import com.entropyteam.entropay.employees.models.Holiday;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record HolidayDto(UUID id,
                         @NotNull
                         @JsonFormat(pattern = "yyyy-MM-dd")
                         LocalDate date,
                         @NotNull String description,
                         @NotNull UUID countryId,
                         boolean deleted,
                         @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                         LocalDateTime createdAt,
                         @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                         LocalDateTime modifiedAt) {

    public HolidayDto(Holiday holiday) {
        this(
                holiday.getId(),
                holiday.getDate(),
                holiday.getDescription(),
                holiday.getCountry().getId(),
                holiday.isDeleted(),
                holiday.getCreatedAt(),
                holiday.getModifiedAt()
        );
    }

}
