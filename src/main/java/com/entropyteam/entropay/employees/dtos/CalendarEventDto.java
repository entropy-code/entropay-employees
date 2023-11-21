package com.entropyteam.entropay.employees.dtos;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public record CalendarEventDto(@NotNull String id,
                               @NotNull String description,
                               @NotNull LocalDate startDate,
                               @NotNull LocalDate endDate
) {
    public CalendarEventDto(String id, String description, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}