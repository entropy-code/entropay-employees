package com.entropyteam.entropay.employees.dtos;

import java.util.UUID;
import com.entropyteam.entropay.employees.models.Role;

import javax.validation.constraints.NotNull;

public record RoleDto(UUID id,
                      @NotNull(message = "Name is mandatory")
                      String name) {

    public RoleDto(Role role) {
        this(role.getId(), role.getName());
    }
}
