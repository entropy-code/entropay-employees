package com.entropyteam.entropay.employees.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.employees.models.Assignment;
import com.entropyteam.entropay.employees.repositories.projections.MonthlyAssignment;

public interface AssignmentRepository extends BaseRepository<Assignment, UUID> {

    List<Assignment> findAssignmentByEmployee_IdAndDeletedIsFalse(UUID employee_id);

    @Query(value = """
            SELECT a
            FROM Assignment a
            WHERE a.active is TRUE
              AND a.deleted is FALSE
              AND a.employee.id = :employeeId
              AND a.project.id = :projectId
            """)
    Optional<Assignment> findActiveAssignmentByEmployeeAndProject(UUID employeeId, UUID projectId);

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

    @Query(value =
            "SELECT a.* FROM assignment AS a INNER JOIN project AS p ON a.project_id = p.id WHERE p.client_id = "
            + ":clientId AND a.deleted = false "
            + " AND p.deleted = FALSE AND a.active = true", nativeQuery = true)
    List<Assignment> findAllAssignmentsByClientId(@Param("clientId") UUID clientId);

    @Query(value =
            "SELECT a.* FROM assignment AS a INNER JOIN project AS p ON a.project_id = p.id WHERE p.client_id IN "
            + ":clientIds AND a.deleted = false "
            + " AND p.deleted = FALSE AND a.active = true", nativeQuery = true)
    List<Assignment> findAllAssignmentsByClientIdIn(@Param("clientIds") List<UUID> clientIds);

    List<Assignment> findAllByEmployeeIdInAndDeletedIsFalse(List<UUID> employeesId);

    /**
     * This query is used for the Billing. Therefore, we are only including assignments that are full time or part
     *
     * @param startDate start date
     * @param endDate end date
     * @return list of assignments
     */
    @Query(value = """
            FROM Assignment a
            JOIN FETCH a.employee e
            JOIN FETCH e.country co
            JOIN FETCH a.project p
            JOIN FETCH p.client c
            WHERE a.startDate <= :endDate
                AND (a.endDate IS NULL OR a.endDate between :startDate and :endDate)
                AND a.deleted = FALSE
                AND a.engagementType in ('FULL_TIME', 'PART_TIME')
                AND e.active = TRUE""")
    List<Assignment> findAllBetweenPeriod(LocalDate startDate, LocalDate endDate);

    @Query(value = """
            WITH project_periods AS (SELECT MIN(a.start_date) as start_date,
                                            CASE
                                                WHEN COUNT(*) FILTER (WHERE a.end_date IS NULL) > 0
                                                    THEN NOW()
                                                ELSE MAX(a.end_date)
                                                END           as end_date,
                                            a.employee_id     as employee_id,
                                            a.project_id      as project_id
                                     FROM assignment a
                                              JOIN employee e ON e.id = a.employee_id
                                              JOIN project p ON p.id = a.project_id
                                     where a.deleted is false
                                       and e.deleted is false
                                       and p.deleted is false
                                       and a.engagement_type in ('FULL_TIME', 'PART_TIME')
                                     GROUP BY a.employee_id, a.project_id),
            data as (SELECT TO_CHAR(month_series, 'YYYY-MM') as "year-month",
                   pp.employee_id,
                   e.internal_id,
                   e.first_name,
                   e.last_name,
                   p.id                             as project_id,
                   p.name                           as project_name,
                   c.id                             as client_id,
                   c.name                           as client_name
            FROM project_periods pp
                     inner join project p on p.id = pp.project_id
                     inner join client c on p.client_id = c.id
                     inner join employee e on e.id = pp.employee_id
                     CROSS JOIN generate_series(
                    DATE_TRUNC('month', pp.start_date),
                    DATE_TRUNC('month', pp.end_date),
                    INTERVAL '1 month'
                                ) AS month_series)
            select *
            from data
            where "year-month" >= TO_CHAR(CAST(:start_date AS date), 'YYYY-MM')
              and "year-month" <= TO_CHAR(CAST(:end_date AS date), 'YYYY-MM')
            order by "year-month", client_id, project_id, employee_id
            """, nativeQuery = true)
    List<MonthlyAssignment> findMonthlyAssignmentBetweenPeriod(@Param("start_date") LocalDate startDate,
            @Param("end_date") LocalDate endDate);

    @Query("""
            select distinct a.employee.id
            from Assignment a
            join a.project p 
            where a.deleted = false 
              and a.active = true 
              and p.deleted = false
              and p.isInternal = true
            """)
    Set<UUID> findAllInternalEmployeeIds();
}