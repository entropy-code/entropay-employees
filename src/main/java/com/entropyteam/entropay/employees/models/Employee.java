package com.entropyteam.entropay.employees.models;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.ManyToOne;
import com.entropyteam.entropay.common.BaseEntity;
import com.entropyteam.entropay.employees.dtos.EmployeeDto;



@Entity(name = "Employee")
@Table(name = "employee")
public class Employee extends BaseEntity {

    private String internalId;
    private String firstName;
    private String lastName;
    private String personalEmail;
    private String phoneNumber;
    private String mobileNumber;
    private String address;
    private String city;
    private String state;
    private String zip;
    private LocalDate birthDate;
    private String personalNumber;
    private String taxId;
    private String emergencyContactFullName;
    private String emergencyContactPhone;
    private String healthInsurance;
    private String notes;
    private String labourEmail;
    private boolean active;

    @OneToMany(mappedBy = "employee")
    private Set<PaymentInformation> paymentsInformation = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "employee_role",
            joinColumns = {@JoinColumn(name = "employee_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")}
    )
    private Set<Role> roles;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "employee_technology",
            joinColumns = {@JoinColumn(name = "employee_id")},
            inverseJoinColumns = {@JoinColumn(name = "technology_id")}
    )
    private Set<Technology> technologies;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    private Country country;

    public Employee() {
    }

    public Employee(EmployeeDto entity) {
        this.internalId = entity.internalId();
        this.firstName = entity.firstName();
        this.lastName = entity.lastName();
        this.personalEmail = entity.personalEmail();
        this.phoneNumber = entity.phoneNumber();
        this.mobileNumber = entity.mobileNumber();
        this.address = entity.address();
        this.city = entity.city();
        this.state = entity.state();
        this.zip = entity.zip();
        this.birthDate = entity.birthDate();
        this.personalNumber = entity.personalNumber();
        this.taxId = entity.taxId();
        this.emergencyContactFullName = entity.emergencyContactFullName();
        this.emergencyContactPhone = entity.emergencyContactPhone();
        this.notes = entity.notes();
        this.healthInsurance = entity.healthInsurance();
        this.labourEmail = entity.labourEmail();
        this.active = entity.active();
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phone) {
        this.phoneNumber = phone;
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

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
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

    public String getPersonalNumber() {
        return personalNumber;
    }

    public void setPersonalNumber(String personalNumber) {
        this.personalNumber = personalNumber;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public String getEmergencyContactFullName() {
        return emergencyContactFullName;
    }

    public void setEmergencyContactFullName(String emergencyContactFullName) {
        this.emergencyContactFullName = emergencyContactFullName;
    }

    public String getEmergencyContactPhone() {
        return emergencyContactPhone;
    }

    public void setEmergencyContactPhone(String emergencyContactPhone) {
        this.emergencyContactPhone = emergencyContactPhone;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> rolesList) {
        this.roles = rolesList;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getHealthInsurance() {
        return healthInsurance;
    }

    public void setHealthInsurance(String healthInsurance) {
        this.healthInsurance = healthInsurance;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Set<PaymentInformation> getPaymentsInformation() {
        return paymentsInformation;
    }

    public void setPaymentsInformation(Set<PaymentInformation> paymentsInformation) {
        this.paymentsInformation = paymentsInformation;
    }

    public Set<Technology> getTechnologies() {
        return technologies;
    }

    public void setTechnologies(Set<Technology> technologies) {
        this.technologies = technologies;
    }

    public String getLabourEmail() {
        return labourEmail;
    }

    public void setLabourEmail(String labourEmail) {
        this.labourEmail = labourEmail;
    }

    public boolean isActive() {
        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
