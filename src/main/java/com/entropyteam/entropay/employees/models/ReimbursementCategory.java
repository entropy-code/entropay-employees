package com.entropyteam.entropay.employees.models;

import java.math.BigDecimal;
import com.entropyteam.entropay.common.BaseEntity;
import com.entropyteam.entropay.employees.dtos.ReimbursementCategoryDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity(name = "ReimbursementCategory")
@Table(name = "reimbursement_category")
public class ReimbursementCategory extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column(name = "maximum_amount", precision = 10, scale = 2)
    private BigDecimal maximumAmount;

    @Column(name = "period_in_months")
    private Integer periodInMonths;

    public ReimbursementCategory() {
    }

    public ReimbursementCategory(ReimbursementCategoryDto entity) {
        this.name = entity.name();
        this.description = entity.description();
        this.maximumAmount = entity.maximumAmount();
        this.periodInMonths = entity.periodInMonths();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getMaximumAmount() {
        return maximumAmount;
    }

    public void setMaximumAmount(BigDecimal maximumAmount) {
        this.maximumAmount = maximumAmount;
    }

    public Integer getPeriodInMonths() {
        return periodInMonths;
    }

    public void setPeriodInMonths(Integer periodInMonths) {
        this.periodInMonths = periodInMonths;
    }
}