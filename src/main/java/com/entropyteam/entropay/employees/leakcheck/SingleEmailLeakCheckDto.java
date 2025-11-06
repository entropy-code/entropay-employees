package com.entropyteam.entropay.employees.leakcheck;

import java.util.List;

/**
 * Response DTO for single email leak check endpoint.
 */
record SingleEmailLeakCheckDto(
        String email,
        boolean hasLeaks,
        int totalLeaksFound,
        List<LeakDetailDto> leaks
) {

    record LeakDetailDto(
            String sourceName,
            String breachDate,
            LeakType leakType,
            boolean hasPassword,
            List<String> origins
    ) {}

    /**
     * Creates a response indicating no leaks were found.
     */
    static SingleEmailLeakCheckDto noLeaks(String email) {
        return new SingleEmailLeakCheckDto(email, false, 0, List.of());
    }
}
