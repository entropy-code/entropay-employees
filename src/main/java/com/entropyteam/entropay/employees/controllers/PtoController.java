package com.entropyteam.entropay.employees.controllers;

import java.util.UUID;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.entropyteam.entropay.common.BaseController;
import com.entropyteam.entropay.common.CrudService;
import com.entropyteam.entropay.employees.dtos.PtoDto;

@RestController
@CrossOrigin
@RequestMapping(value = "/ptos", produces = MediaType.APPLICATION_JSON_VALUE)
public class PtoController extends BaseController<PtoDto, UUID> {

    public PtoController(CrudService<PtoDto, UUID> crudService) {
        super(crudService);
    }
}
