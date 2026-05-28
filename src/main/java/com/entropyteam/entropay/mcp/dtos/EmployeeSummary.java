package com.entropyteam.entropay.mcp.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import com.entropyteam.entropay.common.sensitiveInformation.EmployeeIdAware;
import com.entropyteam.entropay.common.sensitiveInformation.SensitiveInformation;

/**
 * Composite view assembled by {@code get_employee_summary}. The shape mirrors what an HR
 * leader would see when opening an employee's profile: identity + current engagement +
 * compensation + vacation balance + the freshest feedbacks and reimbursements.
 *
 * <p>{@code currentRate} and {@code currentSalary} are masked through the standard
 * {@code @SensitiveInformation} pipeline for non-ADMIN callers viewing internal employees,
 * consistent with how the platform masks the same numbers on the assignment and salaries
 * report screens.
 */
public record EmployeeSummary(
        UUID id,
        String internalId,
        String firstName,
        String lastName,
        String labourEmail,
        String country,
        boolean active,
        String currentRole,
        String currentProject,
        String currentClient,
        LocalDate startDate,
        LocalDate endDate,
        String timeSinceStart,
        @SensitiveInformation BigDecimal currentRate,
        @SensitiveInformation BigDecimal currentSalary,
        Integer vacationBalance,
        List<FeedbackHighlight> recentFeedbacks,
        List<ReimbursementHighlight> latestReimbursements) implements EmployeeIdAware {

    @Override
    public UUID getEmployeeId() {
        return id;
    }

    public record FeedbackHighlight(LocalDate feedbackDate, String source, String title, String createdBy) {
    }

    public record ReimbursementHighlight(LocalDate date, String category, BigDecimal amount, String comment) {
    }
}
