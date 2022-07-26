package com.entropyteam.entropay.employees.clients.dtos;

import java.util.List;
import org.springframework.data.domain.Sort.Direction;

public record ClientListDto(
        int pageNumber,
        int pageSize,
        long offset,
        Direction sort,
        List<ClientDto> results) {

}
