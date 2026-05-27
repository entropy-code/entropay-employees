package com.entropyteam.entropay.employees.repositories;

import java.util.List;
import java.util.UUID;

import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.employees.models.Employee;
import org.springframework.data.jpa.repository.Query;

public interface EmployeeRepository extends BaseRepository<Employee, UUID>, ReportRepository {

    List<Employee> findAllByDeletedIsFalse();

    List<Employee> findAllByDeletedIsFalseAndActiveIsTrue();

    @Query(value = "SELECT e.* FROM Employee AS e INNER JOIN Contract AS c ON e.id = c.employee_id WHERE c.active = true AND c.deleted = false AND e.deleted = false",
            nativeQuery = true)
    List<Employee> getEmployeesWithAtLeastAnActiveContract();

    List<Employee> findAllByIdInAndDeletedIsFalse(List<UUID> employeesIds);

    @Query(value = "SELECT e.* FROM Employee AS e WHERE e.deleted = false AND e.active = true AND NOT EXISTS(" +
            "SELECT * FROM Assignment AS a WHERE e.id = a.employee_id AND a.active = true AND a.deleted = false)",
            nativeQuery = true)
    List<Employee> findAllUnassignedEmployees();

    @Query(value = "SELECT nextval('employee_internal_id_seq')", nativeQuery = true)
    Long nextInternalIdSequenceValue();

    /**
     * Returns what the next nextval() would produce, without advancing the sequence.
     *
     * A Postgres sequence stores last_value (the most recently handed-out number) and is_called
     * (false right after CREATE/RESTART, true after any nextval). The next value is:
     *   - last_value     when is_called = false (the sequence has not been used yet)
     *   - last_value + 1 when is_called = true  (last_value was already handed out)
     *
     * Used by the create-employee form to preview the ID before submit. It's best-effort —
     * concurrent creates may bump the sequence between peek and submit, in which case the
     * backend's own nextval at create time wins.
     */
    @Query(value = "SELECT last_value + CASE WHEN is_called THEN 1 ELSE 0 END FROM employee_internal_id_seq",
            nativeQuery = true)
    Long peekInternalIdSequenceValue();
}
