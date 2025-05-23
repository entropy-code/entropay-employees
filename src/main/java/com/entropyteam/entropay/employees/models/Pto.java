package com.entropyteam.entropay.employees.models;

import java.time.LocalDate;
import com.entropyteam.entropay.common.BaseEntity;
import com.entropyteam.entropay.employees.dtos.PtoDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "pto")
public class Pto extends BaseEntity {

    @Column
    private LocalDate startDate;

    @Column
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column
    private String details;

    @Column
    private Double days;

    @Column
    private Integer labourHours;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leave_type_id")
    private LeaveType leaveType;

    public Pto(PtoDto dto) {
        this.startDate = dto.ptoStartDate();
        this.endDate = dto.ptoEndDate();
        this.details = dto.details();
        this.labourHours = dto.labourHours();
    }

    public Pto() {
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate from) {
        this.startDate = from;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate to) {
        this.endDate = to;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Double getDays() {
        return days;
    }

    public void setDays(Double days) {
        this.days = days;
    }

    public Integer getLabourHours() {
        return labourHours;
    }

    public void setLabourHours(Integer labour_hours) {
        this.labourHours = labour_hours;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public LeaveType getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(LeaveType ptoType) {
        this.leaveType = ptoType;
    }

    public Integer getDaysAsInteger() {
        return days.intValue();
    }

    public boolean isHalfDay() {
        return startDate.isEqual(endDate);
    }
}
