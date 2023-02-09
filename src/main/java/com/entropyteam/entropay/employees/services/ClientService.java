package com.entropyteam.entropay.employees.services;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.common.BaseService;
import com.entropyteam.entropay.common.ReactAdminMapper;
import com.entropyteam.entropay.employees.dtos.ClientDto;
import com.entropyteam.entropay.employees.models.Assignment;
import com.entropyteam.entropay.employees.models.Client;
import com.entropyteam.entropay.employees.models.Company;
import com.entropyteam.entropay.employees.repositories.ClientRepository;
import com.entropyteam.entropay.employees.repositories.CompanyRepository;

@Service
public class ClientService extends BaseService<Client, ClientDto, UUID> {

    private final ClientRepository clientRepository;
    private final CompanyRepository companyRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository, CompanyRepository companyRepository,
            ReactAdminMapper reactAdminMapper) {
        super(Client.class, reactAdminMapper);
        this.clientRepository = clientRepository;
        this.companyRepository = companyRepository;
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
}
