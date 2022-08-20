package com.entropyteam.entropay.employees.controllers;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.entropyteam.entropay.common.BaseController;
import com.entropyteam.entropay.employees.dtos.ClientDto;
import com.entropyteam.entropay.employees.services.ClientService;

@RestController
@CrossOrigin
@RequestMapping(value = "/clients", produces = MediaType.APPLICATION_JSON_VALUE)
public class ClientController extends BaseController<ClientDto, UUID> {

    @Autowired
    public ClientController(ClientService clientService) {
        super(clientService);
    }
}
