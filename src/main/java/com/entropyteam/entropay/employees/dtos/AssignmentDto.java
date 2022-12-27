package com.entropyteam.entropay.employees.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import com.entropyteam.entropay.employees.models.Assignment;
import com.fasterxml.jackson.annotation.JsonFormat;

public record AssignmentDto(

        UUID id,
        UUID projectId,
        UUID employeeId,
        UUID roleId,
        UUID seniorityId,
        Integer hoursPerWeek,
        BigDecimal billableRate,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate startDate,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate endDate,
        boolean deleted,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdAt,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime modifiedAt
) {


    public AssignmentDto(Assignment assignment) {
        this(
                assignment.getId(), assignment.getProject().getId(), assignment.getEmployee().getId(),
                assignment.getRole().getId(), assignment.getSeniority().getId(), assignment.getHoursPerWeek(),
                assignment.getBillableRate(), assignment.getStartDate(), assignment.getEndDate(),
                assignment.isDeleted(), assignment.getCreatedAt(), assignment.getModifiedAt()
        );
    }

}
