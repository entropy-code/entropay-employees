package com.entropyteam.entropay.employees.payroll;

import java.time.LocalDate;
import jakarta.validation.constraints.NotNull;

/**
 * Inputs the FE provides when launching a payroll run. Today every entropista is paid in USD,
 * so the target period is the only thing the run needs.
 */
record TriggerPayrollDto(
        @NotNull LocalDate period
) {
}
