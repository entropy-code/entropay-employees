package com.entropyteam.entropay.employees.controllers;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.entropyteam.entropay.common.BaseController;
import com.entropyteam.entropay.employees.dtos.ContractDto;
import com.entropyteam.entropay.employees.services.ContractService;

@RestController
@CrossOrigin
@RequestMapping(value = "/contracts", produces = MediaType.APPLICATION_JSON_VALUE)
public class ContractController extends BaseController<ContractDto, UUID> {

    @Autowired
    public ContractController(ContractService contractService) {
        super(contractService);
    }

}
