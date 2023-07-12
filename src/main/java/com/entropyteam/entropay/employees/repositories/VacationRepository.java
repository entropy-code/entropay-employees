package com.entropyteam.entropay.employees.repositories;

import java.util.UUID;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.employees.models.Vacation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VacationRepository extends BaseRepository<Vacation, UUID> {

    @Query("SELECT CAST((SUM(v.credit) - COALESCE(SUM(v.debit), 0)) AS int) AS total_balance FROM Vacation v WHERE v.employee.id = :employeeId AND v.deleted = false GROUP BY v.employee.id")
    Integer getAvailableDays(@Param("employeeId") UUID employeeId);

}
