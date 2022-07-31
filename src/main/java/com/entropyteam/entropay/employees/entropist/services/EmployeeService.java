package com.entropyteam.entropay.employees.entropist.services;

import java.util.Objects;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.entropyteam.entropay.employees.common.BaseRepository;
import com.entropyteam.entropay.employees.common.BaseService;
import com.entropyteam.entropay.employees.entropist.dtos.EmployeeDto;
import com.entropyteam.entropay.employees.entropist.models.Employee;
import com.entropyteam.entropay.employees.entropist.repositories.EmployeeRepository;

@Service
public class EmployeeService extends BaseService<Employee, EmployeeDto, UUID> {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = Objects.requireNonNull(employeeRepository);
    }

    @Override
    protected BaseRepository<Employee, UUID> getRepository() {
        return employeeRepository;
    }

    @Override
    protected EmployeeDto toDTO(Employee entity) {
        return new EmployeeDto(entity);
    }

    @Override
    protected Employee toEntity(EmployeeDto entity) {
        return new Employee(entity);
    }
}
