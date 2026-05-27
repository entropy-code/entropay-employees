package com.entropyteam.entropay.employees.payroll;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Drives a payroll run end-to-end: the {@code @Async} entrypoint runs on the small dedicated
 * {@code payrollDispatchExecutor}, loads context once, fans out the per-employee calculations
 * across the separate {@code payrollExecutor}, then hands the results to {@link PayrollPersister}.
 * Keeping dispatch and calculation on distinct pools avoids self-contention and queue overflow.
 * Models the orchestration after {@code EmailLeakCheckService}'s {@code CompletableFuture.allOf(...)}
 * pattern.
 */
@Service
class PayrollOrchestratorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PayrollOrchestratorService.class);

    private final PayrollDataLoader dataLoader;
    private final PayrollCalculatorService calculator;
    private final PayrollPersister persister;
    private final Executor payrollExecutor;

    public PayrollOrchestratorService(PayrollDataLoader dataLoader,
            PayrollCalculatorService calculator,
            PayrollPersister persister,
            @Qualifier("payrollExecutor") Executor payrollExecutor) {
        this.dataLoader = dataLoader;
        this.calculator = calculator;
        this.persister = persister;
        this.payrollExecutor = payrollExecutor;
    }

    @Async("payrollDispatchExecutor")
    public CompletableFuture<UUID> startPayrollRun(UUID runId, LocalDate period) {
        LOGGER.info("Payroll run {} starting for period {}", runId, period);
        try {
            PayrollContext ctx = dataLoader.loadContext(period);

            List<CompletableFuture<PayrollItem>> futures = ctx.contracts().stream()
                    .map(contract -> CompletableFuture.supplyAsync(
                            () -> calculator.calculate(contract, ctx), payrollExecutor))
                    .toList();
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

            List<PayrollItem> items = futures.stream().map(CompletableFuture::join).toList();
            BigDecimal total = items.stream()
                    .map(PayrollItem::getTotalAmount)
                    .filter(java.util.Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            persister.persistRunResult(
                    runId, items,
                    total.setScale(2, RoundingMode.HALF_UP),
                    items.size());

            int erroredCount = (int) items.stream().filter(i -> i.getCalculationError() != null).count();
            LOGGER.info("Payroll run {} completed: {} items, {} errored, total={}",
                    runId, items.size(), erroredCount, total);
            return CompletableFuture.completedFuture(runId);
        } catch (RuntimeException e) {
            LOGGER.error("Payroll run {} failed: {}", runId, e.getMessage(), e);
            persister.persistRunFailure(runId, e.getClass().getSimpleName() + ": " + e.getMessage());
            return CompletableFuture.failedFuture(e);
        }
    }
}
