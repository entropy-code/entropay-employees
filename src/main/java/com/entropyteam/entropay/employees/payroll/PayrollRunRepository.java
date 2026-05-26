package com.entropyteam.entropay.employees.payroll;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import com.entropyteam.entropay.common.BaseRepository;

interface PayrollRunRepository extends BaseRepository<PayrollRun, UUID> {

    Optional<PayrollRun> findByPeriodAndDeletedIsFalse(LocalDate period);

    List<PayrollRun> findAllByPeriodAndStatusInAndDeletedIsFalse(LocalDate period, List<PayrollRunStatus> statuses);

    List<PayrollRun> findAllByStatusAndDeletedIsFalse(PayrollRunStatus status);
}
