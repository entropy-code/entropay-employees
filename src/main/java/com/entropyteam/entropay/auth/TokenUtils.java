package com.entropyteam.entropay.auth;

import java.util.Collection;
import java.util.Map;
import com.entropyteam.entropay.common.exceptions.AuthException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class TokenUtils {

    public static final String COGNITO_CLAIMS_ROLE_KEY = "custom:role";

    public static Map<String, Collection<String>> getRoles(Map<String, Object> tokenClaims) {
        try {
            return new ObjectMapper().readValue((String) tokenClaims.get(COGNITO_CLAIMS_ROLE_KEY), Map.class);
        } catch (JsonProcessingException e) {
            throw new AuthException("Error reading roles from token");
        }
    }
}
