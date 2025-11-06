package com.entropyteam.entropay.employees.leakcheck;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
record LeakResponseDto(
        boolean success,
        List<LeakDto> result
) {}