package com.entropyteam.entropay.employees.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.entropyteam.entropay.employees.repositories.EmployeeRepository;

@ExtendWith(MockitoExtension.class)
class EmployeeInternalIdGeneratorTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeInternalIdGenerator generator;

    @DisplayName("Values below 1000 are zero-padded to three digits with the E prefix")
    @Test
    void formatsBelowThousandWithLeadingZeros() {
        assertEquals("E001", EmployeeInternalIdGenerator.format(1));
        assertEquals("E007", EmployeeInternalIdGenerator.format(7));
        assertEquals("E124", EmployeeInternalIdGenerator.format(124));
        assertEquals("E999", EmployeeInternalIdGenerator.format(999));
    }

    @DisplayName("Values at or above 1000 are printed without padding")
    @Test
    void formatsAtOrAboveThousandWithoutPadding() {
        assertEquals("E1000", EmployeeInternalIdGenerator.format(1000));
        assertEquals("E1001", EmployeeInternalIdGenerator.format(1001));
        assertEquals("E12345", EmployeeInternalIdGenerator.format(12345));
    }

    @DisplayName("next() consumes the sequence and formats the result")
    @Test
    void nextConsumesTheSequence() {
        when(employeeRepository.nextInternalIdSequenceValue()).thenReturn(7L);

        String result = generator.next();

        assertEquals("E007", result);
        verify(employeeRepository).nextInternalIdSequenceValue();
    }

    @DisplayName("peek() reads the next value without consuming the sequence")
    @Test
    void peekDoesNotConsumeTheSequence() {
        when(employeeRepository.peekInternalIdSequenceValue()).thenReturn(124L);

        String result = generator.peek();

        assertEquals("E124", result);
        verify(employeeRepository).peekInternalIdSequenceValue();
        verify(employeeRepository, never()).nextInternalIdSequenceValue();
    }

    @DisplayName("Two consecutive next() calls produce two consecutive Internal IDs")
    @Test
    void successiveNextCallsReturnConsecutiveValues() {
        when(employeeRepository.nextInternalIdSequenceValue()).thenReturn(123L, 124L);

        assertEquals("E123", generator.next());
        assertEquals("E124", generator.next());
    }

    @DisplayName("Crossing 999 produces E1000 then E1001 with no gaps")
    @Test
    void sequenceCrossesFromE999ToE1000() {
        when(employeeRepository.nextInternalIdSequenceValue()).thenReturn(999L, 1000L, 1001L);

        assertEquals("E999", generator.next());
        assertEquals("E1000", generator.next());
        assertEquals("E1001", generator.next());
    }
}
