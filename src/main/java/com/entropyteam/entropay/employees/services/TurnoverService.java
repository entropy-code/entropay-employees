package com.entropyteam.entropay.employees.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.apache.commons.lang3.Range;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.entropyteam.entropay.common.DateRangeDto;
import com.entropyteam.entropay.common.ReactAdminParams;
import com.entropyteam.entropay.common.ReactAdminSqlMapper;
import com.entropyteam.entropay.employees.dtos.ReportDto;
import com.entropyteam.entropay.employees.dtos.TurnoverEntryDto;
import com.entropyteam.entropay.employees.dtos.TurnoverReportDto;
import com.entropyteam.entropay.employees.repositories.AssignmentRepository;
import com.entropyteam.entropay.employees.repositories.projections.MonthlyAssignment;

/**
 * Service for calculating employee turnover rates for clients and projects.
 */
@Service
public class TurnoverService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TurnoverService.class);
    private final AssignmentRepository assignmentRepository;
    private final ReactAdminSqlMapper sqlMapper;

    public TurnoverService(
            AssignmentRepository assignmentRepository,
            ReactAdminSqlMapper sqlMapper) {
        this.assignmentRepository = assignmentRepository;
        this.sqlMapper = sqlMapper;
    }

    /**
     * Generates a hierarchical turnover report for clients and projects based on the provided parameters.
     *
     * @param params The parameters for filtering and pagination
     * @return A hierarchical report containing turnover data
     */
    @Transactional(readOnly = true)
    public TurnoverReportDto generateHierarchicalTurnoverReport(ReactAdminParams params) {
        DateRangeDto dateRange = new DateRangeDto(sqlMapper.map(params));
        LocalDate startDate = dateRange.getStartDate();
        LocalDate endDate = dateRange.getEndDate();

        LOGGER.info("Generating hierarchical turnover report for period {} - {}", startDate, endDate);

        // Get all assignments within the date range
        List<MonthlyAssignment> assignments =
                assignmentRepository.findMonthlyAssignmentBetweenPeriod(startDate, endDate);

        // Calculate turnover
        return calculateTurnover(assignments);
    }

    private TurnoverReportDto calculateTurnover(List<MonthlyAssignment> assignments) {
        if (assignments.isEmpty()) {
            return new TurnoverReportDto(
                    LocalDate.now(),
                    LocalDate.now(),
                    new TurnoverReportDto.TurnoverMetrics(0, 0, 0, BigDecimal.ZERO),
                    Map.of(),
                    List.of()
            );
        }

        // Extract start and end dates from the first assignment's yearMonth
        String firstYearMonth = assignments.getFirst().yearMonth();
        String lastYearMonth = assignments.getLast().yearMonth();
        LocalDate startDate = LocalDate.parse(firstYearMonth + "-01");
        LocalDate endDate = LocalDate.parse(lastYearMonth + "-01").plusMonths(1).minusDays(1);

        // Group assignments by yearMonth
        Map<String, List<MonthlyAssignment>> assignmentsByMonth = assignments.stream()
                .collect(java.util.stream.Collectors.groupingBy(MonthlyAssignment::yearMonth));

        // Group assignments by client
        Map<UUID, List<MonthlyAssignment>> assignmentsByClient = assignments.stream()
                .collect(java.util.stream.Collectors.groupingBy(MonthlyAssignment::clientId));

        // Calculate overall company metrics
        int overallEmployeesAtStart = countUniqueEmployees(assignmentsByMonth.get(firstYearMonth));
        int overallEmployeesAtEnd = countUniqueEmployees(assignmentsByMonth.get(lastYearMonth));
        int overallEmployeesLeft = calculateEmployeesLeft(assignmentsByMonth, firstYearMonth, lastYearMonth);
        BigDecimal overallTurnoverRate = calculateTurnoverRate(
                overallEmployeesLeft, overallEmployeesAtStart, overallEmployeesAtEnd);

        TurnoverReportDto.TurnoverMetrics overallMetrics = new TurnoverReportDto.TurnoverMetrics(
                overallEmployeesAtStart, overallEmployeesLeft, overallEmployeesAtEnd, overallTurnoverRate);

        // Calculate monthly company metrics
        Map<String, TurnoverReportDto.TurnoverMetrics> monthlyMetrics = calculateMonthlyMetrics(assignmentsByMonth);

        // Calculate client metrics
        List<TurnoverReportDto.ClientTurnoverDto> clientDtos = new ArrayList<>();
        for (Map.Entry<UUID, List<MonthlyAssignment>> clientEntry : assignmentsByClient.entrySet()) {
            UUID clientId = clientEntry.getKey();
            List<MonthlyAssignment> clientAssignments = clientEntry.getValue();
            String clientName = clientAssignments.getFirst().clientName();

            // Group client assignments by month
            Map<String, List<MonthlyAssignment>> clientAssignmentsByMonth = clientAssignments.stream()
                    .collect(java.util.stream.Collectors.groupingBy(MonthlyAssignment::yearMonth));

            // Calculate overall client metrics
            int clientEmployeesAtStart = countUniqueEmployees(
                    clientAssignmentsByMonth.getOrDefault(firstYearMonth, List.of()));
            int clientEmployeesAtEnd = countUniqueEmployees(
                    clientAssignmentsByMonth.getOrDefault(lastYearMonth, List.of()));
            int clientEmployeesLeft = calculateEmployeesLeft(
                    clientAssignmentsByMonth, firstYearMonth, lastYearMonth);
            BigDecimal clientTurnoverRate = calculateTurnoverRate(
                    clientEmployeesLeft, clientEmployeesAtStart, clientEmployeesAtEnd);

            TurnoverReportDto.TurnoverMetrics clientOverallMetrics = new TurnoverReportDto.TurnoverMetrics(
                    clientEmployeesAtStart, clientEmployeesLeft, clientEmployeesAtEnd, clientTurnoverRate);

            // Calculate monthly client metrics
            Map<String, TurnoverReportDto.TurnoverMetrics> clientMonthlyMetrics =
                    calculateMonthlyMetrics(clientAssignmentsByMonth);

            // Group client assignments by project
            Map<UUID, List<MonthlyAssignment>> assignmentsByProject = clientAssignments.stream()
                    .collect(java.util.stream.Collectors.groupingBy(MonthlyAssignment::projectId));

            // Calculate project metrics
            List<TurnoverReportDto.ProjectTurnoverDto> projectDtos = new ArrayList<>();
            for (Map.Entry<UUID, List<MonthlyAssignment>> projectEntry : assignmentsByProject.entrySet()) {
                UUID projectId = projectEntry.getKey();
                List<MonthlyAssignment> projectAssignments = projectEntry.getValue();
                String projectName = projectAssignments.getFirst().projectName();

                // Group project assignments by month
                Map<String, List<MonthlyAssignment>> projectAssignmentsByMonth = projectAssignments.stream()
                        .collect(java.util.stream.Collectors.groupingBy(MonthlyAssignment::yearMonth));

                // Calculate overall project metrics
                int projectEmployeesAtStart = countUniqueEmployees(
                        projectAssignmentsByMonth.getOrDefault(firstYearMonth, List.of()));
                int projectEmployeesAtEnd = countUniqueEmployees(
                        projectAssignmentsByMonth.getOrDefault(lastYearMonth, List.of()));
                int projectEmployeesLeft = calculateEmployeesLeft(
                        projectAssignmentsByMonth, firstYearMonth, lastYearMonth);
                BigDecimal projectTurnoverRate = calculateTurnoverRate(
                        projectEmployeesLeft, projectEmployeesAtStart, projectEmployeesAtEnd);

                TurnoverReportDto.TurnoverMetrics projectOverallMetrics = new TurnoverReportDto.TurnoverMetrics(
                        projectEmployeesAtStart, projectEmployeesLeft, projectEmployeesAtEnd, projectTurnoverRate);

                // Calculate monthly project metrics
                Map<String, TurnoverReportDto.TurnoverMetrics> projectMonthlyMetrics =
                        calculateMonthlyMetrics(projectAssignmentsByMonth);

                // Create project DTO
                TurnoverReportDto.ProjectTurnoverDto projectDto = new TurnoverReportDto.ProjectTurnoverDto(
                        projectId, projectName, clientId, projectOverallMetrics, projectMonthlyMetrics);
                projectDtos.add(projectDto);
            }

            // Create client DTO
            TurnoverReportDto.ClientTurnoverDto clientDto = new TurnoverReportDto.ClientTurnoverDto(
                    clientId, clientName, clientOverallMetrics, clientMonthlyMetrics, projectDtos);
            clientDtos.add(clientDto);
        }

        // Create and return the final report
        return new TurnoverReportDto(startDate, endDate, overallMetrics, monthlyMetrics, clientDtos);
    }

    private int countUniqueEmployees(List<MonthlyAssignment> assignments) {
        if (assignments == null || assignments.isEmpty()) {
            return 0;
        }
        return (int) assignments.stream()
                .map(MonthlyAssignment::employeeId)
                .distinct()
                .count();
    }

    private int calculateEmployeesLeft(
            Map<String, List<MonthlyAssignment>> assignmentsByMonth,
            String firstMonth,
            String lastMonth) {

        // Get employees at the start
        Set<UUID> employeesAtStart = assignmentsByMonth.getOrDefault(firstMonth, List.of()).stream()
                .map(MonthlyAssignment::employeeId)
                .collect(java.util.stream.Collectors.toSet());

        // Get employees at the end
        Set<UUID> employeesAtEnd = assignmentsByMonth.getOrDefault(lastMonth, List.of()).stream()
                .map(MonthlyAssignment::employeeId)
                .collect(java.util.stream.Collectors.toSet());

        // Employees who were present at the start but not at the end have left
        employeesAtStart.removeAll(employeesAtEnd);
        return employeesAtStart.size();
    }

    private Map<String, TurnoverReportDto.TurnoverMetrics> calculateMonthlyMetrics(
            Map<String, List<MonthlyAssignment>> assignmentsByMonth) {

        Map<String, TurnoverReportDto.TurnoverMetrics> monthlyMetrics = new LinkedHashMap<>();

        // Sort months chronologically
        List<String> sortedMonths = new ArrayList<>(assignmentsByMonth.keySet());
        Collections.sort(sortedMonths);

        for (int i = 0; i < sortedMonths.size(); i++) {
            String currentMonth = sortedMonths.get(i);
            List<MonthlyAssignment> currentMonthAssignments = assignmentsByMonth.get(currentMonth);

            int employeesAtStart = countUniqueEmployees(currentMonthAssignments);

            // For the last month, there are no employees who left
            if (i == sortedMonths.size() - 1) {
                monthlyMetrics.put(currentMonth, new TurnoverReportDto.TurnoverMetrics(
                        employeesAtStart, 0, employeesAtStart, BigDecimal.ZERO));
                continue;
            }

            // For other months, calculate employees who left
            String nextMonth = sortedMonths.get(i + 1);
            List<MonthlyAssignment> nextMonthAssignments = assignmentsByMonth.get(nextMonth);

            Set<UUID> currentEmployees = currentMonthAssignments.stream()
                    .map(MonthlyAssignment::employeeId)
                    .collect(java.util.stream.Collectors.toSet());

            Set<UUID> nextEmployees = nextMonthAssignments.stream()
                    .map(MonthlyAssignment::employeeId)
                    .collect(java.util.stream.Collectors.toSet());

            // Employees in current month but not in next month have left
            Set<UUID> leftEmployees = new HashSet<>(currentEmployees);
            leftEmployees.removeAll(nextEmployees);

            int employeesLeft = leftEmployees.size();
            int employeesAtEnd = employeesAtStart - employeesLeft;

            BigDecimal turnoverRate = calculateTurnoverRate(
                    employeesLeft, employeesAtStart, employeesAtEnd);

            monthlyMetrics.put(currentMonth, new TurnoverReportDto.TurnoverMetrics(
                    employeesAtStart, employeesLeft, employeesAtEnd, turnoverRate));
        }

        return monthlyMetrics;
    }

    /**
     * Calculates the turnover rate using the formula:
     * Turnover Rate = NELDY / [(NEBY + NEEY) / 2]
     * Where:
     * NELDY = Number of Employees who Left During the period
     * NEBY = Number of Employees at the Beginning of the period
     * NEEY = Number of Employees at the End of the period
     */
    private BigDecimal calculateTurnoverRate(int employeesLeft, int employeesAtStart, int employeesAtEnd) {
        if (employeesAtStart == 0 && employeesAtEnd == 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal avgEmployees = new BigDecimal(employeesAtStart + employeesAtEnd)
                .divide(new BigDecimal(2), 2, RoundingMode.HALF_UP);

        if (avgEmployees.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return new BigDecimal(employeesLeft)
                .divide(avgEmployees, 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal(100))
                .setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Generates a flat turnover report for clients and projects based on the provided parameters.
     * This method uses the hierarchical report and flattens it into a list of entries.
     *
     * @param params The parameters for filtering and pagination
     * @return A flat report containing turnover data
     */
    @Transactional(readOnly = true)
    public ReportDto<TurnoverEntryDto> generateFlatTurnoverReport(ReactAdminParams params) {
        LOGGER.info("Generating flat turnover report");

        // Get the hierarchical report
        TurnoverReportDto hierarchicalReport = generateHierarchicalTurnoverReport(params);

        // Transform it to a flat report
        List<TurnoverEntryDto> entries = transformToFlatReport(hierarchicalReport);

        // Apply pagination and return
        return getPaginatedEntries(params, entries);
    }

    /**
     * Transforms a hierarchical turnover report into a flat report.
     *
     * @param hierarchicalReport The hierarchical report to transform
     * @return A list of turnover entries
     */
    private List<TurnoverEntryDto> transformToFlatReport(TurnoverReportDto hierarchicalReport) {
        List<TurnoverEntryDto> entries = new ArrayList<>();

        // Add company overall metrics
        entries.add(new TurnoverEntryDto(
                TurnoverEntryDto.LevelType.COMPANY,
                "Company", // No ID for company
                "Company", // Generic name for company
                null, // No parent for company
                TurnoverEntryDto.PeriodType.OVERALL,
                null, // No year-month for overall
                hierarchicalReport.overall().employeesAtStart(),
                hierarchicalReport.overall().employeesLeft(),
                hierarchicalReport.overall().employeesAtEnd(),
                hierarchicalReport.overall().turnoverRate()
        ));

        // Add company monthly metrics
        for (Map.Entry<String, TurnoverReportDto.TurnoverMetrics> entry : hierarchicalReport.yearMonths().entrySet()) {
            String yearMonth = entry.getKey();
            TurnoverReportDto.TurnoverMetrics metrics = entry.getValue();

            entries.add(new TurnoverEntryDto(
                    TurnoverEntryDto.LevelType.COMPANY,
                    "Company-" + yearMonth,
                    "Company", // Generic name for company
                    null, // No parent for company
                    TurnoverEntryDto.PeriodType.MONTHLY,
                    yearMonth,
                    metrics.employeesAtStart(),
                    metrics.employeesLeft(),
                    metrics.employeesAtEnd(),
                    metrics.turnoverRate()
            ));
        }

        // Add client metrics
        for (TurnoverReportDto.ClientTurnoverDto client : hierarchicalReport.clients()) {
            // Add client overall metrics
            entries.add(new TurnoverEntryDto(
                    TurnoverEntryDto.LevelType.CLIENT,
                    "Client-" + client.name(),
                    client.name(),
                    null, // No parent for client
                    TurnoverEntryDto.PeriodType.OVERALL,
                    null, // No year-month for overall
                    client.overall().employeesAtStart(),
                    client.overall().employeesLeft(),
                    client.overall().employeesAtEnd(),
                    client.overall().turnoverRate()
            ));

            // Add client monthly metrics
            for (Map.Entry<String, TurnoverReportDto.TurnoverMetrics> entry : client.yearMonths().entrySet()) {
                String yearMonth = entry.getKey();
                TurnoverReportDto.TurnoverMetrics metrics = entry.getValue();

                entries.add(new TurnoverEntryDto(
                        TurnoverEntryDto.LevelType.CLIENT,
                        "Client-" + client.name() + "-" + yearMonth,
                        client.name(),
                        null, // No parent for client
                        TurnoverEntryDto.PeriodType.MONTHLY,
                        yearMonth,
                        metrics.employeesAtStart(),
                        metrics.employeesLeft(),
                        metrics.employeesAtEnd(),
                        metrics.turnoverRate()
                ));
            }

            // Add project metrics
            for (TurnoverReportDto.ProjectTurnoverDto project : client.projects()) {
                // Add project overall metrics
                entries.add(new TurnoverEntryDto(
                        TurnoverEntryDto.LevelType.PROJECT,
                        "Client-" + client.name() + "-Project-" + project.name(),
                        project.name(),
                        client.id(), // Client is the parent of project
                        TurnoverEntryDto.PeriodType.OVERALL,
                        null, // No year-month for overall
                        project.overall().employeesAtStart(),
                        project.overall().employeesLeft(),
                        project.overall().employeesAtEnd(),
                        project.overall().turnoverRate()
                ));

                // Add project monthly metrics
                for (Map.Entry<String, TurnoverReportDto.TurnoverMetrics> entry : project.yearMonths().entrySet()) {
                    String yearMonth = entry.getKey();
                    TurnoverReportDto.TurnoverMetrics metrics = entry.getValue();

                    entries.add(new TurnoverEntryDto(
                            TurnoverEntryDto.LevelType.PROJECT,
                            "Client-" + client.name() + "-Project-" + project.name() + "-" + yearMonth,
                            project.name(),
                            client.id(), // Client is the parent of project
                            TurnoverEntryDto.PeriodType.MONTHLY,
                            yearMonth,
                            metrics.employeesAtStart(),
                            metrics.employeesLeft(),
                            metrics.employeesAtEnd(),
                            metrics.turnoverRate()
                    ));
                }
            }
        }

        return entries;
    }

    /**
     * Returns a paginated list of turnover entries based on the provided parameters.
     *
     * @param params The parameters for filtering and pagination
     * @param entries The list of turnover entries to paginate
     * @return A paginated list of turnover entries
     */
    private static ReportDto<TurnoverEntryDto> getPaginatedEntries(ReactAdminParams params,
            List<TurnoverEntryDto> entries) {

        List<TurnoverEntryDto> data = entries.stream()
                .filter(params.getFilter(TurnoverEntryDto.class))
                .sorted(params.getComparator(TurnoverEntryDto.class))
                .toList();

        Range<Integer> range = params.getRangeInterval();
        int minimum = range.getMinimum();
        int maximum = Math.min(range.getMaximum() + 1, data.size());

        return new ReportDto<>(data.subList(minimum, maximum), data.size());
    }
}
