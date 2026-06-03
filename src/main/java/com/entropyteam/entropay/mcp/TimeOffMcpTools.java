package com.entropyteam.entropay.mcp;

import java.time.LocalDate;
import java.util.List;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;
import com.entropyteam.entropay.employees.dtos.PtoDto;
import com.entropyteam.entropay.mcp.dtos.VacationBalance;

/**
 * Spring AI tool surface for the Time off domain. Each method is a thin delegate to
 * {@link TimeOffQueryService}; the role gating and data shaping live there.
 */
@Service
public class TimeOffMcpTools {

    private final TimeOffQueryService queryService;

    public TimeOffMcpTools(TimeOffQueryService queryService) {
        this.queryService = queryService;
    }

    @Tool(name = "get_vacation_balance",
            description = "Current vacation balance for an Entroteam employee, broken down by year. "
                    + "Returns the same numbers the admin UI shows on the employee's time-off screen.")
    public VacationBalance getVacationBalance(
            @ToolParam(description = "Employee internal ID (e.g. 'INT-42') to fetch the vacation balance for.")
            String internalId) {
        return queryService.getVacationBalance(internalId);
    }

    @Tool(name = "list_employee_ptos",
            description = "PTO history for a given Entroteam employee, optionally filtered by date "
                    + "range (inclusive overlap) and leave type name (case-insensitive). Returns all "
                    + "statuses (approved, pending, rejected), sorted from most recent start date.")
    public List<PtoDto> listEmployeePtos(
            @ToolParam(description = "Employee internal ID (e.g. 'INT-42') to list PTOs for.")
            String internalId,
            @ToolParam(required = false, description = "Earliest start date to include (yyyy-MM-dd). Optional.")
            LocalDate startDate,
            @ToolParam(required = false, description = "Latest end date to include (yyyy-MM-dd). Optional.")
            LocalDate endDate,
            @ToolParam(required = false, description = "Leave type name to filter by, e.g. 'Vacation'. Optional.")
            String leaveType) {
        return queryService.listEmployeePtos(internalId, startDate, endDate, leaveType);
    }

    @Tool(name = "list_upcoming_ptos",
            description = "Approved PTOs across all Entroteam employees that overlap the given date "
                    + "range. Defaults to today through the next 30 days when no dates are provided. "
                    + "Sorted by start date ascending.")
    public List<PtoDto> listUpcomingPtos(
            @ToolParam(required = false, description = "Window start date (yyyy-MM-dd). Defaults to today.")
            LocalDate startDate,
            @ToolParam(required = false, description = "Window end date (yyyy-MM-dd). Defaults to startDate + 30 days.")
            LocalDate endDate) {
        return queryService.listUpcomingPtos(startDate, endDate);
    }
}
