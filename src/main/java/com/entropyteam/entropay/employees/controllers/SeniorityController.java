package com.entropyteam.entropay.employees.controllers;

import java.util.UUID;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.entropyteam.entropay.common.BaseController;
import com.entropyteam.entropay.employees.dtos.SeniorityDto;
import com.entropyteam.entropay.employees.services.SeniorityService;

@RestController
@CrossOrigin
@RequestMapping(value = "/seniorities", produces = MediaType.APPLICATION_JSON_VALUE)
public class SeniorityController extends BaseController<SeniorityDto, UUID> {

    public SeniorityController(SeniorityService seniorityService) {
        super(seniorityService);
    }
}
