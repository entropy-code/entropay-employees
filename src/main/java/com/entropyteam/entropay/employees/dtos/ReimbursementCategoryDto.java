package com.entropyteam.entropay.employees.dtos;

import java.math.BigDecimal;
import java.util.UUID;
import com.entropyteam.entropay.employees.models.ReimbursementCategory;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ReimbursementCategoryDto(UUID id,
                                       @NotNull(message = "Name is mandatory")
                                       String name,
                                       String description,
                                       @Positive(message = "Maximum amount must be positive")
                                       BigDecimal maximumAmount,
                                       @Min(value = 1, message = "Period must be at least 1 month")
                                       Integer periodInMonths) {

    public ReimbursementCategoryDto(ReimbursementCategory category) {
        this(category.getId(), 
             category.getName(), 
             category.getDescription(),
             category.getMaximumAmount(),
             category.getPeriodInMonths());
    }
}