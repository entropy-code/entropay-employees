package com.entropyteam.entropay.employees.models;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
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
    private Integer hoursPerMonth;
    @Column
    private String benefits;
    @Column
    private String notes;
    @Enumerated(EnumType.STRING)
    @Column(name = "contractType", nullable = false)
    private ContractType contractType;
    @Column
    private boolean active;
    @OneToMany(mappedBy="contract")
    private Set<PaymentSettlement> paymentsSettlement = new HashSet<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "end_reason_id")
    private EndReason endReason;

    public Contract() {
    }

    public Contract(ContractDto entity) {
        this.startDate = entity.startDate();
        this.endDate = entity.endDate();
        this.hoursPerMonth = entity.hoursPerMonth();
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

    public Integer getHoursPerMonth() {
        return hoursPerMonth;
    }

    public void setHoursPerMonth(Integer hoursPerMonth) {
        this.hoursPerMonth = hoursPerMonth;
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

    public Set<PaymentSettlement> getPaymentsSettlement() {
        return paymentsSettlement;
    }

    public void setPaymentsSettlement(Set<PaymentSettlement> paymentsSettlement) {
        this.paymentsSettlement = paymentsSettlement;
    }

    public Integer getLatestPayment(Currency currency) {
        return getPaymentsSettlement().stream()
                .filter(p -> p.getCurrency() == currency)
                .findFirst()
                .map(payment -> payment.getSalary().intValue())
                .orElse(0);
    }

    public EndReason getEndReason() {
        return endReason;
    }

    @Nullable
    public UUID getEndReasonId() {
        return endReason != null ? endReason.getId() : null;
    }

    public void setEndReason(EndReason endReason) {
        this.endReason = endReason;
    }


}
