package com.entropyteam.entropay.employees.dtos;

import java.math.BigDecimal;
import java.util.UUID;
import com.entropyteam.entropay.common.sensitiveInformation.EmployeeIdAware;
import com.entropyteam.entropay.common.sensitiveInformation.SensitiveInformation;
import com.entropyteam.entropay.employees.models.PaymentSettlement;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotNull;

public record PaymentSettlementDto(UUID id,
                                   @NotNull(message = "Salary is mandatory")
                                   @SensitiveInformation
                                   BigDecimal salary,
                                   @NotNull(message = "Currency is mandatory")
                                   String currency,
                                   @NotNull(message = "Currency is mandatory")
                                   String modality,
                                   UUID contractId,
                                   @JsonIgnore
                                   UUID employeeId,
                                   boolean deleted
) implements EmployeeIdAware {

    public PaymentSettlementDto(PaymentSettlement paymentSettlement) {
        this(paymentSettlement.getId(), paymentSettlement.getSalary(), paymentSettlement.getCurrency().name(),
                paymentSettlement.getModality().name(), paymentSettlement.getContract().getId(),
                paymentSettlement.getContract().getEmployee().getId(), paymentSettlement.isDeleted());
    }

    @Override
    public UUID getEmployeeId() {
        return employeeId();
    }
}
