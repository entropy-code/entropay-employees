package com.entropyteam.entropay.employees.models;

import java.util.UUID;
import com.entropyteam.entropay.common.BaseEntity;
import com.entropyteam.entropay.employees.dtos.EmployeeEducationDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity(name = "EmployeeEducation")
@Table(name = "employee_education")
public class EmployeeEducation extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false, unique = true)
    private Employee employee;

    @Column(name = "education_level", nullable = false)
    private String level;

    @Column(name = "education_level_other")
    private String levelOther;

    @Column(name = "educational_institution", nullable = false)
    private String institution;

    @Column(nullable = false)
    private String degree;

    public EmployeeEducation(EmployeeEducationDto employeeEducationDto) {
        this.level = employeeEducationDto.level();
        this.levelOther = employeeEducationDto.levelOther();
        this.institution = employeeEducationDto.institution();
        this.degree = employeeEducationDto.degree();
    }

    public EmployeeEducation() {
    }

    // Getters and Setters
    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getLevelOther() {
        return levelOther;
    }

    public void setLevelOther(String levelOther) {
        this.levelOther = levelOther;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }
}
