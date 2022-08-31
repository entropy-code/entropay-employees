package com.entropyteam.entropay.employees.controllers;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.entropyteam.entropay.common.BaseController;
import com.entropyteam.entropay.employees.dtos.CompanyDto;
import com.entropyteam.entropay.employees.services.CompanyService;

@RestController
@CrossOrigin
@RequestMapping(value = "/companies", produces = MediaType.APPLICATION_JSON_VALUE)
public class CompanyController extends BaseController<CompanyDto, UUID> {

    @Autowired
    public CompanyController(CompanyService companyService) {
        super(companyService);
    }
}
