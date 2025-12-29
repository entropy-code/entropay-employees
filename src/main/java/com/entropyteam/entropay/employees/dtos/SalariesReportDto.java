package com.entropyteam.entropay.employees.dtos;

import java.math.BigDecimal;
import java.util.UUID;
import com.entropyteam.entropay.common.sensitiveInformation.EmployeeIdAware;
import com.entropyteam.entropay.common.sensitiveInformation.SensitiveInformation;

public record SalariesReportDto(UUID id, UUID employeeId, String internalId, String firstName, String lastName,
                                String clientName, @SensitiveInformation BigDecimal salary, String modality,
                                String currency, String platform, String country) implements EmployeeIdAware {

    @Override
    public UUID getEmployeeId() {
        return employeeId();
    }
}
