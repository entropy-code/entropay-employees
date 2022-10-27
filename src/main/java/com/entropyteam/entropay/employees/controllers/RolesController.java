package com.entropyteam.entropay.employees.controllers;

import java.util.UUID;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.entropyteam.entropay.common.BaseController;
import com.entropyteam.entropay.employees.dtos.RoleDto;
import com.entropyteam.entropay.employees.services.RoleService;

@RestController
@CrossOrigin
@RequestMapping(value = "/roles", produces = MediaType.APPLICATION_JSON_VALUE)
public class RolesController extends BaseController<RoleDto, UUID> {

    public RolesController(RoleService roleService) {
        super(roleService);
    }
}

