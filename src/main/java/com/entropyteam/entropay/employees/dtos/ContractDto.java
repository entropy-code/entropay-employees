package com.entropyteam.entropay.employees.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.entropyteam.entropay.employees.models.Contract;
import com.entropyteam.entropay.employees.models.PaymentSettlement;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public record ContractDto(UUID id,
                          @NotNull(message = "Company is mandatory")
                          UUID companyId,
                          @NotNull(message ="Employee is mandatory")
                          UUID employeeId,
                          @NotNull(message = "Role is mandatory")
                          UUID roleId,
                          @NotNull(message = "Seniority is mandatory")
                          UUID seniorityId,
                          Integer hoursPerMonth,
                          @NotNull(message = "Start date is mandatory")
                          @JsonFormat(pattern = "yyyy-MM-dd")
                          LocalDate startDate,
                          @JsonFormat(pattern = "yyyy-MM-dd")
                          LocalDate endDate,
                          String benefits,
                          String notes,
                          @NotNull(message = "Contract type is mandatory")
                          String contractType,
                          List<PaymentSettlementDto> paymentSettlement,
                          boolean deleted,
                          boolean active,
                          @Nullable
                          UUID endReasonId,
                          @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                          LocalDateTime createdAt,
                          @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime modifiedAt
) {


    public ContractDto(Contract contract) {
        this(contract.getId(), contract.getCompany().getId(), contract.getEmployee().getId(),
                contract.getRole().getId(), contract.getSeniority().getId(), contract.getHoursPerMonth(), contract.getStartDate(), contract.getEndDate(),
                contract.getBenefits(), contract.getNotes(), contract.getContractType().name(),
                contract.getPaymentsSettlement().stream().map(PaymentSettlementDto::new).toList(), contract.isDeleted(),
                contract.isActive(), contract.getEndReasonId(), contract.getCreatedAt(), contract.getModifiedAt());
    }

    public ContractDto withActive(boolean active) {
        return this.active == active ? this : new ContractDto(
                this.id,
                this.companyId,
                this.employeeId,
                this.roleId,
                this.seniorityId,
                this.hoursPerMonth,
                this.startDate,
                this.endDate,
                this.benefits,
                this.notes,
                this.contractType,
                this.paymentSettlement,
                this.deleted,
                active,
                this.endReasonId,
                this.createdAt,
                this.modifiedAt
        );
    }

    public ContractDto(Contract contract, List<PaymentSettlement> paymentSettlementList ) {
        this(contract.getId(), contract.getCompany().getId(), contract.getEmployee().getId(),
                contract.getRole().getId(), contract.getSeniority().getId(), contract.getHoursPerMonth(),
                contract.getStartDate(), contract.getEndDate(),
                contract.getBenefits(), contract.getNotes(), contract.getContractType().name(),
                paymentSettlementList.stream().map(PaymentSettlementDto::new).toList(), contract.isDeleted(),
                contract.isActive(), contract.getEndReasonId(), contract.getCreatedAt(), contract.getModifiedAt());
    }

}
