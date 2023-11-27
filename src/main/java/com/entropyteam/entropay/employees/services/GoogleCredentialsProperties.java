package com.entropyteam.entropay.employees.services;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("google-credentials")
@Data
public class GoogleCredentialsProperties {
    private String calendarId;
    private String type;
    private String projectId;
    private String privateKeyId;
    private String privateKey;
    private String clientEmail;
    private String clientId;
    private String authUri;
    private String tokenUri;
    private String authProviderX509CertUrl;
    private String clientX509CertUrl;
    private String universeDomain;

    public byte[] getCredentials() {
        String propertiesString = "{" +
                "\"type\": \"" + type + "\",\n" +
                "\"project_id\": \"" + projectId + "\",\n" +
                "\"private_key_id\": \"" + privateKeyId + "\",\n" +
                "\"private_key\": \"" + privateKey + "\",\n" +
                "\"client_email\": \"" + clientEmail + "\",\n" +
                "\"client_id\": \"" + clientId + "\",\n" +
                "\"auth_uri\": \"" + authUri + "\",\n" +
                "\"token_uri\": \"" + tokenUri + "\",\n" +
                "\"auth_provider_x509_cert_url\": \"" + authProviderX509CertUrl + "\",\n" +
                "\"client_x509_cert_url\": \"" + clientX509CertUrl + "\",\n" +
                "\"universe_domain\": \"" + universeDomain + "\"}";
        return propertiesString.getBytes();
    }
}



