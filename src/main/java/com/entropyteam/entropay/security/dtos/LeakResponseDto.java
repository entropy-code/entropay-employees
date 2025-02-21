package com.entropyteam.entropay.security.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LeakResponseDto(
        boolean success,
        List<LeakDto> result
) {}