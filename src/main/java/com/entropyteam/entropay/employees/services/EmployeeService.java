package com.entropyteam.entropay.employees.services;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import com.entropyteam.entropay.employees.models.Role;
import com.entropyteam.entropay.employees.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.common.BaseService;
import com.entropyteam.entropay.employees.dtos.EmployeeDto;
import com.entropyteam.entropay.employees.models.Employee;
import com.entropyteam.entropay.employees.repositories.EmployeeRepository;

@Service
public class EmployeeService extends BaseService<Employee, EmployeeDto, UUID> {

    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, RoleRepository roleRepository) {
        this.employeeRepository = Objects.requireNonNull(employeeRepository);
        this.roleRepository = roleRepository;
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
        Employee employee = new Employee(entity);
        List<Role> roles = roleRepository.findAllByDeletedIsFalseAndIdIn(entity.profile());
        employee.setRolesList(roles);
        return employee;
    }
}
