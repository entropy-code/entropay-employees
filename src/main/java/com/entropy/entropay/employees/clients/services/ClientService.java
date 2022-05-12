package com.entropy.entropay.employees.clients.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.entropy.entropay.employees.clients.dtos.ClientDto;
import com.entropy.entropay.employees.clients.dtos.ClientSaveRequestDto;
import com.entropy.entropay.employees.clients.dtos.ClientSaveResponseDto;
import com.entropy.entropay.employees.clients.models.Client;
import com.entropy.entropay.employees.clients.repositories.ClientRepository;
import com.entropy.entropay.employees.common.exceptions.ResourceNotFoundException;
import com.entropy.entropay.employees.common.mappers.ClientMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientSaveResponseDto createClient(ClientSaveRequestDto newClientDto) {
        Client newClient = ClientMapper.MAPPER.toEntity(newClientDto);
        newClient.setCreatedOn(LocalDateTime.now());
        return ClientMapper.MAPPER.toSaveResponseDto(clientRepository.save(newClient));
    }

    public ClientDto findById(String clientId) {
        return ClientMapper.MAPPER.toDto(clientRepository.findByIdAndStatus(UUID.fromString(clientId), true)
                .orElseThrow(() -> new ResourceNotFoundException("Client does not exist: " + clientId)));
    }

    public ClientSaveResponseDto updateClient(String clientId, ClientSaveRequestDto clientDto) {
        Client client = clientRepository.findByIdAndStatus(UUID.fromString(clientId), true)
                .orElseThrow(() -> new ResourceNotFoundException("Client does not exist: " + clientId));
        client.setAddress(clientDto.getAddress());
        client.setContact(clientDto.getContact());
        client.setPreferredCurrency(clientDto.getPreferredCurrency());
        client.setModifiedOn(LocalDateTime.now());
        client = clientRepository.save(client);
        return ClientMapper.MAPPER.toSaveResponseDto(client);
    }

    public void deleteClient(String clientId) {
        Client client = clientRepository.findById(UUID.fromString(clientId))
                .orElseThrow(() -> new ResourceNotFoundException("Client does not exist: " + clientId));
        client.setActive(false);
        client.setModifiedOn(LocalDateTime.now());
        clientRepository.save(client);
    }

    public List<ClientDto> listClients() {
        return ClientMapper.MAPPER.toDtos(clientRepository.findAllByStatus(
                true, Sort.by(Sort.Direction.ASC, "name")));
    }
}
