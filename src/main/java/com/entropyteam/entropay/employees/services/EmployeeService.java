package com.entropyteam.entropay.employees.services;

import java.util.Objects;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.common.BaseService;
import com.entropyteam.entropay.employees.dtos.EmployeeDto;
import com.entropyteam.entropay.employees.models.Company;
import com.entropyteam.entropay.employees.models.Employee;
import com.entropyteam.entropay.employees.repositories.CompanyRepository;
import com.entropyteam.entropay.employees.repositories.EmployeeRepository;

@Service
public class EmployeeService extends BaseService<Employee, EmployeeDto, UUID> {

    private final EmployeeRepository employeeRepository;
    private final CompanyRepository companyRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, CompanyRepository companyRepository) {
        this.employeeRepository = Objects.requireNonNull(employeeRepository);
        this.companyRepository = companyRepository;
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
        Company company = companyRepository.findById(entity.companyId()).orElseThrow();
        Employee employee = new Employee(entity);
        employee.setCompany(company);
        return employee;
    }
}
