package com.entropyteam.entropay.employees.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonFormat(shape= JsonFormat.Shape.OBJECT)
public enum Modality {
    HOUR("Hour"),
    MONTHLY("Monthly");

    @JsonProperty(value="id")
    private final String name = this.name();
    @JsonProperty()
    private final String value;

    Modality(String value) {
        this.value = value;
    }
}
