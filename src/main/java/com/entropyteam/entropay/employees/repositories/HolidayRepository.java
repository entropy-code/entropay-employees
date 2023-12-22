package com.entropyteam.entropay.employees.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.employees.models.Holiday;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HolidayRepository extends BaseRepository<Holiday, UUID> {

    List<Holiday> findAllByDateBetweenAndDeletedFalseOrderByDateAsc(LocalDate startDate, LocalDate endDate);

    @Query("SELECT h FROM Holiday h WHERE h.deleted = false AND YEAR(h.date) = :year")
    List<Holiday> findAllByDeletedFalseAndYear(@Param("year") int year);

    @Query(value = "SELECT DISTINCT extract('Year' FROM date) AS year FROM holiday_calendar WHERE deleted=false "
            + " ORDER BY year ASC", nativeQuery = true)
    List<Integer> getHolidaysYears();
    @Query(value = "SELECT hc.* FROM holiday_calendar AS hc INNER JOIN country AS c ON hc.country_id = c.id " +
            " WHERE (c.name LIKE :country or c.name LIKE 'ALL' ) AND hc.date BETWEEN :startDate AND :endDate AND hc.deleted = false ORDER BY hc.date ASC", nativeQuery = true)
    List<Holiday> findHolidaysByCountryAndPeriod(@Param("country") String country,
                                                 @Param("startDate") LocalDate startDate,
                                                 @Param("endDate") LocalDate endDate);
}
