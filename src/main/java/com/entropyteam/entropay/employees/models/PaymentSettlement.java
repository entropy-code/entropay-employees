package com.entropyteam.entropay.employees.models;

import java.math.BigDecimal;
import com.entropyteam.entropay.common.BaseEntity;
import com.entropyteam.entropay.employees.dtos.PaymentSettlementDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity(name = "PaymentSettlement")
@Table(name = "payment_settlement")
public class PaymentSettlement extends BaseEntity {

    @Column
    private BigDecimal salary;
    @Enumerated(EnumType.STRING)
    @Column(name = "currency")
    private Currency currency;
    @Enumerated(EnumType.STRING)
    @Column(name = "modality")
    private Modality modality;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id")
    private Contract contract;

    public PaymentSettlement(PaymentSettlementDto entity) {
        this.salary = entity.salary();
        this.modality = Modality.valueOf(entity.modality());
        this.currency = Currency.valueOf(entity.currency());
        this.setId(entity.id());
    }

    public PaymentSettlement() {
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Modality getModality() {
        return modality;
    }

    public void setModality(Modality modality) {
        this.modality = modality;
    }
}
