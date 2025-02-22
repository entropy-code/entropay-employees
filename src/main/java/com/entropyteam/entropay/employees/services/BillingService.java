package com.entropyteam.entropay.employees.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.entropyteam.entropay.common.DateRangeDto;
import com.entropyteam.entropay.common.ReactAdminParams;
import com.entropyteam.entropay.common.ReactAdminSqlMapper;
import com.entropyteam.entropay.employees.models.Assignment;
import com.entropyteam.entropay.employees.models.Country;
import com.entropyteam.entropay.employees.models.Employee;
import com.entropyteam.entropay.employees.models.Holiday;
import com.entropyteam.entropay.employees.models.Pto;
import com.entropyteam.entropay.employees.repositories.AssignmentRepository;
import com.entropyteam.entropay.employees.repositories.CountryRepository;
import com.entropyteam.entropay.employees.repositories.HolidayRepository;
import com.entropyteam.entropay.employees.repositories.PtoRepository;

@Service
public class BillingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BillingService.class);
    private static final int SATURDAY = 6;
    private final AssignmentRepository assignmentRepository;
    private final PtoRepository ptoRepository;
    private final HolidayRepository holidayRepository;
    private final CountryRepository countryRepository;
    private final ReactAdminSqlMapper sqlMapper;


    public BillingService(AssignmentRepository assignmentRepository, PtoRepository ptoRepository,
            HolidayRepository holidayRepository, CountryRepository countryRepository, ReactAdminSqlMapper sqlMapper) {
        this.assignmentRepository = assignmentRepository;
        this.ptoRepository = ptoRepository;
        this.holidayRepository = holidayRepository;
        this.countryRepository = countryRepository;
        this.sqlMapper = sqlMapper;
    }

    public record BillingDto(UUID id, UUID employeeId, String internalId, String firstName, String lastName,
                             String clientName, String projectName, BigDecimal rate, int hours, BigDecimal total) {

    }

    public List<BillingDto> generateBilling(ReactAdminParams params) {
        DateRangeDto dateRange = new DateRangeDto(sqlMapper.map(params));
        LocalDate startDate = dateRange.getStartDate();
        LocalDate endDate = dateRange.getEndDate();

        LOGGER.info("Generating billing for period {} - {}", startDate, endDate);

        Map<Country, Set<LocalDate>> workingDaysByCountry = calculateWorkingDays(startDate, endDate);
        Map<Employee, Set<LocalDate>> ptoDaysByEmployee = retrieveEmployeePtoDays(startDate, endDate);

        // calculate billable days
        List<BillingEntry> billingList = new ArrayList<>();
        assignmentRepository.findAllBetweenPeriod(startDate, endDate).forEach(assignment -> {
            Set<LocalDate> workingDays = new HashSet<>(workingDaysByCountry.get(assignment.getEmployee().getCountry()));
            // if the assignment started or finished during the middle of the month, adjust the billable days
            if (assignment.getStartDate().isAfter(startDate)) {
                workingDays.removeAll(startDate.datesUntil(assignment.getStartDate()).collect(Collectors.toSet()));
            }
            if (assignment.getEndDate() != null && assignment.getEndDate().isBefore(endDate)) {
                workingDays.removeAll(assignment.getEndDate().datesUntil(endDate).collect(Collectors.toSet()));
            }
            workingDays.removeAll(ptoDaysByEmployee.getOrDefault(assignment.getEmployee(), Set.of()));
            billingList.add(new BillingEntry(assignment, workingDays));
        });

        return billingList.stream().map(BillingEntry::toDto).toList();
    }

    /**
     * Calculates the working days for each country between the given start and end dates.
     *
     * @param startDate the start date of the period
     * @param endDate the end date of the period
     * @return a map where the key is the country and the value is a set of working days
     */
    private Map<Country, Set<LocalDate>> calculateWorkingDays(LocalDate startDate, LocalDate endDate) {
        var activeCountries = countryRepository.findAllByDeletedIsFalse();

        Map<Country, Set<LocalDate>> holidaysByCountry = holidayRepository.findAllBetweenPeriod(startDate, endDate)
                .stream()
                .collect(Collectors.groupingBy(Holiday::getCountry,
                        Collectors.mapping(Holiday::getDate, Collectors.toSet())));

        Set<LocalDate> weekdays = startDate.datesUntil(endDate.plusDays(1))
                .filter(date -> date.getDayOfWeek().getValue() < SATURDAY)
                .collect(Collectors.toSet());

        Map<Country, Set<LocalDate>> billableDays = new HashMap<>();
        activeCountries.forEach(country -> {
            Set<LocalDate> billable = new HashSet<>(weekdays);
            billable.removeAll(holidaysByCountry.getOrDefault(country, Set.of()));
            billableDays.put(country, billable);
        });

        return billableDays;
    }

    private Map<Employee, Set<LocalDate>> retrieveEmployeePtoDays(LocalDate startDate, LocalDate endDate) {
        return ptoRepository.findAllBetweenPeriod(startDate, endDate)
                .stream()
                .collect(Collectors.groupingBy(
                        Pto::getEmployee,
                        Collectors.flatMapping(pto -> pto.getStartDate().datesUntil(pto.getEndDate().plusDays(1)),
                                Collectors.toSet())
                ));
    }

    private record BillingEntry(Assignment assignment, Set<LocalDate> days) {

        @Override
        public String toString() {
            Employee employee = assignment.getEmployee();
            BigDecimal rate = assignment.getBillableRate() != null ? assignment.getBillableRate() : BigDecimal.ZERO;

            return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                    .append("internalId", employee.getInternalId())
                    .append("first name", employee.getFirstName())
                    .append("last name", employee.getLastName())
                    .append("client", assignment.getProject().getClient().getName())
                    .append("project", assignment.getProject().getName())
                    .append("rate", rate)
                    .append("hours", days.size() * 8)
                    .append("total", rate.multiply(BigDecimal.valueOf(days.size() * 8L)))
                    .toString();
        }

        public BillingDto toDto() {
            Employee employee = assignment.getEmployee();
            BigDecimal rate = assignment.getBillableRate() != null ? assignment.getBillableRate() : BigDecimal.ZERO;
            int hours = days.size() * 8;
            BigDecimal total = rate.multiply(BigDecimal.valueOf(hours));

            return new BillingDto(
                    assignment.getId(),
                    employee.getId(),
                    employee.getInternalId(),
                    employee.getFirstName(),
                    employee.getLastName(),
                    assignment.getProject().getClient().getName(),
                    assignment.getProject().getName(),
                    rate,
                    hours,
                    total
            );
        }
    }
}
