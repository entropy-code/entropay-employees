package com.entropyteam.entropay.employees.clients.dtos;

import java.util.UUID;

import lombok.Data;

@Data
public class ClientSaveResponseDto {

    private UUID id;
    private String name;
    private String address;
    private String zipCode;
    private String city;
    private String state;
    private String country;
    private String contact;
    private String preferredCurrency;

}
