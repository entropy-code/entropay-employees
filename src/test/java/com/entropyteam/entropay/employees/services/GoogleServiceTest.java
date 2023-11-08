package com.entropyteam.entropay.employees.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date tomorrow = calendar.getTime();

        googleService.createGoogleCalendarEvent("Event Test", tomorrow);
    }

    @Test
    public void testGoogleServiceWithServiceAccount() {
        googleService.initGoogleForServiceAccount();
    }
}