package com.entropyteam.entropay.employees.payroll;

import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.entropyteam.entropay.auth.AppRole;
import com.entropyteam.entropay.auth.AuthUtils;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.common.BaseService;
import com.entropyteam.entropay.common.ReactAdminMapper;
import com.entropyteam.entropay.common.ReactAdminParams;

@Service
class PayrollRunService extends BaseService<PayrollRun, PayrollRunDto, UUID> {

    private final PayrollRunRepository payrollRunRepository;
    private final PayrollItemRepository payrollItemRepository;
    private final PayrollItemService payrollItemService;

    public PayrollRunService(PayrollRunRepository payrollRunRepository,
            PayrollItemRepository payrollItemRepository,
            PayrollItemService payrollItemService,
            ReactAdminMapper reactAdminMapper) {
        super(PayrollRun.class, reactAdminMapper);
        this.payrollRunRepository = payrollRunRepository;
        this.payrollItemRepository = payrollItemRepository;
        this.payrollItemService = payrollItemService;
    }

    @Override
    protected BaseRepository<PayrollRun, UUID> getRepository() {
        return payrollRunRepository;
    }

    /**
     * Validate state, drop any prior DRAFT for the same period (cascade deletes its items), and
     * create a new RUNNING run synchronously so the FE has an id to poll. The actual calculation
     * happens asynchronously after this returns.
     */
    @Transactional
    public PayrollRun createRunForPeriod(TriggerPayrollDto dto) {
        LocalDate period = YearMonth.from(dto.period()).atDay(1);
        Optional<PayrollRun> existing = payrollRunRepository.findByPeriodAndDeletedIsFalse(period);
        if (existing.isPresent()) {
            PayrollRun current = existing.get();
            switch (current.getStatus()) {
                case APPROVED -> throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "An APPROVED payroll run already exists for " + period
                                + ". Un-approve it first if you need to re-run.");
                case CLOSED -> throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "A CLOSED payroll run already exists for " + period
                                + ". Closed runs can only be removed by an admin.");
                case RUNNING -> throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "A payroll run is already in progress for " + period);
                case DRAFT, FAILED -> deleteRunAndItems(current);
            }
        }
        PayrollRun run = new PayrollRun();
        run.setPeriod(period);
        run.setStatus(PayrollRunStatus.RUNNING);
        run.setStartedAt(Instant.now());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof JwtAuthenticationToken jwtAuth) {
            // auth.getName() is the `sub` claim (a UUID for Cognito); we want the human-readable email.
            String email = jwtAuth.getToken().getClaimAsString("email");
            if (email != null) {
                run.setTriggeredByEmail(email);
            } else {
                run.setTriggeredByEmail(auth.getName());
            }
            try {
                run.setTriggeredByUserId(UUID.fromString(jwtAuth.getToken().getSubject()));
            } catch (IllegalArgumentException ignored) {
                // sub isn't a UUID — leave triggeredByUserId null.
            }
        } else if (auth != null) {
            run.setTriggeredByEmail(auth.getName());
        }
        // saveAndFlush + catch DataIntegrityViolationException to make the race-safe path produce a
        // clean 409: two concurrent POSTs for the same period would both pass the existence check
        // above and both INSERT; one then hits idx_payroll_run_period_unique_active. With save() the
        // violation surfaces at commit (outside this method) as a 500. saveAndFlush forces it here.
        try {
            return payrollRunRepository.saveAndFlush(run);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "A payroll run already exists for " + period);
        }
    }

    @Transactional
    public PayrollRunDto approve(UUID runId) {
        return transition(runId, PayrollRunStatus.DRAFT, PayrollRunStatus.APPROVED);
    }

    @Transactional
    public PayrollRunDto close(UUID runId) {
        return transition(runId, PayrollRunStatus.APPROVED, PayrollRunStatus.CLOSED);
    }

    @Transactional
    public PayrollRunDto unapprove(UUID runId) {
        return transition(runId, PayrollRunStatus.APPROVED, PayrollRunStatus.DRAFT);
    }

    private PayrollRunDto transition(UUID runId, PayrollRunStatus from, PayrollRunStatus to) {
        PayrollRun run = payrollRunRepository.findById(runId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payroll run not found"));
        if (run.getStatus() != from) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Cannot transition from " + run.getStatus() + " to " + to);
        }
        run.setStatus(to);
        return toDTO(payrollRunRepository.save(run));
    }

    @Override
    @Transactional
    public PayrollRunDto delete(UUID runId) {
        PayrollRun run = payrollRunRepository.findById(runId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payroll run not found"));
        if (run.getStatus() == PayrollRunStatus.RUNNING) {
            // Deleting a RUNNING run would race the async orchestrator's persist step. Even with
            // PayrollPersister guarding against deleted/non-RUNNING runs, blocking here gives the
            // user a clear answer instead of a silently dropped run. Wait for the run to finish
            // (it will land in DRAFT or FAILED) and delete that.
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Cannot delete a payroll run while it is still RUNNING. "
                            + "Wait for it to finish (DRAFT or FAILED) and try again.");
        }
        boolean lockedStatus = run.getStatus() == PayrollRunStatus.APPROVED
                || run.getStatus() == PayrollRunStatus.CLOSED;
        if (lockedStatus && AuthUtils.getUserRole() != AppRole.ROLE_ADMIN) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Cannot delete a " + run.getStatus() + " payroll run; un-approve it first.");
        }
        deleteRunAndItems(run);
        return toDTO(run);
    }

    private void deleteRunAndItems(PayrollRun run) {
        payrollItemRepository.findAllByPayrollRunIdAndDeletedIsFalse(run.getId())
                .forEach(i -> i.setDeleted(true));
        run.setDeleted(true);
        // Flush so the soft-delete UPDATE hits the DB before any subsequent INSERT for the same
        // period. Hibernate's default ActionQueue runs INSERTs before UPDATEs at commit, which
        // would otherwise violate idx_payroll_run_period_unique_active when re-running a period.
        payrollRunRepository.saveAndFlush(run);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PayrollRunDto> findAllActive(ReactAdminParams params) {
        Page<PayrollRunDto> page = super.findAllActive(params);
        // Don't eagerly load items into the list response — the detail endpoint does that.
        List<PayrollRunDto> stripped = page.getContent().stream()
                .map(this::stripItems)
                .toList();
        return new PageImpl<>(stripped, Pageable.unpaged(), page.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PayrollRunDto> findOne(UUID id) {
        return payrollRunRepository.findById(id).map(this::toDTOWithItems);
    }

    private PayrollRunDto stripItems(PayrollRunDto dto) {
        return new PayrollRunDto(dto.id(), dto.period(), dto.status(),
                dto.triggeredByUserId(), dto.triggeredByEmail(), dto.startedAt(), dto.completedAt(),
                dto.errorMessage(), dto.totalAmount(), dto.employeeCount(), List.of());
    }

    @Override
    protected PayrollRunDto toDTO(PayrollRun entity) {
        return new PayrollRunDto(
                entity.getId(),
                entity.getPeriod(),
                entity.getStatus(),
                entity.getTriggeredByUserId(),
                entity.getTriggeredByEmail(),
                entity.getStartedAt(),
                entity.getCompletedAt(),
                entity.getErrorMessage(),
                entity.getTotalAmount(),
                entity.getEmployeeCount(),
                List.of()
        );
    }

    PayrollRunDto toDTOWithItems(PayrollRun entity) {
        List<PayrollItemDto> items = payrollItemRepository
                .findAllByPayrollRunIdAndDeletedIsFalse(entity.getId())
                .stream()
                .map(payrollItemService::toDTO)
                .toList();
        return new PayrollRunDto(
                entity.getId(),
                entity.getPeriod(),
                entity.getStatus(),
                entity.getTriggeredByUserId(),
                entity.getTriggeredByEmail(),
                entity.getStartedAt(),
                entity.getCompletedAt(),
                entity.getErrorMessage(),
                entity.getTotalAmount(),
                entity.getEmployeeCount(),
                items
        );
    }

    @Override
    protected PayrollRun toEntity(PayrollRunDto dto) {
        // Runs aren't created via generic CRUD — only via createRunForPeriod.
        PayrollRun run = new PayrollRun();
        run.setId(dto.id());
        return run;
    }

    @Override
    public PayrollRunDto create(PayrollRunDto entity) {
        throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED,
                "Use POST /payroll-runs with TriggerPayrollDto to start a run");
    }

    @Override
    public PayrollRunDto update(UUID id, PayrollRunDto entity) {
        throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED,
                "Payroll runs are not editable in place; use /approve, /close, or DELETE");
    }
}
