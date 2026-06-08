package com.entropyteam.entropay.employees.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.apache.commons.lang3.builder.ToStringBuilder;
import com.entropyteam.entropay.common.BaseEntity;
import com.entropyteam.entropay.employees.dtos.AssignmentDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


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
    private BigDecimal billableRate;
    @Enumerated(EnumType.STRING)
    @Column(name = "currency")
    private Currency currency;
    @Column
    private boolean active;
    @Column
    private String endReason;
    @Enumerated(EnumType.STRING)
    @Column(name = "engagement_type")
    private EngagementType engagementType;

    public Assignment() {
    }

    public Assignment(AssignmentDto assignmentDto) {
        this.startDate = assignmentDto.startDate();
        this.endDate = assignmentDto.endDate();
        this.billableRate = assignmentDto.billableRate();
        this.currency = Currency.findByName(assignmentDto.currency());
        this.endReason = assignmentDto.endReason();
        this.engagementType = EngagementType.valueOf(assignmentDto.engagementType());
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

    public BigDecimal getBillableRate() {
        return billableRate != null ? billableRate : BigDecimal.ZERO;
    }

    public void setBillableRate(BigDecimal billableRate) {
        this.billableRate = billableRate;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getEndReason() {
        return endReason;
    }

    public void setEndReason(String endReason) {
        this.endReason = endReason;
    }

    public EngagementType getEngagementType() {
        return engagementType;
    }
    public void setEngagementType(EngagementType engagementType) {
        this.engagementType = engagementType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("project", project)
                .append("role", role)
                .append("seniority", seniority)
                .append("employee", employee)
                .append("startDate", startDate)
                .append("endDate", endDate)
                .append("billableRate", billableRate)
                .append("currency", currency)
                .append("active", active)
                .append("endReason", endReason)
                .append("engagementType", engagementType)
                .toString();
    }
}
