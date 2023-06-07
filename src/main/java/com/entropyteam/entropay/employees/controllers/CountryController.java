package com.entropyteam.entropay.employees.controllers;

import static com.entropyteam.entropay.auth.AuthConstants.ROLE_ADMIN;

import java.util.UUID;

import com.entropyteam.entropay.employees.dtos.CountryDto;
import com.entropyteam.entropay.employees.services.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.entropyteam.entropay.common.BaseController;

@RestController
@CrossOrigin
@Secured({ROLE_ADMIN})
@RequestMapping(value = "/countries", produces = MediaType.APPLICATION_JSON_VALUE)
public class CountryController extends BaseController<CountryDto, UUID> {

    @Autowired
    public CountryController(CountryService countryService) {
        super(countryService);
    }

}