package com.entropyteam.entropay.employees.payroll;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.entropyteam.entropay.employees.models.Assignment;
import com.entropyteam.entropay.employees.models.Contract;
import com.entropyteam.entropay.employees.models.Country;
import com.entropyteam.entropay.employees.models.Employee;
import com.entropyteam.entropay.employees.models.Holiday;
import com.entropyteam.entropay.employees.models.Overtime;
import com.entropyteam.entropay.employees.models.PaymentInformation;
import com.entropyteam.entropay.employees.models.Pto;
import com.entropyteam.entropay.employees.models.Reimbursement;
import com.entropyteam.entropay.employees.models.Vacation;
import com.entropyteam.entropay.employees.repositories.ContractRepository;
import com.entropyteam.entropay.employees.repositories.HolidayRepository;
import com.entropyteam.entropay.employees.repositories.OvertimeRepository;
import com.entropyteam.entropay.employees.repositories.PtoRepository;
import com.entropyteam.entropay.employees.repositories.ReimbursementRepository;
import com.entropyteam.entropay.employees.repositories.VacationRepository;

@Service
class PayrollDataLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(PayrollDataLoader.class);
    private static final int HOURS_PER_BUSINESS_DAY = 8;
    private static final String HARDWARE_CATEGORY_NAME = "Bono de Hardware";

    private final ContractRepository contractRepository;
    private final OvertimeRepository overtimeRepository;
    private final ReimbursementRepository reimbursementRepository;
    private final VacationRepository vacationRepository;
    private final PtoRepository ptoRepository;
    private final HolidayRepository holidayRepository;

    public PayrollDataLoader(ContractRepository contractRepository,
            OvertimeRepository overtimeRepository,
            ReimbursementRepository reimbursementRepository,
            VacationRepository vacationRepository,
            PtoRepository ptoRepository,
            HolidayRepository holidayRepository) {
        this.contractRepository = contractRepository;
        this.overtimeRepository = overtimeRepository;
        this.reimbursementRepository = reimbursementRepository;
        this.vacationRepository = vacationRepository;
        this.ptoRepository = ptoRepository;
        this.holidayRepository = holidayRepository;
    }

    @Transactional(readOnly = true)
    public PayrollContext loadContext(LocalDate period) {
        YearMonth ym = YearMonth.from(period);
        LocalDate periodStart = ym.atDay(1);
        LocalDate periodEnd = ym.atEndOfMonth();
        LocalDate twelveMonthsBack = periodEnd.minusMonths(12).plusDays(1);

        List<Contract> contracts = contractRepository.findAllOverlappingPeriodForPayroll(periodStart, periodEnd);
        LOGGER.info("Loaded {} contracts for payroll period {}", contracts.size(), ym);

        Set<UUID> employeeIds = contracts.stream()
                .map(c -> c.getEmployee().getId())
                .collect(Collectors.toSet());
        Set<UUID> countryIds = contracts.stream()
                .map(c -> c.getEmployee().getCountry())
                .filter(java.util.Objects::nonNull)
                .map(Country::getId)
                .collect(Collectors.toSet());

        Map<UUID, List<Overtime>> overtimesByEmployee =
                overtimeRepository.findByDateBetweenAndDeletedIsFalse(periodStart, periodEnd).stream()
                .filter(o -> o.getEmployee() != null)
                .collect(Collectors.groupingBy(o -> o.getEmployee().getId()));

        Map<UUID, List<Reimbursement>> reimbursementsByEmployee =
                reimbursementRepository.findAllBetweenPeriod(periodStart, periodEnd).stream()
                .filter(r -> r.getEmployee() != null)
                .collect(Collectors.groupingBy(r -> r.getEmployee().getId()));

        List<Reimbursement> last12mReimbursements =
                reimbursementRepository.findAllBetweenPeriod(twelveMonthsBack, periodEnd);
        Map<UUID, List<Reimbursement>> hardwareLast12m = last12mReimbursements.stream()
                .filter(r -> r.getEmployee() != null && r.getCategory() != null)
                .filter(r -> HARDWARE_CATEGORY_NAME.equalsIgnoreCase(r.getCategory().getName()))
                .collect(Collectors.groupingBy(r -> r.getEmployee().getId()));

        Map<UUID, List<Vacation>> vacationsByEmployee = vacationRepository.findAllByDeletedIsFalse().stream()
                .filter(v -> v.getEmployee() != null && employeeIds.contains(v.getEmployee().getId()))
                .collect(Collectors.groupingBy(v -> v.getEmployee().getId()));

        Map<UUID, List<Pto>> ptosByEmployee = ptoRepository.findAllBetweenPeriod(periodStart, periodEnd).stream()
                .filter(p -> p.getEmployee() != null && employeeIds.contains(p.getEmployee().getId()))
                .collect(Collectors.groupingBy(p -> p.getEmployee().getId()));

        Map<UUID, Set<LocalDate>> holidayDatesByCountry = new HashMap<>();
        Map<UUID, Integer> workingHoursByCountry = new HashMap<>();
        if (!countryIds.isEmpty()) {
            List<Holiday> holidays = holidayRepository.findAllBetweenPeriod(periodStart, periodEnd);
            for (UUID countryId : countryIds) {
                Set<LocalDate> dates = holidays.stream()
                        .filter(h -> h.getCountry() != null
                                && (countryId.equals(h.getCountry().getId())
                                || "ALL".equalsIgnoreCase(h.getCountry().getName())))
                        .map(Holiday::getDate)
                        .collect(Collectors.toSet());
                holidayDatesByCountry.put(countryId, dates);
                workingHoursByCountry.put(countryId, computeWorkingHours(periodStart, periodEnd, dates));
            }
        }

        Map<UUID, String> clientNameByEmployee = new HashMap<>();
        Map<UUID, String> platformByEmployee = new HashMap<>();
        for (Contract c : contracts) {
            Employee e = c.getEmployee();
            UUID empId = e.getId();
            clientNameByEmployee.computeIfAbsent(empId, k -> findClientName(e, periodStart, periodEnd));
            platformByEmployee.computeIfAbsent(empId, k -> findPlatform(e));
        }

        Map<UUID, LocalDate> hireDateByEmployee = new HashMap<>();
        if (!employeeIds.isEmpty()) {
            for (Object[] row : contractRepository.findEarliestStartDateByEmployeeIds(employeeIds)) {
                hireDateByEmployee.put((UUID) row[0], (LocalDate) row[1]);
            }
        }

        return new PayrollContext(
                periodStart,
                contracts,
                overtimesByEmployee,
                reimbursementsByEmployee,
                hardwareLast12m,
                vacationsByEmployee,
                ptosByEmployee,
                holidayDatesByCountry,
                workingHoursByCountry,
                clientNameByEmployee,
                platformByEmployee,
                hireDateByEmployee
        );
    }

    static int computeWorkingHours(LocalDate start, LocalDate end, Set<LocalDate> holidays) {
        int days = 0;
        LocalDate cursor = start;
        while (!cursor.isAfter(end)) {
            DayOfWeek dow = cursor.getDayOfWeek();
            if (dow != DayOfWeek.SATURDAY && dow != DayOfWeek.SUNDAY && !holidays.contains(cursor)) {
                days++;
            }
            cursor = cursor.plusDays(1);
        }
        return days * HOURS_PER_BUSINESS_DAY;
    }

    private String findClientName(Employee employee, LocalDate periodStart, LocalDate periodEnd) {
        if (employee.getAssignments() == null) {
            return null;
        }
        return employee.getAssignments().stream()
                .filter(a -> a.getStartDate() == null || !a.getStartDate().isAfter(periodEnd))
                .filter(a -> a.getEndDate() == null || !a.getEndDate().isBefore(periodStart))
                .filter(Assignment::isActive)
                .findFirst()
                .map(this::projectClientName)
                .orElse(null);
    }

    private String projectClientName(Assignment assignment) {
        if (assignment.getProject() == null) {
            return null;
        }
        if (assignment.getProject().getClient() != null) {
            return assignment.getProject().getClient().getName();
        }
        return assignment.getProject().getName();
    }

    private String findPlatform(Employee employee) {
        if (employee.getPaymentsInformation() == null) {
            return null;
        }
        return employee.getPaymentsInformation().stream()
                .map(PaymentInformation::getPlatform)
                .filter(java.util.Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    /** Visible for test packaging — explicit empty list builder. */
    static List<Reimbursement> emptyReimbursements() {
        return new ArrayList<>();
    }
}
