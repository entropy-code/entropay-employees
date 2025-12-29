package com.entropyteam.entropay.employees.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.employees.models.EmployeeFeedbackSummary;

public interface EmployeeFeedbackSummaryRepository extends BaseRepository<EmployeeFeedbackSummary, UUID> {

    List<EmployeeFeedbackSummary> findAllByEmployee_IdAndDeletedIsFalse(UUID employeeId);

    Optional<EmployeeFeedbackSummary> findFirstByEmployee_IdAndDeletedIsFalseOrderByCreatedAtDesc(UUID employeeId);

}
