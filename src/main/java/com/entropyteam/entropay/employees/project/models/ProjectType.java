package com.entropyteam.entropay.employees.project.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.entropyteam.entropay.common.BaseEntity;
import com.entropyteam.entropay.employees.project.dtos.ProjectTypeDto;

@Entity(name = "ProjectType")
@Table(name = "project_type", schema = "employees")
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
