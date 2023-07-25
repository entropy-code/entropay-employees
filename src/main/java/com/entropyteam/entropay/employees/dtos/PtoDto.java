package com.entropyteam.entropay.employees.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import com.entropyteam.entropay.employees.models.Pto;
import com.fasterxml.jackson.annotation.JsonFormat;

public record PtoDto(UUID id,
                     @NotNull(message = "Start date is mandatory")
                     @JsonFormat(pattern = "yyyy-MM-dd")
                     LocalDate ptoStartDate,
                     @NotNull(message = "End date is mandatory")
                     @JsonFormat(pattern = "yyyy-MM-dd")
                     LocalDate ptoEndDate,
                     String status,
                     String details,
                     @NotNull(message = "Employee is mandatory")
                     UUID employeeId,
                     @NotNull(message = "Leave type is mandatory")
                     UUID leaveTypeId,
                     Integer days,
                     Integer labourHours,
                     @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                     LocalDateTime createdAt,
                     @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                     LocalDateTime modifiedAt) {

    public PtoDto(Pto pto) {
        this(pto.getId(), pto.getStartDate(), pto.getEndDate(), pto.getStatus().name(), pto.getDetails(),
                pto.getEmployee().getId(), pto.getLeaveType().getId(), pto.getDays(), pto.getLabourHours(),
                pto.getCreatedAt(), pto.getModifiedAt());
    }
}
