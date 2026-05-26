package com.entropyteam.entropay.employees.payroll;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonFormat;

record PayrollRunDto(
        UUID id,
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDate period,
        PayrollRunStatus status,
        UUID triggeredByUserId,
        String triggeredByEmail,
        Instant startedAt,
        Instant completedAt,
        String errorMessage,
        BigDecimal totalAmount,
        Integer employeeCount,
        List<PayrollItemDto> items
) {
}
