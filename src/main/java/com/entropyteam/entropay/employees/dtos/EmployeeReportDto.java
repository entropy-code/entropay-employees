package com.entropyteam.entropay.employees.dtos;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import com.entropyteam.entropay.employees.models.Contract;
import com.entropyteam.entropay.employees.models.Employee;
import com.fasterxml.jackson.annotation.JsonFormat;

public class EmployeeReportDto {
    private UUID id;
    private String internalId;
    private String firstName;
    private String lastName;
    private String city;
    private String role;
    private List<String> profile;
    private String seniority;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    private Boolean status;
    private String clientName;
    private String projectName;
    private Integer usdPayment;
    private Integer arsPayment;
    private String country;
    private String labourEmail;
    private Boolean activeContract;

    public EmployeeReportDto(UUID id,
                             String internalId,
                             String firstName,
                             String lastName,
                             String city,
                             String role,
                             List<String> profile,
                             String seniority,
                             LocalDate startDate,
                             LocalDate endDate,
                             Boolean status,
                             String clientName,
                             String projectName,
                             Integer usdPayment,
                             Integer arsPayment,
                             String country,
                             String labourEmail,
                             Boolean activeContract) {
        this.id = id;
        this.internalId = internalId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.role = role;
        this.profile = profile;
        this.seniority = seniority;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.clientName = clientName;
        this.projectName = projectName;
        this.usdPayment = usdPayment;
        this.arsPayment = arsPayment;
        this.country = country;
        this.labourEmail = labourEmail;
        this.activeContract = activeContract;
    }

    public EmployeeReportDto(Employee employee, List<String> profile, Contract firstContract, Contract latestContract, String client, String project, Integer usdPayment, Integer arsPayment, String country, String labourEmail) {
        this(employee.getId(), employee.getInternalId(), employee.getFirstName(), employee.getLastName(),
                employee.getCity() != null ? employee.getCity() : "",
                latestContract != null && latestContract.isActive() ? latestContract.getRole().getName() : null,
                profile, latestContract != null && latestContract.isActive() ? latestContract.getSeniority().getName() : null,
                firstContract != null ? firstContract.getStartDate() : null,
                latestContract != null ? latestContract.getEndDate() : null,
                employee.isActive(), client, project, usdPayment, arsPayment, country != null ? country : "", labourEmail != null ? labourEmail : "",
                latestContract != null ? latestContract.isActive() : false);
    }

    public String getInternalId() {
        return internalId;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<String> getProfile() {
        return profile;
    }

    public void setProfile(List<String> profile) {
        this.profile = profile;
    }

    public String getSeniority() {
        return seniority;
    }

    public void setSeniority(String seniority) {
        this.seniority = seniority;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Integer getUsdPayment() {
        return usdPayment;
    }

    public void setUsdPayment(Integer usdPayment) {
        this.usdPayment = usdPayment;
    }

    public Integer getArsPayment() {
        return arsPayment;
    }

    public void setArsPayment(Integer arsPayment) {
        this.arsPayment = arsPayment;
    }

    public void setInternalId(String internalId) {
        this.internalId = internalId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLabourEmail() {
        return labourEmail;
    }

    public void setLabourEmail(String labourEmail) {
        this.labourEmail = labourEmail;
    }

    public void setActiveContract(Boolean activeContract) {
        this.activeContract = activeContract;
    }

    public Boolean isActiveContract() {
        return activeContract;
    }
}
