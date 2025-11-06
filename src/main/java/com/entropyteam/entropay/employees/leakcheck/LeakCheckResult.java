package com.entropyteam.entropay.employees.leakcheck;

import java.util.Map;

/**
 * Result of checking an employee's emails for leaks.
 * Immutable data transfer object for async processing results.
 */
record LeakCheckResult(
        String employeeName,
        Map<LeakType, Integer> leaksByType
) {
    /**
     * Creates an empty result with no leaks found.
     */
    static LeakCheckResult empty(String employeeName) {
        return new LeakCheckResult(employeeName, Map.of());
    }

    /**
     * Returns the total number of leaks across all types.
     */
    int totalLeaks() {
        return leaksByType.values().stream()
                .mapToInt(Integer::intValue)
                .sum();
    }
}
