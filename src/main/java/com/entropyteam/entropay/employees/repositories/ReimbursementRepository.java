package com.entropyteam.entropay.employees.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.employees.models.Reimbursement;

public interface ReimbursementRepository extends BaseRepository<Reimbursement, UUID> {

    List<Reimbursement> findAllByEmployeeIdAndDateBetweenAndDeletedIsFalse(
            UUID employeeId, LocalDate from, LocalDate to);
}