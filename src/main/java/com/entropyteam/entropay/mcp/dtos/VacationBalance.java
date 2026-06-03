package com.entropyteam.entropay.mcp.dtos;

import java.util.List;

/**
 * Vacation balance returned by {@code get_vacation_balance}. Carries the total available days
 * plus a per-year breakdown, the same view the admin UI shows on the employee profile.
 *
 * <p>Not {@code @SensitiveInformation}-bearing: vacation balances are visible to every role
 * that can see the time-off screens in the admin UI.
 */
public record VacationBalance(String internalId, int totalAvailableDays, List<YearBalance> byYear) {

    public record YearBalance(String year, int balance) {
    }
}
