package com.entropyteam.entropay.employees.models;

import com.entropyteam.entropay.common.BaseEntity;
import com.entropyteam.entropay.employees.dtos.EducationLevelDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity(name = "EducationLevel")
@Table(name = "education_level")
public class EducationLevel extends BaseEntity {

    @Column
    private String name;

    public EducationLevel(EducationLevelDto educationLevelDto) {
        this.name = educationLevelDto.name();
    }

    public EducationLevel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
