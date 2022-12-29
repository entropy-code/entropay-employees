package com.entropyteam.entropay.employees.controllers;

import static com.entropyteam.entropay.auth.AuthConstants.ROLE_ADMIN;
import static com.entropyteam.entropay.auth.AuthConstants.ROLE_DEVELOPMENT;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.entropyteam.entropay.common.BaseController;
import com.entropyteam.entropay.employees.dtos.ProjectTypeDto;
import com.entropyteam.entropay.employees.services.ProjectTypeService;

@RestController
@CrossOrigin
@Secured({ROLE_ADMIN, ROLE_DEVELOPMENT})
@RequestMapping(value = "/project-types", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProjectTypeController extends BaseController<ProjectTypeDto, UUID> {

    @Autowired
    public ProjectTypeController(ProjectTypeService projectTypeService) {
        super(projectTypeService);
    }

}
