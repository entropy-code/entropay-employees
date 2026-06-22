package com.entropyteam.entropay.employees.payroll;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PayrollStaleRunSweeperTest {

    private static final long THRESHOLD_MINUTES = 60;

    @Mock
    private PayrollRunRepository payrollRunRepository;

    private PayrollStaleRunSweeper sweeper;

    @BeforeEach
    void setUp() {
        sweeper = new PayrollStaleRunSweeper(payrollRunRepository, THRESHOLD_MINUTES);
    }

    @Test
    void givenStaleRunningRun_whenSweep_thenMarkedFailedWithErrorMessage() {
        PayrollRun stale = run(PayrollRunStatus.RUNNING,
                Instant.now().minus(THRESHOLD_MINUTES + 10, ChronoUnit.MINUTES));
        when(payrollRunRepository.findAllByStatusAndDeletedIsFalse(PayrollRunStatus.RUNNING))
                .thenReturn(List.of(stale));

        sweeper.sweepStaleRunningRuns();

        assertThat(stale.getStatus()).isEqualTo(PayrollRunStatus.FAILED);
        assertThat(stale.getCompletedAt()).isNotNull();
        assertThat(stale.getErrorMessage()).isNotNull().contains("FAILED");
        verify(payrollRunRepository, times(1)).save(stale);
    }

    @Test
    void givenRecentRunningRun_whenSweep_thenLeftUntouched() {
        // Started 5 minutes ago — another ECS instance may legitimately still be processing it.
        PayrollRun recent = run(PayrollRunStatus.RUNNING,
                Instant.now().minus(5, ChronoUnit.MINUTES));
        when(payrollRunRepository.findAllByStatusAndDeletedIsFalse(PayrollRunStatus.RUNNING))
                .thenReturn(List.of(recent));

        sweeper.sweepStaleRunningRuns();

        assertThat(recent.getStatus()).isEqualTo(PayrollRunStatus.RUNNING);
        verify(payrollRunRepository, never()).save(any());
    }

    @Test
    void givenRunningRunWithoutStartedAt_whenSweep_thenLeftUntouched() {
        PayrollRun noStart = run(PayrollRunStatus.RUNNING, null);
        when(payrollRunRepository.findAllByStatusAndDeletedIsFalse(PayrollRunStatus.RUNNING))
                .thenReturn(List.of(noStart));

        sweeper.sweepStaleRunningRuns();

        assertThat(noStart.getStatus()).isEqualTo(PayrollRunStatus.RUNNING);
        verify(payrollRunRepository, never()).save(any());
    }

    @Test
    void givenNoRunningRuns_whenSweep_thenNothingSaved() {
        when(payrollRunRepository.findAllByStatusAndDeletedIsFalse(PayrollRunStatus.RUNNING))
                .thenReturn(List.of());

        sweeper.sweepStaleRunningRuns();

        verify(payrollRunRepository, never()).save(any());
    }

    @Test
    void givenMixOfStaleAndRecentRuns_whenSweep_thenOnlyStaleOnesFailed() {
        PayrollRun stale = run(PayrollRunStatus.RUNNING,
                Instant.now().minus(THRESHOLD_MINUTES + 30, ChronoUnit.MINUTES));
        PayrollRun recent = run(PayrollRunStatus.RUNNING,
                Instant.now().minus(2, ChronoUnit.MINUTES));
        when(payrollRunRepository.findAllByStatusAndDeletedIsFalse(PayrollRunStatus.RUNNING))
                .thenReturn(List.of(stale, recent));

        sweeper.sweepStaleRunningRuns();

        assertThat(stale.getStatus()).isEqualTo(PayrollRunStatus.FAILED);
        assertThat(recent.getStatus()).isEqualTo(PayrollRunStatus.RUNNING);
        verify(payrollRunRepository, times(1)).save(stale);
        verify(payrollRunRepository, never()).save(recent);
    }

    private PayrollRun run(PayrollRunStatus status, Instant startedAt) {
        PayrollRun r = new PayrollRun();
        r.setId(UUID.randomUUID());
        r.setPeriod(LocalDate.of(2026, 4, 1));
        r.setStatus(status);
        r.setStartedAt(startedAt);
        return r;
    }
}
