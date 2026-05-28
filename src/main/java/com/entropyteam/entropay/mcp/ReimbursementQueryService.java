package com.entropyteam.entropay.mcp;

import static com.entropyteam.entropay.auth.AuthConstants.ROLE_ADMIN;
import static com.entropyteam.entropay.auth.AuthConstants.ROLE_DEVELOPMENT;
import static com.entropyteam.entropay.auth.AuthConstants.ROLE_HR_DIRECTOR;
import static com.entropyteam.entropay.auth.AuthConstants.ROLE_MANAGER_HR;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.entropyteam.entropay.employees.models.Reimbursement;
import com.entropyteam.entropay.employees.repositories.ReimbursementRepository;
import com.entropyteam.entropay.mcp.dtos.ReimbursementEntry;

/**
 * Read-only Reimbursement queries backing the MCP tools. Role gates mirror
 * {@code ReimbursementController}: ADMIN, MANAGER_HR, DEVELOPMENT, HR_DIRECTOR (the
 * ANALYST role intentionally has no UI access and therefore no MCP access).
 */
@Service
public class ReimbursementQueryService {

    private final ReimbursementRepository reimbursementRepository;

    public ReimbursementQueryService(ReimbursementRepository reimbursementRepository) {
        this.reimbursementRepository = reimbursementRepository;
    }

    @Secured({ROLE_ADMIN, ROLE_MANAGER_HR, ROLE_DEVELOPMENT, ROLE_HR_DIRECTOR})
    @Transactional(readOnly = true)
    public List<ReimbursementEntry> listReimbursements(UUID employeeId, LocalDate startDate, LocalDate endDate) {
        LocalDate from = startDate != null ? startDate : LocalDate.now().withDayOfYear(1);
        LocalDate to = endDate != null ? endDate : LocalDate.now();
        if (to.isBefore(from)) {
            throw new IllegalArgumentException("endDate must not be before startDate");
        }
        List<Reimbursement> reimbursements = employeeId != null
                ? reimbursementRepository.findAllByEmployeeIdAndDateBetweenAndDeletedIsFalse(employeeId, from, to)
                : reimbursementRepository.findAllBetweenPeriod(from, to);
        return reimbursements.stream()
                .sorted(Comparator.comparing(Reimbursement::getDate,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .map(this::toEntry)
                .toList();
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
