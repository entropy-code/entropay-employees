package com.entropyteam.entropay.employees.dtos;

import java.util.UUID;
import com.entropyteam.entropay.employees.models.Position;

public record PositionDto(UUID id, String name) {

    public PositionDto(Position position) {
        this(position.getId(), position.getName());
    }
}
