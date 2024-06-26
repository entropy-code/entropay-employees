package com.entropyteam.entropay.employees.dtos;

import java.time.LocalDateTime;
import java.util.UUID;
import com.entropyteam.entropay.employees.models.Client;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotNull;


public record ClientDto(UUID id,
                        @NotNull(message = "Company is mandatory")
                        UUID companyId,
                        @NotNull(message = "Name is mandatory")
                        String name,
                        String address,
                        String zipCode,
                        String city,
                        String state,
                        String country,
                        String contactFullName,
                        String contactEmail,
                        String preferredCurrency,
                        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                        LocalDateTime createdAt,
                        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                        LocalDateTime modifiedAt,
                        String companyName,
                        String internalId,
                        @NotNull(message = "Active state is mandatory")
                        boolean active) {

    public ClientDto(Client client) {
        this(client.getId(), client.getCompanyId(), client.getName(), client.getAddressLine(), client.getZipCode(),
                client.getCity(), client.getState(), client.getCountry(), client.getContactFullName(),
                client.getContactEmail(), client.getPreferredCurrency(), client.getCreatedAt(), client.getModifiedAt(),
                client.getCompany().getName(), client.getInternalId(), client.isActive());
    }
}
