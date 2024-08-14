package com.entropyteam.entropay.employees.dtos;

import com.entropyteam.entropay.employees.models.Overtime;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record OvertimeDto(UUID id,
                          int hours,
                          String description,
                          @JsonFormat(pattern = "yyyy-MM-dd") LocalDate date,
                          UUID employeeId,
                          UUID assigmentId,
                          boolean deleted,
                          @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime createdAt,
                          @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime modifiedAt
                          )  {
    public OvertimeDto (Overtime overtime){
        this(overtime.getId(), overtime.getHours(), overtime.getDescription(), overtime.getDate(),
                overtime.getEmployee().getId(), overtime.getAssignment().getId(),
                overtime.isDeleted(), overtime.getCreatedAt(), overtime.getModifiedAt());
    }
}
