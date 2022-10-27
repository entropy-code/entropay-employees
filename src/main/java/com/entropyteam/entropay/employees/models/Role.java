package com.entropyteam.entropay.employees.models;

import javax.persistence.*;

import com.entropyteam.entropay.common.BaseEntity;
import com.entropyteam.entropay.employees.dtos.RoleDto;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "Role")
@Table(name = "Role")
public class Role extends BaseEntity {

    @Column
    private String name;

    @ManyToMany
    @JoinTable(name = "employee_role",
                joinColumns = @JoinColumn (name = "role_id", referencedColumnName = "id"),
                inverseJoinColumns = @JoinColumn (name = "employee_id" , referencedColumnName = "id")
    )
    private Set<Employee> employees = new HashSet<Employee>();

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
