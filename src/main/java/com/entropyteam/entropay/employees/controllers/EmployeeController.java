package com.entropyteam.entropay.employees.controllers;


import java.io.IOException;
import java.util.UUID;

import com.entropyteam.entropay.employees.jobs.EmployeeJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import com.entropyteam.entropay.common.BaseController;
import com.entropyteam.entropay.employees.dtos.EmployeeDto;
import com.entropyteam.entropay.employees.services.EmployeeService;

import static com.entropyteam.entropay.auth.AuthConstants.ROLE_ADMIN;
import static com.entropyteam.entropay.auth.AuthConstants.ROLE_DEVELOPMENT;


@RestController
@CrossOrigin
@RequestMapping(value = "/employees", produces = MediaType.APPLICATION_JSON_VALUE)
public class EmployeeController extends BaseController<EmployeeDto, UUID> {
    private final EmployeeJob employeeJob;

    @Autowired
    public EmployeeController(EmployeeService employeeService, EmployeeJob employeeJob) {
        super(employeeService);
        this.employeeJob = employeeJob;
    }

    @GetMapping("/sync-birthday")
    @Secured({ROLE_ADMIN, ROLE_DEVELOPMENT})
    @Transactional
    public ResponseEntity<String> birthdayEmployee() {
        try {
            employeeJob.createGoogleCalendarEventsForBirthdays();
            return ResponseEntity.ok("Successful job execute");
        } catch (IOException exception) {
            String errorMessage = "An error occurred: " + exception.getMessage();
            return ResponseEntity.badRequest().body(errorMessage);
        }
    }
}
