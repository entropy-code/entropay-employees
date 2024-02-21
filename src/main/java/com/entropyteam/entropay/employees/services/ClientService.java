package com.entropyteam.entropay.employees.services;

import java.util.UUID;

import com.entropyteam.entropay.employees.models.Assignment;
import com.entropyteam.entropay.employees.repositories.AssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.common.BaseService;
import com.entropyteam.entropay.common.ReactAdminMapper;
import com.entropyteam.entropay.employees.dtos.ClientDto;
import com.entropyteam.entropay.employees.models.Client;
import com.entropyteam.entropay.employees.models.Company;
import com.entropyteam.entropay.employees.repositories.ClientRepository;
import com.entropyteam.entropay.employees.repositories.CompanyRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientService extends BaseService<Client, ClientDto, UUID> {

    private final ClientRepository clientRepository;
    private final CompanyRepository companyRepository;
    private final AssignmentRepository assignmentRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository, CompanyRepository companyRepository, AssignmentRepository assignmentRepository,
            ReactAdminMapper reactAdminMapper) {
        super(Client.class, reactAdminMapper);
        this.clientRepository = clientRepository;
        this.companyRepository = companyRepository;
        this.assignmentRepository = assignmentRepository;
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
        Company company = companyRepository.findById(entity.companyId()).orElseThrow();
        Client client = new Client(entity);
        client.setCompany(company);
        return client;
    }

    @Override
    @Transactional
    public ClientDto update(UUID clientId, ClientDto clientDto) {
        Client entityToUpdate = toEntity(clientDto);
        entityToUpdate.setId(clientId);
        canDeactivateClient(clientId, entityToUpdate);
        Client savedEntity = getRepository().save(entityToUpdate);
        return toDTO(savedEntity);
    }

    public void canDeactivateClient(UUID clientId, Client clientToUpdate) {
        Client existingClient = clientRepository.findById(clientId).orElseThrow();
        if(existingClient.isActive() && !clientToUpdate.isActive()) {
            if(assignmentRepository.findAllAssignmentsByClientId(clientId).stream().anyMatch(Assignment::isActive)){
                throw new RuntimeException("Can't deactivate a client with an active assignment");
            }
        }
    }
}
