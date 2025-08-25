package com.entropyteam.entropay.employees.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.entropyteam.entropay.common.DateRangeDto;
import com.entropyteam.entropay.common.ReactAdminParams;
import com.entropyteam.entropay.common.ReactAdminSqlMapper;
import com.entropyteam.entropay.common.sensitiveInformation.EmployeeIdAware;
import com.entropyteam.entropay.common.sensitiveInformation.SensitiveInformation;
import com.entropyteam.entropay.employees.dtos.ReportDto;
import com.entropyteam.entropay.employees.models.Assignment;
import com.entropyteam.entropay.employees.models.Employee;
import com.entropyteam.entropay.employees.models.Overtime;
import com.entropyteam.entropay.employees.timetracking.AssignmentTimeEntry;
import com.entropyteam.entropay.employees.timetracking.OvertimeTimeEntry;
import com.entropyteam.entropay.employees.timetracking.PtoTimeEntry;
import com.entropyteam.entropay.employees.timetracking.TimeTrackingEntry;

@Service
public class MarginService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MarginService.class);
    private final PtoService ptoService;
    private final OvertimeService overtimeService;
    private final AssignmentService assignmentService;
    private final ContractService contractService;
    private final ReactAdminSqlMapper sqlMapper;


    public MarginService(PtoService ptoService, OvertimeService overtimeService, AssignmentService assignmentService,
            ContractService contractService, ReactAdminSqlMapper sqlMapper) {
        this.ptoService = ptoService;
        this.overtimeService = overtimeService;
        this.assignmentService = assignmentService;
        this.contractService = contractService;
        this.sqlMapper = sqlMapper;
    }

    public record MarginDto(UUID id, String yearMonth, UUID employeeId, String internalId, String firstName,
                            String lastName, String clientName, String projectName,
                            @SensitiveInformation BigDecimal rate, double hours,
                            double ptoHours, @SensitiveInformation BigDecimal total,
                            @SensitiveInformation BigDecimal paid, @SensitiveInformation BigDecimal margin)
            implements EmployeeIdAware {

        @Override
        public UUID getEmployeeId() {
            return employeeId();
        }
    }

    private record MonthlyAssignment(Assignment assignment, YearMonth month) {

        public MonthlyAssignment(AssignmentTimeEntry assignment) {
            this(assignment.getAssignment(), YearMonth.from(assignment.getDate()));
        }

        public MonthlyAssignment(Overtime overtime) {
            this(overtime.getAssignment(), YearMonth.from(overtime.getDate()));
        }
    }

    private record EmployeeMonthly(Employee employee, YearMonth month) {

        public EmployeeMonthly(PtoTimeEntry ptoTimeEntry) {
            this(ptoTimeEntry.getEmployee(), YearMonth.from(ptoTimeEntry.getDate()));
        }
    }

    @Transactional(readOnly = true)
    public ReportDto<MarginDto> generateMarginReport(ReactAdminParams params) {
        DateRangeDto dateRange = new DateRangeDto(sqlMapper.map(params));
        LocalDate startDate = dateRange.getStartDate();
        LocalDate endDate = dateRange.getEndDate();

        StopWatch stopWatch = StopWatch.createStarted();
        LOGGER.info("Generating margin for period {} - {}", startDate, endDate);

        Map<EmployeeMonthly, BigDecimal> salaries = getSalaries(startDate, endDate);
        Map<EmployeeMonthly, Double> ptoHours = getPtoHours(startDate, endDate);

        Map<MonthlyAssignment, List<TimeTrackingEntry>> timeTrackingByAssignment =
                assignmentService.findActivityForAssignments(startDate, endDate).stream()
                        .collect(Collectors.groupingBy(MonthlyAssignment::new, Collectors.toList()));

        overtimeService.findByDateBetween(startDate, endDate).forEach(overtime -> {
            MonthlyAssignment key = new MonthlyAssignment(overtime);
            OvertimeTimeEntry overtimeTimeEntry = new OvertimeTimeEntry(overtime);
            timeTrackingByAssignment.computeIfAbsent(key, k -> new ArrayList<>()).add(overtimeTimeEntry);
        });

        List<MarginDto> report = new ArrayList<>();
        timeTrackingByAssignment.forEach((key, value) -> {
            EmployeeMonthly employeeMonthly = new EmployeeMonthly(key.assignment().getEmployee(), key.month());
            BigDecimal salary = salaries.get(employeeMonthly);
            Double ptoHoursValue = ptoHours.getOrDefault(employeeMonthly, 0.0);
            Double hours = value.stream().map(TimeTrackingEntry::getHours).reduce(Double::sum).orElse(0.0);
            BigDecimal rate = key.assignment.getBillableRate();

            report.add(getMarginDto(key, rate, hours, ptoHoursValue, salary));
        });

        LOGGER.info("Margin report generated in {} ms", stopWatch.getTime(TimeUnit.MILLISECONDS));

        return getPaginatedEntries(params, report);
    }

    private static MarginDto getMarginDto(MonthlyAssignment key, BigDecimal rate, Double hours, Double ptoHoursValue,
            BigDecimal salary) {
        BigDecimal total = rate.multiply(BigDecimal.valueOf(hours - ptoHoursValue));
        BigDecimal margin = salary != null ? total.subtract(salary) : BigDecimal.ZERO;
        return new MarginDto(UUID.randomUUID(), key.month.toString(), key.assignment.getEmployee().getId(),
                key.assignment.getEmployee().getInternalId(), key.assignment.getEmployee().getFirstName(),
                key.assignment.getEmployee().getLastName(), key.assignment.getProject().getClient().getName(),
                key.assignment.getProject().getName(), rate, hours, ptoHoursValue,
                total, salary, margin);
    }

    private Map<EmployeeMonthly, Double> getPtoHours(LocalDate startDate, LocalDate endDate) {
        return ptoService.findPtoActivities(startDate, endDate).stream()
                .collect(Collectors.groupingBy(EmployeeMonthly::new, Collectors.summingDouble(PtoTimeEntry::getHours)));
    }

    private Map<EmployeeMonthly, BigDecimal> getSalaries(LocalDate startDate, LocalDate endDate) {
        Map<EmployeeMonthly, BigDecimal> salaries = new HashMap<>();

        contractService.findByDateBetween(startDate, endDate)
                .forEach(contract -> {
                    Map<YearMonth, BigDecimal> settlementByMonth =
                            contract.getPaymentSettlementByMonth(startDate, endDate);
                    settlementByMonth.forEach((yearMonth, salary) -> {
                        EmployeeMonthly key = new EmployeeMonthly(contract.getEmployee(), yearMonth);
                        BigDecimal existingSalary = salaries.getOrDefault(key, BigDecimal.ZERO);
                        salaries.put(key, existingSalary.add(salary));
                    });
                });

        return salaries;
    }

    private static ReportDto<MarginDto> getPaginatedEntries(ReactAdminParams params,
            List<MarginDto> marginDtos) {

        List<MarginDto> data = marginDtos.stream()
                .filter(params.getFilter(MarginDto.class))
                .sorted(params.getComparator(MarginDto.class))
                .toList();

        Range<Integer> range = params.getRangeInterval();
        int minimum = range.getMinimum();
        int maximum = Math.min(range.getMaximum() + 1, data.size());

        return new ReportDto<>(data.subList(minimum, maximum), data.size());
    }

}
