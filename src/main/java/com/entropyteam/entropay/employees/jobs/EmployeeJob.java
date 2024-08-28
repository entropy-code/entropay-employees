package com.entropyteam.entropay.employees.jobs;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.entropyteam.entropay.employees.calendar.CalendarService;
import com.entropyteam.entropay.employees.models.Holiday;
import com.entropyteam.entropay.employees.repositories.EmployeeRepository;
import com.entropyteam.entropay.employees.repositories.HolidayRepository;
import com.entropyteam.entropay.employees.services.ReportService;


@Component
public class EmployeeJob {

    private final static Logger LOGGER = LoggerFactory.getLogger(ReportService.class);
    private final EmployeeRepository employeeRepository;
    private final HolidayRepository holidayRepository;
    private final CalendarService calendarService;

    @Autowired
    public EmployeeJob(EmployeeRepository employeeRepository, HolidayRepository holidayRepository,
            CalendarService calendarService) {
        this.employeeRepository = employeeRepository;
        this.holidayRepository = holidayRepository;
        this.calendarService = calendarService;
    }

    //Job to execute in January
    @Scheduled(cron = "40 51 12 27 8 ?")
    @Transactional
    public void syncEmployeesBirthdayWithCalendar() throws IOException {
        LOGGER.info("Starting employees birthday sync job");
        employeeRepository.findAllByDeletedIsFalseAndActiveIsTrue()
                .stream()
                .filter(employee -> employee.getBirthDate() != null)
                .forEach(employee -> {
                    calendarService.createBirthdayEvent(employee.getId().toString(), employee.getFirstName(),
                            employee.getLastName(), employee.getBirthDate());
                    LOGGER.info("Adding birthday for employee {}", employee.getId());
                    // if (isArgentinianNonWorkingDay(employee.getBirthDate())) {
                    //     calendarService.createBirthdayEvent(UUID.randomUUID().toString(), "(Duplicated)" + employee.getFirstName(),
                    //             employee.getLastName(), getNextWorkingDay(employee.getBirthDate()));
                    //     LOGGER.info(
                    //             "Adding duplicate birthday for employee {} {}, because his birthday fell on a holiday or"
                    //                     + " weekend",
                    //             employee.getFirstName(), employee.getLastName());
                    // }
                });
    }

    private boolean isArgentinianNonWorkingDay(LocalDate birthDate) {
        List<Holiday> argentinianHoliday = holidayRepository.findHolidaysByCountryAndPeriod(
                UUID.fromString("bfa33035-5f9d-40c1-a669-2232f9baf726"),
                LocalDate.of(LocalDate.now().getYear(), 1, 1), LocalDate.of(LocalDate.now().getYear(), 12, 31));
        return argentinianHoliday.stream().anyMatch(ah -> ah.getDate().isEqual(birthDate))
                || birthDate.getDayOfWeek().equals(DayOfWeek.SATURDAY)
                || birthDate.getDayOfWeek().equals(DayOfWeek.SUNDAY);
    }

    private LocalDate getNextWorkingDay(LocalDate birthDate) {
        LocalDate nextDay = birthDate.plusDays(1);
        while (isArgentinianNonWorkingDay(nextDay)) {
            nextDay = nextDay.plusDays(1);
        }
        return nextDay;
    }

    // @Scheduled(cron = "30 43 12 27 8 ?")
    // @Transactional
    // public void deleteAllEmployeesBirthdayEvents() {
    //     LOGGER.info("Starting deletion of all employees' birthday events");
    //     employeeRepository.findAllByDeletedIsFalseAndActiveIsTrue()
    //             .forEach(employee -> {
    //                 calendarService.deleteBirthdayEvent(employee.getId().toString());
    //                 LOGGER.info("Deleted birthday events for employee {}", employee.getId());
    //             });
    //     LOGGER.info("Completed deletion of all employees' birthday events");
    // }
}
