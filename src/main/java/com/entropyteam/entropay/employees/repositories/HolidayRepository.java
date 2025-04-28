package com.entropyteam.entropay.employees.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.employees.models.Holiday;

public interface HolidayRepository extends BaseRepository<Holiday, UUID> {

    List<Holiday> findAllByDateBetweenAndDeletedFalseOrderByDateAsc(LocalDate startDate, LocalDate endDate);

    @Query(value = "SELECT DISTINCT extract('Year' FROM date) AS year FROM holiday_calendar WHERE deleted=false "
                   + " ORDER BY year ASC", nativeQuery = true)
    List<Integer> getHolidaysYears();

    @Query(value = """
            SELECT hc.*
            FROM holiday_calendar AS hc
                INNER JOIN country AS c ON hc.country_id = c.id
            WHERE (c.id = :countryId or c.name = 'ALL' )
                AND hc.date BETWEEN :startDate AND :endDate 
                AND hc.deleted = false 
            ORDER BY hc.date ASC""", nativeQuery = true)
    List<Holiday> findHolidaysByCountryAndPeriod(@Param("countryId") UUID countryId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query(value = """
              SELECT h
              FROM Holiday h
              JOIN FETCH h.country c
              WHERE
                c.name = :countryName
                AND h.date between :startDate and :endDate
                AND h.deleted = false
                AND c.deleted = false""")
    List<Holiday> findHolidaysByCountryAndPeriod(@Param("countryName") String countryName,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("""
            SELECT h
            FROM Holiday h
            JOIN FETCH h.country c
            WHERE
              h.date between :startDate and :endDate
              AND h.deleted = false""")
    List<Holiday> findAllBetweenPeriod(LocalDate startDate, LocalDate endDate);
}
