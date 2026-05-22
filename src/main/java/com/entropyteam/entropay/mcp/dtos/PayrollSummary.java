package com.entropyteam.entropay.mcp.dtos;

import java.math.BigDecimal;
import java.util.List;

/**
 * Aggregated monthly payroll cost across active employees, with a per-country breakdown,
 * exposed by the MCP {@code get_payroll_summary} tool.
 */
public record PayrollSummary(
        int headcount,
        BigDecimal totalMonthlySalaryUsd,
        List<CountryBreakdown> byCountry) {

    public record CountryBreakdown(String country, int headcount, BigDecimal totalMonthlySalaryUsd) {
    }
}
