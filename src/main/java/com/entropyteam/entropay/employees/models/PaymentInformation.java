package com.entropyteam.entropay.employees.models;

import com.entropyteam.entropay.common.BaseEntity;
import com.entropyteam.entropay.employees.dtos.PaymentInformationDto;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity(name = "PaymentInformation")
@Table(name = "payment_information")
public class PaymentInformation extends BaseEntity {

    private String platform;
    private String country;
    private String cbu;
    private String routingNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;



    public PaymentInformation(PaymentInformationDto paymentInformationDto) {
        this.platform = paymentInformationDto.platform();
        this.country = paymentInformationDto.country();
        this.cbu = paymentInformationDto.cbu();
        this.routingNumber = paymentInformationDto.routingNumber();
        this.setId(paymentInformationDto.id());
    }

    public PaymentInformation() {
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCbu() {
        return cbu;
    }

    public void setCbu(String cbu) {
        this.cbu = cbu;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getRoutingNumber() {
        return routingNumber;
    }

    public void setRoutingNumber(String routingNumber) {
        this.routingNumber = routingNumber;
    }
}
