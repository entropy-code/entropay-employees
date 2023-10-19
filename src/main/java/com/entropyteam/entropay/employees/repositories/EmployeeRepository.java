package com.entropyteam.entropay.employees.repositories;

import java.util.List;
import java.util.UUID;

import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.employees.models.Employee;
import org.springframework.data.jpa.repository.Query;

public interface EmployeeRepository extends BaseRepository<Employee, UUID> {

    List<Employee> findAllByDeletedIsFalse();

    List<Employee> findAllByDeletedIsFalseAndActiveIsTrue();

    @Query("SELECT e FROM Employee e WHERE e.deleted = false AND e.active = true AND NOT EXISTS (SELECT r FROM e.roles r WHERE r.name = 'HR')")
    List<Employee> findAllByDeletedIsFalseAndActiveIsTrueAndRolesNameNotLikeIgnoreCase();

}
