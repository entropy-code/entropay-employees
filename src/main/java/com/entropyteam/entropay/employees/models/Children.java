package com.entropyteam.entropay.employees.models;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import com.entropyteam.entropay.common.BaseEntity;
import com.entropyteam.entropay.employees.dtos.ChildrenDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity(name = "Children")
@Table(name = "children")
public class Children extends BaseEntity {

    @Column
    private String firstName;
    @Column
    private String lastName;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Column
    private LocalDate birthDate;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "employee_children",
            joinColumns = {@JoinColumn(name = "children_id")},
            inverseJoinColumns = {@JoinColumn(name = "employee_id")}
    )
    private Set<Employee> employee;


    public Children(ChildrenDto childrenDto) {
        this.firstName = childrenDto.firstName();
        this.lastName = childrenDto.lastName();
        this.gender = childrenDto.gender();
        this.birthDate = childrenDto.birthDate();
    }

    public Children() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Set<Employee> getEmployees() {
        return employee;
    }

    public void setEmployees(Set<Employee> employee) {
        this.employee = employee;
    }

    public void setEmployee(Employee employee) {
        if (this.employee == null) {
            this.employee = new HashSet<>();
        }
        this.employee.add(employee);
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}


