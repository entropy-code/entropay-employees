package com.entropyteam.entropay.employees.controllers;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.entropyteam.entropay.common.BaseController;
import com.entropyteam.entropay.employees.dtos.ConfigDto;
import com.entropyteam.entropay.employees.services.ConfigService;


@RestController
@CrossOrigin
@RequestMapping(value = "/config", produces = MediaType.APPLICATION_JSON_VALUE)
public class ConfigController extends BaseController<ConfigDto, UUID> {

    @Autowired
    public ConfigController(ConfigService configService){
        super(configService);
    }

}
