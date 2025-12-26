package com.entropyteam.entropay.employees.services;

import java.util.List;
import java.util.UUID;

import com.entropyteam.entropay.common.exceptions.InvalidRequestParametersException;
import com.entropyteam.entropay.common.exceptions.ResourceNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.entropyteam.entropay.common.BaseService;
import com.entropyteam.entropay.common.ReactAdminMapper;
import com.entropyteam.entropay.employees.dtos.EmployeeFeedbackSummaryDto;
import com.entropyteam.entropay.employees.dtos.FeedbackDto;
import com.entropyteam.entropay.employees.models.Employee;
import com.entropyteam.entropay.employees.models.EmployeeFeedbackSummary;
import com.entropyteam.entropay.employees.repositories.EmployeeFeedbackRepository;
import com.entropyteam.entropay.employees.repositories.EmployeeFeedbackSummaryRepository;
import com.entropyteam.entropay.employees.repositories.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class EmployeeFeedbackSummaryService extends BaseService<EmployeeFeedbackSummary, EmployeeFeedbackSummaryDto, UUID> {

    private final EmployeeFeedbackSummaryRepository summaryRepository;
    private final EmployeeRepository employeeRepository;
    private final EmployeeFeedbackRepository feedbackRepository;
    private final EmployeeAIService employeeAiService;

    public EmployeeFeedbackSummaryService(
            EmployeeFeedbackSummaryRepository summaryRepository,
            EmployeeRepository employeeRepository,
            EmployeeFeedbackRepository feedbackRepository,
            EmployeeAIService employeeAiService,
            ReactAdminMapper mapper) {
        super(EmployeeFeedbackSummary.class, mapper);
        this.summaryRepository = summaryRepository;
        this.employeeRepository = employeeRepository;
        this.feedbackRepository = feedbackRepository;
        this.employeeAiService = employeeAiService;
    }

    @Override
    @Transactional
    public EmployeeFeedbackSummaryDto create(EmployeeFeedbackSummaryDto dto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        EmployeeFeedbackSummaryDto updatedDto = new EmployeeFeedbackSummaryDto(
                dto.id(),
                dto.employeeId(),
                dto.summary(),
                username,
                dto.createdAt(),
                dto.modifiedAt(),
                dto.deleted()
        );

        return super.create(updatedDto);
    }

    /**
     * Get the current (latest non-deleted) summary for an employee
     */
    @Transactional(readOnly = true)
    public EmployeeFeedbackSummaryDto getCurrentSummaryByEmployeeId(UUID employeeId) {
        return summaryRepository.findFirstByEmployee_IdAndDeletedIsFalseOrderByCreatedAtDesc(employeeId)
                .map(this::toDTO)
                .orElse(null);
    }

    /**
     * Get all summaries (including deleted/historical) for an employee
     */
    @Transactional(readOnly = true)
    public List<EmployeeFeedbackSummaryDto> getAllSummariesByEmployeeId(UUID employeeId) {
        return summaryRepository.findAllByEmployee_IdAndDeletedIsFalse(employeeId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    /**
     * Generate an AI-powered summary of all feedbacks for an employee
     *
     * @param employeeId The UUID of the employee
     * @return The created summary DTO with AI-generated content
     * @throws EntityNotFoundException if employee not found
     * @throws IllegalArgumentException if employee has no feedbacks to summarize
     */
    @Transactional
    public EmployeeFeedbackSummaryDto generateSummaryWithAI(UUID employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));

        List<FeedbackDto> feedbacks = feedbackRepository.findAllByEmployee_IdAndDeletedIsFalse(employeeId)
                .stream()
                .map(FeedbackDto::new)
                .toList();

        if (feedbacks.isEmpty()) {
            throw new InvalidRequestParametersException(
                "Cannot generate AI summary: No feedbacks found for employee with ID: " + employeeId);
        }

        String aiGeneratedSummary = employeeAiService.generateFeedbackSummary(feedbacks, employee.getFullName());

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        EmployeeFeedbackSummary entity = new EmployeeFeedbackSummary(employee, aiGeneratedSummary, username);
        EmployeeFeedbackSummary savedEntity = summaryRepository.save(entity);

        return toDTO(savedEntity);
    }

    @Override
    public EmployeeFeedbackSummaryRepository getRepository() {
        return summaryRepository;
    }

    @Override
    public EmployeeFeedbackSummaryDto toDTO(EmployeeFeedbackSummary entity) {
        return new EmployeeFeedbackSummaryDto(entity);
    }

    @Override
    public EmployeeFeedbackSummary toEntity(EmployeeFeedbackSummaryDto dto) {
        Employee employee = employeeRepository.findById(dto.employeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + dto.employeeId()));

        return new EmployeeFeedbackSummary(employee, dto.summary(), dto.createdBy());
    }
}