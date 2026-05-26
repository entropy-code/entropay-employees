package com.entropyteam.entropay.employees.payroll;

import java.math.BigDecimal;
import java.util.UUID;
import com.entropyteam.entropay.common.sensitiveInformation.EmployeeIdAware;
import com.entropyteam.entropay.common.sensitiveInformation.SensitiveInformation;

record PayrollItemDto(
        UUID id,
        UUID payrollRunId,
        UUID employeeId,
        String internalId,
        String firstName,
        String lastName,
        UUID contractId,
        String clientName,
        String paymentPlatform,
        String modality,
        @SensitiveInformation BigDecimal baseSalary,
        @SensitiveInformation BigDecimal proportionalSalary,
        Integer countryWorkingHoursInMonth,
        BigDecimal ptoHoursInMonth,
        BigDecimal unpaidLeaveHoursInMonth,
        @SensitiveInformation BigDecimal unpaidLeaveDeduction,
        BigDecimal billableHoursInMonth,
        BigDecimal overtimeHours,
        @SensitiveInformation BigDecimal overtimeAmount,
        @SensitiveInformation BigDecimal reimbursementsAmount,
        @SensitiveInformation BigDecimal hardwareClawback,
        @SensitiveInformation BigDecimal vacationCashout,
        @SensitiveInformation BigDecimal adjustment,
        @SensitiveInformation BigDecimal previousBalance,
        String notes,
        @SensitiveInformation BigDecimal totalAmount,
        boolean isFinalSettlement,
        String calculationError
) implements EmployeeIdAware {

    @Override
    public UUID getEmployeeId() {
        return employeeId;
    }
}
