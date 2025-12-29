package com.entropyteam.entropay.employees.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import com.entropyteam.entropay.common.BaseEntity;
import com.entropyteam.entropay.employees.models.Assignment;
import com.entropyteam.entropay.employees.models.Client;
import com.entropyteam.entropay.employees.models.Contract;
import com.entropyteam.entropay.employees.models.Employee;
import com.entropyteam.entropay.employees.models.Gender;
import com.entropyteam.entropay.employees.models.Project;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;


public final class EmployeeDto {

    private UUID id;
    private String internalId;
    private String firstName;
    private String lastName;
    private Gender gender;
    @Email
    @NotNull(message = "Email is mandatory")
    private String personalEmail;
    private String phoneNumber;
    private String mobileNumber;
    private String address;
    private String city;
    private String state;
    private String zip;
    @NotNull(message = "Country is mandatory")
    private UUID countryId;
    @NotNull(message = "Personal Number is mandatory")
    private String personalNumber;
    private String taxId;
    private String emergencyContactFullName;
    private String emergencyContactPhone;
    private List<UUID> profile;
    private String notes;
    private String healthInsurance;
    private List<PaymentInformationDto> paymentInformation;
    private List<ChildrenDto> children;
    @Email
    private String labourEmail;
    @NotNull(message = "Birth Date is mandatory")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifiedAt;
    private boolean deleted;
    private String project;
    private String client;
    private String role;
    private UUID lastAssignmentId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    @NotNull(message = "Active state is mandatory")
    private boolean active;
    private Integer availableDays;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate nearestPto;
    private String timeSinceStart;
    private String countryName;

    public EmployeeDto() {
    }

    public EmployeeDto(Employee employee, Assignment lastAssignment, Contract firstContract,
            Contract activeContract, String timeSinceStart) {
        this.id = employee.getId();
        this.internalId = employee.getInternalId();
        this.firstName = employee.getFirstName();
        this.lastName = employee.getLastName();
        this.gender = employee.getGender();
        this.personalEmail = employee.getPersonalEmail();
        this.phoneNumber = employee.getPhoneNumber();
        this.mobileNumber = employee.getMobileNumber();
        this.address = employee.getAddress();
        this.city = employee.getCity();
        this.state = employee.getState();
        this.zip = employee.getZip();
        this.countryId = employee.getCountry().getId();
        this.personalNumber = employee.getPersonalNumber();
        this.taxId = employee.getTaxId();
        this.emergencyContactFullName = employee.getEmergencyContactFullName();
        this.emergencyContactPhone = employee.getEmergencyContactPhone();
        this.profile = employee.getRoles().stream().map(BaseEntity::getId).collect(Collectors.toList());
        this.notes = employee.getNotes();
        this.healthInsurance = employee.getHealthInsurance();
        this.paymentInformation = employee.getPaymentsInformation().stream().map(PaymentInformationDto::new).toList();
        this.children = employee.getChildren().stream().map(ChildrenDto::new).toList();
        this.labourEmail = employee.getLabourEmail();
        this.birthDate = employee.getBirthDate();
        this.createdAt = employee.getCreatedAt();
        this.modifiedAt = employee.getModifiedAt();
        this.deleted = employee.isDeleted();
        this.project = employee.getAssignments().stream()
                .filter(Assignment::isActive)
                .map(Assignment::getProject)
                .map(Project::getName)
                .distinct()
                .collect(Collectors.joining(", "));
        this.client = employee.getAssignments().stream()
                .filter(Assignment::isActive)
                .map(Assignment::getProject)
                .map(Project::getClient)
                .map(Client::getName)
                .distinct()
                .collect(Collectors.joining(", "));
        this.role = lastAssignment != null ? lastAssignment.getRole().getName() : "-";
        this.lastAssignmentId = lastAssignment != null ? lastAssignment.getId() : null;
        this.startDate = firstContract != null ? firstContract.getStartDate() : null;
        this.endDate = activeContract != null ? activeContract.getEndDate() : null;
        this.active = employee.isActive();
        this.availableDays = employee.getAvailableVacationsDays();
        this.nearestPto = employee.getNearestPto(LocalDate.now());
        this.timeSinceStart = timeSinceStart;
        this.countryName = employee.getCountry().getName();
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setInternalId(String internalId) {
        this.internalId = internalId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setPersonalEmail(String personalEmail) {
        this.personalEmail = personalEmail;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public void setCountryId(UUID countryId) {
        this.countryId = countryId;
    }

    public void setPersonalNumber(String personalNumber) {
        this.personalNumber = personalNumber;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public void setEmergencyContactFullName(String emergencyContactFullName) {
        this.emergencyContactFullName = emergencyContactFullName;
    }

    public void setEmergencyContactPhone(String emergencyContactPhone) {
        this.emergencyContactPhone = emergencyContactPhone;
    }

    public void setProfile(List<UUID> profile) {
        this.profile = profile;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setHealthInsurance(String healthInsurance) {
        this.healthInsurance = healthInsurance;
    }

    public void setPaymentInformation(
            List<PaymentInformationDto> paymentInformation) {
        this.paymentInformation = paymentInformation;
    }

    public void setChildren(List<ChildrenDto> children) {
        this.children = children;
    }

    public void setLabourEmail(String labourEmail) {
        this.labourEmail = labourEmail;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setLastAssignmentId(UUID lastAssignmentId) {
        this.lastAssignmentId = lastAssignmentId;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setAvailableDays(Integer availableDays) {
        this.availableDays = availableDays;
    }

    public void setNearestPto(LocalDate nearestPto) {
        this.nearestPto = nearestPto;
    }

    public void setTimeSinceStart(String timeSinceStart) {
        this.timeSinceStart = timeSinceStart;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public UUID getId() {
        return id;
    }

    public String getInternalId() {
        return internalId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Gender getGender() {
        return gender;
    }

    public String getPersonalEmail() {
        return personalEmail;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZip() {
        return zip;
    }

    public UUID getCountryId() {
        return countryId;
    }

    public String getPersonalNumber() {
        return personalNumber;
    }

    public String getTaxId() {
        return taxId;
    }

    public String getEmergencyContactFullName() {
        return emergencyContactFullName;
    }

    public String getEmergencyContactPhone() {
        return emergencyContactPhone;
    }

    public List<UUID> getProfile() {
        return profile;
    }

    public String getNotes() {
        return notes;
    }

    public String getHealthInsurance() {
        return healthInsurance;
    }

    public List<PaymentInformationDto> getPaymentInformation() {
        return paymentInformation;
    }

    public List<ChildrenDto> getChildren() {
        return children;
    }

    public String getLabourEmail() {
        return labourEmail;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public String getProject() {
        return project;
    }

    public String getClient() {
        return client;
    }

    public String getRole() {
        return role;
    }

    public UUID getLastAssignmentId() {
        return lastAssignmentId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public boolean isActive() {
        return active;
    }

    public Integer getAvailableDays() {
        return availableDays;
    }

    public LocalDate getNearestPto() {
        return nearestPto;
    }

    public String getTimeSinceStart() {
        return timeSinceStart;
    }

    public String getCountryName() {
        return countryName;
    }
}
