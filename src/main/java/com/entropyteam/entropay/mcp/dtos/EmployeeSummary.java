package com.entropyteam.entropay.mcp.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import com.entropyteam.entropay.common.sensitiveInformation.EmployeeIdAware;
import com.entropyteam.entropay.common.sensitiveInformation.SensitiveInformation;

/**
 * Composite view assembled by {@code get_employee_summary}. The shape mirrors what an HR
 * leader would see when opening an employee's profile: identity + current engagements +
 * compensation + vacation balance + the freshest feedbacks and reimbursements.
 *
 * <p>An employee can be active on more than one project/client at the same time, so the
 * current engagement is modelled as a list of {@link ActiveEngagement} rather than a single
 * project/client/rate triple. Each engagement carries its own billable rate.
 *
 * <p>{@code rate} on every {@link ActiveEngagement} and {@code currentSalary} are masked
 * through the standard {@code @SensitiveInformation} pipeline for non-ADMIN callers viewing
 * internal employees. The nested {@code ActiveEngagement} records are intentionally not
 * {@code EmployeeIdAware}: the serializer walks up to this record (the nearest
 * {@code EmployeeIdAware} ancestor) to resolve the masking subject, which is correct because
 * every engagement belongs to this same employee. This keeps the masking consistent with how
 * the platform masks the same numbers on the assignment and salaries report screens.
 */
public record EmployeeSummary(
        UUID id,
        String internalId,
        String firstName,
        String lastName,
        String labourEmail,
        String country,
        boolean active,
        LocalDate startDate,
        LocalDate endDate,
        String timeSinceStart,
        List<ActiveEngagement> activeEngagements,
        @SensitiveInformation BigDecimal currentSalary,
        Integer vacationBalance,
        List<FeedbackHighlight> recentFeedbacks,
        List<ReimbursementHighlight> latestReimbursements) implements EmployeeIdAware {

    @Override
    public UUID getEmployeeId() {
        return id;
    }

    /**
     * A single active project assignment: the project, its client, the role the employee plays
     * on it, and the billable rate for that engagement. {@code rate} is masked for non-admin
     * callers viewing internal employees (see the class javadoc on how the masking subject is
     * resolved for this nested record).
     */
    public record ActiveEngagement(String project, String client, String role,
            @SensitiveInformation BigDecimal rate) {
    }

    public record FeedbackHighlight(LocalDate feedbackDate, String source, String title, String createdBy) {
    }

    public record ReimbursementHighlight(LocalDate date, String category, BigDecimal amount, String comment) {
    }
}
