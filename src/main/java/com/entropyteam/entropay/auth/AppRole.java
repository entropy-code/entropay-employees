package com.entropyteam.entropay.auth;

public enum AppRole {
    ROLE_ADMIN("ROLE_ADMIN", 1),
    ROLE_DEVELOPMENT("ROLE_DEVELOPMENT", 2),
    ROLE_MANAGER_HR("ROLE_MANAGER/HR", 3),
    ROLE_ANALYST("ROLE_ANALYST", 4);


    public String value;
    public Integer score;

    AppRole(String value, Integer score) {
        this.value = value;
        this.score = score;
    }
}
