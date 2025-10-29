package com.entropyteam.entropay.security.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SourceDto(
        String name,
        @JsonProperty("breach_date") String breachDate
) {}