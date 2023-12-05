package com.entropyteam.entropay.employees.repositories;

import java.util.List;
import java.util.UUID;

import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.employees.models.Employee;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmployeeRepository extends BaseRepository<Employee, UUID> {

    List<Employee> findAllByDeletedIsFalse();

    List<Employee> findAllByDeletedIsFalseAndActiveIsTrue();

    @Query(value = "SELECT * FROM Employee inner join Contract on Employee.id = Contract.employee_id WHERE Contract.active = true", nativeQuery = true)
    List<Employee> getEmployeesWithAtLeastAnActiveContract();
}
