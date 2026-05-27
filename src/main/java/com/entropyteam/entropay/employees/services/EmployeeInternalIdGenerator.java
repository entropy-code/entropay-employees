package com.entropyteam.entropay.employees.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.entropyteam.entropay.employees.repositories.EmployeeRepository;

@Component
public class EmployeeInternalIdGenerator {

    private static final String PREFIX = "E";
    private static final long PADDED_THRESHOLD = 1000L;

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeInternalIdGenerator(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public String next() {
        return format(employeeRepository.nextInternalIdSequenceValue());
    }

    public String peek() {
        return format(employeeRepository.peekInternalIdSequenceValue());
    }

    static String format(long value) {
        if (value < PADDED_THRESHOLD) {
            return String.format("%s%03d", PREFIX, value);
        }
        return PREFIX + value;
    }
}
