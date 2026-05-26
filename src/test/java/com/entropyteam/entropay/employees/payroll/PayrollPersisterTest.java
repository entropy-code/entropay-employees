package com.entropyteam.entropay.employees.payroll;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PayrollPersisterTest {

    @Mock private PayrollRunRepository payrollRunRepository;
    @Mock private PayrollItemRepository payrollItemRepository;

    @InjectMocks private PayrollPersister persister;

    private UUID runId;

    @BeforeEach
    void setUp() {
        runId = UUID.randomUUID();
    }

    @Test
    void persistRunResult_whenRunIsRunning_thenWritesItemsAndFlipsToDraft() {
        PayrollRun run = newRun(PayrollRunStatus.RUNNING, false);
        when(payrollRunRepository.findById(runId)).thenReturn(Optional.of(run));
        PayrollItem item = new PayrollItem();

        persister.persistRunResult(runId, List.of(item), new BigDecimal("123.45"), 1);

        assertThat(run.getStatus()).isEqualTo(PayrollRunStatus.DRAFT);
        assertThat(run.getTotalAmount()).isEqualByComparingTo("123.45");
        assertThat(run.getEmployeeCount()).isEqualTo(1);
        assertThat(item.getPayrollRun()).isSameAs(run);
        verify(payrollItemRepository).saveAll(List.of(item));
        verify(payrollRunRepository).save(run);
    }

    @Test
    void persistRunResult_whenRunWasSoftDeletedMidRun_thenSkipsWriteAndDoesNotResurrect() {
        PayrollRun deleted = newRun(PayrollRunStatus.RUNNING, true);
        when(payrollRunRepository.findById(runId)).thenReturn(Optional.of(deleted));

        persister.persistRunResult(runId, List.of(new PayrollItem()), new BigDecimal("1"), 1);

        // Without the guard the orchestrator would write items + flip the deleted run to DRAFT,
        // leaving a zombie row (deleted=true, status=DRAFT, with items). The guard makes the
        // late persist a no-op so the user-initiated delete is honored.
        assertThat(deleted.getStatus()).isEqualTo(PayrollRunStatus.RUNNING);
        assertThat(deleted.isDeleted()).isTrue();
        verify(payrollItemRepository, never()).saveAll(any());
        verify(payrollRunRepository, never()).save(any());
    }

    @Test
    void persistRunResult_whenStatusNoLongerRunning_thenSkipsWrite() {
        // Could happen if the startup sweeper marked the run FAILED in between.
        PayrollRun swept = newRun(PayrollRunStatus.FAILED, false);
        when(payrollRunRepository.findById(runId)).thenReturn(Optional.of(swept));

        persister.persistRunResult(runId, List.of(new PayrollItem()), new BigDecimal("1"), 1);

        assertThat(swept.getStatus()).isEqualTo(PayrollRunStatus.FAILED);
        verify(payrollItemRepository, never()).saveAll(any());
        verify(payrollRunRepository, never()).save(any());
    }

    @Test
    void persistRunResult_whenRunNoLongerExists_thenSkipsWrite() {
        when(payrollRunRepository.findById(runId)).thenReturn(Optional.empty());

        persister.persistRunResult(runId, List.of(new PayrollItem()), new BigDecimal("1"), 1);

        verify(payrollItemRepository, never()).saveAll(any());
        verify(payrollRunRepository, never()).save(any());
    }

    @Test
    void persistRunFailure_whenRunIsRunning_thenMarksFailed() {
        PayrollRun run = newRun(PayrollRunStatus.RUNNING, false);
        when(payrollRunRepository.findById(runId)).thenReturn(Optional.of(run));

        persister.persistRunFailure(runId, "boom");

        assertThat(run.getStatus()).isEqualTo(PayrollRunStatus.FAILED);
        assertThat(run.getErrorMessage()).isEqualTo("boom");
        verify(payrollRunRepository).save(run);
    }

    @Test
    void persistRunFailure_whenRunWasSoftDeletedMidRun_thenSkipsWrite() {
        PayrollRun deleted = newRun(PayrollRunStatus.RUNNING, true);
        when(payrollRunRepository.findById(runId)).thenReturn(Optional.of(deleted));

        persister.persistRunFailure(runId, "boom");

        assertThat(deleted.getStatus()).isEqualTo(PayrollRunStatus.RUNNING);
        assertThat(deleted.getErrorMessage()).isNull();
        verify(payrollRunRepository, never()).save(any());
    }

    private PayrollRun newRun(PayrollRunStatus status, boolean deleted) {
        PayrollRun r = new PayrollRun();
        r.setId(runId);
        r.setStatus(status);
        r.setDeleted(deleted);
        return r;
    }
}
