package com.entropyteam.entropay.employees.models;

import com.entropyteam.entropay.common.BaseEntity;
import com.entropyteam.entropay.employees.dtos.TechnologyDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity(name = "Technology")
@Table(name = "technology")
public class Technology extends BaseEntity {

    @Column
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "technology_type")
    private TechnologyType technologyType;

    public Technology() {
    }

    public Technology(TechnologyDto entity) {
        this.name = entity.name();
        this.technologyType = TechnologyType.valueOf(entity.technologyType());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TechnologyType getTechnologyType() {
        return technologyType;
    }

    public void setTechnologyType(TechnologyType technologyType) {
        this.technologyType = technologyType;
    }
}