package com.entropyteam.entropay.employees.payroll;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import com.entropyteam.entropay.employees.models.Contract;
import com.entropyteam.entropay.employees.models.Overtime;
import com.entropyteam.entropay.employees.models.Pto;
import com.entropyteam.entropay.employees.models.Reimbursement;
import com.entropyteam.entropay.employees.models.Vacation;

/**
 * Snapshot of every piece of data needed to compute a payroll for the given month.
 * Built once by {@link PayrollDataLoader} inside a single read-only transaction; the calculator
 * works exclusively against this immutable view, so per-employee calculations never round-trip
 * to the database and can be parallelized safely.
 */
record PayrollContext(
        LocalDate period,
        List<Contract> contracts,
        Map<UUID, List<Overtime>> overtimesByEmployeeId,
        Map<UUID, List<Reimbursement>> reimbursementsByEmployeeId,
        Map<UUID, List<Reimbursement>> hardwareReimbursementsLast12mByEmployeeId,
        Map<UUID, List<Vacation>> vacationsByEmployeeId,
        Map<UUID, List<Pto>> ptosByEmployeeId,
        Map<UUID, Set<LocalDate>> holidayDatesByCountryId,
        Map<UUID, Integer> workingHoursByCountryId,
        Map<UUID, String> clientNameByEmployeeId,
        Map<UUID, String> paymentPlatformByEmployeeId,
        Map<UUID, LocalDate> hireDateByEmployeeId
) {
}
