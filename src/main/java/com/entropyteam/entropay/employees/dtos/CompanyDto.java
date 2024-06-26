package com.entropyteam.entropay.employees.dtos;

import java.time.LocalDateTime;
import java.util.UUID;
import com.entropyteam.entropay.employees.models.Company;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotNull;

public record CompanyDto(UUID id,
                        @NotNull(message = "Tenant is mandatory")
                        UUID tenantId,
                        @NotNull(message = "Name is mandatory")
                        String name,
                        String address,
                        String zipCode,
                        String city,
                        String state,
                        String country,
                        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime createdAt,
                        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime modifiedAt) {

    public CompanyDto(Company company) {
        this(company.getId(), company.getTenantId(), company.getName(), company.getAddressLine(), company.getZipCode(),
                company.getCity(), company.getState(), company.getCountry(), company.getCreatedAt(),
                company.getModifiedAt());
    }
}
