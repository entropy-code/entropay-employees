package com.entropyteam.entropay.employees.repositories;

import java.util.Optional;
import java.util.UUID;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.employees.models.EmployeeEducation;

public interface EmployeeEducationRepository extends BaseRepository<EmployeeEducation, UUID> {

    Optional<EmployeeEducation> findByEmployeeIdAndDeletedFalse(UUID employeeId);
}
