package com.entropyteam.entropay.employees.clients.dtos;

import java.time.LocalDateTime;
import java.util.UUID;
import com.entropyteam.entropay.employees.clients.models.Client;
import com.fasterxml.jackson.annotation.JsonFormat;

public record ClientDto(
        UUID id,
        String name,
        String address,
        String zipCode,
        String city,
        String state,
        String country,
        String contact,
        String preferredCurrency,
        @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss") LocalDateTime createdAt,
        @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss") LocalDateTime modifiedAt) {

    public ClientDto(Client client) {
        this(client.getId(), client.getName(), client.getAddress(), client.getZipCode(), client.getCity(), client.getState(),
                client.getCountry(), client.getContact(), client.getPreferredCurrency(), client.getCreatedAt(),
                client.getModifiedAt());
    }
}