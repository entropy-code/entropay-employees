package com.entropyteam.entropay.employees.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Currency {
    USD("USD - United States dollar"),
    ARS("ARS - Argentine peso");

    @JsonProperty(value = "id")
    private final String name = this.name();
    @JsonProperty(value = "name")
    private final String description;

    Currency(String value) {
        this.description = value;
    }

    public static Currency findByName(String name) {
        Currency result = null;
        for (Currency currency : values()) {
            if ( StringUtils.equalsIgnoreCase( currency.name() , name )) {
                result = currency;
                break;
            }
        }
        return result;
    }

}
