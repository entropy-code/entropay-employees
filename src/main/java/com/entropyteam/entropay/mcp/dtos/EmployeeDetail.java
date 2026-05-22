package com.entropyteam.entropay.mcp.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Curated employee detail exposed by the MCP {@code get_employee} tool. Includes
 * compensation (monthly salary in USD, billable rate, margin) but deliberately omits
 * highly sensitive PII such as tax id, personal number, address and emergency contacts.
 */
public record EmployeeDetail(
        UUID id,
        String internalId,
        String firstName,
        String lastName,
        String labourEmail,
        String personalEmail,
        String country,
        String gender,
        LocalDate birthDate,
        String project,
        String client,
        String role,
        LocalDate startDate,
        LocalDate endDate,
        boolean active,
        String timeSinceStart,
        BigDecimal monthlySalaryUsd,
        BigDecimal billableRate,
        BigDecimal margin,
        Integer availableVacationDays,
        LocalDate nearestPto) {
}
