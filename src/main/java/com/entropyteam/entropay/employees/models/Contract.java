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
import com.entropyteam.entropay.employees.dtos.ContractDto;

@Entity(name = "Contract")
@Table(name = "contract")
public class Contract extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seniority_id")
    private Seniority seniority;

    @Column
    private LocalDate startDate;
    @Column
    private LocalDate endDate;
    @Column
    private Integer hoursPerWeek;
    @Column
    private BigDecimal costRate;
    @Enumerated(EnumType.STRING)
    @Column(name = "currency")
    private Currency currency;
    @Column
    private BigDecimal monthlySalary;
    @Column
    private Integer vacations;
    @Column
    private String benefits;
    @Column
    private String notes;
    @Enumerated(EnumType.STRING)
    @Column(name = "contractType", nullable = false)
    private ContractType contractType;
    @Column
    private boolean active;

    public Contract() {
    }

    public Contract(ContractDto entity) {
        this.startDate = entity.startDate();
        this.endDate = entity.endDate();
        this.hoursPerWeek = entity.hoursPerWeek();
        this.costRate = entity.costRate();
        this.currency = Currency.valueOf(entity.currency());
        this.monthlySalary = entity.monthlySalary();
        this.vacations = entity.vacations();
        this.benefits = entity.benefits();
        this.notes = entity.notes();
        this.contractType = ContractType.valueOf(entity.contractType());
        this.active = entity.active();
    }


    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
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

    public Integer getHoursPerWeek() {
        return hoursPerWeek;
    }

    public void setHoursPerWeek(Integer hoursPerWeek) {
        this.hoursPerWeek = hoursPerWeek;
    }

    public BigDecimal getCostRate() {
        return costRate;
    }

    public void setCostRate(BigDecimal costRate) {
        this.costRate = costRate;
    }

    public Integer getVacations() {
        return vacations;
    }

    public void setVacations(Integer vacations) {
        this.vacations = vacations;
    }

    public String getBenefits() {
        return benefits;
    }

    public void setBenefits(String benefits) {
        this.benefits = benefits;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public ContractType getContractType() {
        return contractType;
    }

    public void setContractType(ContractType contractType) {
        this.contractType = contractType;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public BigDecimal getMonthlySalary() {
        return monthlySalary;
    }

    public void setMonthlySalary(BigDecimal monthlySalary) {
        this.monthlySalary = monthlySalary;
    }
}
