package com.entropyteam.entropay.employees.controllers;

import static com.entropyteam.entropay.auth.AuthConstants.ROLE_ADMIN;
import static com.entropyteam.entropay.auth.AuthConstants.ROLE_DEVELOPMENT;
import static com.entropyteam.entropay.auth.AuthConstants.ROLE_MANAGER_HR;

import java.util.UUID;

import com.entropyteam.entropay.employees.services.PtoService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import com.entropyteam.entropay.common.BaseController;
import com.entropyteam.entropay.common.CrudService;
import com.entropyteam.entropay.employees.dtos.PtoDto;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@CrossOrigin
@Secured({ROLE_MANAGER_HR, ROLE_ADMIN, ROLE_DEVELOPMENT})
@RequestMapping(value = "/ptos", produces = MediaType.APPLICATION_JSON_VALUE)
public class PtoController extends BaseController<PtoDto, UUID> {

    private final PtoService ptoService;

    public PtoController(CrudService<PtoDto, UUID> crudService, PtoService ptoService) {
        super(crudService);
        this.ptoService = ptoService;
    }

    @PostMapping("/{id}/cancel")
    @Secured({ROLE_MANAGER_HR, ROLE_ADMIN, ROLE_DEVELOPMENT})
    public ResponseEntity<PtoDto> cancelStatus(@PathVariable UUID id, @RequestBody PtoDto ptoDto) {
        return ResponseEntity.ok(ptoService.cancelPto(id, ptoDto));
    }
}
