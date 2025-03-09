package com.entropyteam.entropay.employees.services;

import java.math.BigDecimal;
import java.util.UUID;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.entropyteam.entropay.employees.models.Assignment;
import com.entropyteam.entropay.employees.models.Employee;
import com.entropyteam.entropay.employees.models.Overtime;
import com.entropyteam.entropay.employees.services.BillingService.BillingDto;

public class BillingEntry {

    private final Assignment assignment;
    private final double workedHours;
    private final double ptoHours;
    private final BigDecimal rate;
    private final BigDecimal total;
    private final String notes;

    public BillingEntry(Assignment assignment, double workingHours, Double ptoHours) {
        this.assignment = assignment;
        this.rate = assignment.getBillableRate();
        this.ptoHours = ptoHours;
        this.workedHours = workingHours - ptoHours;
        this.total = rate.multiply(BigDecimal.valueOf(this.workedHours));
        this.notes = "";
    }

    public BillingEntry(Overtime overtime) {
        this.assignment = overtime.getAssignment();
        this.rate = assignment.getBillableRate();
        this.ptoHours = 0;
        this.workedHours = overtime.getHours();
        this.total = rate.multiply(BigDecimal.valueOf(this.workedHours));
        this.notes = overtime.getDescription();
    }

    public BillingDto toDto() {
        Employee employee = assignment.getEmployee();

        return new BillingDto(UUID.randomUUID(), employee.getId(), employee.getInternalId(),
                employee.getFirstName(), employee.getLastName(), assignment.getProject().getClient().getName(),
                assignment.getProject().getName(), rate, workedHours, ptoHours, total, notes);
    }

    public String toString() {
        Employee employee = assignment.getEmployee();

        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("internalId", employee.getInternalId())
                .append("first name", employee.getFirstName())
                .append("last name", employee.getLastName())
                .append("client", assignment.getProject().getClient().getName())
                .append("project", assignment.getProject().getName())
                .append("rate", rate)
                .append("hours", workedHours)
                .append("ptoHours", ptoHours)
                .append("total", total)
                .append("notes", notes)
                .toString();
    }
}
