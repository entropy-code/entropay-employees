package com.entropyteam.entropay.auth;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestClient;

@Service
public class AuthorizationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationService.class);
    private final RestClient restClient;

    public AuthorizationService(RestClient.Builder restClientBuilder, @Value("${user-auth-api-url}") String baseUrl) {
        this.restClient = restClientBuilder.baseUrl(baseUrl).build();
    }

    public record Identity(UUID id, String username, String firstName, String lastName, String email,
                           Map<UUID, Set<String>> rolesByTenant) {

    }

    /**
     * Get the identity of the user with the given JWT.
     *
     * @param jwt The JWT to use for authentication.
     * @return The identity of the user.
     */
    public Identity identity(String jwt) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Identity identity = this.restClient.get()
                .uri("/auth/identity")
                .header("authorization", "Bearer " + jwt)
                .retrieve()
                .body(Identity.class);

        stopWatch.stop();
        LOGGER.debug("Finish getting identity for user: {}, took: {}ms", identity.email(),
                stopWatch.getTotalTimeMillis());

        return identity;
    }

}
