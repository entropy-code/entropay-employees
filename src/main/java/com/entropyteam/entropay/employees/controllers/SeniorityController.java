package com.entropyteam.entropay.employees.controllers;

import static com.entropyteam.entropay.auth.AuthConstants.ROLE_ADMIN;
import static com.entropyteam.entropay.auth.AuthConstants.ROLE_DEVELOPMENT;

import java.util.UUID;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.entropyteam.entropay.common.BaseController;
import com.entropyteam.entropay.employees.dtos.SeniorityDto;
import com.entropyteam.entropay.employees.services.SeniorityService;

@RestController
@CrossOrigin
@Secured({ROLE_ADMIN, ROLE_DEVELOPMENT})
@RequestMapping(value = "/seniorities", produces = MediaType.APPLICATION_JSON_VALUE)
public class SeniorityController extends BaseController<SeniorityDto, UUID> {

    public SeniorityController(SeniorityService seniorityService) {
        super(seniorityService);
    }
}
