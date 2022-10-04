package com.entropyteam.entropay.employees.repositories;

import java.util.List;
import java.util.UUID;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.employees.models.Position;

public interface PositionRepository extends BaseRepository<Position, UUID> {

    List<Position> findAllByDeletedIsFalse();
}
