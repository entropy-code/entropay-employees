package com.entropyteam.entropay.employees.controllers;

import static com.entropyteam.entropay.auth.AuthConstants.ROLE_ADMIN;
import static com.entropyteam.entropay.auth.AuthConstants.ROLE_DEVELOPMENT;
import static com.entropyteam.entropay.auth.AuthConstants.ROLE_HR_DIRECTOR;
import static com.entropyteam.entropay.auth.AuthConstants.ROLE_MANAGER_HR;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.entropyteam.entropay.common.BaseController;
import com.entropyteam.entropay.common.ReactAdminParams;
import com.entropyteam.entropay.employees.dtos.EmployeeReportDto;
import com.entropyteam.entropay.employees.dtos.TurnoverEntryDto;
import com.entropyteam.entropay.employees.dtos.TurnoverReportDto;
import com.entropyteam.entropay.employees.dtos.PtoReportClientDto;
import com.entropyteam.entropay.employees.dtos.PtoReportDetailDto;
import com.entropyteam.entropay.employees.dtos.PtoReportEmployeeDto;
import com.entropyteam.entropay.employees.dtos.ReportDto;
import com.entropyteam.entropay.employees.dtos.SalariesReportDto;
import com.entropyteam.entropay.employees.services.BillingService;
import com.entropyteam.entropay.employees.services.BillingService.BillingDto;
import com.entropyteam.entropay.employees.services.MarginService;
import com.entropyteam.entropay.employees.services.MarginService.MarginDto;
import com.entropyteam.entropay.employees.services.ReportService;
import com.entropyteam.entropay.employees.services.TurnoverService;


@RestController
@CrossOrigin
@RequestMapping(value = "/reports", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReportController {

    private final ReportService reportService;
    private final BillingService billingService;
    private final MarginService marginService;
    private final TurnoverService turnoverService;

    public ReportController(ReportService reportService, BillingService billingService, MarginService marginService, TurnoverService turnoverService) {
        this.reportService = reportService;
        this.billingService = billingService;
        this.marginService = marginService;
        this.turnoverService = turnoverService;
    }

    @GetMapping("/employees")
    @Secured({ROLE_ADMIN, ROLE_MANAGER_HR, ROLE_HR_DIRECTOR})
    @Transactional
    public ResponseEntity<List<EmployeeReportDto>> getEmployeesReport(ReactAdminParams params) {
        Page<EmployeeReportDto> response = reportService.getEmployeesReport(params);
        return ResponseEntity.ok()
                .header(BaseController.X_TOTAL_COUNT, String.valueOf(response.getTotalElements()))
                .body(response.getContent());
    }

    @GetMapping("/ptos/details")
    @Secured({ROLE_ADMIN, ROLE_MANAGER_HR, ROLE_HR_DIRECTOR, ROLE_DEVELOPMENT})
    @Transactional
    public ResponseEntity<List<PtoReportDetailDto>> getPtosReportDetail(ReactAdminParams params) {
        Page<PtoReportDetailDto> response = reportService.getPtoReportDetail(params);
        return ResponseEntity.ok()
                .header(BaseController.X_TOTAL_COUNT, String.valueOf(response.getTotalElements()))
                .body(response.getContent());
    }

    @GetMapping("/ptos/all-details")
    @Secured({ROLE_ADMIN, ROLE_MANAGER_HR, ROLE_HR_DIRECTOR, ROLE_DEVELOPMENT})
    @Transactional
    public ResponseEntity<List<PtoReportDetailDto>> getPtosReportAllDetails(ReactAdminParams params) {
        Page<PtoReportDetailDto> response = reportService.getPtoReportAllDetails(params);
        return ResponseEntity.ok()
                .header(BaseController.X_TOTAL_COUNT, String.valueOf(response.getTotalElements()))
                .body(response.getContent());
    }

    @GetMapping("/ptos/employees")
    @Secured({ROLE_ADMIN, ROLE_MANAGER_HR, ROLE_HR_DIRECTOR, ROLE_DEVELOPMENT})
    @Transactional
    public ResponseEntity<List<PtoReportEmployeeDto>> getPtosByEmployeesReport(ReactAdminParams params) {
        Page<PtoReportEmployeeDto> response = reportService.getPtosReportByEmployees(params);
        return ResponseEntity.ok()
                .header(BaseController.X_TOTAL_COUNT, String.valueOf(response.getTotalElements()))
                .body(response.getContent());
    }

    @GetMapping("/ptos/clients")
    @Secured({ROLE_ADMIN, ROLE_MANAGER_HR, ROLE_HR_DIRECTOR, ROLE_DEVELOPMENT})
    @Transactional
    public ResponseEntity<List<PtoReportClientDto>> getPtosByClientsReport(ReactAdminParams params) {
        Page<PtoReportClientDto> response = reportService.getPtosReportByClients(params);
        return ResponseEntity.ok()
                .header(BaseController.X_TOTAL_COUNT, String.valueOf(response.getTotalElements()))
                .body(response.getContent());
    }

    @GetMapping("/salaries")
    @Secured({ROLE_ADMIN, ROLE_MANAGER_HR, ROLE_HR_DIRECTOR})
    @Transactional
    public ResponseEntity<List<SalariesReportDto>> getSalariesReport(ReactAdminParams params) {
        Page<SalariesReportDto> response = reportService.getSalariesReport(params);
        return ResponseEntity.ok()
                .header(BaseController.X_TOTAL_COUNT, String.valueOf(response.getTotalElements()))
                .body(response.getContent());
    }


    @GetMapping("/billing")
    @Secured({ROLE_ADMIN})
    @Transactional(readOnly = true)
    public ResponseEntity<List<BillingDto>> getBillingReport(ReactAdminParams params) {
        ReportDto<BillingDto> report = billingService.generateBillingReport(params);
        return ResponseEntity.ok()
                .header(BaseController.X_TOTAL_COUNT, String.valueOf(report.size()))
                .body(report.data());
    }

    @GetMapping("/margin")
    @Secured({ROLE_ADMIN})
    @Transactional(readOnly = true)
    public ResponseEntity<List<MarginDto>> getMarginList(ReactAdminParams params) {
        ReportDto<MarginDto> report = marginService.generateMarginReport(params);
        return ResponseEntity.ok()
                .header(BaseController.X_TOTAL_COUNT, String.valueOf(report.size()))
                .body(report.data());
    }

    @GetMapping("/turnover")
    @Secured({ROLE_ADMIN, ROLE_MANAGER_HR, ROLE_HR_DIRECTOR})
    @Transactional(readOnly = true)
    public ResponseEntity<TurnoverReportDto> getTurnoverReport(ReactAdminParams params) {
        TurnoverReportDto report = turnoverService.generateHierarchicalTurnoverReport(params);
        return ResponseEntity.ok(report);
    }

    /**
     * Returns a flat turnover report for clients and projects.
     * This endpoint flattens the hierarchical structure of the turnover report into a list of entries.
     *
     * @param params The parameters for filtering and pagination
     * @return A flat turnover report
     */
    @GetMapping("/turnover/flat")
    @Secured({ROLE_ADMIN, ROLE_MANAGER_HR, ROLE_HR_DIRECTOR})
    @Transactional(readOnly = true)
    public ResponseEntity<List<TurnoverEntryDto>> getFlatTurnoverReport(ReactAdminParams params) {
        ReportDto<TurnoverEntryDto> report = turnoverService.generateFlatTurnoverReport(params);
        return ResponseEntity.ok()
                .header(BaseController.X_TOTAL_COUNT, String.valueOf(report.size()))
                .body(report.data());

    }
}
