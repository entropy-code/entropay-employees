package com.entropyteam.entropay.employees.services;


import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.ArrayList;
import com.entropyteam.entropay.employees.dtos.EmployeeReportDto;
import com.entropyteam.entropay.employees.models.Assignment;
import com.entropyteam.entropay.employees.models.Contract;
import com.entropyteam.entropay.employees.models.Employee;
import com.entropyteam.entropay.employees.models.Role;
import com.entropyteam.entropay.employees.models.Technology;
import com.entropyteam.entropay.employees.models.Project;
import com.entropyteam.entropay.employees.models.Currency;
import com.entropyteam.entropay.employees.repositories.EmployeeRepository;
import com.entropyteam.entropay.employees.repositories.RoleRepository;
import com.entropyteam.entropay.employees.repositories.TechnologyRepository;
import com.entropyteam.entropay.employees.repositories.AssignmentRepository;
import com.entropyteam.entropay.employees.repositories.ContractRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.PageImpl;



@Service
public class ReportService {

    private final RoleRepository roleRepository;
    private final TechnologyRepository technologyRepository;
    private final AssignmentRepository assignmentRepository;
    private final ContractRepository contractRepository;
    private final EmployeeRepository employeeRepository;


    @Autowired
    public ReportService(RoleRepository roleRepository, TechnologyRepository technologyRepository,
                         AssignmentRepository assignmentRepository, ContractRepository contractRepository, EmployeeRepository employeeRepository) {
        this.roleRepository = roleRepository;
        this.technologyRepository = technologyRepository;
        this.assignmentRepository = assignmentRepository;
        this.contractRepository = contractRepository;
        this.employeeRepository = employeeRepository;
    }
    public Page<EmployeeReportDto> getEmployeesReport() {
        List<Employee> employeesList = employeeRepository.findAllByDeletedIsFalse();
        Map<UUID, List<Contract>> employeeContractsMap = contractRepository.findAllByDeletedIsFalse()
                .stream()
                .collect(Collectors.groupingBy(c -> c.getEmployee().getId()));
        List<Role> employeeRolesList = roleRepository.findAllByDeletedIsFalse();
        List<Technology> employeeTechnologiesList = technologyRepository.findAllByDeletedIsFalse();
        Map<UUID, List<Assignment>> employeeAssignmentsMap = assignmentRepository.findAllByDeletedIsFalse()
                .stream()
                .collect(Collectors.groupingBy(a -> a.getEmployee().getId()));
        List<EmployeeReportDto> employeesReportDtoList = new ArrayList<>();

        for (Employee employee : employeesList) {
            List<Contract> employeeContracts = employeeContractsMap.getOrDefault(employee.getId(), Collections.emptyList());
            List<Role> employeeRoles = employeeRolesList.stream().filter(t -> t.getEmployees().contains(employee)).toList();
            List<String> technologiesName = employeeTechnologiesList.stream()
                    .filter(t -> t.getEmployees().contains(employee))
                    .map(Technology::getName)
                    .toList();
            List<Assignment> employeeAssignments = employeeAssignmentsMap.getOrDefault(employee.getId(), Collections.emptyList());
            List<String> profile = employeeRoles.stream().map(Role::getName).toList();

            Optional<Contract> firstContract = employeeContracts.stream().min(Comparator.comparing(Contract::getStartDate));
            Optional<Contract> activeContract = employeeContracts.stream().filter(Contract::isActive).findFirst();
            Optional<Assignment> lastAssignment = employeeAssignments.stream().filter(Assignment::isActive).findFirst();

            String client = lastAssignment.map(assignment -> assignment.getProject().getClient().getName()).orElse("No client");
            String projectName = lastAssignment.flatMap(assignment -> Optional.ofNullable(assignment.getProject()))
                    .map(Project::getName)
                    .orElse("No project");

            Integer usdPayment = activeContract.map(contract ->
                    contract.getPaymentsSettlement().stream()
                            .filter(p -> p.getCurrency() == Currency.USD)
                            .findFirst()
                            .map(payment -> payment.getSalary().intValue())
                            .orElse(0)
            ).orElse(0);

            Integer arsPayment = activeContract.map(contract ->
                    contract.getPaymentsSettlement().stream()
                            .filter(p -> p.getCurrency() == Currency.ARS)
                            .findFirst()
                            .map(payment -> payment.getSalary().intValue())
                            .orElse(0)
            ).orElse(0);

            EmployeeReportDto employeeReportDto = new EmployeeReportDto(employee, profile, firstContract.orElse(null), activeContract.orElse(null), client, projectName, technologiesName, usdPayment, arsPayment);
            employeesReportDtoList.add(employeeReportDto);
        }
        return new PageImpl<>(employeesReportDtoList, Pageable.unpaged(), employeesReportDtoList.size());
    }
}
