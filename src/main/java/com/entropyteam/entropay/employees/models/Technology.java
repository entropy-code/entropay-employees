package com.entropyteam.entropay.employees.models;

import java.util.Set;
import com.entropyteam.entropay.common.BaseEntity;
import com.entropyteam.entropay.employees.dtos.TechnologyDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity(name = "Technology")
@Table(name = "technology")
public class Technology extends BaseEntity {

    @Column
    private String name;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "technologies")
    private Set<Employee> employees;

    public Technology() {
    }

    public Technology(TechnologyDto entity) {
        this.name = entity.name();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Employee> getEmployees() {return employees;}

    public void setEmployees(Set<Employee> employees) {this.employees = employees;}
}