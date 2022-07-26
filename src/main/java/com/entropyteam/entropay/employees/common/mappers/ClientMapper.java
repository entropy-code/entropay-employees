package com.entropyteam.entropay.employees.common.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.entropyteam.entropay.employees.clients.dtos.ClientDto;
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
