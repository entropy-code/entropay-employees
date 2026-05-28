package com.entropyteam.entropay.mcp;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;
import com.entropyteam.entropay.mcp.dtos.ReimbursementEntry;

/**
 * Spring AI tool surface for the Reimbursements domain.
 */
@Service
public class ReimbursementMcpTools {

    private final ReimbursementQueryService queryService;

    public ReimbursementMcpTools(ReimbursementQueryService queryService) {
        this.queryService = queryService;
    }

    @Tool(name = "list_reimbursements",
            description = "Reimbursements filterable by employee and by date range (inclusive). When "
                    + "no date range is given, defaults to the current year to date. When no employee "
                    + "is given, returns reimbursements across the company. Sorted from most recent.")
    public List<ReimbursementEntry> listReimbursements(
            @ToolParam(required = false, description = "Employee UUID to filter by. Optional; when omitted, returns company-wide.")
            UUID employeeId,
            @ToolParam(required = false, description = "Earliest reimbursement date (yyyy-MM-dd). Defaults to first of current year.")
            LocalDate startDate,
            @ToolParam(required = false, description = "Latest reimbursement date (yyyy-MM-dd). Defaults to today.")
            LocalDate endDate) {
        return queryService.listReimbursements(employeeId, startDate, endDate);
    }
}
