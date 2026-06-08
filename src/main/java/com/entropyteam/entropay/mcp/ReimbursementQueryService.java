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
import com.entropyteam.entropay.employees.models.Reimbursement;
import com.entropyteam.entropay.employees.repositories.ReimbursementRepository;
import com.entropyteam.entropay.employees.services.EmployeeService;
import com.entropyteam.entropay.mcp.dtos.ReimbursementEntry;

/**
 * Read-only Reimbursement queries backing the MCP tools. Role gates mirror
 * {@code ReimbursementController}: ADMIN, MANAGER_HR, DEVELOPMENT, HR_DIRECTOR (the
 * ANALYST role intentionally has no UI access and therefore no MCP access).
 */
@Service
public class ReimbursementQueryService {

    private final ReimbursementRepository reimbursementRepository;
    private final EmployeeService employeeService;

    public ReimbursementQueryService(ReimbursementRepository reimbursementRepository,
            EmployeeService employeeService) {
        this.reimbursementRepository = reimbursementRepository;
        this.employeeService = employeeService;
    }

    @Secured({ROLE_ADMIN, ROLE_MANAGER_HR, ROLE_DEVELOPMENT, ROLE_HR_DIRECTOR})
    @Transactional(readOnly = true)
    public List<ReimbursementEntry> listReimbursements(String internalId, LocalDate startDate, LocalDate endDate) {
        LocalDate from = startDate != null ? startDate : LocalDate.now().withDayOfYear(1);
        LocalDate to = endDate != null ? endDate : LocalDate.now();
        if (to.isBefore(from)) {
            throw new IllegalArgumentException("endDate must not be before startDate");
        }
        List<Reimbursement> reimbursements = StringUtils.isNotBlank(internalId)
                ? reimbursementRepository.findAllByEmployeeIdAndDateBetweenAndDeletedIsFalse(
                        resolveEmployeeId(internalId), from, to)
                : reimbursementRepository.findAllBetweenPeriod(from, to);
        return reimbursements.stream()
                .sorted(Comparator.comparing(Reimbursement::getDate,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .map(this::toEntry)
                .toList();
    }

    /**
     * Resolves an employee's internal ID (the identifier the admin UI exposes, e.g. {@code E042})
     * to its UUID. Delegates to the platform's active-employee search — the same path the
     * {@code get_employee} tool uses — so {@code internalId} is matched exactly the way the UI does,
     * and callers never have to know the internal UUID.
     */
    private UUID resolveEmployeeId(String internalId) {
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

    private ReimbursementEntry toEntry(Reimbursement reimbursement) {
        return new ReimbursementEntry(
                reimbursement.getId(),
                reimbursement.getEmployee() != null ? reimbursement.getEmployee().getId() : null,
                reimbursement.getEmployee() != null ? reimbursement.getEmployee().getFullName() : null,
                reimbursement.getCategory() != null ? reimbursement.getCategory().getName() : null,
                reimbursement.getAmount(),
                reimbursement.getDate(),
                reimbursement.getComment());
    }
}
