package com.entropyteam.entropay.employees.common.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import com.entropyteam.entropay.employees.clients.dtos.ClientDto;
import com.entropyteam.entropay.employees.clients.dtos.ClientSaveRequestDto;
import com.entropyteam.entropay.employees.clients.dtos.ClientSaveResponseDto;
import com.entropyteam.entropay.employees.clients.models.Client;

class ClientMapperTest {

    @Test
    void whenToDtoValidateOk() {
        Client client = Client.builder()
                .id(UUID.randomUUID())
                .name("testClient")
                .address("testAddress")
                .contact("00000")
                .createdOn(LocalDateTime.now())
                .preferredCurrency("USD")
                .build();
        ClientDto result = ClientMapper.MAPPER.toDto(client);
        assertEquals(client.getId(), result.getId(), "ID not equal");
        assertEquals(client.getName(), result.getName(), "Name not equal");
        assertEquals(client.getAddress(), result.getAddress(), "Address not equal");
        assertEquals(client.getContact(), result.getContact(), "Contact not equal");
        assertEquals(client.getCreatedOn(), result.getCreatedOn(), "Creation Date not equal");
    }

    @Test
    void whenToSaveResponseDtoValidateOk() {
        Client client = Client.builder()
                .id(UUID.randomUUID())
                .name("testClient")
                .address("testAddress")
                .contact("00000")
                .createdOn(LocalDateTime.now())
                .preferredCurrency("USD")
                .build();
        ClientSaveResponseDto result = ClientMapper.MAPPER.toSaveResponseDto(client);
        assertEquals(client.getId(), result.getId(), "ID not equal");
        assertEquals(client.getName(), result.getName(), "Name not equal");
        assertEquals(client.getAddress(), result.getAddress(), "Address not equal");
        assertEquals(client.getContact(), result.getContact(), "Contact not equal");
        assertEquals(client.getPreferredCurrency(), result.getPreferredCurrency(), "Currency not equal");
    }

    @Test
    void whenToEntityValidateOk() {
        ClientSaveRequestDto dto = ClientSaveRequestDto.builder()
                .name("testClient")
                .address("testAddress")
                .contact("00000")
                .preferredCurrency("USD")
                .build();
        Client result = ClientMapper.MAPPER.toEntity(dto);
        assertEquals(dto.getName(), result.getName(), "Name not equal");
        assertEquals(dto.getAddress(), result.getAddress(), "Address not equal");
        assertEquals(dto.getContact(), result.getContact(), "Contact not equal");
        assertEquals(dto.getPreferredCurrency(), result.getPreferredCurrency(), "Currency not equal");
    }
}