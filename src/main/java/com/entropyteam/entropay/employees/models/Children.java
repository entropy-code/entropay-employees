package com.entropyteam.entropay.employees.models;

import com.entropyteam.entropay.common.BaseEntity;
import com.entropyteam.entropay.employees.dtos.ChildrenDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

@Entity(name = "Children")
@Table(name = "children")
public class Children extends BaseEntity {

    private String firstName;
    private String lastName;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Column
    private LocalDate birthDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;


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

    public Employee getEmployee() { return employee;  }

    public void setEmployee(Employee employee) {
        this.employee = employee;
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


