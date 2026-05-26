package com.entropyteam.entropay.employees.repositories;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.employees.models.Contract;

public interface ContractRepository extends BaseRepository<Contract, UUID> {

    List<Contract> findAllByEmployeeIdAndDeletedIsFalse(UUID employeeId);

    Optional<Contract> findContractByEmployeeIdAndActiveIsTrueAndDeletedIsFalse(UUID employeeId);

    List<Contract> findAllByDeletedIsFalseAndActiveIsTrueAndEndDateLessThan(LocalDate date);

    @Query(value = "SELECT * FROM contract c WHERE start_date <= :date AND (end_date IS NULL OR end_date >= "
            + ":date) AND active = FALSE AND deleted = FALSE "
            + "AND employee_id NOT IN (select employee_id FROM contract c2 WHERE c2.active = TRUE AND c2.deleted = "
            + "FALSE)", nativeQuery = true)
    List<Contract> findAllContractsToActivateInDate(@Param("date") LocalDate date);


    @Query(value = """
            FROM Contract c
            JOIN FETCH c.employee
            JOIN FETCH c.paymentsSettlement
            WHERE c.startDate <= :endDate
              AND (c.endDate is NULL or c.endDate between :startDate and :endDate)
              AND c.deleted = false
            """)
    List<Contract> findAllBetweenPeriod(LocalDate startDate, LocalDate endDate);

    /**
     * For payroll: contracts whose date range overlaps the period AND that are either currently
     * active OR ended within the period (final settlement scenario — inactive contracts whose
     * endDate falls in the month still need to be paid out for the days actually worked).
     *
     * Inactive contracts with no endDate or with endDate outside the period are excluded — they're
     * historical records that should not be liquidated. Excludes soft-deleted employees too.
     */
    @Query(value = """
            SELECT DISTINCT c FROM Contract c
            JOIN FETCH c.employee e
            LEFT JOIN FETCH e.country
            JOIN FETCH c.paymentsSettlement
            WHERE c.startDate <= :periodEnd
              AND (c.endDate IS NULL OR c.endDate >= :periodStart)
              AND c.deleted = false
              AND e.deleted = false
              AND (c.active = true
                   OR (c.endDate IS NOT NULL
                       AND c.endDate BETWEEN :periodStart AND :periodEnd))
            """)
    List<Contract> findAllOverlappingPeriodForPayroll(LocalDate periodStart, LocalDate periodEnd);

    /**
     * For payroll's hardware-clawback rule: hire date per employee, defined as the earliest
     * non-deleted contract start. Pre-loaded into PayrollContext so the calculator never has to
     * touch the Employee.contracts lazy collection from a thread without a Hibernate session.
     */
    @Query("""
            SELECT c.employee.id, MIN(c.startDate)
            FROM Contract c
            WHERE c.employee.id IN :employeeIds
              AND c.deleted = false
            GROUP BY c.employee.id
            """)
    List<Object[]> findEarliestStartDateByEmployeeIds(@Param("employeeIds") Collection<UUID> employeeIds);
}
