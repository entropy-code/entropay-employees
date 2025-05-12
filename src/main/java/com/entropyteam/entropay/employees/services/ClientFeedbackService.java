package com.entropyteam.entropay.employees.services;

import java.util.UUID;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.entropyteam.entropay.common.BaseService;
import com.entropyteam.entropay.common.ReactAdminMapper;
import com.entropyteam.entropay.employees.dtos.ClientFeedbackDto;
import com.entropyteam.entropay.employees.models.Client;
import com.entropyteam.entropay.employees.models.ClientFeedback;
import com.entropyteam.entropay.employees.repositories.ClientFeedbackRepository;
import com.entropyteam.entropay.employees.repositories.ClientRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ClientFeedbackService extends BaseService<ClientFeedback, ClientFeedbackDto, UUID> {

    private final ClientFeedbackRepository clientFeedbackRepository;
    private final ClientRepository clientRepository;

    public ClientFeedbackService(ClientFeedbackRepository clientFeedbackRepository,
            ClientRepository clientRepository, ReactAdminMapper mapper) {
        super(ClientFeedback.class, mapper);
        this.clientFeedbackRepository = clientFeedbackRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    public ClientFeedbackDto create(ClientFeedbackDto dto) {
        // Set the created by field from the current user's username
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // Create a new DTO with the updated created by field
        ClientFeedbackDto updatedDto = new ClientFeedbackDto(
                dto.id(),
                dto.clientId(),
                username,
                dto.feedbackDate(),
                dto.source(),
                dto.title(),
                dto.text(),
                dto.createdAt(),
                dto.modifiedAt(),
                dto.deleted()
        );

        return super.create(updatedDto);
    }

    @Override
    public ClientFeedbackRepository getRepository() {
        return clientFeedbackRepository;
    }

    @Override
    public ClientFeedbackDto toDTO(ClientFeedback entity) {
        return new ClientFeedbackDto(entity);
    }

    @Override
    public ClientFeedback toEntity(ClientFeedbackDto dto) {
        Client client = clientRepository.findById(dto.clientId())
                .orElseThrow(() -> new EntityNotFoundException("Client not found with ID: " + dto.clientId()));

        return new ClientFeedback(dto, client);
    }
}