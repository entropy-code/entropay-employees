package com.entropyteam.entropay.employees.controllers;

import static com.entropyteam.entropay.auth.AuthConstants.ROLE_ADMIN;
import static com.entropyteam.entropay.auth.AuthConstants.ROLE_ANALYST;
import static com.entropyteam.entropay.auth.AuthConstants.ROLE_DEVELOPMENT;
import static com.entropyteam.entropay.auth.AuthConstants.ROLE_HR_DIRECTOR;
import static com.entropyteam.entropay.auth.AuthConstants.ROLE_MANAGER_HR;

import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.entropyteam.entropay.common.BaseController;
import com.entropyteam.entropay.employees.dtos.EmployeeFeedbackSummaryDto;
import com.entropyteam.entropay.employees.services.EmployeeFeedbackSummaryService;

@RestController
@CrossOrigin
@RequestMapping("/feedback-summary")
public class EmployeeFeedbackSummaryController extends BaseController<EmployeeFeedbackSummaryDto, UUID> {

    private final EmployeeFeedbackSummaryService summaryService;

    public EmployeeFeedbackSummaryController(EmployeeFeedbackSummaryService summaryService) {
        super(summaryService);
        this.summaryService = summaryService;
    }

    @GetMapping("/employee/{employeeId}/current")
    @Secured({ROLE_ADMIN, ROLE_MANAGER_HR, ROLE_ANALYST, ROLE_DEVELOPMENT, ROLE_HR_DIRECTOR})
    public ResponseEntity<EmployeeFeedbackSummaryDto> getCurrentSummaryByEmployee(
            @PathVariable UUID employeeId) {
        EmployeeFeedbackSummaryDto summary = summaryService.getCurrentSummaryByEmployeeId(employeeId);
        if (summary == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/employee/{employeeId}")
    @Secured({ROLE_ADMIN, ROLE_MANAGER_HR, ROLE_ANALYST, ROLE_DEVELOPMENT, ROLE_HR_DIRECTOR})
    public ResponseEntity<List<EmployeeFeedbackSummaryDto>> getAllSummariesByEmployee(
            @PathVariable UUID employeeId) {
        List<EmployeeFeedbackSummaryDto> summaries = summaryService.getAllSummariesByEmployeeId(employeeId);
        return ResponseEntity.ok(summaries);
    }

    /**
     * Generate an AI-powered summary of all feedbacks for an employee
     * POST /feedback-summary/employee/{employeeId}/generate
     */
    @PostMapping("/employee/{employeeId}/generate")
    @Secured({ROLE_ADMIN, ROLE_MANAGER_HR, ROLE_HR_DIRECTOR})
    public ResponseEntity<EmployeeFeedbackSummaryDto> generateAISummary(
            @PathVariable UUID employeeId) {
        EmployeeFeedbackSummaryDto summary = summaryService.generateSummaryWithAI(employeeId);
        return ResponseEntity.ok(summary);
    }
}