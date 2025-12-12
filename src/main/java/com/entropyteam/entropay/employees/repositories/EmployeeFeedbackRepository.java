package com.entropyteam.entropay.employees.repositories;

import java.util.List;
import java.util.UUID;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.employees.models.EmployeeFeedback;

public interface EmployeeFeedbackRepository extends BaseRepository<EmployeeFeedback, UUID> {

    List<EmployeeFeedback> findAllByEmployee_IdAndDeletedFalse(UUID employeeId);
}