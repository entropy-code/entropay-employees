package com.entropyteam.entropay.employees.clients.dtos;

import java.util.UUID;

public record ClientSaveResponseDto(
        UUID id,
        String name,
        String address,
        String zipCode,
        String city,
        String state,
        String country,
        String contact,
        String preferredCurrency) {

}
