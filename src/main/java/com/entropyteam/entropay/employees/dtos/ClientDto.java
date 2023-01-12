package com.entropyteam.entropay.employees.dtos;

import java.time.LocalDateTime;
import java.util.UUID;
import com.entropyteam.entropay.employees.models.Client;
import com.fasterxml.jackson.annotation.JsonFormat;

public record ClientDto(UUID id, UUID companyId, String name, String address, String zipCode, String city, String state,
                        String country, String contactFullName, String contactEmail, String preferredCurrency,
                        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime createdAt,
                        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime modifiedAt) {

    public ClientDto(Client client) {
        this(client.getId(), client.getCompanyId(), client.getName(), client.getAddressLine(), client.getZipCode(),
                client.getCity(), client.getState(), client.getCountry(), client.getContactFullName(), client.getContactEmail(),
                client.getPreferredCurrency(), client.getCreatedAt(), client.getModifiedAt());
    }
}
