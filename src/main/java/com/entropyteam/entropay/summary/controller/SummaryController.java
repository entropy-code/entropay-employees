package com.entropyteam.entropay.summary.controller;

import com.entropyteam.entropay.summary.dtos.EmployeeSummaryDto;
import com.entropyteam.entropay.summary.dtos.SummaryRequest;
import com.entropyteam.entropay.employees.models.Employee;
import com.entropyteam.entropay.summary.model.EmployeeSummary;
import com.entropyteam.entropay.employees.repositories.EmployeeRepository;
import com.entropyteam.entropay.summary.repository.EmployeeSummaryRepository;
import com.entropyteam.entropay.summary.services.SummaryService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;


@RestController
@RequestMapping("/api/summaries")
public class SummaryController {

    private final SummaryService summaryService;
    private final EmployeeRepository employeeRepo;
    private final EmployeeSummaryRepository summaryRepo;

    public SummaryController(SummaryService summaryService,
                             EmployeeRepository employeeRepo,
                             EmployeeSummaryRepository summaryRepo) {
        this.summaryService = summaryService;
        this.employeeRepo = employeeRepo;
        this.summaryRepo = summaryRepo;
    }

    @PostMapping
    public ResponseEntity<EmployeeSummaryDto> generateSummary(@RequestBody SummaryRequest request) {
        Employee employee = employeeRepo.findById(request.employeeId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empleado no encontrado"));

        String summaryText = summaryService.generateSummary(employee.getFullName(), request.feedbacks());
        String prompt = String.format("Resumen de feedback para %s", employee.getFullName());

        EmployeeSummary summary = new EmployeeSummary(employee, prompt, summaryText, "system");
        summary.setModifiedAt(summary.getCreatedAt());

        EmployeeSummary savedSummary = summaryRepo.save(summary);

        EmployeeSummaryDto dto = new EmployeeSummaryDto(
                savedSummary.getId(),
                savedSummary.getPrompt(),
                savedSummary.getSummaryText(),
                savedSummary.getCreatedBy(),
                savedSummary.getCreatedAt(),
                savedSummary.getEmployee().getFullName()
        );

        return ResponseEntity.ok(dto);
    }
}

