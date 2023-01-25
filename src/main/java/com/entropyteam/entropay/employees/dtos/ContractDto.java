package com.entropyteam.entropay.employees.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.entropyteam.entropay.employees.models.Contract;
import com.entropyteam.entropay.employees.models.PaymentInformation;
import com.entropyteam.entropay.employees.models.PaymentSettlement;
import com.fasterxml.jackson.annotation.JsonFormat;

public record ContractDto(
        UUID id,
        UUID companyId,
        UUID employeeId,
        UUID roleId,
        UUID seniorityId,
        Integer hoursPerMonth,
        Integer vacations,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate startDate,
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
        String benefits,
        String notes,
        String contractType,
        List<PaymentSettlementDto> paymentSettlement,
        boolean deleted,
        boolean active,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdAt,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime modifiedAt
) {


    public ContractDto(Contract contract) {
        this(
                contract.getId(), contract.getCompany().getId(), contract.getEmployee().getId(),
                contract.getRole().getId(), contract.getSeniority().getId(), contract.getHoursPerMonth()
                ,contract.getVacations(), contract.getStartDate(), contract.getEndDate(),
                contract.getBenefits(), contract.getNotes(), contract.getContractType().name(),
                contract.getPaymentsSettlement().stream().map(PaymentSettlementDto::new).toList(), contract.isDeleted(),
                contract.isActive(), contract.getCreatedAt(), contract.getModifiedAt()
        );
    }

    public ContractDto withActive(boolean active) {
        return this.active == active ? this : new ContractDto(
                this.id,
                this.companyId,
                this.employeeId,
                this.roleId,
                this.seniorityId,
                this.hoursPerMonth,
                this.vacations,
                this.startDate,
                this.endDate,
                this.benefits,
                this.notes,
                this.contractType,
                this.paymentSettlement,
                this.deleted,
                active,
                this.createdAt,
                this.modifiedAt
        );
    }

    public ContractDto(Contract contract, List<PaymentSettlement> paymentSettlementList ) {
        this(
                contract.getId(), contract.getCompany().getId(), contract.getEmployee().getId(),
                contract.getRole().getId(), contract.getSeniority().getId(), contract.getHoursPerMonth()
                ,contract.getVacations(), contract.getStartDate(), contract.getEndDate(),
                contract.getBenefits(), contract.getNotes(), contract.getContractType().name(),
                paymentSettlementList.stream().map(PaymentSettlementDto::new).toList(), contract.isDeleted(),
                contract.isActive(), contract.getCreatedAt(), contract.getModifiedAt()
        );
    }

}
