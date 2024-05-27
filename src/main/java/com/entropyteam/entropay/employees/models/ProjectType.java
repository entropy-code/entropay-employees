package com.entropyteam.entropay.employees.models;

import com.entropyteam.entropay.common.BaseEntity;
import com.entropyteam.entropay.employees.dtos.ProjectTypeDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity(name = "ProjectType")
@Table(name = "project_type")
public class ProjectType extends BaseEntity {

    @Column
    private String name;

    public ProjectType() {
    }

    public ProjectType(ProjectTypeDto entity) {
        this.name = entity.name();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
