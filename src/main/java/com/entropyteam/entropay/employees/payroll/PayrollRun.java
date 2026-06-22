package com.entropyteam.entropay.employees.payroll;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.hibernate.annotations.SQLRestriction;
import com.entropyteam.entropay.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity(name = "PayrollRun")
@Table(name = "payroll_run")
class PayrollRun extends BaseEntity {

    @Column(nullable = false)
    private LocalDate period;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PayrollRunStatus status;

    @Column(name = "triggered_by_user_id")
    private UUID triggeredByUserId;

    @Column(name = "triggered_by_email")
    private String triggeredByEmail;

    @Column(name = "started_at")
    private Instant startedAt;

    @Column(name = "completed_at")
    private Instant completedAt;

    @Column(name = "error_message", length = 2000)
    private String errorMessage;

    @Column(name = "total_amount", precision = 14, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "employee_count")
    private Integer employeeCount;

    @OneToMany(mappedBy = "payrollRun")
    @SQLRestriction("deleted = false")
    private Set<PayrollItem> items = new HashSet<>();

    public PayrollRun() {
    }

    public LocalDate getPeriod() {
        return period;
    }

    public void setPeriod(LocalDate period) {
        this.period = period;
    }

    public PayrollRunStatus getStatus() {
        return status;
    }

    public void setStatus(PayrollRunStatus status) {
        this.status = status;
    }

    public UUID getTriggeredByUserId() {
        return triggeredByUserId;
    }

    public void setTriggeredByUserId(UUID triggeredByUserId) {
        this.triggeredByUserId = triggeredByUserId;
    }

    public String getTriggeredByEmail() {
        return triggeredByEmail;
    }

    public void setTriggeredByEmail(String triggeredByEmail) {
        this.triggeredByEmail = triggeredByEmail;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Instant completedAt) {
        this.completedAt = completedAt;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getEmployeeCount() {
        return employeeCount;
    }

    public void setEmployeeCount(Integer employeeCount) {
        this.employeeCount = employeeCount;
    }

    public Set<PayrollItem> getItems() {
        return items;
    }

    public void setItems(Set<PayrollItem> items) {
        this.items = items;
    }
}
