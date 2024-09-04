package com.entropyteam.entropay.employees.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.employees.models.Assignment;

public interface AssignmentRepository extends BaseRepository<Assignment, UUID> {

    List<Assignment> findAssignmentByEmployee_IdAndDeletedIsFalse(UUID employee_id);

    Optional<Assignment> findAssignmentByEmployeeIdAndActiveIsTrueAndDeletedIsFalse(UUID employee_id);

    List<Assignment> findAllByDeletedIsFalseAndActiveIsTrueAndEndDateLessThan(LocalDate date);

    @Query(value = """
            SELECT * FROM assignment a
                     WHERE start_date <= :date AND (end_date IS NULL OR end_date >= :date)
                       AND active = FALSE
                       AND deleted = FALSE
                    AND employee_id NOT IN (
                        select employee_id
                        FROM assignment a2
                        WHERE a2.active = TRUE
                          AND a2.deleted = FALSE)""", nativeQuery = true)
    List<Assignment> findAllAssignmentsToActivateInDate(@Param("date") LocalDate date);

    @Query(value = "SELECT a.* FROM assignment AS a INNER JOIN project AS p ON a.project_id = p.id WHERE p.client_id = :clientId AND a.deleted = false " +
            " AND p.deleted = FALSE AND a.active = true", nativeQuery = true)
    List<Assignment> findAllAssignmentsByClientId(@Param("clientId") UUID clientId);

    @Query(value = "SELECT a.* FROM assignment AS a INNER JOIN project AS p ON a.project_id = p.id WHERE p.client_id IN :clientIds AND a.deleted = false " +
            " AND p.deleted = FALSE AND a.active = true", nativeQuery = true)
    List<Assignment> findAllAssignmentsByClientIdIn(@Param("clientIds") List<UUID> clientIds);

    List<Assignment> findAllByEmployeeIdInAndDeletedIsFalse(List<UUID> employeesId);
    
}