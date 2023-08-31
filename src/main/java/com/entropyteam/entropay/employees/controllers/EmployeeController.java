package com.entropyteam.entropay.employees.controllers;

import java.util.List;
import java.util.UUID;

import com.entropyteam.entropay.employees.dtos.EmployeeReportDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import com.entropyteam.entropay.common.BaseController;
import com.entropyteam.entropay.employees.dtos.EmployeeDto;
import com.entropyteam.entropay.employees.services.EmployeeService;

import static com.entropyteam.entropay.auth.AuthConstants.ROLE_ADMIN;

@RestController
@CrossOrigin
@RequestMapping(value = "/employees", produces = MediaType.APPLICATION_JSON_VALUE)
public class EmployeeController extends BaseController<EmployeeDto, UUID> {

    private EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        super(employeeService);
        this.employeeService = employeeService;
    }

    @GetMapping("/report")
    @Secured({ROLE_ADMIN})
    @Transactional
    public ResponseEntity<List<EmployeeReportDto>> getEmployeesReport() {
        Page<EmployeeReportDto> response = employeeService.getEmployeesReport();
        return ResponseEntity.ok()
                .header("X_TOTAL_COUNT", String.valueOf(response.getTotalElements()))
                .body(response.getContent());
    }

}
