package com.entropyteam.entropay.summary.model;

import com.entropyteam.entropay.employees.models.Employee;
import jakarta.persistence.*;
import java.time.LocalDateTime;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.entropyteam.entropay.common.BaseEntity;
import com.entropyteam.entropay.employees.dtos.EmployeeDto;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "employee_summary")
public class EmployeeSummary {
    @Id
    @Column(nullable = false)
    private UUID id;

    public EmployeeSummary() {
        this.id = UUID.randomUUID();
    }

    public EmployeeSummary(Employee employee, String prompt, String summaryText, String createdBy) {
        this.id = UUID.randomUUID();
        this.employee = employee;
        this.prompt = prompt;
        this.summaryText = summaryText;
        this.createdBy = createdBy;
        this.createdAt = LocalDateTime.now();
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(length = 5000)
    private String prompt;

    @Column(length = 5000)
    private String summaryText;

    @Column(length = 255)
    private String createdBy;

    private LocalDateTime modifiedAt;
    private LocalDateTime createdAt;

    // Getters y Setters
    public UUID  getId() {
        return id;
    }

    public void setId(UUID  id) {
        this.id = id;
    }

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }



    @PreUpdate
    public void updateModifiedAt() {
        this.modifiedAt = LocalDateTime.now();
    }

    @PrePersist
    public void setCreationTimestamp() {
        this.createdAt = LocalDateTime.now();
    }

}