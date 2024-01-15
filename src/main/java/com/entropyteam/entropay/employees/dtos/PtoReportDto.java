package com.entropyteam.entropay.employees.dtos;

import com.entropyteam.entropay.employees.models.Contract;
import com.entropyteam.entropay.employees.models.Employee;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class PtoReportDto extends ReportDto {
    private UUID id;
    private String internalId;
    private String firstName;
    private String lastName;
    private String clientName;
    private Integer totalDays;


    public PtoReportDto(UUID id,
                        String internalId,
                        String firstName,
                        String lastName,
                        String clientName,
                        Integer totalDays) {
        this.id = id;
        this.internalId = internalId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.clientName = clientName;
        this.totalDays = totalDays;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getInternalId() {
        return internalId;
    }

    public void setInternalId(String internalId) {
        this.internalId = internalId;
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

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Integer getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(Integer totalDays) {
        this.totalDays = totalDays;
    }

}
