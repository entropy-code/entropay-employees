package com.entropyteam.entropay.employees.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * DTO for hierarchical turnover report data.
 * Contains a three-level hierarchy:
 * 1. Company level (overall and per month)
 * 2. Client level (overall and per month)
 * 3. Project level (overall and per month)
 */
public record TurnoverReportDto(
        LocalDate startDate,
        LocalDate endDate,
        TurnoverMetrics overall,
        Map<String, TurnoverMetrics> yearMonths,
        List<ClientTurnoverDto> clients
) {

    /**
     * DTO for turnover metrics.
     * Contains the basic turnover data without identifiers.
     */
    public record TurnoverMetrics(
            int employeesAtStart,
            int employeesLeft,
            int employeesAtEnd,
            BigDecimal turnoverRate
    ) {

        /**
         * Creates a new TurnoverMetrics with a turnover rate of 0 if employeesAtStart is 0.
         */
        public static TurnoverMetrics withSafeTurnoverRate(
                int employeesAtStart,
                int employeesLeft,
                int employeesAtEnd,
                BigDecimal turnoverRate
        ) {
            return new TurnoverMetrics(
                    employeesAtStart,
                    employeesLeft,
                    employeesAtEnd,
                    employeesAtStart == 0 ? BigDecimal.ZERO : turnoverRate
            );
        }
    }

    /**
     * DTO for client-level turnover data.
     */
    public record ClientTurnoverDto(
            UUID id,
            String name,
            TurnoverMetrics overall,
            Map<String, TurnoverMetrics> yearMonths,
            List<ProjectTurnoverDto> projects
    ) {

    }

    /**
     * DTO for project-level turnover data.
     */
    public record ProjectTurnoverDto(
            UUID id,
            String name,
            UUID clientId, // ID of the client this project belongs to
            TurnoverMetrics overall,
            Map<String, TurnoverMetrics> yearMonths
    ) {

    }
}
