package com.entropyteam.entropay.employees.payroll;

import java.util.Optional;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.common.BaseService;
import com.entropyteam.entropay.common.ReactAdminMapper;
import com.entropyteam.entropay.employees.models.Employee;

@Service
class PayrollItemService extends BaseService<PayrollItem, PayrollItemDto, UUID> {

    private final PayrollItemRepository payrollItemRepository;
    private final PayrollCalculatorService calculator;

    public PayrollItemService(PayrollItemRepository payrollItemRepository,
            PayrollCalculatorService calculator,
            ReactAdminMapper reactAdminMapper) {
        super(PayrollItem.class, reactAdminMapper);
        this.payrollItemRepository = payrollItemRepository;
        this.calculator = calculator;
    }

    @Override
    protected BaseRepository<PayrollItem, UUID> getRepository() {
        return payrollItemRepository;
    }

    /**
     * Items can only be edited while the parent run is in DRAFT — once approved/closed they're a
     * historical record.
     */
    @Override
    @Transactional
    public PayrollItemDto update(UUID id, PayrollItemDto dto) {
        PayrollItem item = payrollItemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payroll item not found"));
        if (item.getPayrollRun() == null || item.getPayrollRun().getStatus() != PayrollRunStatus.DRAFT) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Payroll item can only be edited while the run is in DRAFT");
        }
        item.setAdjustment(PayrollCalculatorService.nz(dto.adjustment()));
        item.setPreviousBalance(PayrollCalculatorService.nz(dto.previousBalance()));
        item.setNotes(dto.notes());
        calculator.recomputeTotals(item);
        return toDTO(payrollItemRepository.save(item));
    }

    @Override
    public PayrollItemDto create(PayrollItemDto entity) {
        throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED,
                "Payroll items are created by the run, not by the API");
    }

    @Override
    public PayrollItemDto delete(UUID id) {
        throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED,
                "Delete the payroll run instead of individual items");
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PayrollItemDto> findOne(UUID id) {
        return super.findOne(id);
    }

    @Override
    protected PayrollItemDto toDTO(PayrollItem entity) {
        Employee e = entity.getEmployee();
        return new PayrollItemDto(
                entity.getId(),
                entity.getPayrollRun() != null ? entity.getPayrollRun().getId() : null,
                e != null ? e.getId() : null,
                e != null ? e.getInternalId() : null,
                e != null ? e.getFirstName() : null,
                e != null ? e.getLastName() : null,
                entity.getContract() != null ? entity.getContract().getId() : null,
                entity.getClientName(),
                entity.getPaymentPlatform(),
                entity.getModality() != null ? entity.getModality().name() : null,
                entity.getBaseSalary(),
                entity.getProportionalSalary(),
                entity.getCountryWorkingHoursInMonth(),
                entity.getPtoHoursInMonth(),
                entity.getUnpaidLeaveHoursInMonth(),
                entity.getUnpaidLeaveDeduction(),
                entity.getBillableHoursInMonth(),
                entity.getOvertimeHours(),
                entity.getOvertimeAmount(),
                entity.getReimbursementsAmount(),
                entity.getHardwareClawback(),
                entity.getVacationCashout(),
                entity.getAdjustment(),
                entity.getPreviousBalance(),
                entity.getNotes(),
                entity.getTotalAmount(),
                entity.isFinalSettlement(),
                entity.getCalculationError()
        );
    }

    @Override
    protected PayrollItem toEntity(PayrollItemDto dto) {
        // Items are created by the orchestrator; this method only services the BaseService contract.
        PayrollItem item = new PayrollItem();
        item.setId(dto.id());
        return item;
    }
}
