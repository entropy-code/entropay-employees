package com.entropyteam.entropay.mcp;

import java.time.LocalDate;
import java.util.List;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;
import com.entropyteam.entropay.employees.dtos.SalariesReportDto;
import com.entropyteam.entropay.employees.dtos.TurnoverReportDto;
import com.entropyteam.entropay.employees.services.BillingService.BillingDto;
import com.entropyteam.entropay.employees.services.MarginService.MarginDto;

/**
 * Spring AI tool surface for the Reports domain. Each method is a thin delegate to
 * {@link ReportsQueryService}; role gating and parameter translation live there.
 */
@Service
public class ReportsMcpTools {

    private final ReportsQueryService queryService;

    public ReportsMcpTools(ReportsQueryService queryService) {
        this.queryService = queryService;
    }

    @Tool(name = "get_turnover_report",
            description = "Hierarchical turnover report (company → client → project) for the given "
                    + "period. Returns the same numbers the admin UI shows on the /reports/turnover screen, "
                    + "including monthly breakdowns.")
    public TurnoverReportDto getTurnoverReport(
            @ToolParam(description = "Period start date (yyyy-MM-dd).")
            LocalDate startDate,
            @ToolParam(description = "Period end date (yyyy-MM-dd).")
            LocalDate endDate) {
        return queryService.getTurnoverReport(startDate, endDate);
    }

    @Tool(name = "get_billing_report",
            description = "Billing report for the given period, optionally filtered by client name "
                    + "(case-insensitive substring match). Returns one entry per billable assignment, "
                    + "including rate and total. Rate and total are masked for non-admin callers viewing "
                    + "internal employees — but this tool is ADMIN-only in the first place.")
    public List<BillingDto> getBillingReport(
            @ToolParam(description = "Period start date (yyyy-MM-dd).")
            LocalDate startDate,
            @ToolParam(description = "Period end date (yyyy-MM-dd).")
            LocalDate endDate,
            @ToolParam(required = false, description = "Client name (substring, case-insensitive) to filter by. Optional.")
            String clientName) {
        return queryService.getBillingReport(startDate, endDate, clientName);
    }

    @Tool(name = "get_margin_report",
            description = "Margin report for the given period, broken down per employee per month. "
                    + "Includes rate, total billed, salary paid, and margin. ADMIN-only.")
    public List<MarginDto> getMarginReport(
            @ToolParam(description = "Period start date (yyyy-MM-dd).")
            LocalDate startDate,
            @ToolParam(description = "Period end date (yyyy-MM-dd).")
            LocalDate endDate) {
        return queryService.getMarginReport(startDate, endDate);
    }

    @Tool(name = "get_salaries_report",
            description = "Current salaries report — one entry per active employee with their current "
                    + "salary, client and modality. Salary is masked for non-admin callers viewing "
                    + "internal employees. The salaries report itself is a current snapshot and does "
                    + "not take a date range.")
    public List<SalariesReportDto> getSalariesReport() {
        return queryService.getSalariesReport();
    }
}
