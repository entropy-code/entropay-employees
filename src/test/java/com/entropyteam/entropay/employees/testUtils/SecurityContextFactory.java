package com.entropyteam.entropay.employees.testUtils;

import static com.entropyteam.entropay.auth.AppRole.ROLE_ADMIN;

import java.util.Collections;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityContextFactory {

    public static void createSecurityContext() {
        SecurityContextHolder.setContext(
                SecurityContextHolder.createEmptyContext()
        );
        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken("admin", null,
                        Collections.singletonList(new SimpleGrantedAuthority(ROLE_ADMIN.name())))
        );
    }
}