package com.entropyteam.entropay.employees.repositories.projections;

import java.util.UUID;

public record MonthlyAssignment(String yearMonth, UUID employeeId, String internalId, String firstName, String lastName,
                                UUID projectId, String projectName, UUID clientId, String clientName) {

}
