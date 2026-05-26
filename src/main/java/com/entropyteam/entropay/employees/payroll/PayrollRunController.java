package com.entropyteam.entropay.employees.payroll;

import static com.entropyteam.entropay.auth.AuthConstants.ROLE_ADMIN;
import static com.entropyteam.entropay.auth.AuthConstants.ROLE_HR_DIRECTOR;
import static com.entropyteam.entropay.auth.AuthConstants.ROLE_MANAGER_HR;

import java.util.List;
import java.util.UUID;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import com.entropyteam.entropay.common.BaseController;
import com.entropyteam.entropay.common.ReactAdminParams;

/**
 * REST surface for payroll runs. Doesn't extend {@link BaseController} because the POST shape is a
 * trigger DTO ({@link TriggerPayrollDto}), not the read DTO; the inherited approach would force the
 * FE to send all the empty fields of {@link PayrollRunDto}.
 */
@RestController
@CrossOrigin
@RequestMapping(value = "/payroll-runs", produces = MediaType.APPLICATION_JSON_VALUE)
@Secured({ROLE_ADMIN, ROLE_HR_DIRECTOR, ROLE_MANAGER_HR})
class PayrollRunController {

    private final PayrollRunService payrollRunService;
    private final PayrollOrchestratorService orchestrator;

    public PayrollRunController(PayrollRunService payrollRunService,
            PayrollOrchestratorService orchestrator) {
        this.payrollRunService = payrollRunService;
        this.orchestrator = orchestrator;
    }

    @GetMapping
    public ResponseEntity<List<PayrollRunDto>> list(ReactAdminParams params) {
        Page<PayrollRunDto> page = payrollRunService.findAllActive(params);
        return ResponseEntity.ok()
                .header(BaseController.X_TOTAL_COUNT, String.valueOf(page.getTotalElements()))
                .body(page.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PayrollRunDto> getOne(@PathVariable UUID id) {
        return payrollRunService.findOne(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payroll run not found"));
    }

    /** Fire-and-forget. Returns 202 with the freshly-created run (status=RUNNING) for FE polling. */
    @PostMapping
    public ResponseEntity<PayrollRunDto> trigger(@Valid @RequestBody TriggerPayrollDto dto) {
        PayrollRun run = payrollRunService.createRunForPeriod(dto);
        orchestrator.startPayrollRun(run.getId(), run.getPeriod());
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(payrollRunService.findOne(run.getId()).orElseThrow());
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<PayrollRunDto> approve(@PathVariable UUID id) {
        return ResponseEntity.ok(payrollRunService.approve(id));
    }

    @PutMapping("/{id}/unapprove")
    public ResponseEntity<PayrollRunDto> unapprove(@PathVariable UUID id) {
        return ResponseEntity.ok(payrollRunService.unapprove(id));
    }

    @PutMapping("/{id}/close")
    public ResponseEntity<PayrollRunDto> close(@PathVariable UUID id) {
        return ResponseEntity.ok(payrollRunService.close(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PayrollRunDto> delete(@PathVariable UUID id) {
        return ResponseEntity.ok(payrollRunService.delete(id));
    }
}
