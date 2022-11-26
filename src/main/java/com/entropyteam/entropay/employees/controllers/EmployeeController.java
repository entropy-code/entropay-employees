package com.entropyteam.entropay.employees.controllers;

import java.util.UUID;

import com.entropyteam.entropay.employees.dtos.ContractDto;
import com.entropyteam.entropay.employees.services.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
