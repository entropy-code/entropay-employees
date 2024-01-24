package com.entropyteam.entropay.employees.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.employees.models.Pto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PtoRepository extends BaseRepository<Pto, UUID> {
    @Query(value = "SELECT DATE(start_date) FROM pto " +
            "WHERE employee_id = :employeeId AND deleted = false " +
            "AND DATE(start_date) >= CURRENT_DATE " +
            "ORDER BY ABS(EXTRACT(EPOCH FROM (start_date - CURRENT_TIMESTAMP))) " +
            "LIMIT 1", nativeQuery = true)
    LocalDate findNearestPto(@Param("employeeId") UUID employeeId);

    @Query("SELECT p FROM Pto p " +
            "WHERE p.deleted = false " +
            "AND p.status = 'APPROVED'")
    List<Pto> findAllByDeletedIsFalseAndStatusIsApproved();

    @Query(value = "SELECT * FROM pto " +
            "WHERE deleted = false " +
            "AND status = 'APPROVED' " +
            "AND EXTRACT(YEAR FROM start_date) = :year " +
            "OR EXTRACT(YEAR FROM end_date) = :year", nativeQuery = true)
    List<Pto> findAllByDeletedIsFalseAndStatusIsApprovedForYear(@Param("year") int year);

    @Query(value = "SELECT DISTINCT extract('Year' FROM start_date) AS year FROM pto WHERE deleted=false "
            + " ORDER BY year ASC", nativeQuery = true)
    List<Integer> getPtosYears();
}
