package com.entropyteam.entropay.employees.repositories;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.employees.models.Benefit;

public interface BenefitRepository extends BaseRepository<Benefit, UUID> {

    List<Benefit> findAllByDeletedIsFalse();
    
    Set<Benefit> findAllByDeletedIsFalseAndIdIn(List<UUID> benefitIds);
}
