package com.entropyteam.entropay.employees.jobs;

import com.entropyteam.entropay.employees.dtos.CalendarEventDto;
import com.entropyteam.entropay.employees.repositories.HolidayRepository;
import com.entropyteam.entropay.employees.services.GoogleService;
import com.entropyteam.entropay.employees.services.HolidayService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;


@Component
public class HolidayJob {
    private static final Logger LOGGER = LogManager.getLogger();
    private final HolidayRepository holidayRepository;
    private final HolidayService holidayService;
    private final GoogleService googleService;


    @Autowired
    public HolidayJob(HolidayRepository holidayRepository, HolidayService holidayService, GoogleService googleService) {
        this.holidayRepository = holidayRepository;
        this.holidayService = holidayService;
        this.googleService = googleService;
    }

    @Transactional
    public void syncEmployeesBirthdayWithCalendar() throws IOException {
        LOGGER.info("Starting holiday sync job");
        holidayRepository.findAllByDeletedFalseAndYear(LocalDate.now().getYear())
                .forEach(holiday -> {
                    CalendarEventDto eventData = holidayService.formatEventData(holiday.getId(), holiday.getDate(), holiday.getDescription(), holiday.getCountry().getName());
                    googleService.createGoogleCalendarEvent(eventData);
                    LOGGER.info("Adding holiday " + holiday.getId());
                });
    }
}
