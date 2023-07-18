package com.entropyteam.entropay.employees.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import com.entropyteam.entropay.employees.models.Holiday;
import com.entropyteam.entropay.employees.models.Pto;
import com.entropyteam.entropay.employees.repositories.HolidayRepository;

@ExtendWith(MockitoExtension.class)
public class PtoServiceTest {

    @Mock
    private HolidayRepository holidayRepository;
    @InjectMocks
    private PtoService ptoService;


    @Test
    void setTimeAmountTestWithHoursPto() {
        // Config
        Pto ptoEntity = new Pto();
        ptoEntity.setStartDate(LocalDate.now());
        ptoEntity.setEndDate(LocalDate.now());
        ptoEntity.setLabourHours(4);

        // Run
        ptoService.setTimeAmount(ptoEntity);

        // Assert
        assertEquals(4, ptoEntity.getLabourHours());
        assertEquals(0, ptoEntity.getDays());
    }

    @Test
    void setTimeAmountTestOnlyWeekDays() {
        // Config
        Pto ptoEntity = new Pto();
        ptoEntity.setStartDate(LocalDate.of(2023, 7,10));
        ptoEntity.setEndDate(LocalDate.of(2023, 7,14));
        ptoEntity.setLabourHours(null);

        Mockito.when(holidayRepository.findAllByDateBetweenAndDeletedFalseOrderByDateAsc(Mockito.any(), Mockito.any()))
                .thenReturn(Collections.emptyList());

        // Run
        ptoService.setTimeAmount(ptoEntity);

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

        Mockito.when(holidayRepository.findAllByDateBetweenAndDeletedFalseOrderByDateAsc(Mockito.any(), Mockito.any()))
                .thenReturn(Collections.emptyList());

        // Run
        ptoService.setTimeAmount(ptoEntity);

        // Assert
        assertEquals(0, ptoEntity.getLabourHours());
        assertEquals(10, ptoEntity.getDays());
    }

    @Test
    void setTimeAmountTestWithHolidays() {
        // Config
        Pto ptoEntity = new Pto();
        ptoEntity.setStartDate(LocalDate.of(2023, 7,10));
        ptoEntity.setEndDate(LocalDate.of(2023, 7,14));
        ptoEntity.setLabourHours(null);

        Holiday holiday = new Holiday();
        holiday.setDate(LocalDate.of(2023, 7, 12));

        Mockito.when(holidayRepository.findAllByDateBetweenAndDeletedFalseOrderByDateAsc(Mockito.any(), Mockito.any()))
                .thenReturn(Collections.singletonList(holiday));

        // Run
        ptoService.setTimeAmount(ptoEntity);

        // Assert
        assertEquals(0, ptoEntity.getLabourHours());
        assertEquals(4, ptoEntity.getDays());
    }
}
