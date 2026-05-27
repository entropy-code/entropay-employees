package com.entropyteam.entropay.employees.payroll;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.entropyteam.entropay.employees.models.Contract;
import com.entropyteam.entropay.employees.models.Employee;

@ExtendWith(MockitoExtension.class)
class PayrollOrchestratorServiceTest {

    @Mock private PayrollDataLoader dataLoader;
    @Mock private PayrollPersister persister;

    @Captor private ArgumentCaptor<List<PayrollItem>> itemsCaptor;

    private PayrollCalculatorService calculator;
    private PayrollOrchestratorService orchestrator;
    private final Executor executor = Runnable::run;

    @BeforeEach
    void setUp() {
        calculator = new PayrollCalculatorService();
        orchestrator = new PayrollOrchestratorService(
                dataLoader, calculator, persister, executor);
    }

    @Test
    void givenMultipleEmployees_whenStartPayrollRun_thenAllItemsCalculatedAndBatchPersisted() {
        UUID runId = UUID.randomUUID();
        LocalDate period = LocalDate.of(2026, 4, 1);
        Contract c1 = simpleContract("E001", new BigDecimal("4000"));
        Contract c2 = simpleContract("E002", new BigDecimal("5000"));
        when(dataLoader.loadContext(eq(period))).thenReturn(emptyContextWith(period, List.of(c1, c2)));

        CompletableFuture<UUID> result = orchestrator.startPayrollRun(runId, period);
        assertThat(result).isCompletedWithValue(runId);

        verify(persister).persistRunResult(eq(runId), itemsCaptor.capture(), any(), eq(2));
        List<PayrollItem> items = itemsCaptor.getValue();
        assertThat(items).hasSize(2);
        assertThat(items).allMatch(i -> i.getCalculationError() == null);
    }

    @Test
    void givenOneCalculationFails_thenItemMarkedWithErrorAndOthersStillPersisted() {
        UUID runId = UUID.randomUUID();
        LocalDate period = LocalDate.of(2026, 4, 1);
        Contract good = simpleContract("E001", new BigDecimal("4000"));
        Contract broken = brokenContractMissingSettlement("E002");
        when(dataLoader.loadContext(eq(period))).thenReturn(emptyContextWith(period, List.of(good, broken)));

        orchestrator.startPayrollRun(runId, period);

        verify(persister).persistRunResult(eq(runId), itemsCaptor.capture(), any(), eq(2));
        List<PayrollItem> items = itemsCaptor.getValue();
        assertThat(items).hasSize(2);
        assertThat(items).filteredOn(i -> i.getCalculationError() != null).hasSize(1);
        verify(persister, never()).persistRunFailure(any(), any());
    }

    @Test
    void givenLoaderThrows_thenRunMarkedFailedWithErrorMessage() {
        UUID runId = UUID.randomUUID();
        LocalDate period = LocalDate.of(2026, 4, 1);
        when(dataLoader.loadContext(eq(period))).thenThrow(new RuntimeException("DB exploded"));

        CompletableFuture<UUID> result = orchestrator.startPayrollRun(runId, period);

        assertThat(result).isCompletedExceptionally();
        verify(persister).persistRunFailure(eq(runId), org.mockito.ArgumentMatchers.contains("DB exploded"));
        verify(persister, never()).persistRunResult(any(), any(), any(), anyInt());
    }

    private Contract simpleContract(String internalId, BigDecimal salary) {
        Employee e = new Employee();
        e.setId(UUID.randomUUID());
        e.setInternalId(internalId);
        Contract c = new Contract();
        c.setId(UUID.randomUUID());
        c.setEmployee(e);
        c.setStartDate(LocalDate.of(2024, 1, 1));
        c.setActive(true);
        var ps = new com.entropyteam.entropay.employees.models.PaymentSettlement();
        ps.setId(UUID.randomUUID());
        ps.setSalary(salary);
        ps.setModality(com.entropyteam.entropay.employees.models.Modality.MONTHLY);
        ps.setCurrency(com.entropyteam.entropay.employees.models.Currency.USD);
        ps.setContract(c);
        c.setPaymentsSettlement(new HashSet<>(List.of(ps)));
        return c;
    }

    private Contract brokenContractMissingSettlement(String internalId) {
        Employee e = new Employee();
        e.setId(UUID.randomUUID());
        e.setInternalId(internalId);
        Contract c = new Contract();
        c.setId(UUID.randomUUID());
        c.setEmployee(e);
        c.setStartDate(LocalDate.of(2024, 1, 1));
        return c; // No paymentsSettlement → calculator records calculationError, no exception.
    }

    private PayrollContext emptyContextWith(LocalDate period, List<Contract> contracts) {
        return new PayrollContext(
                period,
                contracts,
                Collections.emptyMap(),
                Collections.emptyMap(),
                Collections.emptyMap(),
                Collections.emptyMap(),
                Collections.emptyMap(),
                Collections.emptyMap(),
                new HashMap<>(),
                Collections.emptyMap(),
                Collections.emptyMap(),
                Collections.emptyMap()
        );
    }

    @SuppressWarnings("unused")
    private static HashSet<LocalDate> empty() {
        return new HashSet<>();
    }
}
