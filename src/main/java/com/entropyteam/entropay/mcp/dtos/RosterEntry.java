package com.entropyteam.entropay.mcp.dtos;

import java.util.UUID;

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
