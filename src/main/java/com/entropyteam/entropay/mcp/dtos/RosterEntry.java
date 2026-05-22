package com.entropyteam.entropay.mcp.dtos;

import java.util.UUID;

/**
 * Slim payroll roster row exposed by the MCP {@code list_payroll_roster} tool. Carries no
 * salary or personal data — those require {@code get_employee}.
 */
public record RosterEntry(
        UUID id,
        String internalId,
        String firstName,
        String lastName,
        String country,
        String project,
        String client,
        String role,
        String timeSinceStart,
        boolean active) {
}
