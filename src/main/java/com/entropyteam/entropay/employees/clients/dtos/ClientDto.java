package com.entropyteam.entropay.employees.clients.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;

@Data
public class ClientDto {

    private UUID id;
    private String name;
    private String address;
    private String contact;
    private String preferredCurrency;
    private LocalDateTime createdOn;
    private LocalDateTime modifiedOn;

}
