package com.entropyteam.entropay.employees.project.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.entropyteam.entropay.employees.common.BaseEntity;

@Entity(name = "ProjectType")
@Table(name = "project_type")
public class ProjectType extends BaseEntity {

    @Column
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
