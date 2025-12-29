package com.entropyteam.entropay.employees.models;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.SQLRestriction;
import com.entropyteam.entropay.common.BaseEntity;
import com.entropyteam.entropay.employees.dtos.EmployeeDto;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;


@Entity(name = "Employee")
@Table(name = "employee")
public class Employee extends BaseEntity {

    private String internalId;
    private String firstName;
    private String lastName;
    @Enumerated(EnumType.STRING)
    private Gender gender;
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
    @SQLRestriction("deleted = false")
    @BatchSize(size = 100)
    private Set<PaymentInformation> paymentsInformation = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "employee_role",
            joinColumns = {@JoinColumn(name = "employee_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")}
    )
    @BatchSize(size = 100)
    private Set<Role> roles;

    @OneToMany(mappedBy = "employee")
    @SQLRestriction("deleted = false")
    @BatchSize(size = 100)
    private Set<Skill> skills = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    private Country country;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "parents")
    @SQLRestriction("deleted = false")
    @BatchSize(size = 100)
    private Set<Children> children = new HashSet<>();

    @OneToMany(mappedBy = "employee")
    @SQLRestriction("deleted = false")
    @BatchSize(size = 100)
    private Set<Assignment> assignments = new HashSet<>();

    @OneToMany(mappedBy = "employee")
    @SQLRestriction("deleted = false")
    @BatchSize(size = 100)
    private Set<Contract> contracts = new HashSet<>();

    @OneToMany(mappedBy = "employee")
    @SQLRestriction("deleted = false")
    @BatchSize(size = 100)
    private Set<Vacation> vacations = new HashSet<>();

    @OneToMany(mappedBy = "employee")
    @SQLRestriction("deleted = false")
    @BatchSize(size = 100)
    private Set<Pto> ptos = new HashSet<>();

    public Employee() {
    }

    public Employee(EmployeeDto employeeDto) {
        this.internalId = employeeDto.getInternalId();
        this.firstName = employeeDto.getFirstName();
        this.lastName = employeeDto.getLastName();
        this.gender = employeeDto.getGender();
        this.personalEmail = employeeDto.getPersonalEmail();
        this.phoneNumber = employeeDto.getPhoneNumber();
        this.mobileNumber = employeeDto.getMobileNumber();
        this.address = employeeDto.getAddress();
        this.city = employeeDto.getCity();
        this.state = employeeDto.getState();
        this.zip = employeeDto.getZip();
        this.birthDate = employeeDto.getBirthDate();
        this.personalNumber = employeeDto.getPersonalNumber();
        this.taxId = employeeDto.getTaxId();
        this.emergencyContactFullName = employeeDto.getEmergencyContactFullName();
        this.emergencyContactPhone = employeeDto.getEmergencyContactPhone();
        this.notes = employeeDto.getNotes();
        this.healthInsurance = employeeDto.getHealthInsurance();
        this.labourEmail = employeeDto.getLabourEmail();
        this.active = employeeDto.isActive();
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

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
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

    public Set<Skill> getSkills() {
        return skills;
    }

    public void setSkills(Set<Skill> skills) {
        this.skills = skills;
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

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }

    public Set<Children> getChildren() {
        return children;
    }

    public void setChildren(Set<Children> children) {
        this.children = children;
    }


    public Set<Assignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(Set<Assignment> assignments) {
        this.assignments = assignments;
    }

    public Set<Contract> getContracts() {
        return contracts;
    }

    public void setContracts(Set<Contract> contracts) {
        this.contracts = contracts;
    }

    public Set<Vacation> getVacations() {
        return vacations;
    }

    public void setVacations(Set<Vacation> vacations) {
        this.vacations = vacations;
    }

    public Set<Pto> getPtos() {
        return ptos;
    }

    public void setPtos(Set<Pto> ptos) {
        this.ptos = ptos;
    }

    public int getAvailableVacationsDays() {
        return this.vacations.stream()
                .mapToInt(v -> v.getCredit() - v.getDebit())
                .sum();
    }

    public LocalDate getNearestPto(LocalDate today) {
        return this.ptos.stream()
                .map(Pto::getStartDate)
                .filter(startDate -> !startDate.isBefore(today))
                .min(LocalDate::compareTo)
                .orElse(null);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("internalId", internalId)
                .append("firstName", firstName)
                .append("lastName", lastName)
                .toString();
    }
}
