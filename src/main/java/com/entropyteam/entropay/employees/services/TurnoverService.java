package com.entropyteam.entropay.employees.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.entropyteam.entropay.common.DateRangeDto;
import com.entropyteam.entropay.common.ReactAdminParams;
import com.entropyteam.entropay.common.ReactAdminSqlMapper;
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
        return null;
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


}
