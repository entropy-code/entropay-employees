package com.entropyteam.entropay.employees.dtos;

import com.entropyteam.entropay.employees.models.Vacation;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

public record VacationDto(
                            @NotNull
                            String year,
                            Integer credit,
                            Integer debit,
                            @NotNull
                            UUID employeeId,
                            boolean deleted,
                            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime createdAt,
                            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime modifiedAt) {

    public VacationDto(Vacation vacation) {
        this(vacation.getYear(), vacation.getCredit(), vacation.getDebit(), vacation.getEmployee().getId(),
                vacation.isDeleted(), vacation.getCreatedAt(), vacation.getCreatedAt());
    }
}
