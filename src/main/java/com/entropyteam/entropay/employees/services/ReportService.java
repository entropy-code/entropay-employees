package com.entropyteam.entropay.employees.services;

import static com.entropyteam.entropay.auth.AuthUtils.getUserRole;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.entropyteam.entropay.auth.AppRole;
import com.entropyteam.entropay.common.BaseEntity;
import com.entropyteam.entropay.common.Filter;
import com.entropyteam.entropay.common.ReactAdminMapper;
import com.entropyteam.entropay.common.ReactAdminParams;
import com.entropyteam.entropay.common.ReactAdminSqlMapper;
import com.entropyteam.entropay.common.ReactAdminSqlParams;
import com.entropyteam.entropay.employees.dtos.EmployeeReportDto;
import com.entropyteam.entropay.employees.dtos.PtoReportClientDto;
import com.entropyteam.entropay.employees.dtos.PtoReportDetailDto;
import com.entropyteam.entropay.employees.dtos.PtoReportEmployeeDto;
import com.entropyteam.entropay.employees.dtos.SalariesReportDto;
import com.entropyteam.entropay.employees.models.Assignment;
import com.entropyteam.entropay.employees.models.Client;
import com.entropyteam.entropay.employees.models.Contract;
import com.entropyteam.entropay.employees.models.Currency;
import com.entropyteam.entropay.employees.models.Employee;
import com.entropyteam.entropay.employees.models.Project;
import com.entropyteam.entropay.employees.models.Pto;
import com.entropyteam.entropay.employees.models.Role;
import com.entropyteam.entropay.employees.models.Status;
import com.entropyteam.entropay.employees.models.Technology;
import com.entropyteam.entropay.employees.repositories.AssignmentRepository;
import com.entropyteam.entropay.employees.repositories.ClientRepository;
import com.entropyteam.entropay.employees.repositories.ContractRepository;
import com.entropyteam.entropay.employees.repositories.EmployeeRepository;
import com.entropyteam.entropay.employees.repositories.PtoRepository;
import com.entropyteam.entropay.employees.repositories.RoleRepository;
import com.entropyteam.entropay.employees.repositories.TechnologyRepository;

@Service
public class ReportService {

    private final static Logger LOGGER = LoggerFactory.getLogger(ReportService.class);

    private final static String ACTIVE_CONTRACT = "activeContract";
    private final static String EMPLOYEE_ID = "employeeId";
    private final static String CLIENT_ID = "clientId";
    private final static String YEAR = "year";
    private final static String NO_CLIENT = "noClient";
    private final ReactAdminMapper mapper;
    private final RoleRepository roleRepository;
    private final TechnologyRepository technologyRepository;
    private final AssignmentRepository assignmentRepository;
    private final ContractRepository contractRepository;
    private final EmployeeRepository employeeRepository;
    private final PtoRepository ptoRepository;
    private final ClientRepository clientRepository;
    private final ReactAdminSqlMapper sqlMapper;

    @Autowired
    public ReportService(RoleRepository roleRepository, TechnologyRepository technologyRepository,
            AssignmentRepository assignmentRepository, ContractRepository contractRepository,
            EmployeeRepository employeeRepository, PtoRepository ptoRepository,
            ClientRepository clientRepository, ReactAdminMapper mapper, ReactAdminSqlMapper sqlMapper) {
        this.mapper = mapper;
        this.roleRepository = roleRepository;
        this.technologyRepository = technologyRepository;
        this.assignmentRepository = assignmentRepository;
        this.contractRepository = contractRepository;
        this.employeeRepository = employeeRepository;
        this.ptoRepository = ptoRepository;
        this.clientRepository = clientRepository;
        this.sqlMapper = sqlMapper;
    }

    public Page<EmployeeReportDto> getEmployeesReport(ReactAdminParams params) {
        AppRole userRole = getUserRole();
        List<Employee> employeesList =
                getFilteredEmployeesList(mapper.buildReportFilter(params, EmployeeReportDto.class));
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
            List<Contract> employeeContracts =
                    employeeContractsMap.getOrDefault(employee.getId(), Collections.emptyList());
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
            List<Assignment> employeeAssignments =
                    employeeAssignmentsMap.getOrDefault(employee.getId(), Collections.emptyList());

            Optional<Contract> firstContract =
                    employeeContracts.stream().min(Comparator.comparing(Contract::getStartDate));
            Optional<Contract> latestContract = employeeContracts.stream().filter(Contract::isActive).findFirst();
            if (latestContract.isEmpty()) {
                latestContract = employeeContracts.stream().max(Comparator.comparing((Contract::getStartDate)));
            }
            Optional<Assignment> lastAssignment = employeeAssignments.stream().filter(Assignment::isActive).findFirst();

            String country = employee.getCountry().getName();
            String labourEmail = employee.getLabourEmail();
            String client =
                    lastAssignment.map(assignment -> assignment.getProject().getClient().getName()).orElse("No client");
            String projectName = lastAssignment.flatMap(assignment -> Optional.ofNullable(assignment.getProject()))
                    .map(Project::getName)
                    .orElse("No project");

            Integer usdPayment = latestContract.isPresent() && latestContract.get().isActive() ? latestContract.get()
                    .getLatestPayment(Currency.USD) : 0;
            Integer arsPayment = latestContract.isPresent() && latestContract.get().isActive() ? latestContract.get()
                    .getLatestPayment(Currency.ARS) : 0;

            EmployeeReportDto employeeReportDto =
                    new EmployeeReportDto(employee, profile, firstContract.orElse(null), latestContract.orElse(null),
                            client, projectName, technologiesName, usdPayment, arsPayment, country, labourEmail);
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
        Integer year = getYearFromFilter(filter);
        if (filter.getGetByFieldsFilter().containsKey(EMPLOYEE_ID)) {
            ptoReportDetailDtoList = getPtoReportDetailByEmployee(employeeRepository.findById(
                    UUID.fromString((String) filter.getGetByFieldsFilter().get(EMPLOYEE_ID))).orElseThrow(), year);
        } else if (filter.getGetByFieldsFilter().containsKey(CLIENT_ID)) {
            Client client = (filter.getGetByFieldsFilter().get(CLIENT_ID)).equals(NO_CLIENT) ? null :
                    clientRepository.findById(UUID.fromString((String) filter.getGetByFieldsFilter().get(CLIENT_ID)))
                            .orElseThrow();
            ptoReportDetailDtoList = getPtoReportDetailByClient(client, year);
        } else {
            ptoReportDetailDtoList = Collections.emptyList();
        }
        return new PageImpl<>(ptoReportDetailDtoList, Pageable.unpaged(), ptoReportDetailDtoList.size());
    }

    public List<PtoReportDetailDto> getPtoReportDetailByEmployee(Employee employee, Integer year) {
        List<PtoReportDetailDto> ptoReportDetailDtoList;

        List<Assignment> employeesAssignments =
                assignmentRepository.findAssignmentByEmployee_IdAndDeletedIsFalse(employee.getId());
        Optional<Assignment> lastAssignment = employeesAssignments.stream().filter(Assignment::isActive).findFirst();
        UUID clientId = lastAssignment.map(assignment -> assignment.getProject().getClient().getId()).orElse(null);
        String clientName =
                lastAssignment.map(assignment -> assignment.getProject().getClient().getName()).orElse("No client");
        List<Pto> employeesPtosList = ptoRepository.findPtosByEmployeeIdIsAndDeletedIsFalse(employee.getId(), year);

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
                        pto.getDays(),
                        clientId,
                        pto.getStartDate(),
                        pto.getEndDate(),
                        year
                ))
                .collect(Collectors.toList());

        return ptoReportDetailDtoList;
    }

    public List<PtoReportDetailDto> getPtoReportDetailByClient(Client client, Integer year) {
        List<PtoReportDetailDto> ptoReportDetailDtoList;
        List<Employee> employeeList;
        if (client != null) {
            List<Assignment> clientsAssignmentList = assignmentRepository.findAllAssignmentsByClientId(client.getId());
            employeeList = employeeRepository.findAllById(
                    clientsAssignmentList.stream().map(x -> x.getEmployee().getId()).toList());
        } else {
            employeeList = employeeRepository.findAllUnassignedEmployees();
        }
        List<Pto> employeesPtoList =
                ptoRepository.findPtosByEmployeeIdInAndForYear(employeeList.stream().map(BaseEntity::getId).toList(),
                        year);

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
                            client != null ? client.getName() : "No client",
                            pto.getLeaveType().getName(),
                            pto.getDays(),
                            client != null ? client.getId() : null,
                            pto.getStartDate(),
                            pto.getEndDate(),
                            year
                    );
                })
                .collect(Collectors.toList());

        return ptoReportDetailDtoList;
    }

    public Page<PtoReportEmployeeDto> getPtosReportByEmployees(ReactAdminParams params) {
        List<PtoReportEmployeeDto> ptoReportDtoList;
        Filter filter = mapper.buildReportFilter(params, PtoReportEmployeeDto.class);
        int year = LocalDate.now().getYear();
        if (filter.getGetByFieldsFilter().containsKey(YEAR)) {
            year = getYearFromFilter(filter);
        }
        ptoReportDtoList = getPtoReportEmployeeDtos(
                ptoRepository.findAllByDeletedIsFalseAndStatusIsForYear(Status.APPROVED.name(), year), year);
        return new PageImpl<>(ptoReportDtoList, Pageable.unpaged(), ptoReportDtoList.size());
    }

    private List<PtoReportEmployeeDto> getPtoReportEmployeeDtos(List<Pto> ptoList, Integer year) {
        List<Employee> employeeList = employeeRepository.findAllByIdInAndDeletedIsFalse(
                ptoList.stream().map(pto -> pto.getEmployee().getId()).collect(Collectors.toList()));

        Map<UUID, List<Assignment>> employeeAssignmentsMap =
                assignmentRepository.findAllByEmployeeIdInAndDeletedIsFalse(
                                employeeList.stream().map(BaseEntity::getId).collect(Collectors.toList()))
                        .stream()
                        .collect(Collectors.groupingBy(a -> a.getEmployee().getId()));

        Map<UUID, Double> totalPtoDaysMap = ptoList
                .stream()
                .collect(Collectors.groupingBy(p -> p.getEmployee().getId(), Collectors.summingDouble(Pto::getDays)));

        return employeeList.stream()
                .filter(employee -> totalPtoDaysMap.containsKey(employee.getId())
                        && totalPtoDaysMap.get(employee.getId()) > 0)
                .map(employee -> {
                    List<Assignment> employeeAssignments =
                            employeeAssignmentsMap.getOrDefault(employee.getId(), Collections.emptyList());
                    Optional<Assignment> lastAssignment =
                            employeeAssignments.stream().filter(Assignment::isActive).findFirst();
                    String clientName = lastAssignment
                            .map(assignment -> assignment.getProject().getClient().getName()).orElse("No client");
                    Double totalPtoDays = totalPtoDaysMap.getOrDefault(employee.getId(), 0.0);

                    return new PtoReportEmployeeDto(employee.getId(), employee.getInternalId(), employee.getFirstName(),
                            employee.getLastName(),
                            clientName, totalPtoDays, year);
                })
                .collect(Collectors.toList());
    }

    public Page<PtoReportClientDto> getPtosReportByClients(ReactAdminParams params) {
        List<PtoReportClientDto> ptoReportDtoList;
        Filter filter = mapper.buildReportFilter(params, PtoReportClientDto.class);
        int year = LocalDate.now().getYear();
        if (filter.getGetByFieldsFilter().containsKey(YEAR)) {
            year = getYearFromFilter(filter);
        }
        ptoReportDtoList = getPtoReportClientDtos(
                ptoRepository.findAllByDeletedIsFalseAndStatusIsForYear(Status.APPROVED.name(), year), year);
        return new PageImpl<>(ptoReportDtoList, Pageable.unpaged(), ptoReportDtoList.size());
    }

    private List<PtoReportClientDto> getPtoReportClientDtos(List<Pto> ptoList, Integer year) {
        List<Client> clients = clientRepository.findAllClientsWithAProject();
        List<PtoReportClientDto> ptosByClient = new ArrayList<>();
        Map<UUID, List<Assignment>> assignmentMap = assignmentRepository.findAllAssignmentsByClientIdIn(
                        clients.stream().map(BaseEntity::getId).collect(Collectors.toList()))
                .stream()
                .collect(Collectors.groupingBy(assignment -> assignment.getProject().getClient().getId()));

        ptosByClient.addAll(clients.stream().map(client -> {
            List<Assignment> clientAssignments = assignmentMap.getOrDefault(client.getId(), Collections.emptyList());
            List<Pto> employeesPtos = ptoList.stream().filter(pto -> clientAssignments.stream().
                    anyMatch(assignment -> assignment.getEmployee().getId() == pto.getEmployee().getId())).toList();
            double totalDays = employeesPtos.stream().mapToDouble(Pto::getDays).sum();
            if (totalDays > 0) {
                return new PtoReportClientDto(client.getId().toString(), client.getName(), totalDays, year);
            } else {
                return null;
            }
        }).filter(Objects::nonNull).toList());

        List<UUID> unassignedEmployeesIds =
                employeeRepository.findAllUnassignedEmployees().stream().map(BaseEntity::getId).toList();
        Double days = ptoList.stream().filter(pto -> unassignedEmployeesIds.contains(pto.getEmployee().getId()))
                .mapToDouble(Pto::getDays).sum();
        ptosByClient.add(new PtoReportClientDto(NO_CLIENT, "No client", days, year));
        return ptosByClient;
    }

    public Page<PtoReportDetailDto> getPtoReportAllDetails(ReactAdminParams params) {
        Filter filter = mapper.buildReportFilter(params, PtoReportDetailDto.class);
        Integer year = getYearFromFilter(filter);
        List<PtoReportDetailDto> ptoReportDetailDtoList = getPtoReportAllDetailsDto(year);
        return new PageImpl<>(ptoReportDetailDtoList, Pageable.unpaged(), ptoReportDetailDtoList.size());
    }

    private List<PtoReportDetailDto> getPtoReportAllDetailsDto(Integer year) {
        return employeeRepository.findAllByDeletedIsFalseAndActiveIsTrue().stream()
                .flatMap(employee -> getPtoReportDetailByEmployee(employee, year).stream())
                .collect(Collectors.toList());
    }

    private Integer getYearFromFilter(Filter filter) {
        Object yearObject = filter.getGetByFieldsFilter().get(YEAR);
        return Integer.parseInt(yearObject.toString());
    }

    public Page<SalariesReportDto> getSalariesReport(ReactAdminParams params) {
        LOGGER.info("Getting salaries report with params: {}", params);
        ReactAdminSqlParams queryParams = sqlMapper.map(params);

        List<SalariesReportDto> salariesReport = employeeRepository.getSalariesReport(queryParams);
        Integer salariesCount = employeeRepository.getSalariesCount(queryParams);

        return new PageImpl<>(salariesReport, Pageable.unpaged(), salariesCount);
    }
}
