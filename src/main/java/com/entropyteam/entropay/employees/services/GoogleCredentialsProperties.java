package com.entropyteam.entropay.employees.services;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("google-credentials")
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

    public String getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(String calendarId) {
        this.calendarId = calendarId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getPrivateKeyId() {
        return privateKeyId;
    }

    public void setPrivateKeyId(String privateKeyId) {
        this.privateKeyId = privateKeyId;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getAuthUri() {
        return authUri;
    }

    public void setAuthUri(String authUri) {
        this.authUri = authUri;
    }

    public String getTokenUri() {
        return tokenUri;
    }

    public void setTokenUri(String tokenUri) {
        this.tokenUri = tokenUri;
    }

    public String getAuthProviderX509CertUrl() {
        return authProviderX509CertUrl;
    }

    public void setAuthProviderX509CertUrl(String authProviderX509CertUrl) {
        this.authProviderX509CertUrl = authProviderX509CertUrl;
    }

    public String getClientX509CertUrl() {
        return clientX509CertUrl;
    }

    public void setClientX509CertUrl(String clientX509CertUrl) {
        this.clientX509CertUrl = clientX509CertUrl;
    }

    public String getUniverseDomain() {
        return universeDomain;
    }

    public void setUniverseDomain(String universeDomain) {
        this.universeDomain = universeDomain;
    }
}



