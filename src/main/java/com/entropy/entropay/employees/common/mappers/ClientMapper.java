package com.entropy.entropay.employees.common.mappers;

import java.util.Collection;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.entropy.entropay.employees.clients.dtos.ClientDto;
import com.entropy.entropay.employees.clients.dtos.ClientSaveRequestDto;
import com.entropy.entropay.employees.clients.dtos.ClientSaveResponseDto;
import com.entropy.entropay.employees.clients.models.Client;

@Mapper
public interface ClientMapper {

    ClientMapper MAPPER = Mappers.getMapper(ClientMapper.class);

    ClientDto toDto(Client source);

    ClientSaveResponseDto toSaveResponseDto(Client source);

    Client toEntity(ClientSaveRequestDto source);

    List<ClientDto> toDtos(Collection<Client> source);

}
