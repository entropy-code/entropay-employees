package com.entropyteam.entropay.employees.calendar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.entropyteam.entropay.employees.models.Holiday;
import com.entropyteam.entropay.employees.repositories.HolidayRepository;

@ExtendWith(MockitoExtension.class)
class GoogleCalendarServiceTest {

    private static final String ARGENTINA = "Argentina";
    @Mock
    private HolidayRepository holidayRepository;
    @InjectMocks
    private GoogleCalendarService googleCalendarService;

    @Test
    void testGetBirthdayThisYear() throws Exception {
        // Use reflection to access the private static method
        Method getBirthdayThisYear =
                GoogleCalendarService.class.getDeclaredMethod("getBirthdayThisYear", LocalDate.class);
        getBirthdayThisYear.setAccessible(true);

        // Test with a date from any day of the week
        LocalDate birthDate = LocalDate.of(1990, Month.JANUARY, 15);

        LocalDate result = (LocalDate) getBirthdayThisYear.invoke(null, birthDate);

        // The result should always be the same day in the current year (no adjustment)
        LocalDate expected = LocalDate.of(LocalDate.now().getYear(), birthDate.getMonth(), birthDate.getDayOfMonth());
        assertEquals(expected, result);
    }

    @Test
    void testIsWorkingDay_Weekday() throws Exception {
        // Use reflection to access the private method
        Method isWorkingDay = GoogleCalendarService.class.getDeclaredMethod("isWorkingDay", LocalDate.class);
        isWorkingDay.setAccessible(true);

        // Find a date in the current year that falls on a Wednesday (day 3 of the week)
        LocalDate wednesday = findDateWithDayOfWeek(3); // Wednesday is day 3 (Monday=1, Sunday=7)

        // No holidays configured for this date
        Boolean result = (Boolean) isWorkingDay.invoke(googleCalendarService, wednesday);

        assertTrue(result, "A weekday that is not a holiday should be a working day");
    }

    @Test
    void testIsWorkingDay_Weekend() throws Exception {
        // Use reflection to access the private method
        Method isWorkingDay = GoogleCalendarService.class.getDeclaredMethod("isWorkingDay", LocalDate.class);
        isWorkingDay.setAccessible(true);

        // Find a date in the current year that falls on a Saturday (day 6 of the week)
        LocalDate saturday = findDateWithDayOfWeek(6); // Saturday is day 6 (Monday=1, Sunday=7)
        Boolean saturdayResult = (Boolean) isWorkingDay.invoke(googleCalendarService, saturday);
        assertFalse(saturdayResult, "Saturday should not be a working day");

        // Find a date in the current year that falls on a Sunday (day 7 of the week)
        LocalDate sunday = findDateWithDayOfWeek(7); // Sunday is day 7 (Monday=1, Sunday=7)
        Boolean sundayResult = (Boolean) isWorkingDay.invoke(googleCalendarService, sunday);
        assertFalse(sundayResult, "Sunday should not be a working day");
    }

    @Test
    void testIsWorkingDay_Holiday() throws Exception {
        // Use reflection to access the private method
        Method isWorkingDay = GoogleCalendarService.class.getDeclaredMethod("isWorkingDay", LocalDate.class);
        isWorkingDay.setAccessible(true);

        // Find a date in the current year that falls on a Wednesday (day 3 of the week)
        LocalDate wednesday = findDateWithDayOfWeek(3); // Wednesday is day 3 (Monday=1, Sunday=7)
        Holiday holiday = new Holiday();
        holiday.setDate(wednesday);

        // Configure this date as a holiday in Argentina
        when(holidayRepository.findHolidaysByCountryAndPeriod(ARGENTINA, wednesday, wednesday))
                .thenReturn(List.of(holiday));

        Boolean result = (Boolean) isWorkingDay.invoke(googleCalendarService, wednesday);

        assertFalse(result, "A weekday that is a holiday should not be a working day");
    }

    @Test
    void testGetNextWorkingDay_Weekend() throws Exception {
        // Use reflection to access the private method
        Method getNextWorkingDay = GoogleCalendarService.class.getDeclaredMethod("getNextWorkingDay", LocalDate.class);
        getNextWorkingDay.setAccessible(true);

        // Find a date in the current year that falls on a Saturday (day 6 of the week)
        LocalDate saturday = findDateWithDayOfWeek(6); // Saturday is day 6 (Monday=1, Sunday=7)

        // No holidays configured for Monday
        LocalDate result = (LocalDate) getNextWorkingDay.invoke(googleCalendarService, saturday);

        // The result should be the following Monday (2 days after Saturday)
        LocalDate expected = saturday.plusDays(2);
        assertEquals(expected, result, "Next working day after Saturday should be Monday");

        // Find a date in the current year that falls on a Sunday (day 7 of the week)
        LocalDate sunday = findDateWithDayOfWeek(7); // Sunday is day 7 (Monday=1, Sunday=7)

        // No holidays configured for Monday
        result = (LocalDate) getNextWorkingDay.invoke(googleCalendarService, sunday);

        // The result should be the following Monday (1 day after Sunday)
        expected = sunday.plusDays(1);
        assertEquals(expected, result, "Next working day after Sunday should be Monday");
    }

    @Test
    void testGetNextWorkingDay_Holiday() throws Exception {
        // Use reflection to access the private method
        Method getNextWorkingDay = GoogleCalendarService.class.getDeclaredMethod("getNextWorkingDay", LocalDate.class);
        getNextWorkingDay.setAccessible(true);

        // Find a date in the current year that falls on a Monday (day 1 of the week)
        LocalDate monday = findDateWithDayOfWeek(1); // Monday is day 1
        LocalDate tuesday = monday.plusDays(1);
        Holiday holiday = new Holiday();
        holiday.setDate(monday);

        // Configure Monday as a holiday in Argentina
        when(holidayRepository.findHolidaysByCountryAndPeriod(ARGENTINA, monday, monday)).thenReturn(List.of(holiday));

        // Configure an empty set for Tuesday (not a holiday)
        when(holidayRepository.findHolidaysByCountryAndPeriod(ARGENTINA, tuesday, tuesday)).thenReturn(List.of());

        LocalDate result = (LocalDate) getNextWorkingDay.invoke(googleCalendarService, monday);

        // The result should be Tuesday (1 day after Monday)
        assertEquals(tuesday, result, "Next working day after a holiday Monday should be Tuesday");
    }

    @Test
    void testGetNextWorkingDay_WorkingDay() throws Exception {
        // Use reflection to access the private method
        Method getNextWorkingDay = GoogleCalendarService.class.getDeclaredMethod("getNextWorkingDay", LocalDate.class);
        getNextWorkingDay.setAccessible(true);

        // Find a date in the current year that falls on a Wednesday (day 3 of the week)
        LocalDate wednesday = findDateWithDayOfWeek(3); // Wednesday is day 3 (Monday=1, Sunday=7)

        // No holidays configured for Wednesday
        LocalDate result = (LocalDate) getNextWorkingDay.invoke(googleCalendarService, wednesday);

        // The result should be the same day (Wednesday)
        assertEquals(wednesday, result, "Next working day for a working day should be the same day");
    }

    /**
     * Helper method to find a date in the current year with the specified day of week.
     *
     * @param dayOfWeek The day of week (1=Monday, 7=Sunday)
     * @return A date in the current year with the specified day of week
     */
    private LocalDate findDateWithDayOfWeek(int dayOfWeek) {
        LocalDate date = LocalDate.of(LocalDate.now().getYear(), Month.JANUARY, 1);
        while (date.getDayOfWeek().getValue() != dayOfWeek) {
            date = date.plusDays(1);
        }
        return date;
    }
}
