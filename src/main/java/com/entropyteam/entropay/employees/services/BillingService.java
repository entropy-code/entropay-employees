package com.entropyteam.entropay.employees.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.entropyteam.entropay.common.DateRangeDto;
import com.entropyteam.entropay.common.ReactAdminMapper;
import com.entropyteam.entropay.common.ReactAdminParams;
import com.entropyteam.entropay.common.sensitiveInformation.EmployeeIdAware;
import com.entropyteam.entropay.common.sensitiveInformation.SensitiveInformation;
import com.entropyteam.entropay.employees.dtos.ReportDto;
import com.entropyteam.entropay.employees.models.Employee;
import com.entropyteam.entropay.employees.timetracking.AssignmentTimeEntry;

@Service
public class BillingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BillingService.class);
    private final PtoService ptoService;
    private final OvertimeService overtimeService;
    private final AssignmentService assignmentService;
    private final ReactAdminMapper mapper;


    public BillingService(AssignmentService assignmentService, PtoService ptoService,
            OvertimeService overtimeService, ReactAdminMapper mapper) {
        this.assignmentService = assignmentService;
        this.ptoService = ptoService;
        this.overtimeService = overtimeService;
        this.mapper = mapper;
    }

    public record BillingDto(UUID id, UUID employeeId, String internalId, String firstName, String lastName,
                             String clientName, String projectName, @SensitiveInformation BigDecimal rate, double hours,
                             double ptoHours, @SensitiveInformation BigDecimal total, String notes)
            implements EmployeeIdAware {

        @Override
        public UUID getEmployeeId() {
            return employeeId();
        }
    }

    /**
     * Generates a billing report based on the provided parameters. This method calculates
     * billable days for assignments within a specified date range and adjusts for working days, employee PTO days,
     * holidays and also takes into consideration project that have paid PTO
     *
     * @param params ReactAdminParams object containing pagination and filter parameters for the report.
     * @return A paginated report of billing entries encapsulated in ReportDto with BillingDto data.
     */
    @Transactional(readOnly = true)
    public ReportDto<BillingDto> generateBillingReport(ReactAdminParams params) {
        DateRangeDto dateRange = new DateRangeDto(mapper.map(params));
        LocalDate startDate = dateRange.getStartDate();
        LocalDate endDate = dateRange.getEndDate();

        LOGGER.info("Generating billing for period {} - {}", startDate, endDate);
        List<BillingEntry> billingList = new ArrayList<>();
        billingList.addAll(getBillingEntries(startDate, endDate));
        // Business rule: we paid Overtimes with a 10 days buffer
        billingList.addAll(getOvertimes(startDate.minusDays(10), endDate.minusDays(10)));

        List<BillingDto> billingDtos = billingList.stream()
                .map(BillingEntry::toDto)
                .toList();

        return mapper.paginate(params, billingDtos, BillingDto.class);
    }

    private List<BillingEntry> getBillingEntries(LocalDate startDate, LocalDate endDate) {
        Map<Employee, Double> ptoHoursByEmployee = ptoService.getEmployeePtoHours(startDate, endDate);

        return assignmentService.findActivityForAssignments(startDate, endDate)
                .stream()
                .collect(Collectors.groupingBy(AssignmentTimeEntry::getAssignment, Collectors.toList()))
                .entrySet()
                .stream()
                .map(entry -> {
                    Double ptoHours = ptoHoursByEmployee.getOrDefault(entry.getKey().getEmployee(), 0.0);
                    return new BillingEntry(entry.getKey(), entry.getValue().size() * 8, ptoHours);
                })
                .toList();
    }

    private List<BillingEntry> getOvertimes(LocalDate startDate, LocalDate endDate) {
        return overtimeService.findByDateBetween(startDate, endDate)
                .stream()
                .map(BillingEntry::new)
                .toList();
    }
}
