package com.entropyteam.entropay.mcp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.entropyteam.entropay.common.ReactAdminParams;
import com.entropyteam.entropay.employees.dtos.ReportDto;
import com.entropyteam.entropay.employees.dtos.SalariesReportDto;
import com.entropyteam.entropay.employees.dtos.TurnoverReportDto;
import com.entropyteam.entropay.employees.dtos.TurnoverReportDto.TurnoverMetrics;
import com.entropyteam.entropay.employees.services.BillingService;
import com.entropyteam.entropay.employees.services.BillingService.BillingDto;
import com.entropyteam.entropay.employees.services.MarginService;
import com.entropyteam.entropay.employees.services.MarginService.MarginDto;
import com.entropyteam.entropay.employees.services.ReportService;
import com.entropyteam.entropay.employees.services.TurnoverService;

@ExtendWith(MockitoExtension.class)
class ReportsQueryServiceTest {

    @Mock
    private ReportService reportService;
    @Mock
    private BillingService billingService;
    @Mock
    private MarginService marginService;
    @Mock
    private TurnoverService turnoverService;

    private ReportsQueryService service() {
        return new ReportsQueryService(reportService, billingService, marginService, turnoverService);
    }

    @Test
    @DisplayName("get_turnover_report delegates with a JSON filter carrying the date range")
    void getTurnoverReportPassesParams() {
        TurnoverReportDto stub = new TurnoverReportDto(
                LocalDate.of(2025, 1, 1), LocalDate.of(2025, 3, 31),
                new TurnoverMetrics(10, 1, 9, BigDecimal.ZERO),
                Map.of(), List.of());
        when(turnoverService.generateHierarchicalTurnoverReport(any())).thenReturn(stub);

        TurnoverReportDto result = service().getTurnoverReport(LocalDate.of(2025, 1, 1), LocalDate.of(2025, 3, 31));

        assertEquals(stub, result);

        ArgumentCaptor<ReactAdminParams> captor = ArgumentCaptor.forClass(ReactAdminParams.class);
        org.mockito.Mockito.verify(turnoverService).generateHierarchicalTurnoverReport(captor.capture());
        String filter = captor.getValue().getFilter();
        assertTrue(filter.contains("\"startDate\":\"2025-01-01\""), "filter must carry startDate. got: " + filter);
        assertTrue(filter.contains("\"endDate\":\"2025-03-31\""), "filter must carry endDate. got: " + filter);
    }

    @Test
    @DisplayName("get_billing_report returns all rows when no client filter is given")
    void getBillingReportNoFilter() {
        when(billingService.generateBillingReport(any()))
                .thenReturn(new ReportDto<>(List.of(billing("Acme"), billing("Globex")), 2));

        List<BillingDto> result = service().getBillingReport(
                LocalDate.of(2025, 1, 1), LocalDate.of(2025, 12, 31), null);

        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("get_billing_report filters by client name substring (case-insensitive)")
    void getBillingReportClientFilter() {
        when(billingService.generateBillingReport(any()))
                .thenReturn(new ReportDto<>(List.of(billing("Acme Co"), billing("Globex")), 2));

        List<BillingDto> result = service().getBillingReport(
                LocalDate.of(2025, 1, 1), LocalDate.of(2025, 12, 31), "acme");

        assertEquals(1, result.size());
        assertEquals("Acme Co", result.get(0).clientName());
    }

    @Test
    @DisplayName("get_margin_report returns the underlying ReportDto data")
    void getMarginReport() {
        when(marginService.generateMarginReport(any()))
                .thenReturn(new ReportDto<>(List.of(margin("2025-03"), margin("2025-04")), 2));

        List<MarginDto> result = service().getMarginReport(
                LocalDate.of(2025, 3, 1), LocalDate.of(2025, 4, 30));

        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("get_salaries_report returns the current snapshot, no date range needed")
    void getSalariesReport() {
        when(reportService.getSalariesReport(any()))
                .thenReturn(new ReportDto<>(List.of(salary("Jane", "Doe")), 1));

        List<SalariesReportDto> result = service().getSalariesReport();

        assertEquals(1, result.size());
        assertEquals("Jane", result.get(0).firstName());
    }

    @Test
    @DisplayName("Date-range reports reject a missing start or end date")
    void rejectsMissingDates() {
        assertThrows(IllegalArgumentException.class,
                () -> service().getTurnoverReport(null, LocalDate.of(2025, 3, 31)));
        assertThrows(IllegalArgumentException.class,
                () -> service().getBillingReport(LocalDate.of(2025, 1, 1), null, null));
        assertThrows(IllegalArgumentException.class,
                () -> service().getMarginReport(null, null));
    }

    @Test
    @DisplayName("Date-range reports reject an end date earlier than the start date")
    void rejectsInvertedRange() {
        assertThrows(IllegalArgumentException.class, () -> service().getTurnoverReport(
                LocalDate.of(2025, 6, 1), LocalDate.of(2025, 5, 1)));
    }

    private BillingDto billing(String client) {
        return new BillingDto(UUID.randomUUID(), UUID.randomUUID(), "INT-1", "A", "B", client, "Project",
                new BigDecimal("80"), 160d, 8d, new BigDecimal("12800"), "notes");
    }

    private MarginDto margin(String yearMonth) {
        return new MarginDto(UUID.randomUUID(), yearMonth, UUID.randomUUID(), "INT-1", "A", "B", "Client",
                "Project", new BigDecimal("80"), 160d, 8d, new BigDecimal("12800"),
                new BigDecimal("3000"), new BigDecimal("9800"));
    }

    private SalariesReportDto salary(String first, String last) {
        return new SalariesReportDto(UUID.randomUUID(), UUID.randomUUID(), "INT-1", first, last, "ClientCo",
                new BigDecimal("3000"), "Mural", "USD", "PlatformX", "Argentina", true);
    }
}
