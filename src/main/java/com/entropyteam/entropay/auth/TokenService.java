package com.entropyteam.entropay.auth;

import java.util.Collection;
import java.util.Map;
import org.springframework.stereotype.Service;
import com.entropyteam.entropay.common.exceptions.AuthException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class TokenService {

    private static final String COGNITO_CLAIMS_ROLE_KEY = "custom:role";
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public Map<String, Collection<String>> getRoles(Map<String, Object> tokenClaims) {
        try {
            return MAPPER.readValue((String) tokenClaims.get(COGNITO_CLAIMS_ROLE_KEY), Map.class);
        } catch (JsonProcessingException e) {
            throw new AuthException("Error reading roles from token");
        }
    }
}
