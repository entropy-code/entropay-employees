package com.entropyteam.entropay.employees.services;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


import org.springframework.stereotype.Service;

@EnableConfigurationProperties(AwsCredentialsProperties.class)
@Service
public class AmazonService {
    private final AwsCredentialsProperties awsCredentialsProperties;
    private S3Client s3client;

    @Autowired
    public AmazonService(AwsCredentialsProperties awsCredentialsProperties) {
        this.awsCredentialsProperties = awsCredentialsProperties;
        initAws();
    }

    private void initAws() {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(
                awsCredentialsProperties.getAccessKeyId(), awsCredentialsProperties.getSecretAccessKey()
        );

        s3client = S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .region(Region.US_EAST_1)
                .build();
    }

    public void uploadFile(String bucketName, String keyName, InputStream inputStream) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        IOUtils.copyLarge(inputStream, baos);
        byte[] contentBytes = baos.toByteArray();

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(keyName)
                .build();

        s3client.putObject(request, RequestBody.fromBytes(contentBytes));
    }
}
