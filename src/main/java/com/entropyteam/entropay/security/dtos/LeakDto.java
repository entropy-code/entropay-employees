package com.entropyteam.entropay.security.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LeakDto(
        String email,
        SourceDto source,
        String password,
        List<String> origin
) {}