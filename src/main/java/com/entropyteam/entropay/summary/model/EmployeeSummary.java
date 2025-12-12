package com.entropyteam.entropay.summary.model;

import com.entropyteam.entropay.employees.models.Employee;
import com.entropyteam.entropay.summary.dtos.EmployeeSummaryDto;
import jakarta.persistence.*;
import com.entropyteam.entropay.common.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity(name = "EmployeeSummary")
@Table(name = "employee_summary")

public class EmployeeSummary extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(length = 5000)
    private String prompt;

    @Column(length = 100000)
    private String summaryText;

    @Column(length = 255)
    private String createdBy;

    public EmployeeSummary(EmployeeSummaryDto dto) {}
    protected EmployeeSummary() {}


    public EmployeeSummary ( EmployeeSummaryDto dto, Employee employee) {
        this.employee = employee;
        this.prompt = dto.prompt();
        this.summaryText = dto.summaryText();
        this.createdBy = dto.createdBy();
    }


    // Getters y Setters

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getSummaryText() {
        return summaryText;
    }

    public void setSummaryText(String summaryText) {
        this.summaryText = summaryText;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }


}