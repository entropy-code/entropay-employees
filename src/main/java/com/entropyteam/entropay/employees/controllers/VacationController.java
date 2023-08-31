package com.entropyteam.entropay.employees.controllers;

import com.entropyteam.entropay.common.BaseController;
import com.entropyteam.entropay.employees.dtos.VacationDto;
import com.entropyteam.entropay.employees.jobs.VacationJob;
import com.entropyteam.entropay.employees.services.VacationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.UUID;

import static com.entropyteam.entropay.auth.AuthConstants.ROLE_ADMIN;
import static com.entropyteam.entropay.auth.AuthConstants.ROLE_MANAGER_HR;
import static com.entropyteam.entropay.auth.AuthConstants.ROLE_DEVELOPMENT;

@RestController
@CrossOrigin
@Secured({ROLE_MANAGER_HR,ROLE_ADMIN,ROLE_DEVELOPMENT})
@RequestMapping(value = "/vacations", produces = MediaType.APPLICATION_JSON_VALUE)
public class VacationController extends BaseController<VacationDto, UUID> {

    private final VacationJob vacationJob;

    @Autowired
    public VacationController(VacationService vacationService, VacationJob vacationJob) {
        super(vacationService);
        this.vacationJob = vacationJob;
    }

    @GetMapping("/job")
    @Secured({ROLE_ADMIN})
    public ResponseEntity<String> VacationJobTest() {
        try {
            vacationJob.setEmployeeVacations();
            return ResponseEntity.ok("Successful job execute");
        } catch (IOException exception) {
            String errorMessage = "An error occurred: " + exception.getMessage();
            return ResponseEntity.badRequest().body(errorMessage);
        }
    }

}
