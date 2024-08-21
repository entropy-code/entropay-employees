package com.entropyteam.entropay.employees.calendar;

import java.time.LocalDate;

public interface CalendarService {

    void createBirthdayEvent(String employeeId, String firstName, String lastName, LocalDate birthDate);

    void updateBirthdayEvent(String employeeId, String firstName, String lastName, LocalDate birthDate);

    void deleteBirthdayEvent(String employeeId);

    void createHolidayEvent(String holidayId, LocalDate holidayDate, String holidayName, String holidayCountry);

    void updateHolidayEvent(String holidayId, LocalDate holidayDate, String holidayName, String holidayCountry);

    void deleteHolidayEvent(String holidayId, LocalDate holidayDate);

    void createLeaveEvent(String leaveId, String leaveType, String firstName, String lastName, LocalDate startDate,
            LocalDate endDate);

    void updateLeaveEvent(String leaveId, String leaveType, String firstName, String lastName, LocalDate startDate,
            LocalDate endDate);

    void deleteLeaveEvent(String leaveId);

}
