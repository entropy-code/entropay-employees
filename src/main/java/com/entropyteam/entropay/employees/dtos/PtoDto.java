package com.entropyteam.entropay.employees.dtos;

import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import com.entropyteam.entropay.employees.models.Pto;
import com.fasterxml.jackson.annotation.JsonFormat;

public record PtoDto(UUID id,
                     @NotNull(message = "From date is mandatory")
                     @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                     LocalDateTime from,
                     @NotNull(message = "To date is mandatory")
                     @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                     LocalDateTime to,
                     String status,
                     String details,
                     @NotNull(message = "Employee is mandatory")
                     UUID employeeId,
                     @NotNull(message = "Leave type is mandatory")
                     UUID leaveType,
                     Integer days,
                     Integer labourHours,
                     @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                     LocalDateTime createdAt,
                     @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                     LocalDateTime modifiedAt) {

    public PtoDto(Pto pto) {
        this(pto.getId(), pto.getFrom(), pto.getTo(), pto.getStatus().name(), pto.getDetails(),
                pto.getEmployee().getId(), pto.getLeaveType().getId(), pto.getDays(), pto.getLabourHours(),
                pto.getCreatedAt(), pto.getModifiedAt());
    }
}
