package com.entropyteam.entropay.employees.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import com.entropyteam.entropay.employees.models.Assignment;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotNull;

public record AssignmentDto(UUID id,
                            @NotNull(message ="Project is mandatory")
                            UUID projectId,
                            @NotNull(message ="Employee is mandatory")
                            UUID employeeId,
                            @NotNull(message ="Role is mandatory")
                            UUID roleId,
                            @NotNull(message ="Seniority is mandatory")
                            UUID seniorityId,
                            Integer hoursPerMonth,
                            String labourHours,
                            BigDecimal billableRate,
                            String currency,
                            @NotNull(message ="Start Date is mandatory")
                            @JsonFormat(pattern = "yyyy-MM-dd")
                            LocalDate startDate,
                            @JsonFormat(pattern = "yyyy-MM-dd")
                            LocalDate endDate,
                            boolean deleted,
                            boolean active,
                            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                            LocalDateTime createdAt,
                            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                            LocalDateTime modifiedAt,
                            String endReason

) {


    public AssignmentDto(Assignment assignment) {
        this(
                assignment.getId(), assignment.getProject().getId(), assignment.getEmployee().getId(),
                assignment.getRole().getId(), assignment.getSeniority().getId(), assignment.getHoursPerMonth(),
                assignment.getLabourHours(), assignment.getBillableRate(),
                assignment.getCurrency() != null ? assignment.getCurrency().name() : null,
                assignment.getStartDate(), assignment.getEndDate(), assignment.isDeleted(),
                assignment.isActive(),
                assignment.getModifiedAt(), assignment.getCreatedAt(),
                assignment.getEndReason()
        );
    }

}