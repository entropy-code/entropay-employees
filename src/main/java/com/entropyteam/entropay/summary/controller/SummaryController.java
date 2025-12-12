package com.entropyteam.entropay.summary.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import com.entropyteam.entropay.summary.dtos.EmployeeSummaryDto;
import com.entropyteam.entropay.summary.services.SummaryService;
import com.entropyteam.entropay.common.BaseController;

import java.util.List;
import java.util.UUID;

import static com.entropyteam.entropay.auth.AuthConstants.*;


@RestController
@CrossOrigin
@Secured({ROLE_ADMIN, ROLE_MANAGER_HR, ROLE_ANALYST, ROLE_DEVELOPMENT, ROLE_HR_DIRECTOR})
@RequestMapping("/summary")

public class SummaryController extends BaseController<EmployeeSummaryDto, UUID> {

  /*  public SummaryController(SummaryService summaryService) {
        super(summaryService);  }*/

    private final SummaryService summaryService;

    public SummaryController(SummaryService summaryService) {
        super(summaryService);
        this.summaryService = summaryService;
    }

    // Generar resumen pasando nombre y feedbacks manualmente
    @PostMapping("/{id}/generate")
    public ResponseEntity<String> generateSummary(
            @PathVariable UUID id,
            @RequestParam(required = false) String prompt) {

        String summary = summaryService.generateSummaryByEmployeeId(id, prompt);
        return ResponseEntity.ok(summary);
    }

}







/*
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
*/
