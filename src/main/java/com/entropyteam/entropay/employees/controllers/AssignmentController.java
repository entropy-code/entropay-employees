package com.entropyteam.entropay.employees.controllers;

import static com.entropyteam.entropay.auth.AuthConstants.ROLE_ADMIN;
import static com.entropyteam.entropay.auth.AuthConstants.ROLE_DEVELOPMENT;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.entropyteam.entropay.common.BaseController;
import com.entropyteam.entropay.employees.dtos.AssignmentDto;
import com.entropyteam.entropay.employees.jobs.AssignmentJob;
import com.entropyteam.entropay.employees.services.AssignmentService;

@RestController
@CrossOrigin
@RequestMapping(value = "/assignments", produces = MediaType.APPLICATION_JSON_VALUE)
public class AssignmentController extends BaseController<AssignmentDto, UUID> {

    private final AssignmentJob assignmentJob;

    @Autowired
    public AssignmentController(AssignmentService assignmentService, AssignmentJob assignmentJob) {
        super(assignmentService);
        this.assignmentJob = assignmentJob;
    }

    @GetMapping("/update-assignments-status")
    @Secured({ROLE_ADMIN, ROLE_DEVELOPMENT})
    public ResponseEntity<String> updateContractsStatus() {
        assignmentJob.updateAssignmentsStatus();
        return ResponseEntity.ok("Successful job execution: update assignments status");
    }
}
