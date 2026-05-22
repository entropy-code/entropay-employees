package com.entropyteam.entropay.mcp.dtos;

import java.util.List;
import java.util.UUID;

/**
 * Vacation-day balance for an employee, broken down by year, exposed by the MCP
 * {@code get_vacation_balance} tool.
 */
public record VacationBalance(
        UUID employeeId,
        int totalAvailableDays,
        List<YearBalance> byYear) {

    public record YearBalance(String year, int balance) {
    }
}
