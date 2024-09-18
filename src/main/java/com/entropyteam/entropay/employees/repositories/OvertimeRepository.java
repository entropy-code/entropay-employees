package com.entropyteam.entropay.employees.repositories;

import java.util.UUID;
import org.springframework.stereotype.Repository;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.employees.models.Overtime;

@Repository
public interface OvertimeRepository extends BaseRepository<Overtime, UUID> {

}
