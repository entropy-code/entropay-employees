package com.entropyteam.entropay.mcp;

import java.util.List;
import java.util.UUID;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;
import com.entropyteam.entropay.employees.dtos.AssignmentDto;
import com.entropyteam.entropay.employees.dtos.EmployeeDto;
import com.entropyteam.entropay.employees.dtos.FeedbackDto;
import com.entropyteam.entropay.mcp.dtos.EmployeeSummary;

/**
 * Spring AI tool surface for the Employee 360 domain. Each method is a thin delegate to
 * {@link Employee360QueryService}; the role gating and the {@code @SensitiveInformation}
 * masking live in the query service and the DTOs respectively.
 */
@Service
public class Employee360McpTools {

    private final Employee360QueryService queryService;

    public Employee360McpTools(Employee360QueryService queryService) {
        this.queryService = queryService;
    }

    @Tool(name = "get_employee",
            description = "Look up the basic profile of a single Entroteam employee by UUID, "
                    + "internal ID (e.g. INT-001), or full/partial name. Returns the same shape the "
                    + "admin UI shows on the employee detail screen.")
    public EmployeeDto getEmployee(
            @ToolParam(description = "Employee UUID, internal ID, or full/partial name to look up.")
            String query) {
        return queryService.getEmployee(query);
    }

    @Tool(name = "get_employee_summary",
            description = "Holistic view of an Entroteam employee: profile, current rate and salary, "
                    + "active project and client, recent feedbacks, vacation balance, and latest "
                    + "reimbursements. Sensitive fields (rate, salary) are masked for non-admin callers "
                    + "viewing internal employees, mirroring the admin UI behaviour.")
    public EmployeeSummary getEmployeeSummary(
            @ToolParam(description = "Employee UUID, internal ID, or full/partial name to summarize.")
            String query) {
        return queryService.getEmployeeSummary(query);
    }

    @Tool(name = "list_employee_assignments",
            description = "Current and past project assignments for a given Entroteam employee, sorted "
                    + "from most recent start date. Billable rates are masked for non-admin callers "
                    + "viewing internal employees.")
    public List<AssignmentDto> listEmployeeAssignments(
            @ToolParam(description = "Employee UUID to list assignments for.")
            UUID employeeId) {
        return queryService.listEmployeeAssignments(employeeId);
    }

    @Tool(name = "list_employee_feedbacks",
            description = "Feedbacks recorded for a given Entroteam employee, sorted from most recent. "
                    + "Includes both internal and client feedback sources.")
    public List<FeedbackDto> listEmployeeFeedbacks(
            @ToolParam(description = "Employee UUID to list feedbacks for.")
            UUID employeeId) {
        return queryService.listEmployeeFeedbacks(employeeId);
    }
}
