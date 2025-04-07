package com.entropyteam.entropay.employees.timetracking;

import java.math.BigDecimal;
import java.time.LocalDate;
import com.entropyteam.entropay.employees.models.Employee;

public abstract class TimeTrackingEntry {

    private final ActivityType activityType;
    private final Employee employee;
    private final LocalDate date;

    public TimeTrackingEntry(ActivityType activityType, Employee employee, LocalDate date) {
        this.activityType = activityType;
        this.employee = employee;
        this.date = date;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public Employee getEmployee() {
        return employee;
    }

    public LocalDate getDate() {
        return date;
    }

    public abstract Double getHours();

    public abstract BigDecimal getRate();
}
