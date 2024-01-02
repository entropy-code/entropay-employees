package com.entropyteam.entropay.employees.services;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Set;

import com.entropyteam.entropay.employees.models.Assignment;
import com.entropyteam.entropay.employees.models.Contract;
import com.entropyteam.entropay.employees.models.Employee;
import com.entropyteam.entropay.employees.models.PaymentInformation;
import com.entropyteam.entropay.employees.models.Role;
import com.entropyteam.entropay.employees.models.Technology;
import com.entropyteam.entropay.employees.models.Holiday;
import com.entropyteam.entropay.employees.models.Country;
import com.entropyteam.entropay.employees.repositories.EmployeeRepository;
import com.entropyteam.entropay.employees.repositories.RoleRepository;
import com.entropyteam.entropay.employees.repositories.PaymentInformationRepository;
import com.entropyteam.entropay.employees.repositories.TechnologyRepository;
import com.entropyteam.entropay.employees.repositories.AssignmentRepository;
import com.entropyteam.entropay.employees.repositories.ContractRepository;
import com.entropyteam.entropay.employees.repositories.VacationRepository;
import com.entropyteam.entropay.employees.repositories.PtoRepository;
import com.entropyteam.entropay.employees.repositories.CountryRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.common.BaseService;
import com.entropyteam.entropay.common.ReactAdminMapper;
import com.entropyteam.entropay.employees.dtos.EmployeeDto;
import com.entropyteam.entropay.employees.dtos.CalendarEventDto;


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
    private final PtoRepository ptoRepository;
    private final CountryRepository countryRepository;
    private final GoogleService googleService;


    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, RoleRepository roleRepository,
                           PaymentInformationRepository paymentInformationRepository,
                           PaymentInformationService paymentInformationService, TechnologyRepository technologyRepository,
                           AssignmentRepository assignmentRepository, ContractRepository contractRepository,
                           ReactAdminMapper reactAdminMapper, VacationRepository vacationRepository, PtoRepository ptoRepository,
                           CountryRepository countryRepository, GoogleService googleService) {
        super(Employee.class, reactAdminMapper);
        this.employeeRepository = employeeRepository;
        this.roleRepository = roleRepository;
        this.paymentRepository = paymentInformationRepository;
        this.paymentInformationService = paymentInformationService;
        this.technologyRepository = technologyRepository;
        this.assignmentRepository = assignmentRepository;
        this.contractRepository = contractRepository;
        this.vacationRepository = vacationRepository;
        this.ptoRepository = ptoRepository;
        this.countryRepository = countryRepository;
        this.googleService = googleService;
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
        Optional<Contract> latestContract =
                contractRepository.findContractByEmployeeIdAndActiveIsTrueAndDeletedIsFalse(entity.getId());
        if (latestContract.isEmpty()) {
            latestContract = contracts.stream().max(Comparator.comparing(Contract::getStartDate));
        }
        String timeSinceStart = getEmployeesTimeSinceStart(firstContract.orElse(null), latestContract.orElse(null));
        LocalDate nearestPto = ptoRepository.findNearestPto(entity.getId());
        return new EmployeeDto(entity, paymentInformationList, assignment.orElse(null), firstContract.orElse(null), availableDays, latestContract.orElse(null), nearestPto, timeSinceStart);
    }

    @Override
    protected Employee toEntity(EmployeeDto dto) {
        Employee employee = new Employee(dto);
        Set<Role> roles = roleRepository.findAllByDeletedIsFalseAndIdIn(dto.profile());
        Set<Technology> technologies = technologyRepository.findAllByDeletedIsFalseAndIdIn(dto.technologies());
        Country country = countryRepository.findById(dto.countryId()).orElseThrow();
        employee.setCountry(country);
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

        LocalDate birthDate = employeeDto.birthDate();
        if (birthDate != null) {
            CalendarEventDto eventData = formatEventData(entityToCreate.getId(), employeeDto.birthDate(), employeeDto.firstName(), employeeDto.lastName());
            googleService.createGoogleCalendarEvent(eventData);
        }

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

        CalendarEventDto eventData = formatEventData(employeeDto.id(), employeeDto.birthDate(), employeeDto.firstName(), employeeDto.lastName());
        googleService.updateGoogleCalendarEvent(eventData);
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
        googleService.deleteGoogleCalendarEvent(LocalDate.now().getYear() + employeeId.toString());
        return toDTO(employee);
    }

    @Override
    public List<String> getColumnsForSearch() {
        return Arrays.asList("firstName", "lastName", "internalId");
    }

    public Integer applyVacationRuleToEmployee(Employee employee, String vacationYearToAdd, List<Contract> employeeContracts, LocalDate currentDate, List<Holiday> holidaysInPeriod) {
        //early return if employee already has vacations for considered year
        if (vacationRepository.existsVacationByEmployeeIdAndDeletedIsFalseAndYearIsLike(employee.getId(), vacationYearToAdd)) {
            return 0;
        }
        Optional<Contract> activeContract = employeeContracts.stream()
                .filter(Contract::isActive)
                .findFirst();
        Optional<Contract> firstContract = employeeContracts.stream().min(Comparator.comparing(Contract::getStartDate));

        //calculate vacations if employee has contract to get seniority
        if (activeContract.isPresent() && firstContract.isPresent()) {
            LocalDate startDate = firstContract.get().getStartDate();
            if (currentDate.getMonthValue() == Month.OCTOBER.getValue() && startDate.isBefore(LocalDate.of(currentDate.getYear(), Month.JULY, 1))) {
                int yearDiff = startDate.until(currentDate).getYears();
                int vacationDays = activeContract.get().getSeniority().getVacationDays();
                return yearDiff >= 2 ? 15 : vacationDays;
            } else if (currentDate.getMonthValue() == Month.JANUARY.getValue()) {
                String seniorityName = activeContract.get().getSeniority().getName();
                return vacationDaysPerWorkDay(holidaysInPeriod, currentDate, startDate, seniorityName);
            }
        }
        return 0;
    }

    private int vacationDaysPerWorkDay(List<Holiday> holidaysInPeriod, LocalDate currentDate, LocalDate startDate, String seniorityName) {
        double labourDays = 0;
        while (!startDate.isAfter(currentDate)) {
            LocalDate finalStartDate = startDate;
            if (startDate.getDayOfWeek() != DayOfWeek.SATURDAY &&
                    startDate.getDayOfWeek() != DayOfWeek.SUNDAY &&
                    holidaysInPeriod.stream().noneMatch(holiday -> holiday.getDate().equals(finalStartDate))) {
                labourDays++;
            }
            startDate = startDate.plusDays(1);
        }
        if (StringUtils.equalsIgnoreCase(seniorityName, "Senior 1") || StringUtils.equalsIgnoreCase(seniorityName, "Senior 2")
                || StringUtils.equalsIgnoreCase(seniorityName, "Architect")) {
            return (int) Math.round((labourDays * 1.5) / 20);
        } else {
            return (int) Math.round((labourDays * 1) / 20);
        }
    }

    private boolean shouldDeactivateEmployee(UUID employeeId, Employee entityToUpdate) {
        Employee existingEmployee = getRepository().getById(employeeId);
        return existingEmployee.isActive() && !entityToUpdate.isActive();
    }

    public CalendarEventDto formatEventData(UUID employeeId, LocalDate birthDate, String firstName, String lastName) {
        int currentYear = LocalDate.now().getYear();
        LocalDate startDate = birthDate.withYear(currentYear);
        LocalDate endDate = startDate.plusDays(1);
        String eventId = currentYear + employeeId.toString();
        String eventName = "BirthDay " + firstName + " " + lastName;

        return new CalendarEventDto(eventId, eventName, startDate, endDate);
    }

    public String getEmployeesTimeSinceStart(Contract firstContract, Contract latestContract) {
        LocalDate startDate = firstContract != null ? firstContract.getStartDate() : LocalDate.now();
        LocalDate endDate = LocalDate.now();
        if (latestContract != null && latestContract.getEndDate() != null) {
            endDate = Collections.min(Arrays.asList(latestContract.getEndDate(), endDate));
        }
        Period difference = Period.between(startDate, endDate);
        StringBuilder timeSinceStart = new StringBuilder();
        if (difference.getYears() > 0) {
            timeSinceStart.append(difference.getYears()).append(" year").append(difference.getYears() > 1 ? "s" : "");
            if (difference.getMonths() > 0) {
                timeSinceStart.append(", ");
            }
        }
        if (difference.getMonths() > 0) {
            timeSinceStart.append(difference.getMonths()).append(" month").append(difference.getMonths() > 1 ? "s" : "");
        }
        if (timeSinceStart.isEmpty()) {
            timeSinceStart.append("0 months");
        }
        return timeSinceStart.toString();
    }
}
