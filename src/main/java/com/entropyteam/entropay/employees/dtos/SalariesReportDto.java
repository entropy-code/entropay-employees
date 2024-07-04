package com.entropyteam.entropay.employees.dtos;

import java.math.BigDecimal;
import java.util.UUID;

public record SalariesReportDto(UUID id, UUID employeeId, String internalId, String firstName, String lastName, String clientName,
                                BigDecimal salary, BigDecimal rate, String modality, String currency,
                                String platform, String country) {

}
