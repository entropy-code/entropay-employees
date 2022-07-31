package com.entropyteam.entropay.employees.clients.services;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.entropyteam.entropay.employees.clients.dtos.ClientDto;
import com.entropyteam.entropay.employees.clients.models.Client;
import com.entropyteam.entropay.employees.clients.repositories.ClientRepository;
import com.entropyteam.entropay.employees.common.BaseRepository;
import com.entropyteam.entropay.employees.common.BaseService;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class ClientService extends BaseService<Client, ClientDto, UUID> {

    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    protected BaseRepository<Client, UUID> getRepository() {
        return clientRepository;
    }

    @Override
    protected ClientDto toDTO(Client entity) {
        return new ClientDto(entity);
    }

    @Override
    protected Client toEntity(ClientDto entity) {
        return new Client(entity);
    }
}
