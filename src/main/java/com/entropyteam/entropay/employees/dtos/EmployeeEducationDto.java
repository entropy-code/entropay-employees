package com.entropyteam.entropay.employees.dtos;

import java.time.LocalDateTime;
import java.util.UUID;
import com.entropyteam.entropay.employees.models.EmployeeEducation;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotNull;

public record EmployeeEducationDto(
        UUID id,
        @NotNull(message = "Education Level is mandatory")
        String level,
        String levelOther,
        @NotNull(message = "Educational Institution is mandatory")
        String institution,
        @NotNull(message = "Degree is mandatory")
        String degree,
        boolean deleted,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdAt,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime modifiedAt) {

    public EmployeeEducationDto(EmployeeEducation employeeEducation) {
        this(employeeEducation.getId(), employeeEducation.getLevel(),
                employeeEducation.getLevelOther(),
                employeeEducation.getInstitution(), employeeEducation.getDegree(),
                employeeEducation.isDeleted(), employeeEducation.getCreatedAt(),
                employeeEducation.getModifiedAt());
    }
}
