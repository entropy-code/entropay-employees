package com.entropyteam.entropay.employees.clients.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.entropyteam.entropay.employees.clients.dtos.ClientDto;
import com.entropyteam.entropay.employees.clients.models.Client;
import com.entropyteam.entropay.employees.clients.repositories.ClientRepository;
import com.entropyteam.entropay.employees.common.CrudService;
import com.entropyteam.entropay.employees.common.Filter;
import com.entropyteam.entropay.employees.common.Range;
import com.entropyteam.entropay.employees.common.Sort;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class ClientService implements CrudService<ClientDto, UUID> {

    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Optional<ClientDto> findOne(UUID id) {
        return clientRepository.findById(id)
                .map(ClientDto::new);
    }

    @Override
    public List<ClientDto> findAllActive(Filter filter, Sort sort, Range range) {
        return clientRepository.findAllByDeletedIsFalse()
                .stream()
                .map(ClientDto::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ClientDto delete(UUID id) {
        Client client = clientRepository.findById(id).orElseThrow();
        client.setDeleted(true);
        return new ClientDto(client);
    }

    @Override
    @Transactional
    public ClientDto create(ClientDto entity) {
        Client client = new Client(entity);

        Client save = clientRepository.save(client);

        return new ClientDto(save);
    }

    @Override
    @Transactional
    public ClientDto update(UUID id, ClientDto entity) {
        Client client = clientRepository.findById(id).orElseThrow();
        client.setName(entity.name());
        client.setAddress(entity.address());
        client.setZipCode(entity.zipCode());
        client.setCity(entity.city());
        client.setCountry(entity.country());
        client.setContact(entity.contact());
        client.setPreferredCurrency(entity.preferredCurrency());

        return new ClientDto(client);
    }
}
