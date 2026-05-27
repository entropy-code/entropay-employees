package com.entropyteam.entropay.employees.payroll;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import com.entropyteam.entropay.common.BaseRepository;

interface PayrollItemRepository extends BaseRepository<PayrollItem, UUID> {

    /**
     * Items for a run, with the employee eagerly fetched. The run detail maps every item to a DTO
     * that reads employee fields (internalId, first/last name); fetching the employee here keeps
     * that a single query instead of one lazy load per item.
     */
    @Query("""
            SELECT i FROM PayrollItem i
            JOIN FETCH i.employee
            WHERE i.payrollRun.id = :payrollRunId
              AND i.deleted = false
            """)
    List<PayrollItem> findAllByPayrollRunIdAndDeletedIsFalse(UUID payrollRunId);
}
