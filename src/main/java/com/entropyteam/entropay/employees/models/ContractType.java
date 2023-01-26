package com.entropyteam.entropay.employees.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonFormat(shape= JsonFormat.Shape.OBJECT)
public enum ContractType {

    CONTRACTOR("Contractor"),
    EMPLOYEE_RELATIONSHIP("Employee Relationship"),
    MIX("Employee Relationship and Contractor"),
    TRAINEE("Trainee");

    @JsonProperty(value="id")
    private final String name = this.name();
    @JsonProperty()
    private final String value;

    ContractType(String value) {
        this.value = value;
    }

}
