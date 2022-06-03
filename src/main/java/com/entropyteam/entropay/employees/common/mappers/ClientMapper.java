package com.entropyteam.entropay.employees.common.mappers;

import java.util.Collection;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import com.entropyteam.entropay.employees.clients.dtos.ClientDto;
import com.entropyteam.entropay.employees.clients.dtos.ClientListDto;
import com.entropyteam.entropay.employees.clients.dtos.ClientSaveRequestDto;
import com.entropyteam.entropay.employees.clients.dtos.ClientSaveResponseDto;
import com.entropyteam.entropay.employees.clients.models.Client;

@Mapper
public interface ClientMapper {

    ClientMapper MAPPER = Mappers.getMapper(ClientMapper.class);

    ClientDto toDto(Client source);

    ClientSaveResponseDto toSaveResponseDto(Client source);

    Client toEntity(ClientSaveRequestDto source);

}
