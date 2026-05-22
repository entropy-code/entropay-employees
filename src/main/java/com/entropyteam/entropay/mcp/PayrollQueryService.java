package com.entropyteam.entropay.mcp;

import static com.entropyteam.entropay.auth.AuthConstants.ROLE_ADMIN;
import static com.entropyteam.entropay.auth.AuthConstants.ROLE_ANALYST;
import static com.entropyteam.entropay.auth.AuthConstants.ROLE_DEVELOPMENT;
import static com.entropyteam.entropay.auth.AuthConstants.ROLE_HR_DIRECTOR;
import static com.entropyteam.entropay.auth.AuthConstants.ROLE_MANAGER_HR;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Year;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.entropyteam.entropay.common.ReactAdminParams;
import com.entropyteam.entropay.employees.dtos.EmployeeDto;
import com.entropyteam.entropay.employees.dtos.PtoDto;
import com.entropyteam.entropay.employees.models.Reimbursement;
import com.entropyteam.entropay.employees.repositories.PtoRepository;
import com.entropyteam.entropay.employees.repositories.ReimbursementRepository;
import com.entropyteam.entropay.employees.repositories.VacationRepository;
import com.entropyteam.entropay.employees.services.EmployeeService;
import com.entropyteam.entropay.mcp.dtos.EmployeeDetail;
import com.entropyteam.entropay.mcp.dtos.PayrollSummary;
import com.entropyteam.entropay.mcp.dtos.ReimbursementEntry;
import com.entropyteam.entropay.mcp.dtos.RosterEntry;
import com.entropyteam.entropay.mcp.dtos.VacationBalance;

/**
 * Read-only payroll queries backing the MCP tools. Role-based authorization is enforced
 * here with {@code @Secured} (mirroring the REST controllers' read roles) so the MCP tool
 * surface {@link PayrollMcpTools} can stay a thin, un-proxied delegation layer.
 */
@Service
public class PayrollQueryService {

    private static final int DEFAULT_ROSTER_LIMIT = 50;
    private static final int MAX_ROSTER_LIMIT = 200;

    private final EmployeeService employeeService;
    private final PtoRepository ptoRepository;
    private final ReimbursementRepository reimbursementRepository;
    private final VacationRepository vacationRepository;

    public PayrollQueryService(EmployeeService employeeService, PtoRepository ptoRepository,
            ReimbursementRepository reimbursementRepository, VacationRepository vacationRepository) {
        this.employeeService = employeeService;
        this.ptoRepository = ptoRepository;
        this.reimbursementRepository = reimbursementRepository;
        this.vacationRepository = vacationRepository;
    }

    @Secured({ROLE_ADMIN, ROLE_HR_DIRECTOR, ROLE_MANAGER_HR, ROLE_ANALYST, ROLE_DEVELOPMENT})
    @Transactional(readOnly = true)
    public List<RosterEntry> listPayrollRoster(String search, Integer limit) {
        int cap = limit == null ? DEFAULT_ROSTER_LIMIT : Math.min(Math.max(limit, 1), MAX_ROSTER_LIMIT);
        return activeEmployees().stream()
                .filter(employee -> matchesSearch(employee, search))
                .sorted(Comparator.comparing(EmployeeDto::getLastName,
                        Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)))
                .limit(cap)
                .map(this::toRosterEntry)
                .toList();
    }

    @Secured({ROLE_ADMIN, ROLE_HR_DIRECTOR, ROLE_MANAGER_HR, ROLE_ANALYST, ROLE_DEVELOPMENT})
    @Transactional(readOnly = true)
    public EmployeeDetail getEmployee(UUID employeeId) {
        EmployeeDto employee = employeeService.findOne(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("No employee found with id " + employeeId));
        return new EmployeeDetail(
                employee.getId(), employee.getInternalId(), employee.getFirstName(), employee.getLastName(),
                employee.getLabourEmail(), employee.getPersonalEmail(), employee.getCountryName(),
                employee.getGender() != null ? employee.getGender().name() : null, employee.getBirthDate(),
                employee.getProject(), employee.getClient(), employee.getRole(),
                employee.getStartDate(), employee.getEndDate(), employee.isActive(), employee.getTimeSinceStart(),
                employee.getSalary(), employee.getRate(), employee.getMargin(),
                employee.getAvailableDays(), employee.getNearestPto());
    }

    @Secured({ROLE_ADMIN, ROLE_MANAGER_HR, ROLE_DEVELOPMENT, ROLE_HR_DIRECTOR})
    @Transactional(readOnly = true)
    public List<PtoDto> listEmployeePtos(UUID employeeId, Integer year) {
        int targetYear = year == null ? Year.now().getValue() : year;
        return ptoRepository.findPtosByEmployeeIdIsAndDeletedIsFalse(employeeId, targetYear).stream()
                .map(PtoDto::new)
                .toList();
    }

    @Secured({ROLE_ADMIN, ROLE_MANAGER_HR, ROLE_DEVELOPMENT})
    @Transactional(readOnly = true)
    public VacationBalance getVacationBalance(UUID employeeId) {
        List<VacationBalance.YearBalance> byYear = vacationRepository.getVacationByYear(employeeId).stream()
                .map(balance -> new VacationBalance.YearBalance(balance.getYear(), balance.getBalance()))
                .toList();
        Integer total = vacationRepository.getAvailableDays(employeeId);
        return new VacationBalance(employeeId, total == null ? 0 : total, byYear);
    }

    @Secured({ROLE_ADMIN, ROLE_MANAGER_HR, ROLE_DEVELOPMENT, ROLE_HR_DIRECTOR})
    @Transactional(readOnly = true)
    public List<ReimbursementEntry> listEmployeeReimbursements(UUID employeeId, LocalDate startDate,
            LocalDate endDate) {
        LocalDate from = startDate != null ? startDate : LocalDate.now().withDayOfYear(1);
        LocalDate to = endDate != null ? endDate : LocalDate.now();
        return reimbursementRepository.findAllBetweenPeriod(from, to).stream()
                .filter(reimbursement -> reimbursement.getEmployee().getId().equals(employeeId))
                .map(this::toReimbursementEntry)
                .toList();
    }

    @Secured({ROLE_ADMIN, ROLE_HR_DIRECTOR, ROLE_MANAGER_HR, ROLE_ANALYST})
    @Transactional(readOnly = true)
    public PayrollSummary getPayrollSummary(String country, String client) {
        List<EmployeeDto> employees = activeEmployees().stream()
                .filter(employee -> StringUtils.isBlank(country)
                        || StringUtils.containsIgnoreCase(employee.getCountryName(), country))
                .filter(employee -> StringUtils.isBlank(client)
                        || StringUtils.containsIgnoreCase(employee.getClient(), client))
                .toList();
        BigDecimal total = employees.stream()
                .map(this::salaryOrZero)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        Map<String, List<EmployeeDto>> byCountry = employees.stream()
                .collect(Collectors.groupingBy(employee ->
                        employee.getCountryName() != null ? employee.getCountryName() : "Unknown"));
        List<PayrollSummary.CountryBreakdown> breakdown = byCountry.entrySet().stream()
                .map(entry -> new PayrollSummary.CountryBreakdown(
                        entry.getKey(),
                        entry.getValue().size(),
                        entry.getValue().stream().map(this::salaryOrZero).reduce(BigDecimal.ZERO, BigDecimal::add)))
                .sorted(Comparator.comparing(PayrollSummary.CountryBreakdown::country))
                .toList();
        return new PayrollSummary(employees.size(), total, breakdown);
    }

    private List<EmployeeDto> activeEmployees() {
        return employeeService.findAllActive(new ReactAdminParams()).getContent().stream()
                .filter(EmployeeDto::isActive)
                .toList();
    }

    private boolean matchesSearch(EmployeeDto employee, String search) {
        if (StringUtils.isBlank(search)) {
            return true;
        }
        return StringUtils.containsIgnoreCase(employee.getFirstName(), search)
                || StringUtils.containsIgnoreCase(employee.getLastName(), search)
                || StringUtils.containsIgnoreCase(employee.getInternalId(), search);
    }

    private RosterEntry toRosterEntry(EmployeeDto employee) {
        return new RosterEntry(employee.getId(), employee.getInternalId(), employee.getFirstName(),
                employee.getLastName(), employee.getCountryName(), employee.getProject(), employee.getClient(),
                employee.getRole(), employee.getTimeSinceStart(), employee.isActive());
    }

    private ReimbursementEntry toReimbursementEntry(Reimbursement reimbursement) {
        return new ReimbursementEntry(reimbursement.getId(), reimbursement.getEmployee().getId(),
                reimbursement.getEmployee().getFullName(), reimbursement.getCategory().getName(),
                reimbursement.getAmount(), reimbursement.getDate(), reimbursement.getComment());
    }

    private BigDecimal salaryOrZero(EmployeeDto employee) {
        return employee.getSalary() != null ? employee.getSalary() : BigDecimal.ZERO;
    }
}
