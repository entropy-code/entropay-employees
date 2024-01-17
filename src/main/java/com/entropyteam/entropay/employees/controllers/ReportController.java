package com.entropyteam.entropay.employees.controllers;

import com.entropyteam.entropay.common.BaseController;
import com.entropyteam.entropay.common.ReactAdminParams;
import com.entropyteam.entropay.employees.dtos.EmployeeReportDto;
import com.entropyteam.entropay.employees.dtos.PtoReportDetailDto;
import com.entropyteam.entropay.employees.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.entropyteam.entropay.auth.AuthConstants.ROLE_ADMIN;
import static com.entropyteam.entropay.auth.AuthConstants.ROLE_HR_DIRECTOR;
import static com.entropyteam.entropay.auth.AuthConstants.ROLE_MANAGER_HR;


@RestController
@CrossOrigin
@RequestMapping(value = "/reports", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/employees")
    @Secured({ROLE_ADMIN, ROLE_MANAGER_HR, ROLE_HR_DIRECTOR})
    @Transactional
    public ResponseEntity<List<EmployeeReportDto>> getEmployeesReport(ReactAdminParams params) {
        Page<EmployeeReportDto> response = reportService.getEmployeesReport(params);
        return ResponseEntity.ok()
                .header(BaseController.X_TOTAL_COUNT, String.valueOf(response.getTotalElements()))
                .body(response.getContent());
    }

    @GetMapping("/ptos")
    @Secured({ROLE_ADMIN, ROLE_MANAGER_HR, ROLE_HR_DIRECTOR})
    @Transactional
    public ResponseEntity<List<PtoReportDetailDto>> getPtosReportDetail(ReactAdminParams params) {
        Page<PtoReportDetailDto> response = reportService.getPtoReportDetail(params);
        return ResponseEntity.ok()
                .header(BaseController.X_TOTAL_COUNT, String.valueOf(response.getTotalElements()))
                .body(response.getContent());
    }
}
