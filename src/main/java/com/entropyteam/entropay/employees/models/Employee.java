package com.entropyteam.entropay.employees.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

import com.entropyteam.entropay.common.BaseEntity;
import com.entropyteam.entropay.employees.dtos.EmployeeDto;

@Entity(name = "Employee")
@Table(name = "employee")
public class Employee extends BaseEntity {

    private String internalId;
    private String firstName;
    private String lastName;
    private String personalEmail;
    private String phone;
    private String address;
    private String city;
    private String state;
    private String zip;
    private String country;
    private LocalDate birthDate;
    private String personalNumber;
    private String taxId;
    private String emergencyContactFullName;
    private String emergencyContactPhone;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "employee_role",joinColumns = @JoinColumn (name = "employee_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn (name = "role_id" , referencedColumnName = "id")
    )
    private List<Role> rolesList = new ArrayList<>();

    public Employee() {
    }

    public Employee(EmployeeDto entity) {
        this.internalId = entity.internalId();
        this.firstName = entity.firstName();
        this.lastName = entity.lastName();
        this.personalEmail = entity.personalEmail();
        this.phone = entity.phone();
        this.address = entity.address();
        this.city = entity.city();
        this.state = entity.state();
        this.zip = entity.zip();
        this.country = entity.country();
        this.birthDate = entity.birthDate();
        this.personalNumber = entity.personalNumber();
        this.taxId = entity.taxId();
        this.emergencyContactFullName = entity.emergencyContactFullName();
        this.emergencyContactPhone = entity.emergencyContactPhone();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPersonalEmail() {
        return personalEmail;
    }

    public void setPersonalEmail(String personalEmail) {
        this.personalEmail = personalEmail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }


    public String getInternalId() {
        return internalId;
    }

    public void setInternalId(String internalId) {
        this.internalId = internalId;
    }

    public String getPersonalNumber() {return personalNumber;}
    public void setPersonalNumber(String dni) {this.personalNumber = dni;}

    public String getTaxId() {return taxId;}
    public void setTaxId(String taxId) {this.taxId = taxId;}

    public String getEmergencyContactFullName() {return emergencyContactFullName;}

    public void setEmergencyContactFullName(String emergencyContactFullName) {this.emergencyContactFullName = emergencyContactFullName;}

    public String getEmergencyContactPhone() {return emergencyContactPhone;}

    public void setEmergencyContactPhone(String emergencyContactPhone) {this.emergencyContactPhone = emergencyContactPhone;}

    public List<Role> getRolesList() {return rolesList;}

    public void setRolesList(List<Role> rolesList) {this.rolesList = rolesList;}
}
