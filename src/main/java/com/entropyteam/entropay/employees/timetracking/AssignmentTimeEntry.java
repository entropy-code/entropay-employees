package com.entropyteam.entropay.employees.timetracking;

import java.math.BigDecimal;
import java.time.LocalDate;
import com.entropyteam.entropay.employees.models.Assignment;

public class AssignmentTimeEntry extends TimeTrackingEntry {

    private final Assignment assignment;
    private final Double hours;

    public AssignmentTimeEntry(Assignment assignment, LocalDate date, Double hours) {
        super(ActivityType.WORK, assignment.getEmployee(), date);
        this.assignment = assignment;
        this.hours = hours;
    }

    public Assignment getAssignment() {
        return assignment;
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
