package com.entropyteam.entropay.employees.project.controllers;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.entropyteam.entropay.employees.common.BaseController;
import com.entropyteam.entropay.employees.project.dtos.ProjectTypeDto;
import com.entropyteam.entropay.employees.project.services.ProjectTypeService;

@RestController
@CrossOrigin
@RequestMapping(value = "/project-types", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProjectTypeController extends BaseController<ProjectTypeDto, UUID> {

    @Autowired
    public ProjectTypeController(ProjectTypeService projectTypeService) {
        super(projectTypeService);
    }

}
