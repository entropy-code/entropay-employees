package com.entropyteam.entropay.employees.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Repository;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.employees.models.Overtime;

@Repository
public interface OvertimeRepository extends BaseRepository<Overtime, UUID> {

    List<Overtime> findByDateBetween(final LocalDate startDate, final LocalDate endDate);
}
