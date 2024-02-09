package com.entropyteam.entropay.employees.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.UUID;

public class PtoReportDetailDto extends ReportDto {
    private UUID id;
    private UUID employeeId;
    private String internalId;
    private String firstName;
    private String lastName;
    private String clientName;
    private String leaveTypeName;
    private Integer days;
    private UUID clientId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    private Integer year;

    public PtoReportDetailDto(UUID id, UUID employeeId, String internalId, String firstName, String lastName, String clientName, String leaveTypeName, Integer days, UUID clientId,
                              LocalDate startDate, LocalDate endDate, Integer year) {
        this.id = id;
        this.employeeId = employeeId;
        this.internalId = internalId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.clientName = clientName;
        this.leaveTypeName = leaveTypeName;
        this.days = days;
        this.clientId = clientId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.year = year;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public UUID getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(UUID id) {
        this.employeeId = employeeId;
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

    public String getInternalId() {
        return internalId;
    }

    public void setInternalId(String internalId) {
        this.internalId = internalId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getLeaveTypeName() {
        return leaveTypeName;
    }

    public void setLeaveTypeName(String leaveTypeName) {
        this.leaveTypeName = leaveTypeName;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
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

    public UUID getClientId() {
        return clientId;
    }

    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getYear() {
        return year;
    }
}
