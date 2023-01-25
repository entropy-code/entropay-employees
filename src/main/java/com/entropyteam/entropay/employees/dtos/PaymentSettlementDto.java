package com.entropyteam.entropay.employees.dtos;
import com.entropyteam.entropay.employees.models.PaymentSettlement;

import java.math.BigDecimal;
import java.util.UUID;
public record PaymentSettlementDto(UUID id,
                                   BigDecimal salary,
                                   String currency,
                                   String modality,
                                   UUID contractId,
                                   boolean deleted
){
    public PaymentSettlementDto(PaymentSettlement paymentSettlement) {
        this(   paymentSettlement.getId(), paymentSettlement.getSalary(),paymentSettlement.getCurrency().name(),
                paymentSettlement.getModality().name(), paymentSettlement.getContract().getId(), paymentSettlement.isDeleted());
    }
}
