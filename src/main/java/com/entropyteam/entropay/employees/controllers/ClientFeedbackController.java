package com.entropyteam.entropay.employees.controllers;

import java.util.UUID;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.entropyteam.entropay.common.BaseController;
import com.entropyteam.entropay.employees.dtos.ClientFeedbackDto;
import com.entropyteam.entropay.employees.services.ClientFeedbackService;

@RestController
@CrossOrigin
@RequestMapping("/feedback/client")
public class ClientFeedbackController extends BaseController<ClientFeedbackDto, UUID> {

    public ClientFeedbackController(ClientFeedbackService clientFeedbackService) {
        super(clientFeedbackService);
    }
}