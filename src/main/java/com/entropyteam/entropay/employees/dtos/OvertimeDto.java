package com.entropyteam.entropay.employees.dtos;

import com.entropyteam.entropay.employees.models.Overtime;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record OvertimeDto(UUID id,
                          float hours,
                          String description,
                          @JsonFormat(pattern = "yyyy-MM-dd")
                          LocalDate date,
                          @NotNull(message ="EmployeeId is mandatory")
                          UUID employeeId,
                          @NotNull(message ="AssignmentId is mandatory")
                          UUID assignmentId,
                          boolean deleted,
                          @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                          LocalDateTime createdAt,
                          @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                          LocalDateTime modifiedAt)  {

    public OvertimeDto (Overtime overtime){
        this(overtime.getId(), overtime.getHours(), overtime.getDescription(), overtime.getDate(),
                overtime.getEmployee().getId(), overtime.getAssignment().getId(),
                overtime.isDeleted(), overtime.getCreatedAt(), overtime.getModifiedAt());
    }
}
