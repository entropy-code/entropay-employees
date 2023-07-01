package com.entropyteam.entropay.employees.models;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.entropyteam.entropay.common.BaseEntity;
import com.entropyteam.entropay.employees.dtos.PtoDto;

@Entity
@Table(name = "pto")
public class Pto extends BaseEntity {
    @Column
    private LocalDateTime from;

    @Column
    private LocalDateTime to;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column
    private String details;

    @Column
    private Integer days;

    @Column
    private Integer labour_hours;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pto_type")
    private LeaveType ptoType;

    public Pto(PtoDto dto) {
        this.from = dto.from();
        this.to = dto.to();
        this.details = dto.details();
        this.days = dto.days();
        this.labour_hours = dto.labour_hours();
    }

    public Pto() {
    }

    public LocalDateTime getFrom() {
        return from;
    }

    public void setFrom(LocalDateTime from) {
        this.from = from;
    }

    public LocalDateTime getTo() {
        return to;
    }

    public void setTo(LocalDateTime to) {
        this.to = to;
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

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public Integer getLabour_hours() {
        return labour_hours;
    }

    public void setLabour_hours(Integer labour_hours) {
        this.labour_hours = labour_hours;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public LeaveType getPtoType() {
        return ptoType;
    }

    public void setPtoType(LeaveType ptoType) {
        this.ptoType = ptoType;
    }
}
