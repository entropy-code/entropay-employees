package com.entropyteam.entropay.common.sensitiveInformation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import com.entropyteam.entropay.auth.AuthConstants;
import com.entropyteam.entropay.employees.services.EmployeeService;

@ExtendWith(MockitoExtension.class)
class SensitiveInformationServiceTest {

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private SensitiveInformationService sensitiveInformationService;

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    private void authenticateWithRoles(String... roles) {
        Authentication auth = new UsernamePasswordAuthenticationToken(
                "user", "pwd",
                Arrays.stream(roles).map(SimpleGrantedAuthority::new).collect(Collectors.toSet())
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    @DisplayName("shouldMask returns true when user is non-admin and employee is internal")
    void shouldMaskInternalEmployeeForNonAdmin() {
        // given
        UUID internalId = UUID.randomUUID();
        when(employeeService.getInternalEmployeeIds()).thenReturn(Set.of(internalId));
        authenticateWithRoles(AuthConstants.ROLE_ANALYST); // non-admin role

        // when
        boolean result = sensitiveInformationService.shouldMask(internalId);

        // then
        assertTrue(result, "Expected masking for internal employee when user lacks admin role");
    }

    @Test
    @DisplayName("shouldMask returns true when no authentication (anonymous) and employee is internal")
    void shouldMaskInternalEmployeeForAnonymous() {
        // given
        UUID internalId = UUID.randomUUID();
        when(employeeService.getInternalEmployeeIds()).thenReturn(Set.of(internalId));
        // Do not set authentication (anonymous)

        // when
        boolean result = sensitiveInformationService.shouldMask(internalId);

        // then
        assertTrue(result, "Expected masking for internal employee when anonymous (no auth context)");
    }

    @Test
    @DisplayName("shouldMask returns false when user is admin and employee is internal")
    void shouldNotMaskInternalEmployeeForAdmin() {
        // given
        UUID internalId = UUID.randomUUID();
        authenticateWithRoles(AuthConstants.ROLE_ADMIN); // admin role present

        // when
        boolean result = sensitiveInformationService.shouldMask(internalId);

        // then
        assertFalse(result, "Admin should see internal employee sensitive info unmasked");
    }

    @Test
    @DisplayName("shouldMask returns false when employee is external even if non-admin")
    void shouldNotMaskExternalEmployee() {
        // given
        UUID internalId = UUID.randomUUID();
        UUID externalId = UUID.randomUUID();
        when(employeeService.getInternalEmployeeIds()).thenReturn(Set.of(internalId));
        authenticateWithRoles(AuthConstants.ROLE_ANALYST); // non-admin

        // when
        boolean result = sensitiveInformationService.shouldMask(externalId);

        // then
        assertFalse(result, "Did not expect masking for external employee id");
    }

    @Test
    @DisplayName("shouldMask returns false when employeeId is null (edge case)")
    void shouldNotMaskWhenEmployeeIdNull() {
        // given (no auth set -> treated as lacking admin, but null employee is never internal)
        // when
        boolean result = sensitiveInformationService.shouldMask(null);
        // then
        assertFalse(result, "Null employee id should not trigger masking");
    }
}
