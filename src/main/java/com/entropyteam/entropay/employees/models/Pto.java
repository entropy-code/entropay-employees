package com.entropyteam.entropay.employees.models;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
    @Column(name = "from_date")
    private LocalDateTime from;

    @Column(name = "to_date")
    private LocalDateTime to;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column
    private String details;

    @Column
    private Integer days;

    @Column
    private Integer labourHours;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leave_type_id")
    private LeaveType leaveType;

    public Pto(PtoDto dto) {
        this.from = dto.from();
        this.to = dto.to();
        this.details = dto.details();
        setTimeAmount(dto);
    }

    private void setTimeAmount(PtoDto dto) {
        this.days = 0;
        this.labourHours = 0;
        Long days = ChronoUnit.DAYS.between(dto.to(), dto.from());
        if (days.compareTo(0L) > 0) {
            this.days = days.intValue();
        } else {
            this.labourHours = dto.labourHours();
        }
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
}
