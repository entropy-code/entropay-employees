package com.entropyteam.entropay.employees.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.employees.models.Reimbursement;

public interface ReimbursementRepository extends BaseRepository<Reimbursement, UUID> {

    List<Reimbursement> findAllByEmployeeIdAndDateBetweenAndDeletedIsFalse(
            UUID employeeId, LocalDate from, LocalDate to);

    /**
     * Most recent reimbursements for an employee in a date range, newest first, with the
     * category eagerly fetched so callers can read {@code category.name} without an extra query
     * per row (the association is {@code LAZY}). Pair with a {@link Pageable} to cap the result.
     */
    @Query("""
            SELECT r FROM Reimbursement r
            JOIN FETCH r.category
            WHERE r.employee.id = :employeeId
              AND r.date BETWEEN :from AND :to
              AND r.deleted = false
            ORDER BY r.date DESC NULLS LAST
            """)
    List<Reimbursement> findRecentWithCategoryByEmployee(
            UUID employeeId, LocalDate from, LocalDate to, Pageable pageable);

    @Query("""
            SELECT r FROM Reimbursement r
            JOIN FETCH r.category
            JOIN FETCH r.employee
            WHERE r.date BETWEEN :startDate AND :endDate
              AND r.deleted = false
            """)
    List<Reimbursement> findAllBetweenPeriod(LocalDate startDate, LocalDate endDate);
}
