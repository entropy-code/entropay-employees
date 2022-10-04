package com.entropyteam.entropay.employees.controllers;

import java.util.UUID;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.entropyteam.entropay.common.BaseController;
import com.entropyteam.entropay.employees.dtos.PositionDto;
import com.entropyteam.entropay.employees.services.PositionService;

@RestController
@CrossOrigin
@RequestMapping(value = "/positions", produces = MediaType.APPLICATION_JSON_VALUE)
public class PositionController extends BaseController<PositionDto, UUID> {

    public PositionController(PositionService positionService) {
        super(positionService);
    }
}

