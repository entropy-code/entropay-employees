package com.entropyteam.entropay.employees.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import jakarta.validation.constraints.NotNull;

import com.entropyteam.entropay.employees.models.Pto;
import com.entropyteam.entropay.employees.models.Status;
import com.entropyteam.entropay.employees.services.PtoService;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.ObjectUtils;

public record PtoDto(UUID id,
                     @NotNull(message = "Start date is mandatory")
                     @JsonFormat(pattern = "yyyy-MM-dd")
                     LocalDate ptoStartDate,
                     @NotNull(message = "End date is mandatory")
                     @JsonFormat(pattern = "yyyy-MM-dd")
                     LocalDate ptoEndDate,
                     Status status,
                     String details,
                     @NotNull(message = "Employee is mandatory")
                     UUID employeeId,
                     @NotNull(message = "Leave type is mandatory")
                     UUID leaveTypeId,
                     Double days,
                     Integer labourHours,
                     @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                     LocalDateTime createdAt,
                     @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                     LocalDateTime modifiedAt,
                     boolean isHalfDay,
                     String employeeFullName,
                     String leaveTypeName) {

    public PtoDto(Pto pto) {
        this(pto.getId(), pto.getStartDate(), pto.getEndDate(), pto.getStatus(), pto.getDetails(),
                pto.getEmployee().getId(), pto.getLeaveType().getId(), pto.getDays(), pto.getLabourHours(),
                pto.getCreatedAt(), pto.getModifiedAt(), checkHalfDay(pto), pto.getEmployee().getFullName(), pto.getLeaveType().getName());
    }

    private static boolean checkHalfDay(Pto pto) {
        Long days = ChronoUnit.DAYS.between(pto.getStartDate(), pto.getEndDate());
        return ObjectUtils.notEqual(pto.getDays(), null) && days.compareTo(0L) == 0 && pto.getDays().equals(PtoService.HALF_DAY_OFF);
    }
}
