package com.entropyteam.entropay.employees.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.entropyteam.entropay.common.BaseController;
import com.entropyteam.entropay.employees.dtos.EmployeeDto;
import com.entropyteam.entropay.employees.services.EmployeeService;

@RestController
@CrossOrigin
@RequestMapping(value = "/employees", produces = MediaType.APPLICATION_JSON_VALUE)
public class EmployeeController extends BaseController<EmployeeDto, UUID> {
    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        super(employeeService);
    }
}
