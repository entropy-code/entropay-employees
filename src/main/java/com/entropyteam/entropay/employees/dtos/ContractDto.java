package com.entropyteam.entropay.employees.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import com.entropyteam.entropay.employees.models.Contract;
import com.fasterxml.jackson.annotation.JsonFormat;

public record ContractDto(
        UUID id,
        UUID companyId,
        UUID employeeId,
        UUID positionId,
        UUID seniorityId,
        Integer hoursPerWeek,
        BigDecimal costRate,
        Integer vacations,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate startDate,
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
        boolean deleted,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdAt,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime modifiedAt
) {


    public ContractDto(Contract contract) {
        this(
                contract.getId(), contract.getCompany().getId(), contract.getEmployee().getId(),
                contract.getRole().getId(), contract.getSeniority().getId(), contract.getHoursPerWeek(), contract.getCostRate(),
                contract.getVacations(), contract.getStartDate(), contract.getEndDate(), contract.isDeleted(),
                contract.getCreatedAt(), contract.getModifiedAt()
        );
    }
}
