package com.entropyteam.entropay.employees.services;

import java.util.UUID;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.entropyteam.entropay.common.BaseService;
import com.entropyteam.entropay.common.ReactAdminMapper;
import com.entropyteam.entropay.employees.dtos.FeedbackDto;
import com.entropyteam.entropay.employees.models.Employee;
import com.entropyteam.entropay.employees.models.EmployeeFeedback;
import com.entropyteam.entropay.employees.repositories.EmployeeFeedbackRepository;
import com.entropyteam.entropay.employees.repositories.EmployeeRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class EmployeeFeedbackService extends BaseService<EmployeeFeedback, FeedbackDto, UUID> {

    private final EmployeeFeedbackRepository employeeFeedbackRepository;
    private final EmployeeRepository employeeRepository;

    public EmployeeFeedbackService(EmployeeFeedbackRepository employeeFeedbackRepository,
            EmployeeRepository employeeRepository, ReactAdminMapper mapper) {
        super(EmployeeFeedback.class, mapper);
        this.employeeFeedbackRepository = employeeFeedbackRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public FeedbackDto create(FeedbackDto dto) {
        // Set the created by field from the current user's username
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // Create a new DTO with the updated created by field
        FeedbackDto updatedDto = new FeedbackDto(
                dto.id(),
                dto.employeeId(),
                username,
                dto.feedbackDate(),
                dto.source(),
                dto.title(),
                dto.text(),
                dto.createdAt(),
                dto.modifiedAt(),
                dto.deleted(),
                dto.employeeName()
        );

        return super.create(updatedDto);
    }

    @Override
    public EmployeeFeedbackRepository getRepository() {
        return employeeFeedbackRepository;
    }

    @Override
    public FeedbackDto toDTO(EmployeeFeedback entity) {
        return new FeedbackDto(entity);
    }

    @Override
    public EmployeeFeedback toEntity(FeedbackDto dto) {
        Employee employee = employeeRepository.findById(dto.employeeId())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with ID: " + dto.employeeId()));

        return new EmployeeFeedback(dto, employee);
    }
}