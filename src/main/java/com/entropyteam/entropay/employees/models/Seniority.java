package com.entropyteam.entropay.employees.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.entropyteam.entropay.common.BaseEntity;
import com.entropyteam.entropay.employees.dtos.SeniorityDto;

@Entity(name = "Seniority")
@Table(name = "seniority")
public class Seniority extends BaseEntity {

    @Column
    private String name;

    public Seniority() {
    }

    public Seniority(SeniorityDto entity) {
        this.name = entity.name();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

