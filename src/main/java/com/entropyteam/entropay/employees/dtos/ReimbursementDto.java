package com.entropyteam.entropay.employees.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import com.entropyteam.entropay.employees.models.Reimbursement;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ReimbursementDto(UUID id,
                               @NotNull(message = "Employee ID is mandatory")
                               UUID employeeId,
                               @NotNull(message = "Category ID is mandatory")
                               UUID categoryId,
                               @NotNull(message = "Amount is mandatory")
                               @Positive(message = "Amount must be positive")
                               BigDecimal amount,
                               @NotNull(message = "Date is mandatory")
                               LocalDate date,
                               String comment) {

    public ReimbursementDto(Reimbursement reimbursement) {
        this(reimbursement.getId(),
                reimbursement.getEmployee().getId(),
                reimbursement.getCategory().getId(),
                reimbursement.getAmount(),
                reimbursement.getDate(),
                reimbursement.getComment());
    }
}