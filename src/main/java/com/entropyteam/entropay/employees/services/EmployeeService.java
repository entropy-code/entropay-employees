package com.entropyteam.entropay.employees.services;

import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.common.BaseService;
import com.entropyteam.entropay.common.ReactAdminMapper;
import com.entropyteam.entropay.employees.dtos.EmployeeDto;
import com.entropyteam.entropay.employees.models.Employee;
import com.entropyteam.entropay.employees.models.PaymentInformation;
import com.entropyteam.entropay.employees.models.Role;
import com.entropyteam.entropay.employees.models.Technology;
import com.entropyteam.entropay.employees.repositories.EmployeeRepository;
import com.entropyteam.entropay.employees.repositories.PaymentInformationRepository;
import com.entropyteam.entropay.employees.repositories.RoleRepository;
import com.entropyteam.entropay.employees.repositories.TechnologyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;


@Service
public class EmployeeService extends BaseService<Employee, EmployeeDto, UUID> {

    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final PaymentInformationRepository paymentRepository;
    private final PaymentInformationService paymentInformationService;
    private final TechnologyRepository technologyRepository;


    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, RoleRepository roleRepository,
            PaymentInformationRepository paymentInformationRepository,
            PaymentInformationService paymentInformationService, TechnologyRepository technologyRepository,
            ReactAdminMapper reactAdminMapper) {
        super(Employee.class, reactAdminMapper);
        this.employeeRepository = employeeRepository;
        this.roleRepository = roleRepository;
        this.paymentRepository = paymentInformationRepository;
        this.paymentInformationService = paymentInformationService;
        this.technologyRepository = technologyRepository;
    }

    @Override
    protected BaseRepository<Employee, UUID> getRepository() {
        return employeeRepository;
    }

    @Override
    protected EmployeeDto toDTO(Employee entity) {
        List<PaymentInformation> paymentInformationList =
                paymentRepository.findAllByEmployeeIdAndDeletedIsFalse(entity.getId());
        return new EmployeeDto(entity, paymentInformationList);
    }

    @Override
    protected Employee toEntity(EmployeeDto entity) {
        Employee employee = new Employee(entity);
        Set<Role> roles = roleRepository.findAllByDeletedIsFalseAndIdIn(entity.profile());
        Set<Technology> technologies = technologyRepository.findAllByDeletedIsFalseAndIdIn(entity.technologies());

        employee.setRoles(roles);
        employee.setTechnologies(technologies);
        employee.setPaymentsInformation(entity.paymentInformation() == null ?  Collections.emptySet() : entity.paymentInformation().stream().map(PaymentInformation::new).collect(Collectors.toSet()));
        return employee;
    }

    @Override
    @Transactional
    public EmployeeDto create(EmployeeDto employeeDto) {
        Employee entityToCreate = toEntity(employeeDto);
        Employee savedEntity = getRepository().save(entityToCreate);
        paymentInformationService.createPaymentsInformation(savedEntity.getPaymentsInformation(), savedEntity);
        return toDTO(savedEntity);
    }

    @Override
    @Transactional
    public EmployeeDto update(UUID employeeId, EmployeeDto employeeDto) {
        Employee entityToUpdate = toEntity(employeeDto);
        entityToUpdate.setId(employeeId);
        Employee savedEntity = getRepository().save(entityToUpdate);
        paymentInformationService.updatePaymentsInformation(employeeDto.paymentInformation(), savedEntity);
        return toDTO(savedEntity);
    }

    @Override
    public List<String> getColumnsForSearch() {
        return Arrays.asList("firstName","lastName","internalId");
    }

}
