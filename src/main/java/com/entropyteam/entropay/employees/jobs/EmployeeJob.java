package com.entropyteam.entropay.employees.jobs;

import com.entropyteam.entropay.employees.dtos.CalendarEventDto;

import com.entropyteam.entropay.employees.services.EmployeeService;
import com.entropyteam.entropay.employees.repositories.EmployeeRepository;
import com.entropyteam.entropay.employees.services.GoogleService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;


@Component
public class EmployeeJob {
    private static final Logger LOGGER = LogManager.getLogger();

    private final EmployeeRepository employeeRepository;
    private final EmployeeService employeeService;
    private final GoogleService googleService;

    @Autowired
    public EmployeeJob(EmployeeRepository employeeRepository, EmployeeService employeeService, GoogleService googleService) {
        this.employeeRepository = employeeRepository;
        this.employeeService = employeeService;
        this.googleService = googleService;
    }

    //Job to execute in January
    @Scheduled(cron = "0 0 9 1 1 ?")
    @Transactional
    public void syncEmployeesBirthdayWithCalendar() throws IOException {
        LOGGER.info("Starting employees birthday sync job");
        employeeRepository.findAllByDeletedIsFalseAndActiveIsTrue()
                .forEach(employee -> {
                    CalendarEventDto eventData = employeeService.formatEventData(employee.getId(), employee.getBirthDate(), employee.getFirstName(), employee.getLastName());
                    googleService.createGoogleCalendarEvent(eventData);
                });
    }

}
