package com.entropyteam.entropay.mcp;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;
import com.entropyteam.entropay.employees.dtos.PtoDto;
import com.entropyteam.entropay.mcp.dtos.EmployeeDetail;
import com.entropyteam.entropay.mcp.dtos.PayrollSummary;
import com.entropyteam.entropay.mcp.dtos.ReimbursementEntry;
import com.entropyteam.entropay.mcp.dtos.RosterEntry;
import com.entropyteam.entropay.mcp.dtos.VacationBalance;

/**
 * Read-only MCP tool surface over the Entropy employee payroll. Each method is an MCP
 * tool; the queries and role checks live in {@link PayrollQueryService}. This class is
 * intentionally free of AOP annotations so Spring AI can scan it for {@code @Tool} methods
 * without a CGLIB proxy hiding them.
 */
@Service
public class PayrollMcpTools {

    private final PayrollQueryService payrollQueryService;

    public PayrollMcpTools(PayrollQueryService payrollQueryService) {
        this.payrollQueryService = payrollQueryService;
    }

    @Tool(name = "list_payroll_roster",
            description = "List active employees in the Entropy payroll: name, role, project, client, "
                    + "country and tenure. Use the optional search to filter by name or internal id. "
                    + "Does not include salary or personal data.")
    public List<RosterEntry> listPayrollRoster(
            @ToolParam(required = false,
                    description = "Case-insensitive filter on first name, last name or internal id")
            String search,
            @ToolParam(required = false, description = "Maximum entries to return (default 50, max 200)")
            Integer limit) {
        return payrollQueryService.listPayrollRoster(search, limit);
    }

    @Tool(name = "get_employee",
            description = "Get full detail for a single employee by their UUID, including role, "
                    + "assignment, tenure and compensation (monthly salary in USD, billable rate, margin).")
    public EmployeeDetail getEmployee(
            @ToolParam(description = "Employee UUID, as returned by list_payroll_roster") String employeeId) {
        return payrollQueryService.getEmployee(parseUuid(employeeId));
    }

    @Tool(name = "list_employee_ptos",
            description = "List approved PTO / leave records for an employee in a given calendar year.")
    public List<PtoDto> listEmployeePtos(
            @ToolParam(description = "Employee UUID") String employeeId,
            @ToolParam(required = false, description = "Calendar year, e.g. 2026 (default: current year)")
            Integer year) {
        return payrollQueryService.listEmployeePtos(parseUuid(employeeId), year);
    }

    @Tool(name = "get_vacation_balance",
            description = "Get an employee's vacation-day balance broken down by year, plus total "
                    + "available days.")
    public VacationBalance getVacationBalance(
            @ToolParam(description = "Employee UUID") String employeeId) {
        return payrollQueryService.getVacationBalance(parseUuid(employeeId));
    }

    @Tool(name = "list_employee_reimbursements",
            description = "List expense reimbursements for an employee within a date range.")
    public List<ReimbursementEntry> listEmployeeReimbursements(
            @ToolParam(description = "Employee UUID") String employeeId,
            @ToolParam(required = false,
                    description = "Start date ISO yyyy-MM-dd (default: Jan 1 of current year)")
            String startDate,
            @ToolParam(required = false, description = "End date ISO yyyy-MM-dd (default: today)")
            String endDate) {
        return payrollQueryService.listEmployeeReimbursements(
                parseUuid(employeeId), parseDate(startDate), parseDate(endDate));
    }

    @Tool(name = "get_payroll_summary",
            description = "Aggregate total monthly payroll cost in USD across active employees, with a "
                    + "per-country breakdown. Optionally filter by country or client name.")
    public PayrollSummary getPayrollSummary(
            @ToolParam(required = false, description = "Optional country name filter") String country,
            @ToolParam(required = false, description = "Optional client name filter") String client) {
        return payrollQueryService.getPayrollSummary(country, client);
    }

    private static UUID parseUuid(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("employeeId is required");
        }
        try {
            return UUID.fromString(value.trim());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid employee UUID: " + value);
        }
    }

    private static LocalDate parseDate(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(value.trim());
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date (expected yyyy-MM-dd): " + value);
        }
    }
}
