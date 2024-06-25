package com.entropyteam.entropay.employees.models;

import com.entropyteam.entropay.common.BaseEntity;
import com.entropyteam.entropay.employees.dtos.SeniorityDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity(name = "Seniority")
@Table(name = "seniority")
public class Seniority extends BaseEntity {

    @Column
    private String name;
    @Column
    private Integer vacationDays;

    public Seniority() {
    }

    public Seniority(SeniorityDto entity) {
        this.name = entity.name();
        this.vacationDays = entity.vacationDays();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getVacationDays() {
        return vacationDays;
    }

    public void setVacationDays(Integer vacationDays) {
        this.vacationDays = vacationDays;
    }
}

