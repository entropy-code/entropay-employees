package com.entropyteam.entropay.employees.clients.dtos;

import java.util.List;
import org.springframework.data.domain.Sort.Direction;

import lombok.Data;

@Data
public class ClientListDto {

    int pageNumber;
    int pageSize;
    long offset;
    Direction sort;
    List<ClientDto> results;

}
