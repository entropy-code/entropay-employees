package com.entropyteam.entropay.employees.repositories;

import java.time.LocalDate;
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
}
