package com.entropyteam.entropay.employees.clients.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientDto {

    private UUID id;
    private String name;
    private String address;
    private String zipCode;
    private String city;
    private String state;
    private String country;
    private String contact;
    private String preferredCurrency;
    private LocalDateTime createdOn;
    private LocalDateTime modifiedOn;

}
