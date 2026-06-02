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
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.entropyteam.entropay.common.ReactAdminParams;
import com.entropyteam.entropay.employees.dtos.AssignmentDto;
import com.entropyteam.entropay.employees.dtos.EmployeeDto;
import com.entropyteam.entropay.employees.dtos.FeedbackDto;
import com.entropyteam.entropay.employees.models.Assignment;
import com.entropyteam.entropay.employees.models.Reimbursement;
import com.entropyteam.entropay.employees.repositories.AssignmentRepository;
import com.entropyteam.entropay.employees.repositories.EmployeeFeedbackRepository;
import com.entropyteam.entropay.employees.repositories.ReimbursementRepository;
import com.entropyteam.entropay.employees.repositories.VacationRepository;
import com.entropyteam.entropay.employees.services.EmployeeService;
import com.entropyteam.entropay.mcp.dtos.EmployeeSummary;
import com.entropyteam.entropay.mcp.dtos.EmployeeSummary.ActiveEngagement;
import com.entropyteam.entropay.mcp.dtos.EmployeeSummary.FeedbackHighlight;
import com.entropyteam.entropay.mcp.dtos.EmployeeSummary.ReimbursementHighlight;
import com.google.gson.JsonObject;

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

        // An employee can be active on more than one project/client at once, so the current
        // engagement is a list — each with its own client, role and (sensitive) billable rate —
        // rather than a single collapsed project/rate. Reuses the same finder as
        // list_employee_assignments; no new query.
        List<ActiveEngagement> activeEngagements = assignmentRepository
                .findAssignmentByEmployee_IdAndDeletedIsFalse(employeeId).stream()
                .filter(Assignment::isActive)
                .sorted(Comparator.comparing(a -> Optional.ofNullable(a.getStartDate()).orElse(LocalDate.MIN),
                        Comparator.reverseOrder()))
                .map(a -> new ActiveEngagement(
                        a.getProject() != null ? a.getProject().getName() : null,
                        a.getProject() != null && a.getProject().getClient() != null
                                ? a.getProject().getClient().getName() : null,
                        a.getRole() != null ? a.getRole().getName() : null,
                        a.getBillableRate()))
                .toList();

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
                employee.getStartDate(),
                employee.getEndDate(),
                employee.getTimeSinceStart(),
                activeEngagements,
                employee.getSalary(),
                vacationBalance == null ? 0 : vacationBalance,
                recentFeedbacks,
                latestReimbursements);
    }

    private Optional<EmployeeDto> resolveEmployee(String query) {
        // Exact UUID match first — cheapest and unambiguous.
        Optional<EmployeeDto> byUuid = tryParseUuid(query).flatMap(employeeService::findOne);
        if (byUuid.isPresent()) {
            return byUuid;
        }
        // Otherwise delegate to the platform's active-employee search instead of scanning the
        // whole roster in memory. This mirrors the admin-ui query
        // /employees?filter={"active":true,"q":"<query>"} and reuses EmployeeService's
        // getColumnsForSearch() (firstName, lastName, internalId) so the MCP tool resolves
        // employees exactly the way the UI does.
        List<EmployeeDto> matches = searchActiveEmployees(query);
        if (matches.size() <= 1) {
            return matches.stream().findFirst();
        }
        // Several employees matched the free-text search. Prefer an exact internal-ID hit;
        // if there is none, the query is genuinely ambiguous — surface a clear error rather
        // than silently picking one (the parent story requires "clear error, not partial data").
        Optional<EmployeeDto> exactInternalId = matches.stream()
                .filter(e -> StringUtils.equalsIgnoreCase(e.getInternalId(), query))
                .findFirst();
        if (exactInternalId.isPresent()) {
            return exactInternalId;
        }
        throw new IllegalArgumentException(ambiguousMatchMessage(query, matches));
    }

    private List<EmployeeDto> searchActiveEmployees(String query) {
        JsonObject filter = new JsonObject();
        filter.addProperty("active", true);
        filter.addProperty("q", query);
        ReactAdminParams params = new ReactAdminParams();
        params.setFilter(filter.toString());
        return employeeService.findAllActive(params).getContent();
    }

    private static String ambiguousMatchMessage(String query, List<EmployeeDto> matches) {
        String sample = matches.stream()
                .limit(5)
                .map(e -> (StringUtils.defaultString(e.getInternalId()) + " "
                        + StringUtils.defaultString(e.getFirstName()) + " "
                        + StringUtils.defaultString(e.getLastName())).trim())
                .collect(Collectors.joining(", "));
        return "Multiple employees match '" + query + "' (" + matches.size()
                + "). Refine by internal ID or UUID. Matches: " + sample;
    }

    private static Optional<UUID> tryParseUuid(String input) {
        try {
            return Optional.of(UUID.fromString(input));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
