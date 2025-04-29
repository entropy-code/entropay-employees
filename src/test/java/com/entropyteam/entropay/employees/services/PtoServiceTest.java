package com.entropyteam.entropay.employees.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import com.entropyteam.entropay.employees.repositories.HolidayRepository;
import com.entropyteam.entropay.employees.models.Pto;
import com.entropyteam.entropay.employees.models.Country;
import com.entropyteam.entropay.employees.models.LeaveType;
import com.entropyteam.entropay.employees.models.Holiday;

@ExtendWith(MockitoExtension.class)
public class PtoServiceTest {

    @Mock
    private HolidayRepository holidayRepository;
    @InjectMocks
    private PtoService ptoService;

    @Test
    void setTimeAmountTestIsAHalfDayPto() {
        // Config
        Pto ptoEntity = new Pto();
        ptoEntity.setStartDate(LocalDate.of(2023, 7,10));
        ptoEntity.setEndDate(LocalDate.of(2023, 7,10));
        ptoEntity.setLabourHours(0);

        Country country = new Country();
        country.setId(UUID.randomUUID());

        // Run
        ptoService.setTimeAmount(ptoEntity, true, country.getId());

        // Assert
        assertEquals(0, ptoEntity.getLabourHours());
        assertEquals(0.5, ptoEntity.getDays());
    }

    @Test
    void setTimeAmountTestIsAOneDayVacation() {
        // Config
        Pto ptoEntity = new Pto();
        ptoEntity.setStartDate(LocalDate.of(2023, 7,10));
        ptoEntity.setEndDate(LocalDate.of(2023, 7,10));
        ptoEntity.setLabourHours(0);
        LeaveType leaveTypeEntity = new LeaveType();
        leaveTypeEntity.setName("vacation");
        ptoEntity.setLeaveType(leaveTypeEntity);

        Country country = new Country();
        country.setId(UUID.randomUUID());

        // Run
        ptoService.setTimeAmount(ptoEntity, false, country.getId());

        // Assert
        assertEquals(0, ptoEntity.getLabourHours());
        assertEquals(1, ptoEntity.getDays());
    }

    @Test
    void setTimeAmountTestOnlyWeekDays() {
        // Config
        Pto ptoEntity = new Pto();
        ptoEntity.setStartDate(LocalDate.of(2023, 7,10));
        ptoEntity.setEndDate(LocalDate.of(2023, 7,14));
        ptoEntity.setLabourHours(null);

        Country country = new Country();
        country.setId(UUID.randomUUID());

        Mockito.when(holidayRepository.findHolidaysByCountryAndPeriod(Mockito.any(UUID.class), Mockito.any(), Mockito.any()))
                .thenReturn(Collections.emptyList());

        // Run
        ptoService.setTimeAmount(ptoEntity, false, country.getId());

        // Assert
        assertEquals(0, ptoEntity.getLabourHours());
        assertEquals(5, ptoEntity.getDays());
    }

    @Test
    void setTimeAmountTestWithWeekendDays() {
        // Config
        Pto ptoEntity = new Pto();
        ptoEntity.setStartDate(LocalDate.of(2023, 7,10));
        ptoEntity.setEndDate(LocalDate.of(2023, 7,23));
        ptoEntity.setLabourHours(null);

        Country country = new Country();
        country.setId(UUID.randomUUID());

        Mockito.when(holidayRepository.findHolidaysByCountryAndPeriod(Mockito.any(UUID.class), Mockito.any(), Mockito.any()))
                .thenReturn(Collections.emptyList());

        // Run
        ptoService.setTimeAmount(ptoEntity, false, country.getId());

        // Assert
        assertEquals(0, ptoEntity.getLabourHours());
        assertEquals(10, ptoEntity.getDays());
    }

    @Test
    void setTimeAmountTestWithHolidaysFromDifferentCountries(){
        // Config
        Pto ptoEntity = new Pto();
        ptoEntity.setStartDate(LocalDate.of(2023, 7,10));
        ptoEntity.setEndDate(LocalDate.of(2023, 7,18));
        ptoEntity.setLabourHours(null);

        Country countryArgentina = new Country();
        countryArgentina.setName("Argentina");
        countryArgentina.setId(UUID.fromString("e7cccdc7-b9b8-4017-b0c9-541abc1d6f9d"));
        Country countryAll = new Country();
        countryAll.setName("ALL");

        Holiday holidayArgentina = new Holiday();
        holidayArgentina.setDate(LocalDate.of(2023, 7, 12));
        holidayArgentina.setCountry(countryArgentina);
        Holiday holidayAll = new Holiday();
        holidayAll.setDate(LocalDate.of(2023, 7, 14));
        holidayAll.setCountry(countryAll);

        Mockito.when(holidayRepository.findHolidaysByCountryAndPeriod(eq(countryArgentina.getId()), Mockito.any(), Mockito.any()))
                .thenReturn(Arrays.asList(holidayArgentina, holidayAll));

        // Run
        ptoService.setTimeAmount(ptoEntity, false, countryArgentina.getId());

        // Assert
        assertEquals(0, ptoEntity.getLabourHours());
        assertEquals(5, ptoEntity.getDays());
    }
}
