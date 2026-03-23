package com.entropyteam.entropay.employees.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.employees.models.EmployeeEducation;

public interface EmployeeEducationRepository extends BaseRepository<EmployeeEducation, UUID> {

    @Query("""
            SELECT ee
            FROM EmployeeEducation ee
            WHERE ee.deleted = false
              AND ee.employee.id = ?1""")
    List<EmployeeEducation> findAllByEmployeeIdAndDeletedIsFalse(UUID employeeId);

    @Query("""
            SELECT ee
            FROM EmployeeEducation ee
            WHERE ee.deleted = false
              AND ee.employee.id = ?1""")
    Optional<EmployeeEducation> findByEmployeeIdAndDeletedIsFalse(UUID employeeId);
}
