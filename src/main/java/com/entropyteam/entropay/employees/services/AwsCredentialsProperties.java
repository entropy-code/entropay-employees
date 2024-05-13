package com.entropyteam.entropay.employees.services;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("aws-credentials")
public class AwsCredentialsProperties {
    private String accessKeyId;
    private String secretAccessKey;
    private String bucketName;

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getSecretAccessKey() {
        return secretAccessKey;
    }

    public void setSecretAccessKey(String secretAccessKey) {
        this.secretAccessKey = secretAccessKey;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }
}