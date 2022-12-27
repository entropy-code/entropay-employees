package com.entropyteam.entropay.employees.controllers;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.entropyteam.entropay.common.BaseController;
import com.entropyteam.entropay.employees.dtos.AssignmentDto;
import com.entropyteam.entropay.employees.services.AssignmentService;

@RestController
@CrossOrigin
@RequestMapping(value = "/assignments", produces = MediaType.APPLICATION_JSON_VALUE)
public class AssignmentController extends BaseController<AssignmentDto, UUID> {

    @Autowired
    public AssignmentController(AssignmentService assignmentService) { super(assignmentService); }
}
