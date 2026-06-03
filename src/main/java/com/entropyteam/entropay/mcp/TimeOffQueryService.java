package com.entropyteam.entropay.mcp;

import static com.entropyteam.entropay.auth.AuthConstants.ROLE_ADMIN;
import static com.entropyteam.entropay.auth.AuthConstants.ROLE_DEVELOPMENT;
import static com.entropyteam.entropay.auth.AuthConstants.ROLE_HR_DIRECTOR;
import static com.entropyteam.entropay.auth.AuthConstants.ROLE_MANAGER_HR;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.entropyteam.entropay.common.ReactAdminParams;
import com.entropyteam.entropay.employees.dtos.EmployeeDto;
import com.entropyteam.entropay.employees.dtos.PtoDto;
import com.entropyteam.entropay.employees.models.Pto;
import com.entropyteam.entropay.employees.repositories.PtoRepository;
import com.entropyteam.entropay.employees.repositories.VacationRepository;
import com.entropyteam.entropay.employees.services.EmployeeService;
import com.entropyteam.entropay.mcp.dtos.VacationBalance;

/**
 * Read-only Time off queries backing the MCP tools. Role gates mirror the corresponding
 * admin-ui screens: vacation balance and per-employee PTO listing match
 * {@code VacationController} and {@code PtoController} respectively, and the company-wide
 * upcoming-PTOs view matches the {@code /reports/ptos/all-details} endpoint.
 */
@Service
public class TimeOffQueryService {

    private static final int DEFAULT_UPCOMING_WINDOW_DAYS = 30;

    private final PtoRepository ptoRepository;
    private final VacationRepository vacationRepository;
    private final EmployeeService employeeService;

    public TimeOffQueryService(PtoRepository ptoRepository, VacationRepository vacationRepository,
            EmployeeService employeeService) {
        this.ptoRepository = ptoRepository;
        this.vacationRepository = vacationRepository;
        this.employeeService = employeeService;
    }

    @Secured({ROLE_ADMIN, ROLE_MANAGER_HR, ROLE_DEVELOPMENT})
    @Transactional(readOnly = true)
    public VacationBalance getVacationBalance(String internalId) {
        UUID employeeId = resolveEmployeeId(internalId);
        List<VacationBalance.YearBalance> byYear = vacationRepository.getVacationByYear(employeeId).stream()
                .map(b -> new VacationBalance.YearBalance(b.getYear(), b.getBalance()))
                .toList();
        Integer total = vacationRepository.getAvailableDays(employeeId);
        return new VacationBalance(internalId, total == null ? 0 : total, byYear);
    }

    @Secured({ROLE_ADMIN, ROLE_MANAGER_HR, ROLE_DEVELOPMENT, ROLE_HR_DIRECTOR})
    @Transactional(readOnly = true)
    public List<PtoDto> listEmployeePtos(String internalId, LocalDate startDate, LocalDate endDate,
            String leaveType) {
        UUID employeeId = resolveEmployeeId(internalId);
        return ptoRepository.findAllByEmployee_IdAndDeletedIsFalse(employeeId).stream()
                .filter(p -> dateRangeOverlaps(p, startDate, endDate))
                .filter(p -> leaveTypeMatches(p, leaveType))
                .sorted(Comparator.comparing(Pto::getStartDate, Comparator.nullsLast(Comparator.reverseOrder())))
                .map(PtoDto::new)
                .toList();
    }

    @Secured({ROLE_ADMIN, ROLE_MANAGER_HR, ROLE_DEVELOPMENT, ROLE_HR_DIRECTOR})
    @Transactional(readOnly = true)
    public List<PtoDto> listUpcomingPtos(LocalDate startDate, LocalDate endDate) {
        LocalDate from = startDate != null ? startDate : LocalDate.now();
        LocalDate to = endDate != null ? endDate : from.plusDays(DEFAULT_UPCOMING_WINDOW_DAYS);
        if (to.isBefore(from)) {
            throw new IllegalArgumentException("endDate must not be before startDate");
        }
        return ptoRepository.findAllBetweenPeriod(from, to).stream()
                .sorted(Comparator.comparing(Pto::getStartDate, Comparator.nullsLast(Comparator.naturalOrder())))
                .map(PtoDto::new)
                .toList();
    }

    private static boolean dateRangeOverlaps(Pto pto, LocalDate startDate, LocalDate endDate) {
        if (startDate == null && endDate == null) {
            return true;
        }
        LocalDate ptoStart = pto.getStartDate();
        LocalDate ptoEnd = pto.getEndDate();
        if (ptoStart == null || ptoEnd == null) {
            return false;
        }
        if (startDate != null && ptoEnd.isBefore(startDate)) {
            return false;
        }
        if (endDate != null && ptoStart.isAfter(endDate)) {
            return false;
        }
        return true;
    }

    private static boolean leaveTypeMatches(Pto pto, String leaveType) {
        if (StringUtils.isBlank(leaveType)) {
            return true;
        }
        return pto.getLeaveType() != null
                && StringUtils.equalsIgnoreCase(pto.getLeaveType().getName(), leaveType);
    }

    /**
     * Resolves an employee's internal ID (the identifier the admin UI exposes, e.g. {@code E042})
     * to its UUID. Delegates to the platform's active-employee search — the same path the
     * {@code get_employee} tool uses — so {@code internalId} is matched exactly the way the UI does,
     * and callers never have to know the internal UUID.
     */
    private UUID resolveEmployeeId(String internalId) {
        if (StringUtils.isBlank(internalId)) {
            throw new IllegalArgumentException("internalId is required");
        }
        JsonObject filter = new JsonObject();
        filter.addProperty("active", true);
        filter.addProperty("q", internalId);
        ReactAdminParams params = new ReactAdminParams();
        params.setFilter(filter.toString());
        return employeeService.findAllActive(params).getContent().stream()
                .filter(e -> StringUtils.equalsIgnoreCase(e.getInternalId(), internalId))
                .map(EmployeeDto::getId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "No active employee found with internal ID '" + internalId + "'"));
    }
}
