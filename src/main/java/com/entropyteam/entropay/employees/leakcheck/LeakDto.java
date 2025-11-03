package com.entropyteam.entropay.employees.leakcheck;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LeakDto(
        String email,
        SourceDto source,
        String password,
        List<String> origin
) {}