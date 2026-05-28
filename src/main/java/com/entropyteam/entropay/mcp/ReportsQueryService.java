package com.entropyteam.entropay.mcp;

import static com.entropyteam.entropay.auth.AuthConstants.ROLE_ADMIN;
import static com.entropyteam.entropay.auth.AuthConstants.ROLE_HR_DIRECTOR;
import static com.entropyteam.entropay.auth.AuthConstants.ROLE_MANAGER_HR;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.entropyteam.entropay.common.ReactAdminParams;
import com.entropyteam.entropay.employees.dtos.SalariesReportDto;
import com.entropyteam.entropay.employees.dtos.TurnoverReportDto;
import com.entropyteam.entropay.employees.services.BillingService;
import com.entropyteam.entropay.employees.services.BillingService.BillingDto;
import com.entropyteam.entropay.employees.services.MarginService;
import com.entropyteam.entropay.employees.services.MarginService.MarginDto;
import com.entropyteam.entropay.employees.services.ReportService;
import com.entropyteam.entropay.employees.services.TurnoverService;

/**
 * Read-only Reports queries backing the MCP tools. Role gates mirror
 * {@code ReportController}: the salaries and turnover reports are open to ADMIN,
 * MANAGER_HR and HR_DIRECTOR; the billing and margin reports are ADMIN-only — matching
 * exactly how the UI gates the corresponding screens.
 *
 * <p>The underlying services accept a {@link ReactAdminParams} whose {@code filter} field
 * is a JSON object with {@code startDate}/{@code endDate} keys. This service translates
 * the MCP-friendly parameters into that JSON shape so MCP callers don't have to know about
 * the admin-ui's filter wire format.
 */
@Service
public class ReportsQueryService {

    private final ReportService reportService;
    private final BillingService billingService;
    private final MarginService marginService;
    private final TurnoverService turnoverService;

    public ReportsQueryService(ReportService reportService, BillingService billingService,
            MarginService marginService, TurnoverService turnoverService) {
        this.reportService = reportService;
        this.billingService = billingService;
        this.marginService = marginService;
        this.turnoverService = turnoverService;
    }

    @Secured({ROLE_ADMIN, ROLE_MANAGER_HR, ROLE_HR_DIRECTOR})
    @Transactional(readOnly = true)
    public TurnoverReportDto getTurnoverReport(LocalDate startDate, LocalDate endDate) {
        validateRange(startDate, endDate);
        return turnoverService.generateHierarchicalTurnoverReport(buildParams(startDate, endDate));
    }

    @Secured({ROLE_ADMIN})
    @Transactional(readOnly = true)
    public List<BillingDto> getBillingReport(LocalDate startDate, LocalDate endDate, String clientName) {
        validateRange(startDate, endDate);
        List<BillingDto> rows = billingService.generateBillingReport(buildParams(startDate, endDate)).data();
        if (StringUtils.isBlank(clientName)) {
            return rows;
        }
        return rows.stream()
                .filter(row -> StringUtils.containsIgnoreCase(row.clientName(), clientName))
                .toList();
    }

    @Secured({ROLE_ADMIN})
    @Transactional(readOnly = true)
    public List<MarginDto> getMarginReport(LocalDate startDate, LocalDate endDate) {
        validateRange(startDate, endDate);
        return marginService.generateMarginReport(buildParams(startDate, endDate)).data();
    }

    @Secured({ROLE_ADMIN, ROLE_MANAGER_HR, ROLE_HR_DIRECTOR})
    @Transactional(readOnly = true)
    public List<SalariesReportDto> getSalariesReport() {
        return reportService.getSalariesReport(new ReactAdminParams()).data();
    }

    private static void validateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("startDate and endDate are required");
        }
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("endDate must not be before startDate");
        }
    }

    private static ReactAdminParams buildParams(LocalDate startDate, LocalDate endDate) {
        DateTimeFormatter iso = DateTimeFormatter.ISO_LOCAL_DATE;
        String filter = String.format("{\"startDate\":\"%s\",\"endDate\":\"%s\"}",
                startDate.format(iso), endDate.format(iso));
        return new ReactAdminParams(filter, null, null);
    }
}
