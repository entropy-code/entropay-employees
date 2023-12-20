package com.entropyteam.entropay.employees.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.employees.models.Holiday;
import org.springframework.data.jpa.repository.Query;

public interface HolidayRepository extends BaseRepository<Holiday, UUID> {

    List<Holiday> findAllByDateBetweenAndDeletedFalseOrderByDateAsc(LocalDate startDate, LocalDate endDate);

    List<Holiday> findAllByDeletedFalseAndYear(int year);

    @Query(value = "SELECT DISTINCT extract('Year' FROM date) AS year FROM holiday_calendar WHERE deleted=false "
            + " ORDER BY year ASC", nativeQuery = true)
    List<Integer> getHolidaysYears();
}
