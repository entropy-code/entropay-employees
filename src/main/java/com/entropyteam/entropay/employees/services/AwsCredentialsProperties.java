package com.entropyteam.entropay.employees.services;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("aws-credentials")
@Data
public class AwsCredentialsProperties {
    private String accessKeyId;
    private String secretAccessKey;
    private String bucketName;
}