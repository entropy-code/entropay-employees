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
        Client client = new Client();
        client.setId(UUID.randomUUID());
        client.setName("testClient");
        client.setAddress("testAddress");
        client.setContact("00000");
        client.setCreatedAt(LocalDateTime.now());
        client.setPreferredCurrency("USD");

        ClientDto result = ClientMapper.MAPPER.toDto(client);
        assertEquals(client.getId(), result.id(), "ID not equal");
        assertEquals(client.getName(), result.name(), "Name not equal");
        assertEquals(client.getAddress(), result.address(), "Address not equal");
        assertEquals(client.getContact(), result.contact(), "Contact not equal");
        assertEquals(client.getCreatedAt(), result.createdAt(), "Creation Date not equal");
    }

    @Test
    void whenToSaveResponseDtoValidateOk() {
        Client client = new Client();
        client.setId(UUID.randomUUID());
        client.setName("testClient");
        client.setAddress("testAddress");
        client.setContact("00000");
        client.setCreatedAt(LocalDateTime.now());
        client.setPreferredCurrency("USD");

        ClientSaveResponseDto result = ClientMapper.MAPPER.toSaveResponseDto(client);
        assertEquals(client.getId(), result.id(), "ID not equal");
        assertEquals(client.getName(), result.name(), "Name not equal");
        assertEquals(client.getAddress(), result.address(), "Address not equal");
        assertEquals(client.getContact(), result.contact(), "Contact not equal");
        assertEquals(client.getPreferredCurrency(), result.preferredCurrency(), "Currency not equal");
    }

    @Test
    void whenToEntityValidateOk() {
        ClientSaveRequestDto dto = new ClientSaveRequestDto(
                "testClient",
                "testAddress",
                "00000",
                "city",
                "state",
                "country",
                "contact",
                "USD");

        Client result = ClientMapper.MAPPER.toEntity(dto);
        assertEquals(dto.name(), result.getName(), "Name not equal");
        assertEquals(dto.address(), result.getAddress(), "Address not equal");
        assertEquals(dto.contact(), result.getContact(), "Contact not equal");
        assertEquals(dto.preferredCurrency(), result.getPreferredCurrency(), "Currency not equal");
    }
}
