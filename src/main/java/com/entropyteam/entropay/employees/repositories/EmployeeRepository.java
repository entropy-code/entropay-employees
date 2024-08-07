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
}
