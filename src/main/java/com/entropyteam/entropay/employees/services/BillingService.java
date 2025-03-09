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
import org.apache.commons.lang3.Range;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.entropyteam.entropay.common.DateRangeDto;
import com.entropyteam.entropay.common.ReactAdminParams;
import com.entropyteam.entropay.common.ReactAdminSqlMapper;
import com.entropyteam.entropay.employees.dtos.ReportDto;
import com.entropyteam.entropay.employees.models.Country;
import com.entropyteam.entropay.employees.models.Employee;
import com.entropyteam.entropay.employees.repositories.AssignmentRepository;
import com.entropyteam.entropay.employees.repositories.CountryRepository;

@Service
public class BillingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BillingService.class);
    private static final int SATURDAY = 6;
    private final PtoService ptoService;
    private final HolidayService holidayService;
    private final OvertimeService overtimeService;
    private final AssignmentRepository assignmentRepository;
    private final CountryRepository countryRepository;
    private final ReactAdminSqlMapper sqlMapper;


    public BillingService(AssignmentRepository assignmentRepository, PtoService ptoService,
            HolidayService holidayService, OvertimeService overtimeService, CountryRepository countryRepository,
            ReactAdminSqlMapper sqlMapper) {
        this.assignmentRepository = assignmentRepository;
        this.ptoService = ptoService;
        this.holidayService = holidayService;
        this.overtimeService = overtimeService;
        this.countryRepository = countryRepository;
        this.sqlMapper = sqlMapper;
    }

    public record BillingDto(UUID id, UUID employeeId, String internalId, String firstName, String lastName,
                             String clientName, String projectName, BigDecimal rate, double hours, double ptoHours,
                             BigDecimal total, String notes) {

    }

    /**
     * Generates a billing report based on the provided parameters. This method calculates
     * billable days for assignments within a specified date range and adjusts for working days
     * and employee PTO (Paid Time Off) days.
     *
     * @param params ReactAdminParams object containing pagination and filter parameters for the report.
     * @return A paginated report of billing entries encapsulated in ReportDto with BillingDto data.
     */
    @Transactional(readOnly = true)
    public ReportDto<BillingDto> generateBillingReport(ReactAdminParams params) {
        DateRangeDto dateRange = new DateRangeDto(sqlMapper.map(params));
        LocalDate startDate = dateRange.getStartDate();
        LocalDate endDate = dateRange.getEndDate();

        LOGGER.info("Generating billing for period {} - {}", startDate, endDate);

        Map<Country, Set<LocalDate>> workingDaysByCountry = calculateWorkingDays(startDate, endDate);
        Map<Employee, Double> ptoHoursByEmployee = ptoService.retrieveEmployeePtoHours(startDate, endDate);

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

            billingList.add(new BillingEntry(assignment, workingDays.size() * 8,
                    ptoHoursByEmployee.getOrDefault(assignment.getEmployee(), 0.0)));
        });

        overtimeService.findByDateBetween(startDate, endDate)
                .forEach(overtime -> billingList.add(new BillingEntry(overtime)));

        return getPaginatedBillingEntries(params, billingList);
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

        Map<Country, Set<LocalDate>> holidaysByCountry = holidayService.getHolidaysByCountry(startDate, endDate);

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


    /**
     * Retrieves a paginated list of billing entries based on the specified parameters.
     *
     * @param params the ReactAdminParams object containing pagination and sorting parameters
     * @param billingList the list of BillingEntry objects to be paginated and processed
     * @return a ReportDto containing the paginated and sorted list of BillingDto objects
     *         and the total size of the original billing list
     */
    private static ReportDto<BillingDto> getPaginatedBillingEntries(ReactAdminParams params,
            List<BillingEntry> billingList) {
        Range<Integer> range = params.getRangeInterval();
        int minimum = range.getMinimum();
        int maximum = Math.min(range.getMaximum() + 1, billingList.size());

        List<BillingDto> data = billingList.stream()
                .map(BillingEntry::toDto)
                .sorted(params.getComparator(BillingDto.class))
                .toList()
                .subList(minimum, maximum);

        return new ReportDto<>(data, billingList.size());
    }
}
