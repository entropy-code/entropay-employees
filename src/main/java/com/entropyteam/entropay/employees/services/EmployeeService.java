package com.entropyteam.entropay.employees.services;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import com.entropyteam.entropay.employees.repositories.EmployeeRepository;
import com.entropyteam.entropay.employees.repositories.PaymentInformationRepository;
import com.entropyteam.entropay.employees.repositories.RoleRepository;
import com.entropyteam.entropay.employees.repositories.TechnologyRepository;
import com.entropyteam.entropay.employees.repositories.AssignmentRepository;
import com.entropyteam.entropay.employees.repositories.ContractRepository;
import com.entropyteam.entropay.employees.repositories.VacationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.common.BaseService;
import com.entropyteam.entropay.common.ReactAdminMapper;
import com.entropyteam.entropay.employees.dtos.EmployeeDto;
import com.entropyteam.entropay.employees.models.Assignment;
import com.entropyteam.entropay.employees.models.Contract;
import com.entropyteam.entropay.employees.models.Employee;
import com.entropyteam.entropay.employees.models.PaymentInformation;
import com.entropyteam.entropay.employees.models.Role;
import com.entropyteam.entropay.employees.models.Technology;


@Service
public class EmployeeService extends BaseService<Employee, EmployeeDto, UUID> {

    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final PaymentInformationRepository paymentRepository;
    private final PaymentInformationService paymentInformationService;
    private final TechnologyRepository technologyRepository;
    private final AssignmentRepository assignmentRepository;
    private final ContractRepository contractRepository;
    private final VacationRepository vacationRepository;


    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, RoleRepository roleRepository,
            PaymentInformationRepository paymentInformationRepository,
            PaymentInformationService paymentInformationService, TechnologyRepository technologyRepository,
            AssignmentRepository assignmentRepository, ContractRepository contractRepository,
            ReactAdminMapper reactAdminMapper, VacationRepository vacationRepository) {
        super(Employee.class, reactAdminMapper);
        this.employeeRepository = employeeRepository;
        this.roleRepository = roleRepository;
        this.paymentRepository = paymentInformationRepository;
        this.paymentInformationService = paymentInformationService;
        this.technologyRepository = technologyRepository;
        this.assignmentRepository = assignmentRepository;
        this.contractRepository = contractRepository;
        this.vacationRepository = vacationRepository;
    }

    @Override
    protected BaseRepository<Employee, UUID> getRepository() {
        return employeeRepository;
    }

    @Override
    protected EmployeeDto toDTO(Employee entity) {
        List<PaymentInformation> paymentInformationList =
                paymentRepository.findAllByEmployeeIdAndDeletedIsFalse(entity.getId());
        Optional<Assignment> assignment =
                assignmentRepository.findAssignmentByEmployeeIdAndActiveIsTrueAndDeletedIsFalse(entity.getId());
        List<Contract> contracts = contractRepository.findAllByEmployeeIdAndDeletedIsFalse(entity.getId());
        Optional<Contract> firstContract = contracts.stream().min(Comparator.comparing(Contract::getStartDate));
        Integer availableDays = vacationRepository.getAvailableDays(entity.getId());
        Optional<Contract> activeContract =
                contractRepository.findContractByEmployeeIdAndActiveIsTrueAndDeletedIsFalse(entity.getId());
        return new EmployeeDto(entity, paymentInformationList, assignment.orElse(null), firstContract.orElse(null), availableDays,activeContract.orElse(null));
    }

    @Override
    protected Employee toEntity(EmployeeDto dto) {
        Employee employee = new Employee(dto);
        Set<Role> roles = roleRepository.findAllByDeletedIsFalseAndIdIn(dto.profile());
        Set<Technology> technologies = technologyRepository.findAllByDeletedIsFalseAndIdIn(dto.technologies());
        employee.setRoles(roles);
        employee.setTechnologies(technologies);
        employee.setPaymentsInformation(dto.paymentInformation() == null ? Collections.emptySet()
                : dto.paymentInformation().stream().map(PaymentInformation::new).collect(Collectors.toSet()));
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


        if (shouldDeactivateEmployee(employeeId, entityToUpdate)) {

            List<Contract> employeeContracts = contractRepository.findAllByEmployeeIdAndDeletedIsFalse(employeeId);
            List<Assignment> employeeAssignments = assignmentRepository.findAssignmentByEmployee_IdAndDeletedIsFalse(employeeId);

            employeeContracts.forEach(contract -> {
                contract.setActive(false);
                contract.setEndDate(LocalDate.now());
            });
            contractRepository.saveAll(employeeContracts);

            employeeAssignments.forEach(assignment -> {
                assignment.setActive(false);
                assignment.setEndDate(LocalDate.now());
            });
            assignmentRepository.saveAll(employeeAssignments);
        }

        Employee savedEntity = getRepository().save(entityToUpdate);
        paymentInformationService.updatePaymentsInformation(employeeDto.paymentInformation(), savedEntity);
        return toDTO(savedEntity);
    }

    @Override
    @Transactional
    public EmployeeDto delete(UUID employeeId) {
        Employee employee = getRepository().findById(employeeId).orElseThrow();
        employee.setDeleted(true);
        employee.setActive(false);
        List<Contract> employeeContracts = contractRepository.findAllByEmployeeIdAndDeletedIsFalse(employeeId);
        List<Assignment> employeeAssignment = assignmentRepository.findAssignmentByEmployee_IdAndDeletedIsFalse(employeeId);

        employeeContracts.forEach(contract -> {
                    contract.setDeleted(true);
                    contract.setActive(false);
                    contract.setEndDate(LocalDate.now());
                });
        contractRepository.saveAll(employeeContracts);

        employeeAssignment.forEach(assignment -> {
                    assignment.setDeleted(true);
                    assignment.setActive(false);
                    assignment.setEndDate(LocalDate.now());
                });
        assignmentRepository.saveAll(employeeAssignment);

        return toDTO(employee);
    }

    @Override
    public List<String> getColumnsForSearch() {
        return Arrays.asList("firstName", "lastName", "internalId");
    }

    private boolean shouldDeactivateEmployee(UUID employeeId, Employee entityToUpdate) {
        Employee existingEmployee = getRepository().getById(employeeId);
        return existingEmployee.isActive() && !entityToUpdate.isActive();
    }

}
