package com.entropyteam.entropay.employees.models;

import com.entropyteam.entropay.common.BaseEntity;
import com.entropyteam.entropay.employees.dtos.VacationDto;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.OneToOne;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;

@Entity(name = "Vacation")
@Table(name = "vacation")
public class Vacation extends BaseEntity {

    @Column
    private String year;

    @Column
    private Integer credit;

    @Column
    private Integer debit;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column
    private String details;
    public Vacation(){
    }
    public Vacation(VacationDto entity){
        this.credit = entity.credit();
        this.debit = entity.debit() != null ? entity.debit() : 0;
        this.year = entity.year();
        this.details = entity.details();
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public Integer getDebit() {
        return debit != null ? debit : 0;
    }

    public void setDebit(Integer debit) {
        this.debit = debit;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
