package com.entropyteam.entropay.employees.clients.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientSaveRequestDto {

    private String name;
    private String address;
    private String contact;
    private String preferredCurrency;

}
