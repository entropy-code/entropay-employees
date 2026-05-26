package com.entropyteam.entropay.employees.payroll;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * On application startup, marks orphaned payroll runs as FAILED.
 *
 * <p>If the JVM restarts mid-run, the {@code payroll_run} row stays {@code RUNNING} forever, and
 * {@link PayrollRunService#createRunForPeriod} then rejects that period permanently with 409.
 * This sweeper transitions such rows to {@code FAILED} so the period can be re-run.
 *
 * <p>The service can run as multiple ECS instances: another instance may legitimately be mid-run
 * when this one starts. To avoid failing a healthy concurrent run, only runs whose
 * {@code startedAt} is older than a configurable threshold (default ~1 hour) are swept.
 */
@Component
class PayrollStaleRunSweeper {

    private static final Logger LOGGER = LoggerFactory.getLogger(PayrollStaleRunSweeper.class);

    private final PayrollRunRepository payrollRunRepository;
    private final long thresholdMinutes;

    public PayrollStaleRunSweeper(PayrollRunRepository payrollRunRepository,
            @Value("${payroll.stale-run.threshold-minutes:60}") long thresholdMinutes) {
        this.payrollRunRepository = payrollRunRepository;
        this.thresholdMinutes = thresholdMinutes;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void sweepStaleRunningRuns() {
        Instant cutoff = Instant.now().minus(thresholdMinutes, ChronoUnit.MINUTES);
        List<PayrollRun> runningRuns =
                payrollRunRepository.findAllByStatusAndDeletedIsFalse(PayrollRunStatus.RUNNING);

        int swept = 0;
        for (PayrollRun run : runningRuns) {
            Instant startedAt = run.getStartedAt();
            // Skip runs without a startedAt or that started recently — another ECS instance may
            // legitimately still be processing them.
            if (startedAt == null || startedAt.isAfter(cutoff)) {
                continue;
            }
            run.setStatus(PayrollRunStatus.FAILED);
            run.setCompletedAt(Instant.now());
            run.setErrorMessage("Run was still RUNNING after a server restart and exceeded the "
                    + thresholdMinutes + "-minute stale threshold; marked FAILED by the startup "
                    + "sweep. Re-trigger the payroll run for this period.");
            payrollRunRepository.save(run);
            swept++;
            LOGGER.warn("Stale payroll run {} (period {}, startedAt {}) marked FAILED by startup sweep",
                    run.getId(), run.getPeriod(), startedAt);
        }

        if (swept > 0) {
            LOGGER.info("Payroll startup sweep marked {} stale RUNNING run(s) as FAILED", swept);
        } else {
            LOGGER.debug("Payroll startup sweep found no stale RUNNING runs");
        }
    }
}
