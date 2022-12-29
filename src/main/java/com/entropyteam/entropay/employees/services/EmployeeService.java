package com.entropyteam.entropay.employees.services;


import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.entropyteam.entropay.employees.models.PaymentInformation;
import com.entropyteam.entropay.employees.models.Role;
import com.entropyteam.entropay.employees.repositories.PaymentInformationRepository;
import com.entropyteam.entropay.employees.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.common.BaseService;
import com.entropyteam.entropay.employees.dtos.EmployeeDto;
import com.entropyteam.entropay.employees.models.Employee;
import com.entropyteam.entropay.employees.repositories.EmployeeRepository;
import org.springframework.transaction.annotation.Transactional;



@Service
public class EmployeeService extends BaseService<Employee, EmployeeDto, UUID> {

    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final PaymentInformationRepository paymentRepository;
    private final PaymentInformationService paymentInformationService;



    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, RoleRepository roleRepository, PaymentInformationRepository paymentInformationRepository, PaymentInformationService paymentInformationService) {
        this.employeeRepository = employeeRepository;
        this.roleRepository = roleRepository;
        this.paymentRepository = paymentInformationRepository;
        this.paymentInformationService = paymentInformationService;
    }

    @Override
    protected BaseRepository<Employee, UUID> getRepository() {
        return employeeRepository;
    }
    @Transactional
    @Override
    protected EmployeeDto toDTO(Employee entity) {
        List<PaymentInformation> paymentInformationList = paymentRepository.findAllByEmployeeIdAndDeletedIsFalse(entity.getId());
        return new EmployeeDto(entity,paymentInformationList);
    }

    @Override
    protected Employee toEntity(EmployeeDto entity) {
        Employee employee = new Employee(entity);
        Set<Role> roles = roleRepository.findAllByDeletedIsFalseAndIdIn(entity.profile());
        employee.setRoles(roles);
        return employee;
    }

    @Override
    @Transactional
    public EmployeeDto create(EmployeeDto employeeDto){
        Employee entityToCreate = toEntity(employeeDto);
        Employee savedEntity = getRepository().save(entityToCreate);
        paymentInformationService.create(employeeDto.paymentInformation(),savedEntity);
        return toDTO(savedEntity);
    }

    @Override
    @Transactional
    public EmployeeDto update(UUID employeeId, EmployeeDto employeeDto) {
        Employee entityToUpdate = toEntity(employeeDto);
        entityToUpdate.setId(employeeId);
        Employee savedEntity = getRepository().save(entityToUpdate);
        paymentInformationService.update(employeeDto.paymentInformation(),savedEntity);
        return toDTO(savedEntity);
    }

}
