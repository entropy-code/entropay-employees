package com.entropyteam.entropay.security.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SourceDto {
    private String name;
    @JsonProperty("breach_date")
    private String breachDate;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBreachDate() { return breachDate; }
    public void setBreachDate(String breachDate) { this.breachDate = breachDate; }
}