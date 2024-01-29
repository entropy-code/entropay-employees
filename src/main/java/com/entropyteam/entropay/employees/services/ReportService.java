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

import com.entropyteam.entropay.common.BaseEntity;
import com.entropyteam.entropay.common.Filter;
import com.entropyteam.entropay.common.ReactAdminMapper;
import com.entropyteam.entropay.common.ReactAdminParams;
import com.entropyteam.entropay.employees.dtos.EmployeeReportDto;
import com.entropyteam.entropay.employees.dtos.PtoReportDetailDto;
import com.entropyteam.entropay.employees.dtos.PtoReportDto;
import com.entropyteam.entropay.employees.models.Contract;
import com.entropyteam.entropay.employees.models.Employee;
import com.entropyteam.entropay.employees.models.Contract;
import com.entropyteam.entropay.employees.models.Role;
import com.entropyteam.entropay.employees.models.Technology;
import com.entropyteam.entropay.employees.models.Assignment;
import com.entropyteam.entropay.employees.models.Project;
import com.entropyteam.entropay.employees.models.Currency;
import com.entropyteam.entropay.employees.models.Pto;
import com.entropyteam.entropay.employees.models.Client;
import com.entropyteam.entropay.employees.models.Status;
import com.entropyteam.entropay.employees.repositories.RoleRepository;
import com.entropyteam.entropay.employees.repositories.TechnologyRepository;
import com.entropyteam.entropay.employees.repositories.AssignmentRepository;
import com.entropyteam.entropay.employees.repositories.ContractRepository;
import com.entropyteam.entropay.employees.repositories.EmployeeRepository;
import com.entropyteam.entropay.employees.repositories.PtoRepository;
import com.entropyteam.entropay.employees.repositories.LeaveTypeRepository;
import com.entropyteam.entropay.employees.repositories.ClientRepository;

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
    private final static String EMPLOYEE_ID = "employeeId";
    private final static String CLIENT_ID = "clientId";
    private final static String YEAR = "year";
    private final ReactAdminMapper mapper;
    private final RoleRepository roleRepository;
    private final TechnologyRepository technologyRepository;
    private final AssignmentRepository assignmentRepository;
    private final ContractRepository contractRepository;
    private final EmployeeRepository employeeRepository;
    private final PtoRepository ptoRepository;
    private final ClientRepository clientRepository;
    private final LeaveTypeRepository leaveTypeRepository;

    @Autowired
    public ReportService(RoleRepository roleRepository, TechnologyRepository technologyRepository,
                         AssignmentRepository assignmentRepository, ContractRepository contractRepository, EmployeeRepository employeeRepository, PtoRepository ptoRepository,
                         LeaveTypeRepository leaveTypeRepository, ClientRepository clientRepository, ReactAdminMapper mapper) {
        this.mapper = mapper;
        this.roleRepository = roleRepository;
        this.technologyRepository = technologyRepository;
        this.assignmentRepository = assignmentRepository;
        this.contractRepository = contractRepository;
        this.employeeRepository = employeeRepository;
        this.ptoRepository = ptoRepository;
        this.leaveTypeRepository = leaveTypeRepository;
        this.clientRepository = clientRepository;
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

    public Page<PtoReportDetailDto> getPtoReportDetail(ReactAdminParams params) {
        List<PtoReportDetailDto> ptoReportDetailDtoList;
        Filter filter = mapper.buildReportFilter(params, PtoReportDetailDto.class);
        if(filter.getGetByFieldsFilter().containsKey(EMPLOYEE_ID)) {
            ptoReportDetailDtoList = getPtoReportDetailByEmployee(employeeRepository.findById(UUID.fromString((String)filter.getGetByFieldsFilter().get(EMPLOYEE_ID))).orElseThrow());
        } else if (filter.getGetByFieldsFilter().containsKey(CLIENT_ID)) {
            ptoReportDetailDtoList = getPtoReportDetailByClient(clientRepository.findById(UUID.fromString((String)filter.getGetByFieldsFilter().get(CLIENT_ID))).orElseThrow());
        }
        else {
            ptoReportDetailDtoList = Collections.emptyList();
        }
        return new PageImpl<>(ptoReportDetailDtoList, Pageable.unpaged(), ptoReportDetailDtoList.size());
    }

    public List<PtoReportDetailDto> getPtoReportDetailByEmployee(Employee employee) {
        List<PtoReportDetailDto> ptoReportDetailDtoList;

        List<Assignment> employeesAssignments = assignmentRepository.findAssignmentByEmployee_IdAndDeletedIsFalse(employee.getId());
        Optional<Assignment> lastAssignment = employeesAssignments.stream().filter(Assignment::isActive).findFirst();
        UUID clientId = lastAssignment.map(assignment -> assignment.getProject().getClient().getId()).orElse(null);
        String clientName = lastAssignment.map(assignment -> assignment.getProject().getClient().getName()).orElse("No client");
        List<Pto> employeesPtosList = ptoRepository.findPtosByEmployeeIdIsAndDeletedIsFalse(employee.getId());

        ptoReportDetailDtoList = employeesPtosList.stream()
                .sorted(Comparator.comparing(Pto::getStartDate))
                .filter(pto -> pto.getStatus() == Status.APPROVED)
                .map(pto -> new PtoReportDetailDto(
                        pto.getId(),
                        employee.getId(),
                        employee.getInternalId(),
                        employee.getFirstName(),
                        employee.getLastName(),
                        clientName,
                        pto.getLeaveType().getName(),
                        pto.getDaysAsInteger(),
                        clientId,
                        pto.getStartDate(),
                        pto.getEndDate()
                ))
                .collect(Collectors.toList());

        return ptoReportDetailDtoList;
    }

    public List<PtoReportDetailDto> getPtoReportDetailByClient(Client client) {
        List<PtoReportDetailDto> ptoReportDetailDtoList;

        List<Assignment> clientsAssignmentList = assignmentRepository.findAllAssignmentsByClientId(client.getId());
        List<Employee> employeeList = employeeRepository.findAllById(clientsAssignmentList.stream().map(x-> x.getEmployee().getId()).toList());
        List<Pto> employeesPtoList = ptoRepository.findPtosByEmployeeIdInAndDeletedIsFalse(employeeList.stream().map(BaseEntity::getId).toList());

        ptoReportDetailDtoList = employeesPtoList.stream()
                .sorted(Comparator.comparing(Pto::getStartDate))
                .filter(pto -> pto.getStatus() == Status.APPROVED)
                .map(pto -> {
                    Employee employee = pto.getEmployee();
                    return new PtoReportDetailDto(
                            pto.getId(),
                            employee.getId(),
                            employee.getInternalId(),
                            employee.getFirstName(),
                            employee.getLastName(),
                            client.getName(),
                            pto.getLeaveType().getName(),
                            pto.getDaysAsInteger(),
                            client.getId(),
                            pto.getStartDate(),
                            pto.getEndDate()
                    );
                })
                .collect(Collectors.toList());

        return ptoReportDetailDtoList;
=======
    public Page<PtoReportDto> getPtosByEmployeesReport(ReactAdminParams params) {
        List<Employee> employeeList = employeeRepository.findAllByDeletedIsFalse();

        Map<UUID, List<Assignment>> employeeAssignmentsMap = assignmentRepository.findAllByDeletedIsFalse()
                .stream()
                .collect(Collectors.groupingBy(a -> a.getEmployee().getId()));

        List<Pto> ptoList = getFilteredPtoList(mapper.buildReportFilter(params, PtoReportDto.class));

        Map<UUID, Integer> totalPtoDaysMap = ptoList
                .stream()
                .collect(Collectors.groupingBy(p -> p.getEmployee().getId(), Collectors.summingInt(p -> p.getDays().intValue())));

        List<PtoReportDto> ptoReportDtoList = employeeList.stream()
                .filter(employee -> totalPtoDaysMap.containsKey(employee.getId()) && totalPtoDaysMap.get(employee.getId()) > 0)
                .map(employee -> {
                    List<Assignment> employeeAssignments = employeeAssignmentsMap.getOrDefault(employee.getId(), Collections.emptyList());
                    Optional<Assignment> lastAssignment = employeeAssignments.stream().filter(Assignment::isActive).findFirst();
                    String clientName = lastAssignment
                            .map(assignment -> assignment.getProject().getClient().getName()).orElse("No client");
                    int totalPtoDays = totalPtoDaysMap.getOrDefault(employee.getId(), 0);

                    return new PtoReportDto(employee.getId(), employee.getInternalId(), employee.getFirstName(), employee.getLastName(),
                            clientName, totalPtoDays, 0);
                })
                .collect(Collectors.toList());

        return new PageImpl<>(ptoReportDtoList, Pageable.unpaged(), ptoReportDtoList.size());
    }

    public List<Pto> getFilteredPtoList(Filter filter) {
        if (filter.getGetByFieldsFilter().containsKey(YEAR)) {
            Object yearObject = filter.getGetByFieldsFilter().get(YEAR);
            int year = Integer.parseInt(yearObject.toString());
            return ptoRepository.findAllByDeletedIsFalseAndStatusIsApprovedForYear(year);
        }
        return ptoRepository.findAllByDeletedIsFalseAndStatusIsApproved();
    }
}
