package com.entropyteam.entropay.employees.services;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Optional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import java.util.stream.Collectors;

import com.entropyteam.entropay.auth.AppRole;

import com.entropyteam.entropay.common.Filter;
import com.entropyteam.entropay.common.ReactAdminMapper;
import com.entropyteam.entropay.common.ReactAdminParams;
import com.entropyteam.entropay.employees.dtos.EmployeeReportDto;
import com.entropyteam.entropay.employees.dtos.PtoReportDto;
import com.entropyteam.entropay.employees.models.*;
import com.entropyteam.entropay.employees.repositories.*;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.PageImpl;


import static com.entropyteam.entropay.auth.AuthUtils.getUserRole;

@Service
public class ReportService {

    private final static String ACTIVE_CONTRACT = "activeContract";
    private final ReactAdminMapper mapper;
    private final RoleRepository roleRepository;
    private final TechnologyRepository technologyRepository;
    private final AssignmentRepository assignmentRepository;
    private final ContractRepository contractRepository;
    private final EmployeeRepository employeeRepository;
    private final PtoRepository ptoRepository;

    @Autowired
    public ReportService(RoleRepository roleRepository, TechnologyRepository technologyRepository,
                         AssignmentRepository assignmentRepository, ContractRepository contractRepository, EmployeeRepository employeeRepository,
                         ReactAdminMapper mapper, PtoRepository ptoRepository) {
        this.mapper = mapper;
        this.roleRepository = roleRepository;
        this.technologyRepository = technologyRepository;
        this.assignmentRepository = assignmentRepository;
        this.contractRepository = contractRepository;
        this.employeeRepository = employeeRepository;
        this.ptoRepository = ptoRepository;
    }


    public Page<EmployeeReportDto> getEmployeesReport(ReactAdminParams params) {
        AppRole userRole = getUserRole();
        List<Employee> employeesList = getFilteredEmployeesList(mapper.buildReportFilter(params, EmployeeReportDto.class));
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
            if (AppRole.ROLE_MANAGER_HR.equals(userRole)) {
                Optional<Contract> hrContract = employeeContracts.stream()
                        .filter(c -> StringUtils.contains(c.getRole().getName(), "HR")).findFirst();
                if (hrContract.isPresent()) {
                    continue;
                }
            }

            List<String> profile = employeeRolesList.stream()
                    .filter(t -> t.getEmployees().contains(employee))
                    .map(Role::getName)
                    .toList();
            List<String> technologiesName = employeeTechnologiesList.stream()
                    .filter(t -> t.getEmployees().contains(employee))
                    .map(Technology::getName)
                    .toList();
            List<Assignment> employeeAssignments = employeeAssignmentsMap.getOrDefault(employee.getId(), Collections.emptyList());

            Optional<Contract> firstContract = employeeContracts.stream().min(Comparator.comparing(Contract::getStartDate));
            Optional<Contract> latestContract = employeeContracts.stream().filter(Contract::isActive).findFirst();
            if (latestContract.isEmpty()) {
                latestContract = employeeContracts.stream().max(Comparator.comparing((Contract::getStartDate)));
            }
            Optional<Assignment> lastAssignment = employeeAssignments.stream().filter(Assignment::isActive).findFirst();

            String country = employee.getCountry().getName();
            String labourEmail = employee.getLabourEmail();
            String client = lastAssignment.map(assignment -> assignment.getProject().getClient().getName()).orElse("No client");
            String projectName = lastAssignment.flatMap(assignment -> Optional.ofNullable(assignment.getProject()))
                    .map(Project::getName)
                    .orElse("No project");

            Integer usdPayment = latestContract.isPresent() && latestContract.get().isActive() ? latestContract.get().getLatestPayment(Currency.USD) : 0;
            Integer arsPayment = latestContract.isPresent() && latestContract.get().isActive() ? latestContract.get().getLatestPayment(Currency.ARS) : 0;

            EmployeeReportDto employeeReportDto = new EmployeeReportDto(employee, profile, firstContract.orElse(null), latestContract.orElse(null), client, projectName, technologiesName, usdPayment, arsPayment, country, labourEmail);
            employeesReportDtoList.add(employeeReportDto);
        }
        return new PageImpl<>(employeesReportDtoList, Pageable.unpaged(), employeesReportDtoList.size());
    }

    public List<Employee> getFilteredEmployeesList(Filter filter) {
        if (filter.getGetByFieldsFilter().containsKey(ACTIVE_CONTRACT)) {
            return employeeRepository.getEmployeesWithAtLeastAnActiveContract();
        }
        return employeeRepository.findAllByDeletedIsFalseAndActiveIsTrue();
    }

    public Page<PtoReportDto> getPtosByEmployeesReport() {
        List<Employee> employeeList = employeeRepository.findAllByDeletedIsFalse();

        Map<UUID, List<Assignment>> employeeAssignmentsMap = assignmentRepository.findAllByDeletedIsFalse()
                .stream()
                .collect(Collectors.groupingBy(a -> a.getEmployee().getId()));

        Map<UUID, Integer> totalPtoDaysMap = ptoRepository.findAllByDeletedIsFalseAndStatusIsApproved()
                .stream()
                .collect(Collectors.groupingBy(p -> p.getEmployee().getId(), Collectors.summingInt(p -> p.getDays().intValue())));

        List<PtoReportDto> ptoReportDtoList = employeeList.stream()
                .map(employee -> {
                    List<Assignment> employeeAssignments = employeeAssignmentsMap.getOrDefault(employee.getId(), Collections.emptyList());
                    Optional<Assignment> lastAssignment = employeeAssignments.stream().filter(Assignment::isActive).findFirst();
                    String clientName = lastAssignment.map(assignment -> assignment.getProject().getClient().getName()).orElse("No client");
                    int totalPtoDays = totalPtoDaysMap.getOrDefault(employee.getId(), 0);

                    return new PtoReportDto(employee.getId(), employee.getInternalId(), employee.getFirstName(), employee.getLastName(), clientName, totalPtoDays);
                })
                .collect(Collectors.toList());

        return new PageImpl<>(ptoReportDtoList, Pageable.unpaged(), ptoReportDtoList.size());
    }
}
