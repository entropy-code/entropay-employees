package com.entropy.entropay.employees.clients.dtos;

import java.util.UUID;

import lombok.Data;

@Data
public class ClientSaveResponseDto {

    private UUID id;
    private String name;
    private String address;
    private String contact;
    private String preferredCurrency;

}
