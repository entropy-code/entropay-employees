package com.entropyteam.entropay.common.sensitiveInformation;

import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import com.entropyteam.entropay.auth.AuthConstants;
import com.entropyteam.entropay.employees.services.EmployeeService;

/**
 * Service for handling logic related to masking sensitive information
 * based on user privileges and employee type.
 */
@Component
public class SensitiveInformationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SensitiveInformationService.class);

    private final EmployeeService employeeService;

    public SensitiveInformationService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * Determines if sensitive information for the given employee should be masked
     * based on the current user's privileges and the employee's type.
     *
     * @param employeeId the UUID of the employee
     * @return true if the information should be masked, false otherwise
     */
    public boolean shouldMask(UUID employeeId) {
        return lacksAdminPrivileges() && isInternalEmployee(employeeId);

    }

    private static boolean lacksAdminPrivileges() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication == null || authentication.getAuthorities().stream()
                .noneMatch(a -> a.getAuthority().equals(AuthConstants.ROLE_ADMIN));
    }

    private boolean isInternalEmployee(UUID employeeId) {
        if (employeeId == null) {
            return false;
        }
        return employeeService.getInternalEmployeeIds().contains(employeeId);
    }

}
