package com.entropyteam.entropay.employees.models;

import javax.persistence.*;

import com.entropyteam.entropay.common.BaseEntity;
import com.entropyteam.entropay.employees.dtos.RoleDto;

import java.util.Set;

@Entity(name = "Role")
@Table(name = "role")
public class Role extends BaseEntity {

    @Column
    private String name;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "roles")
    private Set<Employee> employees;

    public Role() {
    }

    public Role(RoleDto entity) {
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
