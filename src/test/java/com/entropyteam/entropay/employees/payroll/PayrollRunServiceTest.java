package com.entropyteam.entropay.employees.payroll;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.AfterEach;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;
import com.entropyteam.entropay.common.ReactAdminMapper;

@ExtendWith(MockitoExtension.class)
class PayrollRunServiceTest {

    @Mock private PayrollRunRepository payrollRunRepository;
    @Mock private PayrollItemRepository payrollItemRepository;
    @Mock private PayrollItemService payrollItemService;
    @Mock private ReactAdminMapper reactAdminMapper;

    private PayrollRunService service;

    @BeforeEach
    void setUp() {
        service = new PayrollRunService(payrollRunRepository, payrollItemRepository, payrollItemService, reactAdminMapper);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    private void authenticateAs(String role) {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("test-user", null,
                        List.of(new SimpleGrantedAuthority(role))));
    }

    @Test
    void givenApprovedRunExistsForPeriod_whenCreateRun_thenConflict() {
        LocalDate period = LocalDate.of(2026, 4, 1);
        PayrollRun existing = run(period, PayrollRunStatus.APPROVED);
        when(payrollRunRepository.findByPeriodAndDeletedIsFalse(period)).thenReturn(Optional.of(existing));

        assertThatThrownBy(() -> service.createRunForPeriod(new TriggerPayrollDto(period)))
                .isInstanceOf(ResponseStatusException.class)
                .extracting(e -> ((ResponseStatusException) e).getStatusCode())
                .isEqualTo(HttpStatus.CONFLICT);

        verify(payrollRunRepository, never()).save(any());
        verify(payrollRunRepository, never()).saveAndFlush(any());
    }

    @Test
    void givenClosedRunExistsForPeriod_whenCreateRun_thenConflict() {
        LocalDate period = LocalDate.of(2026, 4, 1);
        when(payrollRunRepository.findByPeriodAndDeletedIsFalse(period))
                .thenReturn(Optional.of(run(period, PayrollRunStatus.CLOSED)));

        assertThatThrownBy(() -> service.createRunForPeriod(new TriggerPayrollDto(period)))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void givenRunningRunExistsForPeriod_whenCreateRun_thenConflict() {
        LocalDate period = LocalDate.of(2026, 4, 1);
        when(payrollRunRepository.findByPeriodAndDeletedIsFalse(period))
                .thenReturn(Optional.of(run(period, PayrollRunStatus.RUNNING)));

        assertThatThrownBy(() -> service.createRunForPeriod(new TriggerPayrollDto(period)))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void givenDraftRunExistsForPeriod_whenCreateRun_thenSoftDeletesDraftAndItemsAndCreatesNew() {
        LocalDate period = LocalDate.of(2026, 4, 1);
        PayrollRun draft = run(period, PayrollRunStatus.DRAFT);
        when(payrollRunRepository.findByPeriodAndDeletedIsFalse(period)).thenReturn(Optional.of(draft));
        when(payrollItemRepository.findAllByPayrollRunIdAndDeletedIsFalse(draft.getId()))
                .thenReturn(Collections.emptyList());
        when(payrollRunRepository.saveAndFlush(any())).thenAnswer(inv -> inv.getArgument(0));

        PayrollRun created = service.createRunForPeriod(new TriggerPayrollDto(period));

        assertThat(draft.isDeleted()).isTrue();
        assertThat(created.getStatus()).isEqualTo(PayrollRunStatus.RUNNING);
        // Both the soft-deleted draft and the new RUNNING run are persisted with saveAndFlush:
        // the draft so its UPDATE hits the DB before the new INSERT, and the new run so a unique
        // constraint violation surfaces inside createRunForPeriod (as 409) rather than at commit (500).
        verify(payrollRunRepository, times(1)).saveAndFlush(draft);
        verify(payrollRunRepository, times(2)).saveAndFlush(any());
    }

    @Test
    void givenFailedRunExistsForPeriod_whenCreateRun_thenSoftDeletesFailedAndCreatesNew() {
        LocalDate period = LocalDate.of(2026, 4, 1);
        PayrollRun failed = run(period, PayrollRunStatus.FAILED);
        when(payrollRunRepository.findByPeriodAndDeletedIsFalse(period)).thenReturn(Optional.of(failed));
        when(payrollItemRepository.findAllByPayrollRunIdAndDeletedIsFalse(failed.getId()))
                .thenReturn(Collections.emptyList());
        when(payrollRunRepository.saveAndFlush(any())).thenAnswer(inv -> inv.getArgument(0));

        PayrollRun created = service.createRunForPeriod(new TriggerPayrollDto(period));

        assertThat(failed.isDeleted()).isTrue();
        assertThat(created.getStatus()).isEqualTo(PayrollRunStatus.RUNNING);
        // saveAndFlush is the load-bearing call: it pushes the soft-delete UPDATE to the DB before
        // the new RUNNING row's INSERT, avoiding the idx_payroll_run_period_unique_active violation
        // that Hibernate's default insert-before-update flush order would otherwise produce.
        verify(payrollRunRepository, times(1)).saveAndFlush(failed);
    }

    @Test
    void givenNoExistingRun_whenCreateRun_thenNewRunningRunSavedWithStartTimestamp() {
        LocalDate period = LocalDate.of(2026, 4, 1);
        when(payrollRunRepository.findByPeriodAndDeletedIsFalse(period)).thenReturn(Optional.empty());
        ArgumentCaptor<PayrollRun> captor = ArgumentCaptor.forClass(PayrollRun.class);
        when(payrollRunRepository.saveAndFlush(any())).thenAnswer(inv -> {
            PayrollRun r = inv.getArgument(0);
            r.setId(UUID.randomUUID());
            return r;
        });

        PayrollRun result = service.createRunForPeriod(new TriggerPayrollDto(period));

        verify(payrollRunRepository).saveAndFlush(captor.capture());
        PayrollRun saved = captor.getValue();
        assertThat(saved.getStatus()).isEqualTo(PayrollRunStatus.RUNNING);
        assertThat(saved.getStartedAt()).isNotNull();
        assertThat(saved.getPeriod()).isEqualTo(period);
        assertThat(result.getStatus()).isEqualTo(PayrollRunStatus.RUNNING);
    }

    @Test
    void givenConcurrentInsertRaces_whenCreateRun_thenReturnsConflictNot500() {
        // Both threads pass findByPeriodAndDeletedIsFalse (empty), both try to INSERT, the second
        // hits idx_payroll_run_period_unique_active → DataIntegrityViolationException. The fix
        // catches it inside createRunForPeriod (thanks to saveAndFlush) and surfaces a clean 409.
        LocalDate period = LocalDate.of(2026, 4, 1);
        when(payrollRunRepository.findByPeriodAndDeletedIsFalse(period)).thenReturn(Optional.empty());
        when(payrollRunRepository.saveAndFlush(any()))
                .thenThrow(new DataIntegrityViolationException(
                        "duplicate key value violates unique constraint \"idx_payroll_run_period_unique_active\""));

        assertThatThrownBy(() -> service.createRunForPeriod(new TriggerPayrollDto(period)))
                .isInstanceOf(ResponseStatusException.class)
                .extracting(e -> ((ResponseStatusException) e).getStatusCode())
                .isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void approve_whenStatusIsDraft_thenTransitionsToApproved() {
        PayrollRun draft = run(LocalDate.of(2026, 4, 1), PayrollRunStatus.DRAFT);
        when(payrollRunRepository.findById(draft.getId())).thenReturn(Optional.of(draft));
        when(payrollRunRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var result = service.approve(draft.getId());

        assertThat(result.status()).isEqualTo(PayrollRunStatus.APPROVED);
    }

    @Test
    void approve_whenStatusIsRunning_thenConflict() {
        PayrollRun running = run(LocalDate.of(2026, 4, 1), PayrollRunStatus.RUNNING);
        when(payrollRunRepository.findById(running.getId())).thenReturn(Optional.of(running));

        assertThatThrownBy(() -> service.approve(running.getId()))
                .isInstanceOf(ResponseStatusException.class)
                .extracting(e -> ((ResponseStatusException) e).getStatusCode())
                .isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void delete_whenStatusIsApprovedAndUserIsNotAdmin_thenConflict() {
        authenticateAs("ROLE_MANAGER/HR");
        PayrollRun approved = run(LocalDate.of(2026, 4, 1), PayrollRunStatus.APPROVED);
        when(payrollRunRepository.findById(approved.getId())).thenReturn(Optional.of(approved));

        assertThatThrownBy(() -> service.delete(approved.getId()))
                .isInstanceOf(ResponseStatusException.class)
                .extracting(e -> ((ResponseStatusException) e).getStatusCode())
                .isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void delete_whenStatusIsApprovedAndUserIsAdmin_thenSoftDeletes() {
        authenticateAs("ROLE_ADMIN");
        PayrollRun approved = run(LocalDate.of(2026, 4, 1), PayrollRunStatus.APPROVED);
        when(payrollRunRepository.findById(approved.getId())).thenReturn(Optional.of(approved));
        when(payrollItemRepository.findAllByPayrollRunIdAndDeletedIsFalse(approved.getId()))
                .thenReturn(Collections.emptyList());

        service.delete(approved.getId());

        assertThat(approved.isDeleted()).isTrue();
    }

    @Test
    void delete_whenStatusIsClosedAndUserIsAdmin_thenSoftDeletes() {
        authenticateAs("ROLE_ADMIN");
        PayrollRun closed = run(LocalDate.of(2026, 4, 1), PayrollRunStatus.CLOSED);
        when(payrollRunRepository.findById(closed.getId())).thenReturn(Optional.of(closed));
        when(payrollItemRepository.findAllByPayrollRunIdAndDeletedIsFalse(closed.getId()))
                .thenReturn(Collections.emptyList());

        service.delete(closed.getId());

        assertThat(closed.isDeleted()).isTrue();
    }

    @Test
    void delete_whenStatusIsDraft_thenSoftDeletes() {
        PayrollRun draft = run(LocalDate.of(2026, 4, 1), PayrollRunStatus.DRAFT);
        when(payrollRunRepository.findById(draft.getId())).thenReturn(Optional.of(draft));
        when(payrollItemRepository.findAllByPayrollRunIdAndDeletedIsFalse(draft.getId()))
                .thenReturn(Collections.emptyList());

        service.delete(draft.getId());

        assertThat(draft.isDeleted()).isTrue();
    }

    @Test
    void findOne_mapsItemsInOneBatchWithoutPerItemLookup() {
        PayrollRun run = run(LocalDate.of(2026, 4, 1), PayrollRunStatus.DRAFT);
        PayrollItem item1 = new PayrollItem();
        item1.setId(UUID.randomUUID());
        PayrollItem item2 = new PayrollItem();
        item2.setId(UUID.randomUUID());
        when(payrollRunRepository.findById(run.getId())).thenReturn(Optional.of(run));
        when(payrollItemRepository.findAllByPayrollRunIdAndDeletedIsFalse(run.getId()))
                .thenReturn(List.of(item1, item2));

        Optional<PayrollRunDto> dto = service.findOne(run.getId());

        assertThat(dto).isPresent();
        assertThat(dto.get().items()).hasSize(2);
        // Items already loaded by findAll are mapped directly — no per-item findOne round-trip.
        verify(payrollItemService, never()).findOne(any());
        verify(payrollItemService, times(2)).toDTO(any());
    }

    private PayrollRun run(LocalDate period, PayrollRunStatus status) {
        PayrollRun r = new PayrollRun();
        r.setId(UUID.randomUUID());
        r.setPeriod(period);
        r.setStatus(status);
        return r;
    }
}
