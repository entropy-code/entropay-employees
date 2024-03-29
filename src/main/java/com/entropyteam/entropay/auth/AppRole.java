package com.entropyteam.entropay.auth;

import java.util.Arrays;
import org.apache.commons.lang3.StringUtils;

public enum AppRole {
    ROLE_ADMIN("ROLE_ADMIN", 1),
    ROLE_DEVELOPMENT("ROLE_DEVELOPMENT", 2),
    ROLE_HR_DIRECTOR("ROLE_HR_DIRECTOR", 3),
    ROLE_MANAGER_HR("ROLE_MANAGER/HR", 4),
    ROLE_ANALYST("ROLE_ANALYST", 5);

    public String value;
    public Integer score;

    AppRole(String value, Integer score) {
        this.value = value;
        this.score = score;
    }
    public static AppRole getByValue(String value) {
        return Arrays.stream(AppRole.values()).filter(r -> StringUtils.equalsIgnoreCase(r.value, value)).findFirst()
                .orElseThrow();
    }
}
