package com.entropyteam.entropay.employees.services;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.common.BaseService;
import com.entropyteam.entropay.employees.dtos.PositionDto;
import com.entropyteam.entropay.employees.models.Position;
import com.entropyteam.entropay.employees.repositories.PositionRepository;

@Service
public class PositionService extends BaseService<Position, PositionDto, UUID> {

    private final PositionRepository positionRepository;

    @Autowired
    public PositionService(PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }

    @Override
    protected BaseRepository<Position, UUID> getRepository() {
        return positionRepository;
    }

    @Override
    protected PositionDto toDTO(Position entity) {
        return new PositionDto(entity);
    }

    @Override
    protected Position toEntity(PositionDto entity) {
        return new Position(entity);
    }
}
