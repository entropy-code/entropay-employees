package com.entropyteam.entropay.mcp;

import static com.entropyteam.entropay.auth.AuthConstants.ROLE_ADMIN;
import static com.entropyteam.entropay.auth.AuthConstants.ROLE_ANALYST;
import static com.entropyteam.entropay.auth.AuthConstants.ROLE_DEVELOPMENT;
import static com.entropyteam.entropay.auth.AuthConstants.ROLE_HR_DIRECTOR;
import static com.entropyteam.entropay.auth.AuthConstants.ROLE_MANAGER_HR;

import java.util.Comparator;
import java.util.List;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.entropyteam.entropay.common.ReactAdminParams;
import com.entropyteam.entropay.employees.dtos.EmployeeDto;
import com.entropyteam.entropay.employees.services.EmployeeService;
import com.entropyteam.entropay.mcp.dtos.RosterEntry;

@Service
public class RosterQueryService {

    private final EmployeeService employeeService;

    public RosterQueryService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Secured({ROLE_ADMIN, ROLE_HR_DIRECTOR, ROLE_MANAGER_HR, ROLE_ANALYST, ROLE_DEVELOPMENT})
    @Transactional(readOnly = true)
    public List<RosterEntry> listRoster() {
        return employeeService.findAllActive(new ReactAdminParams()).getContent().stream()
                .filter(EmployeeDto::isActive)
                .sorted(Comparator.comparing(EmployeeDto::getLastName,
                        Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)))
                .map(this::toRosterEntry)
                .toList();
    }

    private RosterEntry toRosterEntry(EmployeeDto employee) {
        return new RosterEntry(employee.getId(), employee.getInternalId(), employee.getFirstName(),
                employee.getLastName(), employee.getCountryName(), employee.getProject(), employee.getClient(),
                employee.getRole(), employee.getTimeSinceStart(), employee.isActive());
    }
}
