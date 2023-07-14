package com.entropyteam.entropay.employees.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.employees.models.Holiday;

public interface HolidayRepository extends BaseRepository<Holiday, UUID> {
    List<Holiday> findAllByDateBetweenAndDeletedFalseOrderByDateAsc(LocalDate startDate, LocalDate endDate);
}