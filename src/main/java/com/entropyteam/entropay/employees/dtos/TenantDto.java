package com.entropyteam.entropay.employees.dtos;

import java.time.LocalDateTime;
import java.util.UUID;
import com.entropyteam.entropay.employees.models.Tenant;
import com.fasterxml.jackson.annotation.JsonFormat;

public record TenantDto(UUID id, String name, String displayName,
                        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime createdAt,
                        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime modifiedAt) {

    public TenantDto(Tenant tenant) {
        this(tenant.getId(), tenant.getName(), tenant.getDisplayName(), tenant.getCreatedAt(), tenant.getModifiedAt());
    }
}
