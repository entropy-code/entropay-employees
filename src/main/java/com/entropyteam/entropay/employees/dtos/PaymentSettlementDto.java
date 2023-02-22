package com.entropyteam.entropay.employees.dtos;
import com.entropyteam.entropay.employees.models.PaymentSettlement;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;
public record PaymentSettlementDto(UUID id,
                                   @NotNull(message = "Salary is mandatory")
                                   BigDecimal salary,
                                   @NotNull(message = "Currency is mandatory")
                                   String currency,
                                   @NotNull(message = "Currency is mandatory")
                                   String modality,
                                   UUID contractId,
                                   boolean deleted
){
    public PaymentSettlementDto(PaymentSettlement paymentSettlement) {
        this(   paymentSettlement.getId(), paymentSettlement.getSalary(),paymentSettlement.getCurrency().name(),
                paymentSettlement.getModality().name(), paymentSettlement.getContract().getId(), paymentSettlement.isDeleted());
    }
}
