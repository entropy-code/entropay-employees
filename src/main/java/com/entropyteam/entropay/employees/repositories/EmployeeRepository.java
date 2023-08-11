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

    @Query(value =
            "SELECT e.*\n" +
                    "FROM employee e \n" +
                    "JOIN contract c ON e.id = c.employee_id \n" +
                    "WHERE c.start_date IS NOT NULL\n" +
                    "    AND EXTRACT(MONTH FROM c.start_date) < 7 \n" +
                    "    AND EXTRACT(YEAR FROM c.start_date) = EXTRACT(YEAR FROM CURRENT_DATE) \n" +
                    "    AND c.deleted = false \n" +
                    "    AND e.deleted = false \n" +
                    "    AND e.active = true",
            nativeQuery = true)
    List<Employee> findEmployeeWhereStartDateBeforeJuly();

}
