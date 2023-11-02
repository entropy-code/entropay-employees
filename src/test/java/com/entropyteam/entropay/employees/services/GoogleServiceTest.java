package com.entropyteam.entropay.employees.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GoogleServiceTest {
    @Autowired
    private GoogleService googleService;

    @Test
    public void testGoogleService() {
        googleService.initGoogle();
    }
}