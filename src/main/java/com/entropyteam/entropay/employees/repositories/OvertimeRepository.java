package com.entropyteam.entropay.employees.repositories;

import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.employees.models.Overtime;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface OvertimeRepository extends BaseRepository<Overtime, UUID> {

}
