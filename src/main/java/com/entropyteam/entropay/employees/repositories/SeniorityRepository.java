package com.entropyteam.entropay.employees.repositories;

import java.util.List;
import java.util.UUID;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.employees.models.Seniority;

public interface SeniorityRepository extends BaseRepository<Seniority, UUID> {

    List<Seniority> findAllByDeletedIsFalse();
}
