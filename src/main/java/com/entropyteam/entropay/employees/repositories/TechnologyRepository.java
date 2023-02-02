package com.entropyteam.entropay.employees.repositories;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.employees.models.Technology;

public interface TechnologyRepository extends BaseRepository<Technology, UUID> {

    List<Technology> findAllByDeletedIsFalse();

    Set<Technology> findAllByDeletedIsFalseAndIdIn(List<UUID> roleIds);
}