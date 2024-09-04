package com.entropyteam.entropay.employees.jobs;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.entropyteam.entropay.employees.calendar.CalendarService;
import com.entropyteam.entropay.employees.repositories.EmployeeRepository;
import com.entropyteam.entropay.employees.services.ReportService;


@Component
public class EmployeeJob {

    private final static Logger LOGGER = LoggerFactory.getLogger(ReportService.class);
    private final EmployeeRepository employeeRepository;
    private final CalendarService calendarService;

    @Autowired
    public EmployeeJob(EmployeeRepository employeeRepository, CalendarService calendarService) {
        this.employeeRepository = employeeRepository;
        this.calendarService = calendarService;
    }

    //Job to execute in January
    @Scheduled(cron = "0 0 9 1 1 ?")
    @Transactional
    public void syncEmployeesBirthdayWithCalendar() throws IOException {
        LOGGER.info("Starting employees birthday sync job");
        employeeRepository.findAllByDeletedIsFalseAndActiveIsTrue()
                .stream()
                .filter(employee -> employee.getBirthDate() != null)
                .forEach(employee -> {
                    calendarService.createBirthdayEvent(employee.getId().toString(), employee.getFirstName(),
                            employee.getLastName(), employee.getBirthDate());
                });
    }
}
