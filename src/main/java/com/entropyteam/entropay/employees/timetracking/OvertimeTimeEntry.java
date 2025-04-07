package com.entropyteam.entropay.employees.timetracking;

import java.math.BigDecimal;
import com.entropyteam.entropay.employees.models.Assignment;
import com.entropyteam.entropay.employees.models.Overtime;

public class OvertimeTimeEntry extends TimeTrackingEntry {

    private final Assignment assignment;
    private final Double hours;
    private final String reason;

    public OvertimeTimeEntry(Overtime overtime) {
        super(ActivityType.OVERTIME, overtime.getEmployee(), overtime.getDate());
        this.assignment = overtime.getAssignment();
        this.hours = (double) overtime.getHours();
        this.reason = overtime.getDescription();
    }

    public String getReason() {
        return reason;
    }

    @Override
    public Double getHours() {
        return hours;
    }

    @Override
    public BigDecimal getRate() {
        return assignment.getBillableRate();
    }
}
