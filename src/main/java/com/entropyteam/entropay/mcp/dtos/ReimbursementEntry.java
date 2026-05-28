package com.entropyteam.entropay.mcp.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Reimbursement view returned by {@code list_reimbursements}. Carries the human-readable
 * employee and category names rather than UUIDs so the MCP client can render the result
 * directly without a follow-up lookup.
 *
 * <p>Not {@code @SensitiveInformation}-bearing: amount visibility matches the admin UI,
 * where {@code ReimbursementController} gates the screen to ADMIN, MANAGER_HR, DEVELOPMENT,
 * and HR_DIRECTOR but does not mask the amount for any of them.
 */
public record ReimbursementEntry(UUID id, UUID employeeId, String employeeName, String category,
                                 BigDecimal amount, LocalDate date, String comment) {
}
