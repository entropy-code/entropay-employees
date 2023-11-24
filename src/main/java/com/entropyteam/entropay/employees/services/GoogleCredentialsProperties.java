package com.entropyteam.entropay.employees.services;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("google-credentials")
@Data
public class GoogleCredentialsProperties {
    private String idClient;
    private String secretClient;
    private String idCalendar;
    private String credentials;
}

