package com.entropyteam.entropay.employees.dtos;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO for a single entry in the flat turnover report.
 * Each entry contains the level information, time period, and metrics.
 */
public record TurnoverEntryDto(
        // Level information
        LevelType levelType,
        String id,
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