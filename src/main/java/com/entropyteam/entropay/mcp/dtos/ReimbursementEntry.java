package com.entropyteam.entropay.mcp.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Expense reimbursement row exposed by the MCP {@code list_employee_reimbursements} tool.
 * Includes the resolved category name (the underlying entity only stores a category id).
 */
public record ReimbursementEntry(
        UUID id,
        UUID employeeId,
        String employeeName,
        String category,
        BigDecimal amount,
        LocalDate date,
        String comment) {
}
