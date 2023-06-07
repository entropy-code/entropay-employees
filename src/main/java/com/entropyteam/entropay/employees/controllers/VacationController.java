package com.entropyteam.entropay.employees.controllers;

import com.entropyteam.entropay.common.BaseController;
import com.entropyteam.entropay.employees.dtos.VacationDto;
import com.entropyteam.entropay.employees.services.VacationService;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.entropyteam.entropay.auth.AuthConstants.ROLE_MANAGER_HR;

@RestController
@CrossOrigin
@Secured({ROLE_MANAGER_HR})
@RequestMapping(value = "/vacations", produces = MediaType.APPLICATION_JSON_VALUE)
public class VacationController extends BaseController<VacationDto, UUID> {

    public VacationController(VacationService vacationService) {
        super(vacationService);
    }
}
