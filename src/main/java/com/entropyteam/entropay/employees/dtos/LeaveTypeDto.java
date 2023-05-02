package com.entropyteam.entropay.employees.dtos;

import com.entropyteam.entropay.employees.models.LeaveType;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public record LeaveTypeDto(UUID id,
                           @NotNull(message = "Name is mandatory")
                           String name) {
    public LeaveTypeDto(LeaveType leaveType) {
        this(leaveType.getId(), leaveType.getName());
    }
}
