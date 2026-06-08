package com.entropyteam.entropay.employees.controllers;

import static com.entropyteam.entropay.auth.AuthConstants.ROLE_ADMIN;
import static com.entropyteam.entropay.auth.AuthConstants.ROLE_MANAGER_HR;

import java.util.UUID;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.entropyteam.entropay.common.BaseController;
import com.entropyteam.entropay.employees.dtos.EducationLevelDto;
import com.entropyteam.entropay.employees.services.EducationLevelService;

@RestController
@CrossOrigin
@Secured({ROLE_ADMIN, ROLE_MANAGER_HR})
@RequestMapping(value = "/education-levels", produces = MediaType.APPLICATION_JSON_VALUE)
public class EducationLevelController extends BaseController<EducationLevelDto, UUID> {

    public EducationLevelController(EducationLevelService educationLevelService) {
        super(educationLevelService);
    }
}
