package com.entropyteam.entropay.employees.controllers;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.entropyteam.entropay.common.BaseController;
import com.entropyteam.entropay.employees.dtos.PaymentInformationDto;
import com.entropyteam.entropay.employees.services.PaymentInformationService;

@RestController
@CrossOrigin
@RequestMapping(value = "/payment-information", produces = MediaType.APPLICATION_JSON_VALUE)
public class PaymentInformationController extends BaseController<PaymentInformationDto, UUID> {

        @Autowired
        public PaymentInformationController(PaymentInformationService paymentInformationService) {
        super(paymentInformationService);
        }

}
