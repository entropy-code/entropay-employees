package com.entropyteam.entropay.employees.dtos;

import java.util.List;
import java.util.UUID;
import com.entropyteam.entropay.employees.models.Config;

public record ConfigDto (UUID id,
                         String role,
                         List<PermissionDto> permissions,
                         List<MenuItemDto> menu) {

    public ConfigDto(Config entity, List<PermissionDto> permissions, List<MenuItemDto> menuItems) {
        this(entity.getId(), entity.getRole().name(), permissions, menuItems);
    }
}
