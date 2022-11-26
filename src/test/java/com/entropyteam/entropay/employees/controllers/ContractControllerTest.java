package com.entropyteam.entropay.employees.controllers;

import com.entropyteam.entropay.employees.dtos.ContractDto;
import com.entropyteam.entropay.employees.services.ContractService;
import com.entropyteam.entropay.employees.testUtils.testUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContractControllerTest {
    @Mock
    ContractService contractService;
    @InjectMocks
    ContractController underTest;

    @DisplayName("Test create happy path")
    @Test
    public void testCreate() {
        // given
        ContractDto contractDto = new ContractDto(testUtils.aContract());
        ResponseEntity<ContractDto> expected = ResponseEntity.ok(contractDto);

        // when
        when(contractService.create(any())).thenReturn(contractDto);

        // then
        ResponseEntity<ContractDto> actual = underTest.create(contractDto);

        assertEquals(expected, actual);
        verify(contractService, times(1)).create(eq(contractDto));
        verifyNoMoreInteractions(contractService);
    }

    @DisplayName("Test create when ContractService throws exception")
    @Test
    public void testCreateWhenContractServiceThrowsException() {
        // given
        ContractDto contractDto = new ContractDto(testUtils.aContract());

        // when
        when(contractService.create(any())).thenThrow(new RuntimeException("Test exception thrown!!"));

        // then
        assertThrows(RuntimeException.class, () ->
                        underTest.create(contractDto),
                "RuntimeException was expected");
        verifyNoMoreInteractions(contractService);
    }

    @DisplayName("Test modifyStatus happy path")
    @Test
    public void testModifyStatus() {
        // given
        ContractDto contractDto = new ContractDto(testUtils.aContract());
        boolean setActive = true;
        ResponseEntity<ContractDto> expected = ResponseEntity.ok(contractDto);

        // when
        when(contractService.modifyStatus(any(), anyBoolean())).thenReturn(contractDto);

        // then
        ResponseEntity<ContractDto> actual = underTest.modifyStatus(contractDto.id(), setActive);

        assertEquals(expected, actual);
        verify(contractService, times(1)).modifyStatus(eq(contractDto.id()), eq(setActive));
        verifyNoMoreInteractions(contractService);
    }

    @DisplayName("Test modifyStatus when ContractService throws exception")
    @Test
    public void testModifyStatusWhenContractServiceThrowsException() {
        // given
        ContractDto contractDto = new ContractDto(testUtils.aContract());
        boolean setActive = true;

        // when
        when(contractService.modifyStatus(any(), anyBoolean())).thenThrow(new RuntimeException("Test exception thrown!!"));

        // then
        assertThrows(RuntimeException.class, () ->
                        underTest.modifyStatus(contractDto.id(), setActive),
                "RuntimeException was expected");
        verifyNoMoreInteractions(contractService);

    }
}