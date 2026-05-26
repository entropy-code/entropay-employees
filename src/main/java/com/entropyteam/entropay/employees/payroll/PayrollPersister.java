package com.entropyteam.entropay.employees.payroll;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Owns the database writes for the orchestrated payroll run. Keeping persistence in its own
 * Spring-managed bean lets {@link PayrollOrchestratorService} remain a non-transactional async
 * coordinator, sidestepping the self-invocation limitations of {@code @Async + @Transactional}.
 */
@Service
class PayrollPersister {

    private final PayrollRunRepository payrollRunRepository;
    private final PayrollItemRepository payrollItemRepository;

    public PayrollPersister(PayrollRunRepository payrollRunRepository,
            PayrollItemRepository payrollItemRepository) {
        this.payrollRunRepository = payrollRunRepository;
        this.payrollItemRepository = payrollItemRepository;
    }

    @Transactional
    public void persistRunResult(UUID runId, List<PayrollItem> items, BigDecimal total,
            int employeeCount) {
        PayrollRun run = payrollRunRepository.findById(runId).orElseThrow();
        items.forEach(i -> i.setPayrollRun(run));
        payrollItemRepository.saveAll(items);
        run.setStatus(PayrollRunStatus.DRAFT);
        run.setCompletedAt(Instant.now());
        run.setTotalAmount(total);
        run.setEmployeeCount(employeeCount);
        payrollRunRepository.save(run);
    }

    @Transactional
    public void persistRunFailure(UUID runId, String errorMessage) {
        PayrollRun run = payrollRunRepository.findById(runId).orElseThrow();
        run.setStatus(PayrollRunStatus.FAILED);
        run.setCompletedAt(Instant.now());
        run.setErrorMessage(truncate(errorMessage));
        payrollRunRepository.save(run);
    }

    private static String truncate(String s) {
        if (s == null) {
            return null;
        }
        return s.length() <= 1990 ? s : s.substring(0, 1990) + "...";
    }
}
