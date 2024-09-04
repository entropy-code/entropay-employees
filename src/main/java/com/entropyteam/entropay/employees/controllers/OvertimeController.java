package com.entropyteam.entropay.employees.controllers;

import static com.entropyteam.entropay.auth.AuthConstants.ROLE_ADMIN;
import static com.entropyteam.entropay.auth.AuthConstants.ROLE_DEVELOPMENT;
import static com.entropyteam.entropay.auth.AuthConstants.ROLE_HR_DIRECTOR;
import static com.entropyteam.entropay.auth.AuthConstants.ROLE_MANAGER_HR;

import com.entropyteam.entropay.common.BaseController;
import com.entropyteam.entropay.employees.dtos.OvertimeDto;
import com.entropyteam.entropay.employees.services.OvertimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping(value = "/overtimes", produces = MediaType.APPLICATION_JSON_VALUE)
@Secured({ROLE_ADMIN, ROLE_HR_DIRECTOR, ROLE_MANAGER_HR, ROLE_DEVELOPMENT})

public class OvertimeController extends BaseController<OvertimeDto, UUID> {

    @Autowired
    public OvertimeController(OvertimeService overtimeService) {
        super(overtimeService);
    }


}
