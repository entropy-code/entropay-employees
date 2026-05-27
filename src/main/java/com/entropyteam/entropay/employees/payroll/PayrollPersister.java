package com.entropyteam.entropay.employees.payroll;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Owns the database writes for the orchestrated payroll run. Keeping persistence in its own
 * Spring-managed bean lets {@link PayrollOrchestratorService} remain a non-transactional async
 * coordinator, sidestepping the self-invocation limitations of {@code @Async + @Transactional}.
 *
 * <p>Both write methods guard against the race where a run is soft-deleted (or otherwise transitions
 * out of {@code RUNNING}) while the async orchestrator is still computing. Without the guard, the
 * orchestrator would resurrect the deleted run by writing items + flipping its status. With it, a
 * concurrent delete is honored: the late persist becomes a no-op and the run stays deleted.
 */
@Service
class PayrollPersister {

    private static final Logger LOGGER = LoggerFactory.getLogger(PayrollPersister.class);

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
        Optional<PayrollRun> loaded = loadRunIfStillRunning(runId, "result");
        if (loaded.isEmpty()) {
            return;
        }
        PayrollRun run = loaded.get();
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
        Optional<PayrollRun> loaded = loadRunIfStillRunning(runId, "failure");
        if (loaded.isEmpty()) {
            return;
        }
        PayrollRun run = loaded.get();
        run.setStatus(PayrollRunStatus.FAILED);
        run.setCompletedAt(Instant.now());
        run.setErrorMessage(truncate(errorMessage));
        payrollRunRepository.save(run);
    }

    private Optional<PayrollRun> loadRunIfStillRunning(UUID runId, String operation) {
        Optional<PayrollRun> opt = payrollRunRepository.findById(runId);
        if (opt.isEmpty()) {
            LOGGER.warn("Payroll persist {} skipped: run {} no longer exists", operation, runId);
            return Optional.empty();
        }
        PayrollRun run = opt.get();
        if (run.isDeleted() || run.getStatus() != PayrollRunStatus.RUNNING) {
            LOGGER.warn("Payroll persist {} skipped: run {} is no longer RUNNING "
                            + "(status={}, deleted={}) — likely cancelled or swept",
                    operation, runId, run.getStatus(), run.isDeleted());
            return Optional.empty();
        }
        return Optional.of(run);
    }

    private static String truncate(String s) {
        if (s == null) {
            return null;
        }
        return s.length() <= 1990 ? s : s.substring(0, 1990) + "...";
    }
}
