package com.entropyteam.entropay.employees.controllers;

import static com.entropyteam.entropay.auth.AuthConstants.ROLE_ADMIN;
import static com.entropyteam.entropay.auth.AuthConstants.ROLE_DEVELOPMENT;
import static com.entropyteam.entropay.auth.AuthConstants.ROLE_MANAGER_HR;

import java.util.UUID;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.entropyteam.entropay.common.BaseController;
import com.entropyteam.entropay.common.CrudService;
import com.entropyteam.entropay.employees.dtos.PtoDto;

@RestController
@CrossOrigin
@Secured({ROLE_MANAGER_HR, ROLE_ADMIN, ROLE_DEVELOPMENT})
@RequestMapping(value = "/ptos", produces = MediaType.APPLICATION_JSON_VALUE)
public class PtoController extends BaseController<PtoDto, UUID> {

    public PtoController(CrudService<PtoDto, UUID> crudService) {
        super(crudService);
    }
}
