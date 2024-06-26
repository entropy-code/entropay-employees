package com.entropyteam.entropay.employees.dtos;

import com.entropyteam.entropay.employees.models.Vacation;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

public record VacationDto(  UUID id,
                            @NotNull
                            String year,
                            @NotNull
                            Integer credit,
                            Integer debit,
                            @NotNull
                            UUID employeeId,
                            boolean deleted,
                            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime createdAt,
                            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime modifiedAt,
                            String details) {

    public VacationDto(Vacation vacation) {
        this(vacation.getId(), vacation.getYear(), vacation.getCredit(), vacation.getDebit(), vacation.getEmployee().getId(),
                vacation.isDeleted(), vacation.getCreatedAt(), vacation.getCreatedAt(), vacation.getDetails());
    }
}
