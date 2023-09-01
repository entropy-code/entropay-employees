package com.entropyteam.entropay.employees.dtos;

import com.entropyteam.entropay.employees.models.PaymentInformation;

import java.util.UUID;

public record PaymentInformationDto(UUID id,
                                    String platform,
                                    String country,
                                    String cbu,
                                    String routingNumber,
                                    boolean deleted
) {

    public PaymentInformationDto(PaymentInformation paymentInformation) {
        this(paymentInformation.getId(), paymentInformation.getPlatform(),paymentInformation.getCountry(),
                paymentInformation.getCbu(), paymentInformation.getRoutingNumber(), paymentInformation.isDeleted());
    }

}
