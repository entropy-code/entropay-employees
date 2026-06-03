package com.entropyteam.entropay.employees.repositories;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.employees.models.EmployeeFeedback;

public interface EmployeeFeedbackRepository extends BaseRepository<EmployeeFeedback, UUID> {

    List<EmployeeFeedback> findAllByEmployee_IdAndDeletedIsFalse(UUID employeeId);

    /**
     * Most recent feedbacks for an employee, newest first. Used by the summary, which only needs
     * a handful of lightweight highlights — pair with a {@link Pageable} to cap the result so we
     * neither load the full history nor map rows we will discard.
     */
    @Query("""
            SELECT f FROM EmployeeFeedback f
            WHERE f.employee.id = :employeeId
              AND f.deleted = false
            ORDER BY f.feedbackDate DESC NULLS LAST
            """)
    List<EmployeeFeedback> findRecentByEmployee(UUID employeeId, Pageable pageable);

}