package com.entropyteam.entropay.employees.dtos;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * DTO for flat turnover report data.
 * Flattens the hierarchical structure of TurnoverReportDto into a list of entries.
 * Each entry represents a single set of metrics at a specific level (overall, client, project)
 * and time period (overall, monthly).
 */
public record FlatTurnoverReportDto(
        List<TurnoverEntryDto> entries
) {

    /**
     * DTO for a single entry in the flat turnover report.
     * Each entry contains the level information, time period, and metrics.
     */
    public record TurnoverEntryDto(
            // Level information
            LevelType levelType,
            UUID id,
            String name,
            UUID parentId,  // For project level, this is the client ID; null for other levels

            // Time period information
            PeriodType periodType,
            String yearMonth,  // Only for monthly periods; null for overall periods

            // Metrics
            int employeesAtStart,
            int employeesLeft,
            int employeesAtEnd,
            BigDecimal turnoverRate

    ) {

    }

    /**
     * Enum representing the level of the turnover data.
     */
    public enum LevelType {
        COMPANY,
        CLIENT,
        PROJECT
    }

    /**
     * Enum representing the period type of the turnover data.
     */
    public enum PeriodType {
        OVERALL,
        MONTHLY
    }
}