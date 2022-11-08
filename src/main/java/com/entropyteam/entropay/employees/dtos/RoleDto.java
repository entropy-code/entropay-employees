package com.entropyteam.entropay.employees.dtos;

import java.util.UUID;
import com.entropyteam.entropay.employees.models.Role;

public record RoleDto(UUID id, String name) {

    public RoleDto(Role role) {
        this(role.getId(), role.getName());
    }
}
