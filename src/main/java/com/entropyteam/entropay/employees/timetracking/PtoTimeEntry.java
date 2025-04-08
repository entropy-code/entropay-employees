package com.entropyteam.entropay.employees.timetracking;

import java.math.BigDecimal;
import java.time.LocalDate;
import com.entropyteam.entropay.employees.models.Employee;

public class PtoTimeEntry extends TimeTrackingEntry {

    private final Double hours;
    public PtoTimeEntry(Employee employee, LocalDate date, Double hours) {
        super(ActivityType.LEAVE, employee, date);
        this.hours = hours;
    }

    @Override
    public Double getHours() {
        return hours;
    }

    @Override
    public BigDecimal getRate() {
        return BigDecimal.ZERO;
    }
}
