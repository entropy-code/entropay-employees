package com.entropyteam.entropay.employees.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ContractTest {

    @Test
    void testCalculateMonthlySalaryInUSD_WithMonthlyUSDSettlements() {
        Contract contract = buildContract();
        
        // Add multiple MONTHLY USD settlements
        PaymentSettlement settlement1 = new PaymentSettlement();
        settlement1.setModality(Modality.MONTHLY);
        settlement1.setCurrency(Currency.USD);
        settlement1.setSalary(BigDecimal.valueOf(3000));
        contract.addPaymentSettlement(settlement1);
        
        PaymentSettlement settlement2 = new PaymentSettlement();
        settlement2.setModality(Modality.MONTHLY);
        settlement2.setCurrency(Currency.USD);
        settlement2.setSalary(BigDecimal.valueOf(500));
        contract.addPaymentSettlement(settlement2);

        BigDecimal monthlySalary = contract.calculateMonthlySalaryInUSD();

        // Should sum both MONTHLY USD settlements
        Assertions.assertEquals(BigDecimal.valueOf(3500), monthlySalary);
    }

    @Test
    void testCalculateMonthlySalaryInUSD_FiltersByModality() {
        Contract contract = buildContract();
        
        // Add MONTHLY USD settlement
        PaymentSettlement monthlySettlement = new PaymentSettlement();
        monthlySettlement.setModality(Modality.MONTHLY);
        monthlySettlement.setCurrency(Currency.USD);
        monthlySettlement.setSalary(BigDecimal.valueOf(3000));
        contract.addPaymentSettlement(monthlySettlement);
        
        // Add HOUR USD settlement (should be filtered out)
        PaymentSettlement hourlySettlement = new PaymentSettlement();
        hourlySettlement.setModality(Modality.HOUR);
        hourlySettlement.setCurrency(Currency.USD);
        hourlySettlement.setSalary(BigDecimal.valueOf(50));
        contract.addPaymentSettlement(hourlySettlement);

        BigDecimal monthlySalary = contract.calculateMonthlySalaryInUSD();

        // Should only include MONTHLY settlement
        Assertions.assertEquals(BigDecimal.valueOf(3000), monthlySalary);
    }

    @Test
    void testCalculateMonthlySalaryInUSD_FiltersByCurrency() {
        Contract contract = buildContract();
        
        // Add MONTHLY USD settlement
        PaymentSettlement usdSettlement = new PaymentSettlement();
        usdSettlement.setModality(Modality.MONTHLY);
        usdSettlement.setCurrency(Currency.USD);
        usdSettlement.setSalary(BigDecimal.valueOf(3000));
        contract.addPaymentSettlement(usdSettlement);
        
        // Add MONTHLY ARS settlement (should be filtered out)
        PaymentSettlement arsSettlement = new PaymentSettlement();
        arsSettlement.setModality(Modality.MONTHLY);
        arsSettlement.setCurrency(Currency.ARS);
        arsSettlement.setSalary(BigDecimal.valueOf(500000));
        contract.addPaymentSettlement(arsSettlement);

        BigDecimal monthlySalary = contract.calculateMonthlySalaryInUSD();

        // Should only include USD settlement
        Assertions.assertEquals(BigDecimal.valueOf(3000), monthlySalary);
    }

    @Test
    void testCalculateMonthlySalaryInUSD_WithEmptyPaymentSettlements() {
        Contract contract = buildContract();

        BigDecimal monthlySalary = contract.calculateMonthlySalaryInUSD();

        // Should return ZERO when no settlements
        Assertions.assertEquals(BigDecimal.ZERO, monthlySalary);
    }

    private Contract buildContract() {
        Contract contract = new Contract();
        contract.setId(UUID.randomUUID());
        contract.setStartDate(LocalDate.of(2024, 1, 1));
        contract.setActive(true);
        
        Employee employee = new Employee();
        employee.setId(UUID.randomUUID());
        employee.setInternalId("E001");
        employee.setActive(true);
        contract.setEmployee(employee);
        
        Company company = new Company();
        company.setId(UUID.randomUUID());
        company.setName("Test Company");
        contract.setCompany(company);
        
        return contract;
    }
}
