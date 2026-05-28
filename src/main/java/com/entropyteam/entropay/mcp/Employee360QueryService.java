package com.entropyteam.entropay.mcp;

import static com.entropyteam.entropay.auth.AuthConstants.ROLE_ADMIN;
import static com.entropyteam.entropay.auth.AuthConstants.ROLE_ANALYST;
import static com.entropyteam.entropay.auth.AuthConstants.ROLE_DEVELOPMENT;
import static com.entropyteam.entropay.auth.AuthConstants.ROLE_HR_DIRECTOR;
import static com.entropyteam.entropay.auth.AuthConstants.ROLE_MANAGER_HR;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.entropyteam.entropay.common.ReactAdminParams;
import com.entropyteam.entropay.employees.dtos.AssignmentDto;
import com.entropyteam.entropay.employees.dtos.EmployeeDto;
import com.entropyteam.entropay.employees.dtos.FeedbackDto;
import com.entropyteam.entropay.employees.models.Reimbursement;
import com.entropyteam.entropay.employees.repositories.AssignmentRepository;
import com.entropyteam.entropay.employees.repositories.EmployeeFeedbackRepository;
import com.entropyteam.entropay.employees.repositories.ReimbursementRepository;
import com.entropyteam.entropay.employees.repositories.VacationRepository;
import com.entropyteam.entropay.employees.services.EmployeeService;
import com.entropyteam.entropay.mcp.dtos.EmployeeSummary;
import com.entropyteam.entropay.mcp.dtos.EmployeeSummary.FeedbackHighlight;
import com.entropyteam.entropay.mcp.dtos.EmployeeSummary.ReimbursementHighlight;

/**
 * Read-only Employee 360 queries backing the MCP tools. Role gates mirror the REST API:
 * {@code @Secured} matches the role set the corresponding admin-ui screens require, so an
 * MCP caller sees exactly what they could see in the UI. Each method is intentionally a
 * thin orchestration layer over the existing platform services — no new SQL, no duplicated
 * business logic.
 */
@Service
public class Employee360QueryService {

    private static final int RECENT_FEEDBACKS_LIMIT = 5;
    private static final int LATEST_REIMBURSEMENTS_LIMIT = 5;
    private static final int REIMBURSEMENT_LOOKBACK_DAYS = 365;

    private final EmployeeService employeeService;
    private final AssignmentRepository assignmentRepository;
    private final EmployeeFeedbackRepository employeeFeedbackRepository;
    private final VacationRepository vacationRepository;
    private final ReimbursementRepository reimbursementRepository;

    public Employee360QueryService(EmployeeService employeeService, AssignmentRepository assignmentRepository,
            EmployeeFeedbackRepository employeeFeedbackRepository, VacationRepository vacationRepository,
            ReimbursementRepository reimbursementRepository) {
        this.employeeService = employeeService;
        this.assignmentRepository = assignmentRepository;
        this.employeeFeedbackRepository = employeeFeedbackRepository;
        this.vacationRepository = vacationRepository;
        this.reimbursementRepository = reimbursementRepository;
    }

    @Secured({ROLE_ADMIN, ROLE_MANAGER_HR, ROLE_ANALYST, ROLE_DEVELOPMENT, ROLE_HR_DIRECTOR})
    @Transactional(readOnly = true)
    public EmployeeDto getEmployee(String query) {
        if (StringUtils.isBlank(query)) {
            throw new IllegalArgumentException("query is required (employee UUID, internal ID, or name)");
        }
        return resolveEmployee(query.trim())
                .orElseThrow(() -> new IllegalArgumentException("No employee found matching '" + query + "'"));
    }

    @Secured({ROLE_ADMIN, ROLE_MANAGER_HR, ROLE_ANALYST, ROLE_DEVELOPMENT, ROLE_HR_DIRECTOR})
    @Transactional(readOnly = true)
    public List<AssignmentDto> listEmployeeAssignments(UUID employeeId) {
        if (employeeId == null) {
            throw new IllegalArgumentException("employeeId is required");
        }
        return assignmentRepository.findAssignmentByEmployee_IdAndDeletedIsFalse(employeeId).stream()
                .sorted(Comparator.comparing(a -> Optional.ofNullable(a.getStartDate()).orElse(LocalDate.MIN),
                        Comparator.reverseOrder()))
                .map(AssignmentDto::new)
                .toList();
    }

    @Secured({ROLE_ADMIN, ROLE_MANAGER_HR, ROLE_ANALYST, ROLE_DEVELOPMENT, ROLE_HR_DIRECTOR})
    @Transactional(readOnly = true)
    public List<FeedbackDto> listEmployeeFeedbacks(UUID employeeId) {
        if (employeeId == null) {
            throw new IllegalArgumentException("employeeId is required");
        }
        return employeeFeedbackRepository.findAllByEmployee_IdAndDeletedIsFalse(employeeId).stream()
                .sorted(Comparator.comparing(f -> Optional.ofNullable(f.getFeedbackDate()).orElse(LocalDate.MIN),
                        Comparator.reverseOrder()))
                .map(FeedbackDto::new)
                .toList();
    }

    @Secured({ROLE_ADMIN, ROLE_MANAGER_HR, ROLE_ANALYST, ROLE_DEVELOPMENT, ROLE_HR_DIRECTOR})
    @Transactional(readOnly = true)
    public EmployeeSummary getEmployeeSummary(String query) {
        EmployeeDto employee = getEmployee(query);
        UUID employeeId = employee.getId();

        Integer vacationBalance = vacationRepository.getAvailableDays(employeeId);

        List<FeedbackHighlight> recentFeedbacks = listEmployeeFeedbacks(employeeId).stream()
                .limit(RECENT_FEEDBACKS_LIMIT)
                .map(f -> new FeedbackHighlight(
                        f.feedbackDate(),
                        f.source() != null ? f.source().name() : null,
                        f.title(),
                        f.createdBy()))
                .toList();

        LocalDate to = LocalDate.now();
        LocalDate from = to.minusDays(REIMBURSEMENT_LOOKBACK_DAYS);
        List<ReimbursementHighlight> latestReimbursements = reimbursementRepository
                .findAllByEmployeeIdAndDateBetweenAndDeletedIsFalse(employeeId, from, to).stream()
                .sorted(Comparator.comparing(Reimbursement::getDate,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .limit(LATEST_REIMBURSEMENTS_LIMIT)
                .map(r -> new ReimbursementHighlight(
                        r.getDate(),
                        r.getCategory() != null ? r.getCategory().getName() : null,
                        r.getAmount(),
                        r.getComment()))
                .toList();

        return new EmployeeSummary(
                employeeId,
                employee.getInternalId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getLabourEmail(),
                employee.getCountryName(),
                employee.isActive(),
                employee.getRole(),
                employee.getProject(),
                employee.getClient(),
                employee.getStartDate(),
                employee.getEndDate(),
                employee.getTimeSinceStart(),
                employee.getRate(),
                employee.getSalary(),
                vacationBalance == null ? 0 : vacationBalance,
                recentFeedbacks,
                latestReimbursements);
    }

    private Optional<EmployeeDto> resolveEmployee(String query) {
        Optional<EmployeeDto> byUuid = tryParseUuid(query).flatMap(employeeService::findOne);
        if (byUuid.isPresent()) {
            return byUuid;
        }
        List<EmployeeDto> active = employeeService.findAllActive(new ReactAdminParams()).getContent();
        Optional<EmployeeDto> byInternalId = active.stream()
                .filter(e -> StringUtils.equalsIgnoreCase(e.getInternalId(), query))
                .findFirst();
        if (byInternalId.isPresent()) {
            return byInternalId;
        }
        return active.stream()
                .filter(e -> matchesName(e, query))
                .findFirst();
    }

    private static Optional<UUID> tryParseUuid(String input) {
        try {
            return Optional.of(UUID.fromString(input));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    private static boolean matchesName(EmployeeDto employee, String query) {
        String full = (StringUtils.defaultString(employee.getFirstName()) + " "
                + StringUtils.defaultString(employee.getLastName())).trim();
        return StringUtils.containsIgnoreCase(full, query)
                || StringUtils.containsIgnoreCase(employee.getFirstName(), query)
                || StringUtils.containsIgnoreCase(employee.getLastName(), query);
    }
}
