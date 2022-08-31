package com.entropyteam.entropay.employees.repositories;

import java.util.List;
import java.util.UUID;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.employees.models.Employee;

public interface EmployeeRepository extends BaseRepository<Employee, UUID> {

    List<Employee> findAllByDeletedIsFalse();
}
