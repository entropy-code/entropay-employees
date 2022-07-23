package com.entropyteam.entropay.employees.clients.dtos;

public record ClientSaveRequestDto(
        String name,
        String address,
        String zipCode,
        String city,
        String state,
        String country,
        String contact,
        String preferredCurrency) {

}
