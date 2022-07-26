package com.entropyteam.entropay.employees.project.controllers;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.entropyteam.entropay.employees.common.BaseController;
import com.entropyteam.entropay.employees.project.dtos.ProjectDto;
import com.entropyteam.entropay.employees.project.services.ProjectService;

@RestController
@CrossOrigin
@RequestMapping(value = "/projects", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProjectController extends BaseController<ProjectDto, UUID> {

    @Autowired
    public ProjectController(ProjectService projectService) {
        super(projectService);
    }

}
