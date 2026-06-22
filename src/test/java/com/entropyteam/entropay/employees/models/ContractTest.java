package com.entropyteam.entropay.employees.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ContractTest {

    private static Contract contractWith(PaymentSettlement... settlements) {
        Contract contract = new Contract();
        contract.setStartDate(LocalDate.of(2024, 1, 1));
        for (PaymentSettlement settlement : settlements) {
            contract.addPaymentSettlement(settlement);
        }
        return contract;
    }

    private static PaymentSettlement settlement(Modality modality, Currency currency, BigDecimal salary) {
        PaymentSettlement settlement = new PaymentSettlement();
        settlement.setModality(modality);
        settlement.setCurrency(currency);
        settlement.setSalary(salary);
        return settlement;
    }

    @DisplayName("calculateMonthlySalaryInUSD sums only MONTHLY/USD settlements (unchanged)")
    @Test
    void calculateMonthlySalaryInUSD_monthlyUsd() {
        Contract contract = contractWith(settlement(Modality.MONTHLY, Currency.USD, BigDecimal.valueOf(2300)));

        Assertions.assertEquals(BigDecimal.valueOf(2300), contract.calculateMonthlySalaryInUSD());
    }

    @DisplayName("calculateMonthlySalaryInUSD ignores HOUR settlements")
    @Test
    void calculateMonthlySalaryInUSD_hourReturnsZero() {
        Contract contract = contractWith(settlement(Modality.HOUR, Currency.USD, BigDecimal.valueOf(60)));

        Assertions.assertEquals(0, BigDecimal.ZERO.compareTo(contract.calculateMonthlySalaryInUSD()));
    }

    @DisplayName("calculateHourlyCostInUSD returns the HOUR/USD settlement salary as the hourly cost")
    @Test
    void calculateHourlyCostInUSD_hourUsd() {
        Contract contract = contractWith(settlement(Modality.HOUR, Currency.USD, BigDecimal.valueOf(60)));

        Assertions.assertEquals(BigDecimal.valueOf(60), contract.calculateHourlyCostInUSD());
    }

    @DisplayName("calculateHourlyCostInUSD returns zero for a monthly contract")
    @Test
    void calculateHourlyCostInUSD_monthlyReturnsZero() {
        Contract contract = contractWith(settlement(Modality.MONTHLY, Currency.USD, BigDecimal.valueOf(2300)));

        Assertions.assertEquals(0, BigDecimal.ZERO.compareTo(contract.calculateHourlyCostInUSD()));
    }

    @DisplayName("getHourlyCostByMonth repeats the hourly cost for every active month in range")
    @Test
    void getHourlyCostByMonth_hourly() {
        Contract contract = contractWith(settlement(Modality.HOUR, Currency.USD, BigDecimal.valueOf(60)));

        Map<YearMonth, BigDecimal> byMonth =
                contract.getHourlyCostByMonth(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 3, 31));

        Assertions.assertEquals(3, byMonth.size());
        Assertions.assertEquals(BigDecimal.valueOf(60), byMonth.get(YearMonth.of(2024, 1)));
        Assertions.assertEquals(BigDecimal.valueOf(60), byMonth.get(YearMonth.of(2024, 2)));
        Assertions.assertEquals(BigDecimal.valueOf(60), byMonth.get(YearMonth.of(2024, 3)));
    }

    @DisplayName("getHourlyCostByMonth is empty for a monthly contract")
    @Test
    void getHourlyCostByMonth_monthlyIsEmpty() {
        Contract contract = contractWith(settlement(Modality.MONTHLY, Currency.USD, BigDecimal.valueOf(2300)));

        Assertions.assertTrue(
                contract.getHourlyCostByMonth(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 3, 31)).isEmpty());
    }
}
