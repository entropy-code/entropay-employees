package com.entropyteam.entropay.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;

public class AuthUtils {
    public static AppRole getUserRole() {
        Collection<SimpleGrantedAuthority> authorities =
                (Collection<SimpleGrantedAuthority>) SecurityContextHolder.getContext().getAuthentication()
                        .getAuthorities();
        Optional<AppRole> appRole = authorities.stream().map(a -> AppRole.getByValue(a.getAuthority()))
                .min(Comparator.comparing(r -> r.score));
        return appRole.orElseThrow();
    }

    public static String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}


