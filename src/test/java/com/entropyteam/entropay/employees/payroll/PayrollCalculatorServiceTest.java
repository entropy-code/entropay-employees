package com.entropyteam.entropay.employees.payroll;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.entropyteam.entropay.employees.models.Contract;
import com.entropyteam.entropay.employees.models.Country;
import com.entropyteam.entropay.employees.models.Currency;
import com.entropyteam.entropay.employees.models.Employee;
import com.entropyteam.entropay.employees.models.LeaveType;
import com.entropyteam.entropay.employees.models.Modality;
import com.entropyteam.entropay.employees.models.Overtime;
import com.entropyteam.entropay.employees.models.PaymentSettlement;
import com.entropyteam.entropay.employees.models.Pto;
import com.entropyteam.entropay.employees.models.Reimbursement;
import com.entropyteam.entropay.employees.models.ReimbursementCategory;
import com.entropyteam.entropay.employees.models.Status;
import com.entropyteam.entropay.employees.models.Vacation;

class PayrollCalculatorServiceTest {

    private static final LocalDate PERIOD = LocalDate.of(2026, 4, 1);
    private static final int APRIL_2026_BUSINESS_DAYS_NO_HOLIDAYS = 22;
    private static final int APRIL_2026_BUSINESS_HOURS_NO_HOLIDAYS = APRIL_2026_BUSINESS_DAYS_NO_HOLIDAYS * 8;

    private PayrollCalculatorService calculator;
    private Country argentina;
    private UUID argentinaId;

    @BeforeEach
    void setUp() {
        calculator = new PayrollCalculatorService();
        argentina = new Country();
        argentinaId = UUID.randomUUID();
        argentina.setId(argentinaId);
        argentina.setName("Argentina");
    }

    // -- Sueldo base / proporcional MONTHLY ----------------------------------------------------

    @Test
    @DisplayName("MONTHLY contract covering the full month → baseSalary equals contract salary, no proportional")
    void monthlyFullMonth() {
        Contract contract = monthlyContract("4250.00", LocalDate.of(2024, 1, 1), null);
        var ctx = ctxFor(contract);

        PayrollItem item = calculator.calculate(contract, ctx.build());

        assertThat(item.getCalculationError()).isNull();
        assertThat(item.getBaseSalary()).isEqualByComparingTo("4250.00");
        assertThat(item.getProportionalSalary()).isEqualByComparingTo("0");
        assertThat(item.isFinalSettlement()).isFalse();
    }

    @Test
    @DisplayName("MONTHLY contract starting mid-month → proportional = (salary / 21.6) * workedBusinessDays")
    void monthlyStartingMidMonth() {
        // Starts Apr 15 (Wednesday) → worked days = 12 business days (Apr 15-30 excluding weekends).
        Contract contract = monthlyContract("4400.00", LocalDate.of(2026, 4, 15), null);
        var ctx = ctxFor(contract);

        PayrollItem item = calculator.calculate(contract, ctx.build());

        // Daily rate = 4400 / 21.6 = 203.7037; * 12 = 2444.4444 → 2444.44
        assertThat(item.getBaseSalary()).isEqualByComparingTo("0");
        assertThat(item.getProportionalSalary()).isEqualByComparingTo("2444.44");
        assertThat(item.isFinalSettlement()).isFalse();
    }

    @Test
    @DisplayName("MONTHLY proportional with a holiday inside the worked range → holiday is paid, total unchanged")
    void monthlyProportionalHolidayIsPaid() {
        // Starts Apr 15 → 12 weekdays Apr 15-30. Apr 20 is a holiday but should still be paid.
        Contract contract = monthlyContract("4400.00", LocalDate.of(2026, 4, 15), null);
        var ctx = ctxFor(contract).withHoliday(argentinaId, LocalDate.of(2026, 4, 20));

        PayrollItem item = calculator.calculate(contract, ctx.build());

        // Same as no-holiday case: 12 weekdays * (4400/21.6) = 2444.44.
        assertThat(item.getProportionalSalary()).isEqualByComparingTo("2444.44");
    }

    @Test
    @DisplayName("MONTHLY contract ending mid-month → proportional + isFinalSettlement true")
    void monthlyEndingMidMonth() {
        // Ends Apr 10 (Friday) → worked days = 8 business days (Apr 1-10 mon-fri).
        Contract contract = monthlyContract("4400.00", LocalDate.of(2024, 1, 1), LocalDate.of(2026, 4, 10));
        var ctx = ctxFor(contract);

        PayrollItem item = calculator.calculate(contract, ctx.build());

        // Daily rate = 4400 / 21.6 = 203.7037; * 8 = 1629.6296 → 1629.63
        assertThat(item.getProportionalSalary()).isEqualByComparingTo("1629.63");
        assertThat(item.isFinalSettlement()).isTrue();
    }

    @Test
    @DisplayName("MONTHLY contract starting and ending in the same month → proportional covers only the worked range")
    void monthlyStartingAndEndingSameMonth() {
        // Apr 7 (Tue) to Apr 18 (Sat) → business days Apr 7-17 (Mon-Fri) = 9 days.
        Contract contract = monthlyContract("4400.00", LocalDate.of(2026, 4, 7), LocalDate.of(2026, 4, 18));
        var ctx = ctxFor(contract);

        PayrollItem item = calculator.calculate(contract, ctx.build());

        // Daily rate = 4400 / 21.6 = 203.7037; * 9 = 1833.3333 → 1833.33
        assertThat(item.getProportionalSalary()).isEqualByComparingTo("1833.33");
        assertThat(item.isFinalSettlement()).isTrue();
    }

    // -- Horas billables HOUR ------------------------------------------------------------------

    @Test
    @DisplayName("HOUR contract full month in AR with no PTO → billable hours = country working hours")
    void hourlyFullMonthNoPto() {
        Contract contract = hourlyContract("30.00", LocalDate.of(2024, 1, 1), null);
        var ctx = ctxFor(contract);

        PayrollItem item = calculator.calculate(contract, ctx.build());

        assertThat(item.getBillableHoursInMonth()).isEqualByComparingTo(BigDecimal.valueOf(APRIL_2026_BUSINESS_HOURS_NO_HOLIDAYS));
        assertThat(item.getBaseSalary()).isEqualByComparingTo(new BigDecimal("30").multiply(BigDecimal.valueOf(APRIL_2026_BUSINESS_HOURS_NO_HOLIDAYS)));
    }

    @Test
    @DisplayName("HOUR contract with one full PTO day → billable hours reduced by 8")
    void hourlyWithOneFullDayPto() {
        Contract contract = hourlyContract("30.00", LocalDate.of(2024, 1, 1), null);
        Pto pto = pto(LocalDate.of(2026, 4, 8), LocalDate.of(2026, 4, 8), 1.0, null);
        var ctx = ctxFor(contract).withPtos(contract.getEmployee().getId(), List.of(pto));

        PayrollItem item = calculator.calculate(contract, ctx.build());

        assertThat(item.getPtoHoursInMonth()).isEqualByComparingTo("8");
        assertThat(item.getBillableHoursInMonth())
                .isEqualByComparingTo(BigDecimal.valueOf(APRIL_2026_BUSINESS_HOURS_NO_HOLIDAYS - 8));
    }

    @Test
    @DisplayName("HOUR contract with half-day PTO → billable hours reduced by 4")
    void hourlyWithHalfDayPto() {
        Contract contract = hourlyContract("30.00", LocalDate.of(2024, 1, 1), null);
        Pto pto = pto(LocalDate.of(2026, 4, 8), LocalDate.of(2026, 4, 8), 0.5, null);
        var ctx = ctxFor(contract).withPtos(contract.getEmployee().getId(), List.of(pto));

        PayrollItem item = calculator.calculate(contract, ctx.build());

        assertThat(item.getPtoHoursInMonth()).isEqualByComparingTo("4");
        assertThat(item.getBillableHoursInMonth())
                .isEqualByComparingTo(BigDecimal.valueOf(APRIL_2026_BUSINESS_HOURS_NO_HOLIDAYS - 4));
    }

    @Test
    @DisplayName("PTO with labourHours set and fully in month → labourHours preferred over days*8")
    void hourlyWithPtoLabourHoursPreferred() {
        Contract contract = hourlyContract("30.00", LocalDate.of(2024, 1, 1), null);
        // 2 days but labourHours says 14 (e.g. 7-hour days). Should use 14, not 16.
        Pto pto = pto(LocalDate.of(2026, 4, 8), LocalDate.of(2026, 4, 9), 2.0, 14);
        var ctx = ctxFor(contract).withPtos(contract.getEmployee().getId(), List.of(pto));

        PayrollItem item = calculator.calculate(contract, ctx.build());

        assertThat(item.getPtoHoursInMonth()).isEqualByComparingTo("14");
    }

    @Test
    @DisplayName("PTO spanning month boundary → only hours within the target month are counted")
    void hourlyWithPtoSpanningMonthBoundary() {
        Contract contract = hourlyContract("30.00", LocalDate.of(2024, 1, 1), null);
        // PTO Mar 30 (Mon) - Apr 3 (Fri) = 5 business days, only Apr 1 (Wed), 2 (Thu), 3 (Fri) fall in target month → 3*8=24.
        // labourHours unset so we fall through to business-day arithmetic.
        Pto pto = pto(LocalDate.of(2026, 3, 30), LocalDate.of(2026, 4, 3), 5.0, null);
        var ctx = ctxFor(contract).withPtos(contract.getEmployee().getId(), List.of(pto));

        PayrollItem item = calculator.calculate(contract, ctx.build());

        assertThat(item.getPtoHoursInMonth()).isEqualByComparingTo("24");
    }

    @Test
    @DisplayName("HOUR contract in country with mid-month holiday → billable hours exclude the holiday")
    void hourlyWithMidMonthHoliday() {
        Contract contract = hourlyContract("30.00", LocalDate.of(2024, 1, 1), null);
        // Apr 8 (Wed) is a holiday for AR.
        var ctx = ctxFor(contract).withHoliday(argentinaId, LocalDate.of(2026, 4, 8));

        PayrollItem item = calculator.calculate(contract, ctx.build());

        assertThat(item.getCountryWorkingHoursInMonth())
                .isEqualTo(APRIL_2026_BUSINESS_HOURS_NO_HOLIDAYS - 8);
    }

    @Test
    @DisplayName("PTO covering the entire month → billable hours floored at zero (never negative)")
    void hourlyWithPtoCoveringWholeMonth() {
        Contract contract = hourlyContract("30.00", LocalDate.of(2024, 1, 1), null);
        Pto pto = pto(LocalDate.of(2026, 4, 1), LocalDate.of(2026, 4, 30), 30.0, null);
        var ctx = ctxFor(contract).withPtos(contract.getEmployee().getId(), List.of(pto));

        PayrollItem item = calculator.calculate(contract, ctx.build());

        assertThat(item.getBillableHoursInMonth()).isEqualByComparingTo("0");
        assertThat(item.getBaseSalary()).isEqualByComparingTo("0.00");
    }

    // -- Horas extras --------------------------------------------------------------------------

    @Test
    @DisplayName("MONTHLY contract with overtime → overtime amount = hours * salary / (21.6 * 8)")
    void monthlyOvertimeUsesDerivedRate() {
        Contract contract = monthlyContract("4400.00", LocalDate.of(2024, 1, 1), null);
        Overtime ot = overtime(contract.getEmployee(), 5f, LocalDate.of(2026, 4, 10));
        var ctx = ctxFor(contract).withOvertimes(contract.getEmployee().getId(), List.of(ot));

        PayrollItem item = calculator.calculate(contract, ctx.build());

        // 4400 / 172.8 = 25.4630 per hour; 5h * 25.4630 = 127.3150 → 127.32
        assertThat(item.getOvertimeHours()).isEqualByComparingTo("5");
        assertThat(item.getOvertimeAmount()).isEqualByComparingTo("127.32");
    }

    @Test
    @DisplayName("HOUR contract with overtime → overtime amount uses contract hourly rate")
    void hourlyOvertimeUsesHourlyRate() {
        Contract contract = hourlyContract("30.00", LocalDate.of(2024, 1, 1), null);
        Overtime ot = overtime(contract.getEmployee(), 3f, LocalDate.of(2026, 4, 10));
        var ctx = ctxFor(contract).withOvertimes(contract.getEmployee().getId(), List.of(ot));

        PayrollItem item = calculator.calculate(contract, ctx.build());

        assertThat(item.getOvertimeHours()).isEqualByComparingTo("3");
        assertThat(item.getOvertimeAmount()).isEqualByComparingTo("90.00");
    }

    @Test
    @DisplayName("Overtime hours from float 2.1f → BigDecimal 2.1 exactly, no binary drift")
    void overtimeHoursFromFloatHasNoBinaryDrift() {
        // 2.1f cannot be represented exactly in IEEE-754 binary. BigDecimal.valueOf((double) 2.1f)
        // widens to ~2.0999999046... and propagates the drift into overtimeHours. Routing through
        // Float.toString(2.1f) = "2.1" yields exactly 2.1. This test pins the fix.
        Contract contract = monthlyContract("4400.00", LocalDate.of(2024, 1, 1), null);
        Overtime ot = overtime(contract.getEmployee(), 2.1f, LocalDate.of(2026, 4, 10));
        var ctx = ctxFor(contract).withOvertimes(contract.getEmployee().getId(), List.of(ot));

        PayrollItem item = calculator.calculate(contract, ctx.build());

        assertThat(item.getOvertimeHours()).isEqualByComparingTo("2.1");
    }

    @Test
    @DisplayName("No overtime records → overtime fields are zero")
    void noOvertime() {
        Contract contract = monthlyContract("4400.00", LocalDate.of(2024, 1, 1), null);
        var ctx = ctxFor(contract);

        PayrollItem item = calculator.calculate(contract, ctx.build());

        assertThat(item.getOvertimeHours()).isEqualByComparingTo("0");
        assertThat(item.getOvertimeAmount()).isEqualByComparingTo("0.00");
    }

    // -- Reintegros ----------------------------------------------------------------------------

    @Test
    @DisplayName("Reimbursements → summed into reimbursementsAmount")
    void reimbursementsSummed() {
        Contract contract = monthlyContract("4400.00", LocalDate.of(2024, 1, 1), null);
        Reimbursement r = reimbursement(contract.getEmployee(), "Gym", new BigDecimal("50"));
        var ctx = ctxFor(contract).withReimbursements(contract.getEmployee().getId(), List.of(r));

        PayrollItem item = calculator.calculate(contract, ctx.build());

        assertThat(item.getReimbursementsAmount()).isEqualByComparingTo("50");
    }

    // -- Licencia sin goce ---------------------------------------------------------------------

    @Test
    @DisplayName("MONTHLY full month + 8 business days unpaid leave → deduction = (salary/21.6) * 8, total = salary - deduction")
    void monthlyWithUnpaidLeaveDeducts() {
        // Mirrors the prod example: salary 5000, 8 weekdays sin goce → deduction 1851.85, total 3148.15.
        Contract contract = monthlyContract("5000.00", LocalDate.of(2024, 1, 1), null);
        // Apr 21 (Tue) - Apr 30 (Thu) = 8 weekdays.
        Pto leave = unpaidLeave(LocalDate.of(2026, 4, 21), LocalDate.of(2026, 4, 30), 8.0);
        var ctx = ctxFor(contract).withPtos(contract.getEmployee().getId(), List.of(leave));

        PayrollItem item = calculator.calculate(contract, ctx.build());

        assertThat(item.getBaseSalary()).isEqualByComparingTo("5000.00");
        assertThat(item.getUnpaidLeaveHoursInMonth()).isEqualByComparingTo("64"); // 8 days * 8h
        // (5000/172.8) * 64 = 28.9352 * 64 = 1851.8528 → 1851.85
        assertThat(item.getUnpaidLeaveDeduction()).isEqualByComparingTo("1851.85");
        assertThat(item.getTotalAmount()).isEqualByComparingTo("3148.15");
    }

    @Test
    @DisplayName("Unpaid leave overlapping a holiday → holiday is paid, deduction skips it")
    void unpaidLeaveExcludesHolidaysFromDeduction() {
        // Apr 21-30: 8 weekdays. With holiday Apr 22 → only 7 unpaid business days.
        Contract contract = monthlyContract("5000.00", LocalDate.of(2024, 1, 1), null);
        Pto leave = unpaidLeave(LocalDate.of(2026, 4, 21), LocalDate.of(2026, 4, 30), 8.0);
        var ctx = ctxFor(contract)
                .withPtos(contract.getEmployee().getId(), List.of(leave))
                .withHoliday(argentinaId, LocalDate.of(2026, 4, 22));

        PayrollItem item = calculator.calculate(contract, ctx.build());

        assertThat(item.getUnpaidLeaveHoursInMonth()).isEqualByComparingTo("56"); // 7 * 8
        // (5000/172.8) * 56 = 28.9352 * 56 = 1620.3712 → 1620.37
        assertThat(item.getUnpaidLeaveDeduction()).isEqualByComparingTo("1620.37");
        assertThat(item.getTotalAmount()).isEqualByComparingTo("3379.63");
    }

    @Test
    @DisplayName("Regular paid PTO (no LeaveType.name = Unpaid License) → no salary deduction")
    void regularPtoDoesNotDeductSalary() {
        Contract contract = monthlyContract("5000.00", LocalDate.of(2024, 1, 1), null);
        Pto regular = pto(LocalDate.of(2026, 4, 21), LocalDate.of(2026, 4, 30), 8.0, null); // no leaveType
        var ctx = ctxFor(contract).withPtos(contract.getEmployee().getId(), List.of(regular));

        PayrollItem item = calculator.calculate(contract, ctx.build());

        assertThat(item.getUnpaidLeaveHoursInMonth()).isEqualByComparingTo("0");
        assertThat(item.getUnpaidLeaveDeduction()).isEqualByComparingTo("0");
        assertThat(item.getTotalAmount()).isEqualByComparingTo("5000.00");
    }

    @Test
    @DisplayName("HOUR contract with unpaid leave → no explicit deduction (unpaid hours already drop billableHours)")
    void hourlyWithUnpaidLeaveDoesNotDoubleDeduct() {
        Contract contract = hourlyContract("30.00", LocalDate.of(2024, 1, 1), null);
        Pto leave = unpaidLeave(LocalDate.of(2026, 4, 21), LocalDate.of(2026, 4, 30), 8.0);
        var ctx = ctxFor(contract).withPtos(contract.getEmployee().getId(), List.of(leave));

        PayrollItem item = calculator.calculate(contract, ctx.build());

        assertThat(item.getUnpaidLeaveHoursInMonth()).isEqualByComparingTo("64");
        assertThat(item.getUnpaidLeaveDeduction()).isEqualByComparingTo("0");
        // billableHours = 176 - 64 = 112; baseSalary = 30 * 112 = 3360
        assertThat(item.getBillableHoursInMonth()).isEqualByComparingTo("112");
        assertThat(item.getBaseSalary()).isEqualByComparingTo("3360.00");
        assertThat(item.getTotalAmount()).isEqualByComparingTo("3360.00");
    }

    // -- Liquidación final ---------------------------------------------------------------------

    @Test
    @DisplayName("Final settlement with unused vacation days → vacationCashout = days * dailyRate")
    void finalSettlementUnusedVacation() {
        // contract starts 2 years before period; ends Apr 30. 5 unused days. Daily rate = 4400/21.6 = 203.7037.
        Contract contract = monthlyContract("4400.00", LocalDate.of(2024, 1, 1), LocalDate.of(2026, 4, 30));
        Vacation v = vacation(2026, 10, 5);
        var ctx = ctxFor(contract).withVacations(contract.getEmployee().getId(), List.of(v));

        PayrollItem item = calculator.calculate(contract, ctx.build());

        assertThat(item.isFinalSettlement()).isTrue();
        // 5 unused * 203.7037/day = 1018.5185 → 1018.52
        assertThat(item.getVacationCashout()).isEqualByComparingTo("1018.52");
    }

    @Test
    @DisplayName("Final settlement with <12 months service → hardware clawback equals last-12-months Hardware reimbursements sum")
    void finalSettlementHardwareClawbackUnder12Months() {
        Contract contract = monthlyContract("4400.00", LocalDate.of(2025, 11, 1), LocalDate.of(2026, 4, 30));
        Reimbursement hw1 = reimbursement(contract.getEmployee(), "Bono de Hardware", new BigDecimal("1500"));
        Reimbursement hw2 = reimbursement(contract.getEmployee(), "Bono de Hardware", new BigDecimal("500"));
        var ctx = ctxFor(contract)
                .withHardwareReimbursementsLast12m(contract.getEmployee().getId(), List.of(hw1, hw2));

        PayrollItem item = calculator.calculate(contract, ctx.build());

        assertThat(item.getHardwareClawback()).isEqualByComparingTo("2000");
    }

    @Test
    @DisplayName("Final settlement with ≥12 months service → no hardware clawback")
    void finalSettlementNoClawbackOver12Months() {
        Contract contract = monthlyContract("4400.00", LocalDate.of(2024, 1, 1), LocalDate.of(2026, 4, 30));
        Reimbursement hw = reimbursement(contract.getEmployee(), "Bono de Hardware", new BigDecimal("1500"));
        var ctx = ctxFor(contract)
                .withHardwareReimbursementsLast12m(contract.getEmployee().getId(), List.of(hw));

        PayrollItem item = calculator.calculate(contract, ctx.build());

        assertThat(item.getHardwareClawback()).isEqualByComparingTo("0");
    }

    @Test
    @DisplayName("Final settlement with no Hardware reimbursements seen → clawback is zero (warning logged)")
    void finalSettlementHardwareClawbackZeroWhenAbsent() {
        Contract contract = monthlyContract("4400.00", LocalDate.of(2025, 11, 1), LocalDate.of(2026, 4, 30));
        var ctx = ctxFor(contract);

        PayrollItem item = calculator.calculate(contract, ctx.build());

        assertThat(item.getHardwareClawback()).isEqualByComparingTo("0");
    }

    @Test
    @DisplayName("Final settlement with multi-year vacations → negative-year debt compensates positive-year credit")
    void finalSettlementUnusedVacationCompensatesAcrossYears() {
        // Business rule per CTO: include ALL years, sum net across rows, floor only the final net.
        // 2025: credit 10, debit 4 → +6. 2026: credit 2, debit 5 → -3. Net = +3 unused days.
        Contract contract = monthlyContract("4400.00", LocalDate.of(2024, 1, 1), LocalDate.of(2026, 4, 30));
        Vacation prior = vacation(2025, 10, 4);
        Vacation current = vacation(2026, 2, 5);
        var ctx = ctxFor(contract).withVacations(contract.getEmployee().getId(), List.of(prior, current));

        PayrollItem item = calculator.calculate(contract, ctx.build());

        // 3 unused days * (4400/21.6) = 3 * 203.7037 = 611.1111 → 611.11
        assertThat(item.getVacationCashout()).isEqualByComparingTo("611.11");
    }

    @Test
    @DisplayName("Final settlement with net-negative vacation balance → vacationCashout is 0, never negative")
    void finalSettlementUnusedVacationNetNegativeFlooredAtZero() {
        // 2025: -2 (credit 1 debit 3). 2026: -4 (credit 1 debit 5). Net = -6 → cashout 0, not negative.
        Contract contract = monthlyContract("4400.00", LocalDate.of(2024, 1, 1), LocalDate.of(2026, 4, 30));
        Vacation prior = vacation(2025, 1, 3);
        Vacation current = vacation(2026, 1, 5);
        var ctx = ctxFor(contract).withVacations(contract.getEmployee().getId(), List.of(prior, current));

        PayrollItem item = calculator.calculate(contract, ctx.build());

        assertThat(item.getVacationCashout()).isEqualByComparingTo("0");
    }

    @Test
    @DisplayName("Contract active but not ending → isFinalSettlement false, cashout zero")
    void contractActiveNotEnding() {
        Contract contract = monthlyContract("4400.00", LocalDate.of(2024, 1, 1), null);
        Vacation v = vacation(2026, 10, 5);
        var ctx = ctxFor(contract).withVacations(contract.getEmployee().getId(), List.of(v));

        PayrollItem item = calculator.calculate(contract, ctx.build());

        assertThat(item.isFinalSettlement()).isFalse();
        assertThat(item.getVacationCashout()).isEqualByComparingTo("0");
    }

    // -- Aislamiento de fallos ------------------------------------------------------------------

    @Test
    @DisplayName("Contract with no PaymentSettlement → calculationError set, totals zero, no exception")
    void contractWithNoPaymentSettlement() {
        Employee emp = employee("E099");
        Contract contract = new Contract();
        contract.setId(UUID.randomUUID());
        contract.setEmployee(emp);
        contract.setStartDate(LocalDate.of(2024, 1, 1));
        // No payment settlements at all.
        var ctx = ctxFor(contract);

        PayrollItem item = calculator.calculate(contract, ctx.build());

        assertThat(item.getCalculationError()).contains("payment settlement");
        assertThat(item.getTotalAmount()).isEqualByComparingTo("0");
    }

    // -- Total -----------------------------------------------------------------------------------

    @Test
    @DisplayName("All components positive → totalAmount sums them correctly")
    void totalAmountSumsAllComponents() {
        Contract contract = monthlyContract("4400.00", LocalDate.of(2024, 1, 1), null);
        Overtime ot = overtime(contract.getEmployee(), 4f, LocalDate.of(2026, 4, 10));
        Reimbursement r = reimbursement(contract.getEmployee(), "Internet", new BigDecimal("50"));
        var ctx = ctxFor(contract)
                .withOvertimes(contract.getEmployee().getId(), List.of(ot))
                .withReimbursements(contract.getEmployee().getId(), List.of(r));

        PayrollItem item = calculator.calculate(contract, ctx.build());

        // base 4400 + ot 4 * (4400/172.8) = 4 * 25.4630 = 101.85 + reimbursements 50 = 4551.85
        assertThat(item.getTotalAmount()).isEqualByComparingTo("4551.85");
    }

    @Test
    @DisplayName("recomputeTotals includes adjustment and previousBalance")
    void totalsIncludeAdjustmentAndPreviousBalance() {
        PayrollItem item = new PayrollItem();
        item.setBaseSalary(new BigDecimal("4400"));
        item.setAdjustment(new BigDecimal("100"));
        item.setPreviousBalance(new BigDecimal("-30"));

        calculator.recomputeTotals(item);

        assertThat(item.getTotalAmount()).isEqualByComparingTo("4470.00");
    }

    // -- Helpers ----------------------------------------------------------------------------------

    private Employee employee(String internalId) {
        Employee e = new Employee();
        e.setId(UUID.randomUUID());
        e.setInternalId(internalId);
        e.setFirstName("Test");
        e.setLastName(internalId);
        e.setActive(true);
        e.setCountry(argentina);
        return e;
    }

    private Contract monthlyContract(String salary, LocalDate start, LocalDate end) {
        return contract(employee("E" + (System.nanoTime() % 1000)),
                start, end,
                paymentSettlement(new BigDecimal(salary), Modality.MONTHLY, Currency.USD));
    }

    private Contract hourlyContract(String hourlyRate, LocalDate start, LocalDate end) {
        return contract(employee("E" + (System.nanoTime() % 1000)),
                start, end,
                paymentSettlement(new BigDecimal(hourlyRate), Modality.HOUR, Currency.USD));
    }

    private Contract contract(Employee employee, LocalDate start, LocalDate end, PaymentSettlement... settlements) {
        Contract c = new Contract();
        c.setId(UUID.randomUUID());
        c.setEmployee(employee);
        c.setStartDate(start);
        c.setEndDate(end);
        c.setActive(true);
        Set<PaymentSettlement> set = new HashSet<>();
        for (PaymentSettlement ps : settlements) {
            ps.setContract(c);
            set.add(ps);
        }
        c.setPaymentsSettlement(set);
        return c;
    }

    private PaymentSettlement paymentSettlement(BigDecimal salary, Modality modality, Currency currency) {
        PaymentSettlement ps = new PaymentSettlement();
        ps.setId(UUID.randomUUID());
        ps.setSalary(salary);
        ps.setModality(modality);
        ps.setCurrency(currency);
        return ps;
    }

    private Pto pto(LocalDate start, LocalDate end, double days, Integer labourHours) {
        Pto p = new Pto();
        p.setId(UUID.randomUUID());
        p.setStartDate(start);
        p.setEndDate(end);
        p.setDays(days);
        p.setLabourHours(labourHours);
        p.setStatus(Status.APPROVED);
        return p;
    }

    private Pto unpaidLeave(LocalDate start, LocalDate end, double days) {
        Pto p = pto(start, end, days, null);
        LeaveType lt = new LeaveType();
        lt.setId(UUID.randomUUID());
        lt.setName(PayrollCalculatorService.UNPAID_LEAVE_TYPE);
        p.setLeaveType(lt);
        return p;
    }

    private Overtime overtime(Employee employee, float hours, LocalDate date) {
        Overtime o = new Overtime();
        o.setId(UUID.randomUUID());
        o.setEmployee(employee);
        o.setHours(hours);
        o.setDate(date);
        return o;
    }

    private Reimbursement reimbursement(Employee employee, String categoryName, BigDecimal amount) {
        ReimbursementCategory cat = new ReimbursementCategory();
        cat.setId(UUID.randomUUID());
        cat.setName(categoryName);
        Reimbursement r = new Reimbursement();
        r.setId(UUID.randomUUID());
        r.setEmployee(employee);
        r.setCategory(cat);
        r.setAmount(amount);
        r.setDate(LocalDate.of(2026, 4, 5));
        return r;
    }

    private Vacation vacation(int year, int credit, int debit) {
        Vacation v = new Vacation();
        v.setId(UUID.randomUUID());
        v.setYear(String.valueOf(year));
        v.setCredit(credit);
        v.setDebit(debit);
        return v;
    }

    private CtxBuilder ctxFor(Contract contract) {
        return new CtxBuilder(PERIOD, List.of(contract), argentinaId,
                APRIL_2026_BUSINESS_HOURS_NO_HOLIDAYS);
    }

    /**
     * Builder that wraps the mutable maps the calculator reads from. Records can't be subclassed,
     * so we accumulate state here and pass through to {@link PayrollCalculatorService} by being
     * implicitly converted via {@link #build()} — but since the calculator takes a PayrollContext
     * directly, we just expose all the methods Calculator might call.
     */
    private static class CtxBuilder {
        private final LocalDate period;
        private final List<Contract> contracts;
        private final Map<UUID, List<Overtime>> overtimes = new HashMap<>();
        private final Map<UUID, List<Reimbursement>> reimbursements = new HashMap<>();
        private final Map<UUID, List<Reimbursement>> hardware = new HashMap<>();
        private final Map<UUID, List<Vacation>> vacations = new HashMap<>();
        private final Map<UUID, List<Pto>> ptos = new HashMap<>();
        private final Map<UUID, Set<LocalDate>> holidayDates = new HashMap<>();
        private final Map<UUID, Integer> workingHours = new HashMap<>();
        private final Map<UUID, String> clientNames = new HashMap<>();
        private final Map<UUID, String> platforms = new HashMap<>();
        private final Map<UUID, LocalDate> hireDates = new HashMap<>();

        CtxBuilder(LocalDate period, List<Contract> contracts,
                UUID defaultCountryId, int defaultWorkingHoursInMonth) {
            this.period = period;
            this.contracts = contracts;
            this.holidayDates.put(defaultCountryId, new HashSet<>());
            this.workingHours.put(defaultCountryId, defaultWorkingHoursInMonth);
        }

        CtxBuilder withPtos(UUID employeeId, List<Pto> v) { ptos.put(employeeId, v); return this; }
        CtxBuilder withOvertimes(UUID employeeId, List<Overtime> v) { overtimes.put(employeeId, v); return this; }
        CtxBuilder withReimbursements(UUID employeeId, List<Reimbursement> v) { reimbursements.put(employeeId, v); return this; }
        CtxBuilder withHardwareReimbursementsLast12m(UUID employeeId, List<Reimbursement> v) {
            hardware.put(employeeId, v); return this;
        }
        CtxBuilder withVacations(UUID employeeId, List<Vacation> v) { vacations.put(employeeId, v); return this; }
        CtxBuilder withHireDate(UUID employeeId, LocalDate date) { hireDates.put(employeeId, date); return this; }

        CtxBuilder withHoliday(UUID countryId, LocalDate date) {
            holidayDates.computeIfAbsent(countryId, k -> new HashSet<>()).add(date);
            Set<LocalDate> holidays = holidayDates.get(countryId);
            workingHours.put(countryId,
                    PayrollDataLoader.computeWorkingHours(period.withDayOfMonth(1),
                            period.withDayOfMonth(period.lengthOfMonth()), holidays));
            return this;
        }

        PayrollContext build() {
            return new PayrollContext(period, contracts, overtimes, reimbursements,
                    hardware, vacations, ptos, holidayDates, workingHours, clientNames, platforms, hireDates);
        }
    }

    @SuppressWarnings("unused")
    private static Set<LocalDate> emptyHolidays() {
        return Collections.emptySet();
    }
}
