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
            if(latestContract.isEmpty()) {
                latestContract = employeeContracts.stream().max(Comparator.comparing((Contract::getStartDate)));
            }
            Optional<Assignment> lastAssignment = employeeAssignments.stream().filter(Assignment::isActive).findFirst();

            String country = employee.getCountry().getName();
            String labourEmail = employee.getLabourEmail();
            String client = lastAssignment.map(assignment -> assignment.getProject().getClient().getName()).orElse("No client");
            String projectName = lastAssignment.flatMap(assignment -> Optional.ofNullable(assignment.getProject()))
                    .map(Project::getName)
                    .orElse("No project");

            Integer usdPayment = latestContract.isPresent() && latestContract.get().isActive() ? latestContract.get().getLatestPayment(Currency.USD): 0;
            Integer arsPayment = latestContract.isPresent() && latestContract.get().isActive() ? latestContract.get().getLatestPayment(Currency.ARS): 0;

            EmployeeReportDto employeeReportDto = new EmployeeReportDto(employee, profile, firstContract.orElse(null), latestContract.orElse(null), client, projectName, technologiesName, usdPayment, arsPayment, country, labourEmail);
            employeesReportDtoList.add(employeeReportDto);
        }
        return new PageImpl<>(employeesReportDtoList, Pageable.unpaged(), employeesReportDtoList.size());
    }

    public List<Employee> getFilteredEmployeesList(Filter filter) {
        if(filter.getGetByFieldsFilter().containsKey(ACTIVE_CONTRACT)) {
            return employeeRepository.getEmployeesWithAtLeastAnActiveContract();
        }
        return employeeRepository.findAllByDeletedIsFalseAndActiveIsTrue();
    }

    public Page<PtoReportDetailDto> getPtoReportDetail(ReactAdminParams params) {
        List<PtoReportDetailDto> ptoReportDetailDtoList = getFilteredReportDetail(mapper.buildReportFilter(params, PtoReportDetailDto.class));
        return new PageImpl<>(ptoReportDetailDtoList, Pageable.unpaged(), ptoReportDetailDtoList.size());
    }

    public List<PtoReportDetailDto> getPtoReportDetailByEmployee(Employee employee) {
        List<PtoReportDetailDto> ptoReportDetailDtoList = new ArrayList<>();
        List<Assignment> employeesAssignments = assignmentRepository.findAssignmentByEmployee_IdAndDeletedIsFalse(employee.getId());
        Optional<Assignment> lastAssignment = employeesAssignments.stream().filter(Assignment::isActive).findFirst();
        UUID clientId = lastAssignment.map(assignment -> assignment.getProject().getClient().getId()).orElse(null);
         String clientName = lastAssignment.map(assignment -> assignment.getProject().getClient().getName()).orElse("No client");
        List<Pto> employeesPtos = ptoRepository.findPtosByEmployeeIdIsAndDeletedIsFalse(employee.getId());

        for (Pto employeePto : employeesPtos) {
            if(employeePto.getStatus() == Status.APPROVED) {
                PtoReportDetailDto ptoReportDetailDto = new PtoReportDetailDto(employee.getId(), employee.getInternalId(), employee.getFirstName(),
                        employee.getLastName(), clientName, employeePto.getLeaveType().getName(), employeePto.getDaysAsInteger(), clientId,
                        employeePto.getStartDate(), employeePto.getEndDate());
                ptoReportDetailDtoList.add(ptoReportDetailDto);
            }
        }
        return ptoReportDetailDtoList;
    }

    public List<PtoReportDetailDto> getPtoReportDetailByClient(Client client) {
        List<PtoReportDetailDto> ptoReportDetailDtoList = new ArrayList<>();
        List<Assignment> clientsAssignmentList = assignmentRepository.findAllByDeletedIsFalse()
                .stream()
                .filter(assignment -> assignment.getProject().getClient().getId() == client.getId())
                .toList();

        List<Employee> employeeList = employeeRepository.findAllById(clientsAssignmentList.stream().map(x-> x.getEmployee().getId()).toList());
        List<Pto> employeesPtoList = ptoRepository.findPtosByEmployeeIdInAndDeletedIsFalse(employeeList.stream().map(BaseEntity::getId).toList());

        for (Pto pto : employeesPtoList) {
            if(pto.getStatus() == Status.APPROVED) {
                Employee employee = pto.getEmployee();
                PtoReportDetailDto ptoReportDetailDto = new PtoReportDetailDto(employee.getId(), employee.getInternalId(), employee.getFirstName(),
                        employee.getLastName(), client.getName(), pto.getLeaveType().getName(), pto.getDaysAsInteger(), client.getId(),
                        pto.getStartDate(), pto.getEndDate());
                ptoReportDetailDtoList.add(ptoReportDetailDto);
            }
        }
        return ptoReportDetailDtoList;
    }

    public List<PtoReportDetailDto> getFilteredReportDetail(Filter filter) {
        if(filter.getGetByFieldsFilter().containsKey(EMPLOYEE_ID)) {
            return getPtoReportDetailByEmployee(employeeRepository.findById(UUID.fromString((String)filter.getGetByFieldsFilter().get(EMPLOYEE_ID))).orElseThrow());
        }
        else if (filter.getGetByFieldsFilter().containsKey(CLIENT_ID)) {
            return getPtoReportDetailByClient(clientRepository.findById(UUID.fromString((String)filter.getGetByFieldsFilter().get(CLIENT_ID))).orElseThrow());
        }
        else {
            return Collections.emptyList();
        }
    }
}
