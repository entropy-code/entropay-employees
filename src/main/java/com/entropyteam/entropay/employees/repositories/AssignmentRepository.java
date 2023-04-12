package com.entropyteam.entropay.employees.repositories;

import java.util.List;
import java.util.UUID;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.employees.models.Assignment;

public interface AssignmentRepository extends BaseRepository<Assignment, UUID> {

    List<Assignment> findAssignmentByEmployee_IdAndDeletedIsFalse(UUID employee_id);
}
