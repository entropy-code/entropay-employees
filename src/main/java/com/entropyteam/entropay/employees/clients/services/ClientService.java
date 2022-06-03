package com.entropyteam.entropay.employees.clients.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import com.entropyteam.entropay.employees.clients.dtos.ClientDto;
import com.entropyteam.entropay.employees.clients.dtos.ClientSaveRequestDto;
import com.entropyteam.entropay.employees.clients.dtos.ClientSaveResponseDto;
import com.entropyteam.entropay.employees.clients.models.Client;
import com.entropyteam.entropay.employees.clients.repositories.ClientRepository;
import com.entropyteam.entropay.employees.common.exceptions.ResourceNotFoundException;
import com.entropyteam.entropay.employees.common.mappers.ClientMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientSaveResponseDto createClient(ClientSaveRequestDto newClientDto) {
        Client newClient = ClientMapper.MAPPER.toEntity(newClientDto);
        newClient.setActive(true);

        log.info("Attempting to save client {}.", newClientDto.getName());

        ClientSaveResponseDto result = ClientMapper.MAPPER.toSaveResponseDto(clientRepository.save(newClient));

        log.info("Successfully created client with name: {}.: Id: {}.", result.getName(), result.getId());

        return result;
    }

    public ClientDto findClientById(String clientId) {
        log.debug("Attempting to find client with Id: {}.", clientId);
        return ClientMapper.MAPPER.toDto(clientRepository.findByIdAndIsActiveTrue(UUID.fromString(clientId))
                .orElseThrow(() -> new ResourceNotFoundException("Client does not exist: " + clientId)));
    }

    public ClientSaveResponseDto updateClient(String clientId, ClientSaveRequestDto clientDto) {
        Client client = clientRepository.findByIdAndIsActiveTrue(UUID.fromString(clientId))
                .orElseThrow(() -> new ResourceNotFoundException("Client does not exist: " + clientId));

        log.info("Attempting to Update client with Id: {}.", clientId);
        log.trace("Current Client Data: {}.", client);

        client.setAddress(clientDto.getAddress());
        client.setContact(clientDto.getContact());
        client.setPreferredCurrency(clientDto.getPreferredCurrency());

        log.info("Client Data to update: {}.", client);

        client = clientRepository.save(client);

        log.info("Successfully updated client Id: {}.", clientId);

        return ClientMapper.MAPPER.toSaveResponseDto(client);
    }

    public void deleteClient(String clientId) {
        Client client = clientRepository.findById(UUID.fromString(clientId))
                .orElseThrow(() -> new ResourceNotFoundException("Client does not exist: " + clientId));

        log.debug("Attempting to delete client with Id: {}.", clientId);
        log.trace("Current client Data: {}.", client);

        client.setActive(false);
        clientRepository.save(client);

        log.info("Successfully deleted client with Id: {}.", clientId);
    }

    public Page<ClientDto> listActiveClients(Direction sort, int page, int size, String sortBy) {

        log.debug("Attempting to retrieve the list of active clients. Page: {}. Size: {}. Sort: {}.", page, size, sort);

        Pageable paging = PageRequest.of(page, size).withSort(sort, sortBy);
        Page<ClientDto> result = clientRepository.findAllByIsActiveTrue(paging);
        log.trace("Result: {}.", result);
        return result;
    }
}
