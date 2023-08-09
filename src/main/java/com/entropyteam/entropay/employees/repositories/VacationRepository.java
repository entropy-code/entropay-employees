package com.entropyteam.entropay.employees.repositories;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.employees.models.Vacation;
import com.entropyteam.entropay.employees.repositories.projections.VacationBalanceByYear;

public interface VacationRepository extends BaseRepository<Vacation, UUID> {

    @Query(value = "SELECT year, CAST((SUM(credit) - COALESCE(SUM(debit), 0)) AS int) AS balance "
            + " FROM Vacation WHERE employee_id = :employeeId AND deleted = false "
            + " GROUP BY year HAVING CAST((SUM(credit) - COALESCE(SUM(debit), 0)) AS int) > 0 ORDER BY year ASC",
            nativeQuery = true)
    List<VacationBalanceByYear> getVacationByYear(@Param("employeeId") UUID employeeId);

    List<Vacation> getVacationByEmployeeIdAndDeletedIsFalseAndCreditOrderByYearDesc(
            @Param("employeeId") UUID employeeId, @Param("credit") Integer credit);

    @Query("SELECT CAST((SUM(v.credit) - COALESCE(SUM(v.debit), 0)) AS int) AS total_balance FROM Vacation v WHERE "
            + " v.employee.id = :employeeId AND v.deleted = false GROUP BY v.employee.id")
    Integer getAvailableDays(@Param("employeeId") UUID employeeId);

    boolean existsVacationByEmployeeIdAndDeletedIsFalseAndYearIsLike(@Param("employeeId") UUID employeeId, @Param("year") String year);
}
