package com.entropyteam.entropay.employees.dtos;

import java.util.UUID;

public class PtoReportClientDto extends ReportDto{
    private UUID id;
    private String clientName;
    private Double totalDays;
    private Integer year;

    public PtoReportClientDto(UUID id,
                                String clientName,
                                Double totalDays,
                                Integer year) {
        this.id = id;
        this.clientName = clientName;
        this.totalDays = totalDays;
        this.year = year;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientName() {
        return clientName;
    }

    public Double getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(Double totalDays) {
        this.totalDays = totalDays;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

}
