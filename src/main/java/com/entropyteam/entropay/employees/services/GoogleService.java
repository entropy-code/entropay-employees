package com.entropyteam.entropay.employees.services;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

@EnableConfigurationProperties(GoogleCredentialsProperties.class)
@Service
public class GoogleService {
}
