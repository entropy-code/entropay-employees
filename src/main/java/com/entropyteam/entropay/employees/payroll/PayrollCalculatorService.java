package com.entropyteam.entropay.employees.payroll;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.entropyteam.entropay.employees.models.Contract;
import com.entropyteam.entropay.employees.models.Currency;
import com.entropyteam.entropay.employees.models.Employee;
import com.entropyteam.entropay.employees.models.Modality;
import com.entropyteam.entropay.employees.models.Overtime;
import com.entropyteam.entropay.employees.models.PaymentSettlement;
import com.entropyteam.entropay.employees.models.Pto;
import com.entropyteam.entropay.employees.models.Reimbursement;
import com.entropyteam.entropay.employees.models.Vacation;

/**
 * Pure calculator for a single employee's payroll. No DB access — all required data lives on the
 * passed-in {@link PayrollContext}, so multiple invocations can run in parallel safely.
 * <p>
 * Any unexpected failure for a single contract is captured in {@link PayrollItem#getCalculationError()}
 * with zeroed totals; the caller never sees an exception, so one bad row cannot abort the whole run.
 */
@Service
class PayrollCalculatorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PayrollCalculatorService.class);
    private static final int HOURS_PER_BUSINESS_DAY = 8;
    private static final int HOURS_PER_HALF_DAY = 4;
    /** Intermediate scale for monthly-to-hourly / monthly-to-daily rate divisions. */
    private static final int RATE_SCALE = 4;
    private static final int MONEY_SCALE = 2;
    /** Fixed monthly-to-daily divisor (~average business days per month). */
    private static final BigDecimal MONTHLY_WORKING_DAYS = new BigDecimal("21.6");
    /** Fixed monthly-to-hourly divisor — used to derive an employee's hourly cost from a monthly salary. */
    private static final BigDecimal MONTHLY_WORKING_HOURS =
            MONTHLY_WORKING_DAYS.multiply(BigDecimal.valueOf(HOURS_PER_BUSINESS_DAY));
    /** {@link com.entropyteam.entropay.employees.models.LeaveType#getName()} value used in prod to flag unpaid leave. */
    static final String UNPAID_LEAVE_TYPE = "Unpaid License";

    public PayrollItem calculate(Contract contract, PayrollContext ctx) {
        PayrollItem item = new PayrollItem();
        item.setContract(contract);
        Employee employee = contract.getEmployee();
        item.setEmployee(employee);
        item.setClientName(ctx.clientNameByEmployeeId().get(employee.getId()));
        item.setPaymentPlatform(ctx.paymentPlatformByEmployeeId().get(employee.getId()));

        try {
            return doCalculate(item, contract, ctx);
        } catch (RuntimeException e) {
            LOGGER.error("Payroll calculation failed for employee {}: {}",
                    employee.getInternalId(), e.getMessage(), e);
            item.setCalculationError(truncate(e.getClass().getSimpleName() + ": " + e.getMessage()));
            return item;
        }
    }

    private PayrollItem doCalculate(PayrollItem item, Contract contract, PayrollContext ctx) {
        Optional<PaymentSettlement> primary = pickPrimarySettlement(contract);
        if (primary.isEmpty()) {
            item.setCalculationError("Contract has no payment settlement");
            return item;
        }
        PaymentSettlement settlement = primary.get();
        Modality modality = settlement.getModality();
        BigDecimal settlementSalary = nz(settlement.getSalary());
        item.setModality(modality);

        Employee employee = contract.getEmployee();
        UUID employeeId = employee.getId();
        UUID countryId = employee.getCountry() != null ? employee.getCountry().getId() : null;
        YearMonth ym = YearMonth.from(ctx.period());
        LocalDate periodStart = ym.atDay(1);
        LocalDate periodEnd = ym.atEndOfMonth();

        int countryWorkingHours = countryId != null
                ? ctx.workingHoursByCountryId().getOrDefault(countryId, 0)
                : 0;
        item.setCountryWorkingHoursInMonth(countryWorkingHours);

        Set<LocalDate> countryHolidays = countryId != null
                ? ctx.holidayDatesByCountryId().getOrDefault(countryId, Collections.emptySet())
                : Collections.emptySet();

        List<Pto> ptos = ctx.ptosByEmployeeId().getOrDefault(employeeId, Collections.emptyList());
        BigDecimal ptoHours = ptos.stream()
                .map(p -> hoursForPtoInMonth(p, periodStart, periodEnd, countryHolidays))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        item.setPtoHoursInMonth(ptoHours);

        // Unpaid leave: paid holidays inside the leave range stay paid, so they're excluded from the deduction.
        BigDecimal unpaidLeaveHours = ptos.stream()
                .filter(PayrollCalculatorService::isUnpaidLeave)
                .map(p -> hoursForPtoInMonth(p, periodStart, periodEnd, countryHolidays))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        item.setUnpaidLeaveHoursInMonth(unpaidLeaveHours);

        BigDecimal billableHours = BigDecimal.valueOf(countryWorkingHours).subtract(ptoHours);
        if (billableHours.signum() < 0) {
            billableHours = BigDecimal.ZERO;
        }
        item.setBillableHoursInMonth(billableHours);

        // Base salary / proportional
        boolean coversFullMonth = !contract.getStartDate().isAfter(periodStart)
                                  && (contract.getEndDate() == null || !contract.getEndDate().isBefore(periodEnd));
        boolean isFinalSettlement = contract.getEndDate() != null
                                    && !contract.getEndDate().isBefore(periodStart)
                                    && !contract.getEndDate().isAfter(periodEnd);
        item.setFinalSettlement(isFinalSettlement);

        if (modality == Modality.MONTHLY) {
            if (coversFullMonth) {
                item.setBaseSalary(settlementSalary);
                item.setProportionalSalary(BigDecimal.ZERO);
            } else {
                LocalDate workStart =
                        contract.getStartDate().isAfter(periodStart) ? contract.getStartDate() : periodStart;
                LocalDate workEnd = contract.getEndDate() != null && contract.getEndDate().isBefore(periodEnd)
                        ? contract.getEndDate() : periodEnd;
                // Holidays are a paid benefit: count weekdays in the worked range without subtracting holidays.
                int workedDays = countBusinessDays(workStart, workEnd, Collections.emptySet());
                BigDecimal dailyRate = computeDailyRate(modality, settlementSalary);
                BigDecimal proportional = dailyRate.multiply(BigDecimal.valueOf(workedDays))
                        .setScale(MONEY_SCALE, RoundingMode.HALF_UP);
                item.setBaseSalary(BigDecimal.ZERO);
                item.setProportionalSalary(proportional);
            }
        } else { // HOUR
            BigDecimal hourBased = settlementSalary.multiply(billableHours).setScale(MONEY_SCALE, RoundingMode.HALF_UP);
            item.setBaseSalary(hourBased);
            item.setProportionalSalary(BigDecimal.ZERO);
        }

        // Unpaid leave deduction: only MONTHLY needs an explicit subtraction — for HOUR the unpaid hours
        // already drop out of billableHours and never enter baseSalary, so there's nothing extra to deduct.
        if (modality == Modality.MONTHLY && unpaidLeaveHours.signum() > 0) {
            BigDecimal hourlyCost = computeHourlyRate(modality, settlementSalary);
            BigDecimal deduction = hourlyCost.multiply(unpaidLeaveHours)
                    .setScale(MONEY_SCALE, RoundingMode.HALF_UP);
            item.setUnpaidLeaveDeduction(deduction);
        }

        // Overtime — uses the employee's hourly cost (salary / 21.6 / 8 for MONTHLY), not the client billable rate.
        BigDecimal hourlyRate = computeHourlyRate(modality, settlementSalary);
        List<Overtime> overtimes = ctx.overtimesByEmployeeId().getOrDefault(employeeId, Collections.emptyList());
        // Route through the float's string representation: BigDecimal.valueOf(float) widens to double
        // first, which introduces binary drift (e.g. 2.1f → 2.0999...). Float.toString gives the
        // shortest decimal that round-trips to the same float, so 2.1f → "2.1" → exactly 2.1.
        BigDecimal overtimeHours = overtimes.stream()
                .map(o -> new BigDecimal(Float.toString(o.getHours())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal overtimeAmount = overtimeHours.multiply(hourlyRate).setScale(MONEY_SCALE, RoundingMode.HALF_UP);
        item.setOvertimeHours(overtimeHours);
        item.setOvertimeAmount(overtimeAmount);

        // Reimbursements — paid in USD like everything else.
        List<Reimbursement> reimbursements =
                ctx.reimbursementsByEmployeeId().getOrDefault(employeeId, Collections.emptyList());
        BigDecimal reimbursementsTotal = reimbursements.stream()
                .map(Reimbursement::getAmount)
                .map(PayrollCalculatorService::nz)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        item.setReimbursementsAmount(reimbursementsTotal);

        // Final settlement: vacation cashout + hardware clawback if early termination.
        if (isFinalSettlement) {
            BigDecimal valuePerDay = computeDailyRate(modality, settlementSalary);
            int unusedDays =
                    unusedVacationDays(ctx.vacationsByEmployeeId().getOrDefault(employeeId, Collections.emptyList()));
            BigDecimal cashout =
                    valuePerDay.multiply(BigDecimal.valueOf(unusedDays)).setScale(MONEY_SCALE, RoundingMode.HALF_UP);
            item.setVacationCashout(cashout);

            LocalDate hireDate = ctx.hireDateByEmployeeId().getOrDefault(employeeId, contract.getStartDate());
            long monthsServed = hireDate != null
                    ? ChronoUnit.MONTHS.between(YearMonth.from(hireDate), ym)
                    : Long.MAX_VALUE;
            if (monthsServed < 12) {
                List<Reimbursement> hardware =
                        ctx.hardwareReimbursementsLast12mByEmployeeId()
                                .getOrDefault(employeeId, Collections.emptyList());
                if (hardware.isEmpty()) {
                    LOGGER.warn(
                            "Final settlement for employee {} (months served={}) but no Hardware reimbursements found"
                            + " in last 12 months — clawback set to 0; review manually.",
                            employee.getInternalId(), monthsServed);
                }
                BigDecimal clawback = hardware.stream()
                        .map(Reimbursement::getAmount)
                        .map(PayrollCalculatorService::nz)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                item.setHardwareClawback(clawback);
            }
        }

        recomputeTotals(item);
        return item;
    }

    /**
     * Recompute the total after adjustments change. Pure — call from {@link PayrollItemService}
     * when the user edits adjustment / previousBalance / notes so the persisted total stays in sync.
     */
    public void recomputeTotals(PayrollItem item) {
        BigDecimal total = nz(item.getBaseSalary())
                .add(nz(item.getProportionalSalary()))
                .add(nz(item.getOvertimeAmount()))
                .add(nz(item.getReimbursementsAmount()))
                .add(nz(item.getAdjustment()))
                .add(nz(item.getPreviousBalance()))
                .add(nz(item.getVacationCashout()))
                .subtract(nz(item.getHardwareClawback()))
                .subtract(nz(item.getUnpaidLeaveDeduction()));
        item.setTotalAmount(total.setScale(MONEY_SCALE, RoundingMode.HALF_UP));
    }

    static Optional<PaymentSettlement> pickPrimarySettlement(Contract contract) {
        if (contract.getPaymentsSettlement() == null || contract.getPaymentsSettlement().isEmpty()) {
            return Optional.empty();
        }
        // Prefer MONTHLY USD (Entropy's typical contract), then any MONTHLY, then any HOUR, then any.
        return contract.getPaymentsSettlement().stream()
                .filter(p -> p.getModality() == Modality.MONTHLY && p.getCurrency() == Currency.USD)
                .findFirst()
                .or(() -> contract.getPaymentsSettlement().stream()
                        .filter(p -> p.getModality() == Modality.MONTHLY)
                        .findFirst())
                .or(() -> contract.getPaymentsSettlement().stream()
                        .filter(p -> p.getModality() == Modality.HOUR)
                        .findFirst())
                .or(() -> contract.getPaymentsSettlement().stream().findFirst());
    }

    static boolean isUnpaidLeave(Pto pto) {
        return pto.getLeaveType() != null
                && UNPAID_LEAVE_TYPE.equalsIgnoreCase(pto.getLeaveType().getName());
    }

    static int countBusinessDays(LocalDate start, LocalDate end, Set<LocalDate> holidays) {
        int days = 0;
        LocalDate cursor = start;
        while (!cursor.isAfter(end)) {
            DayOfWeek dow = cursor.getDayOfWeek();
            if (dow != DayOfWeek.SATURDAY && dow != DayOfWeek.SUNDAY && !holidays.contains(cursor)) {
                days++;
            }
            cursor = cursor.plusDays(1);
        }
        return days;
    }

    static BigDecimal hoursForPtoInMonth(Pto pto, LocalDate periodStart, LocalDate periodEnd,
            Set<LocalDate> countryHolidays) {
        if (pto.getStartDate() == null || pto.getEndDate() == null) {
            return BigDecimal.ZERO;
        }
        LocalDate ptoStart = pto.getStartDate();
        LocalDate ptoEnd = pto.getEndDate();
        LocalDate overlapStart = ptoStart.isAfter(periodStart) ? ptoStart : periodStart;
        LocalDate overlapEnd = ptoEnd.isBefore(periodEnd) ? ptoEnd : periodEnd;
        if (overlapEnd.isBefore(overlapStart)) {
            return BigDecimal.ZERO;
        }
        boolean fullyInMonth = !ptoStart.isBefore(periodStart) && !ptoEnd.isAfter(periodEnd);
        if (fullyInMonth && pto.getLabourHours() != null && pto.getLabourHours() > 0) {
            return BigDecimal.valueOf(pto.getLabourHours());
        }
        int businessDaysOverlap = countBusinessDays(overlapStart, overlapEnd, countryHolidays);
        int hoursPerDay = pto.isHalfDay() ? HOURS_PER_HALF_DAY : HOURS_PER_BUSINESS_DAY;
        return BigDecimal.valueOf((long) hoursPerDay * businessDaysOverlap);
    }

    static BigDecimal computeHourlyRate(Modality modality, BigDecimal salary) {
        if (modality == Modality.HOUR) {
            return salary;
        }
        return salary.divide(MONTHLY_WORKING_HOURS, RATE_SCALE, RoundingMode.HALF_UP);
    }

    static BigDecimal computeDailyRate(Modality modality, BigDecimal salary) {
        if (modality == Modality.HOUR) {
            return salary.multiply(BigDecimal.valueOf(HOURS_PER_BUSINESS_DAY));
        }
        return salary.divide(MONTHLY_WORKING_DAYS, RATE_SCALE, RoundingMode.HALF_UP);
    }

    static int unusedVacationDays(List<Vacation> vacations) {
        // Business rule (per CTO): include ALL accumulated years (no year filter), sum the net
        // (credit - debit) across rows so a year with debit > credit subtracts from positive years,
        // then floor the FINAL net at zero. Never floor per row — that would over-pay.
        int net = vacations.stream()
                .mapToInt(v -> nzInt(v.getCredit()) - nzInt(v.getDebit()))
                .sum();
        return Math.max(net, 0);
    }

    /**
     * Returns the given BigDecimal value if it is not null. If the input value is null, returns BigDecimal.ZERO.
     *
     * @param v the BigDecimal value to check for null
     * @return the original BigDecimal value if not null, otherwise BigDecimal.ZERO
     */
    static BigDecimal nz(BigDecimal v) {
        return v == null ? BigDecimal.ZERO : v;
    }

    /**
     * Returns the given Integer value if it is not null. If the input value is null, returns 0.
     *
     * @param v the Integer value to check for null
     * @return the original Integer value if not null, otherwise 0
     */
    static int nzInt(Integer v) {
        return v == null ? 0 : v;
    }

    static String truncate(String s) {
        if (s == null) {
            return null;
        }
        return s.length() <= 1990 ? s : s.substring(0, 1990) + "...";
    }
}
