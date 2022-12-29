package com.entropyteam.entropay.employees.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.entropyteam.entropay.common.BaseEntity;
import com.entropyteam.entropay.employees.dtos.AssignmentDto;

@Entity(name = "Assignment")
@Table(name = "assignment")
public class Assignment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seniority_id")
    private Seniority seniority;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column
    private LocalDate startDate;
    @Column
    private LocalDate endDate;
    @Column
    private Integer hoursPerWeek;
    @Column
    private String labourHours;
    @Column
    private BigDecimal billableRate;
    @Enumerated(EnumType.STRING)
    @Column(name = "currency")
    private Currency currency;

    public Assignment() {
    }

    public Assignment(AssignmentDto assignmentDto) {
        this.startDate = assignmentDto.startDate();
        this.endDate = assignmentDto.endDate();
        this.hoursPerWeek = assignmentDto.hoursPerWeek();
        this.billableRate = assignmentDto.billableRate();
        this.currency = Currency.valueOf(assignmentDto.currency());
        this.labourHours = assignmentDto.labourHours();
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Seniority getSeniority() {
        return seniority;
    }

    public void setSeniority(Seniority seniority) {
        this.seniority = seniority;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Integer getHoursPerWeek() {
        return hoursPerWeek;
    }

    public void setHoursPerWeek(Integer hoursPerWeek) {
        this.hoursPerWeek = hoursPerWeek;
    }

    public BigDecimal getBillableRate() {
        return billableRate;
    }

    public void setBillableRate(BigDecimal billableRate) {
        this.billableRate = billableRate;
    }

    public String getLabourHours() {
        return labourHours;
    }

    public void setLabourHours(String labourHours) {
        this.labourHours = labourHours;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}
