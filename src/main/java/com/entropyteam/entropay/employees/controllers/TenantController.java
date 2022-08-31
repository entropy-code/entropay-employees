package com.entropyteam.entropay.employees.controllers;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.entropyteam.entropay.common.BaseController;
import com.entropyteam.entropay.employees.dtos.TenantDto;
import com.entropyteam.entropay.employees.services.TenantService;

@RestController
@CrossOrigin
@RequestMapping(value = "/tenants", produces = MediaType.APPLICATION_JSON_VALUE)
public class TenantController extends BaseController<TenantDto, UUID> {

    @Autowired
    public TenantController(TenantService tenantService) {
        super(tenantService);
    }
}
