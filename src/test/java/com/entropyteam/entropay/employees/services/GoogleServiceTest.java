package com.entropyteam.entropay.employees.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

@SpringBootTest
public class GoogleServiceTest {
    @Autowired
    private GoogleService googleService;

    @Test
    public void testGoogleService() {
        googleService.initGoogle();
    }

    @Test
    public void testCreateGoogleCalendarEvent() {
        LocalDate today = LocalDate.now();

        LocalDate tomorrow = today.plusDays(5);

        googleService.createGoogleCalendarEvent("Event Test", today, tomorrow);
    }

    @Test
    public void testGoogleServiceWithServiceAccount() {
        googleService.initGoogleForServiceAccount();
    }
}