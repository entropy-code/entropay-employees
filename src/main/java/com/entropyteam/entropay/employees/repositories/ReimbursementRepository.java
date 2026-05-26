package com.entropyteam.entropay.employees.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.employees.models.Reimbursement;

public interface ReimbursementRepository extends BaseRepository<Reimbursement, UUID> {

    @Query("""
            SELECT r FROM Reimbursement r
            JOIN FETCH r.category
            JOIN FETCH r.employee
            WHERE r.date BETWEEN :startDate AND :endDate
              AND r.deleted = false
            """)
    List<Reimbursement> findAllBetweenPeriod(LocalDate startDate, LocalDate endDate);
}
