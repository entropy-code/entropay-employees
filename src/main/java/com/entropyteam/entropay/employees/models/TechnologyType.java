package com.entropyteam.entropay.employees.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum TechnologyType {

    AGILE_METHODOLOGY("Agile Methodology"),
    AI("AI"),
    BACKEND_FRAMEWORK("Backend Framework"),
    CICD_DEVOPS("CI/CD & DevOps"),
    CLOUD_INFRASTRUCTURE("Cloud & Infrastructure"),
    DATABASE("Database"),
    FRONTEND_FRAMEWORK("Frontend Framework"),
    PROGRAMMING_LANGUAGE("Programming Language"),
    TESTING_FRAMEWORK("Testing Framework"),
    WEB_FUNDAMENTALS("Web Fundamentals");

    @JsonProperty(value = "id")
    private final String name = this.name();
    @JsonProperty()
    private final String value;

    TechnologyType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
