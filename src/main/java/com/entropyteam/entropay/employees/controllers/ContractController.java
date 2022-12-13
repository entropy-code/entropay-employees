package com.entropyteam.entropay.employees.controllers;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.entropyteam.entropay.common.BaseController;
import com.entropyteam.entropay.employees.dtos.ContractDto;
import com.entropyteam.entropay.employees.services.ContractService;

@RestController
@CrossOrigin
@RequestMapping(value = "/contracts", produces = MediaType.APPLICATION_JSON_VALUE)
public class ContractController extends BaseController<ContractDto, UUID> {
    private final ContractService contractService;
    @Autowired
    public ContractController(ContractService contractService) {
        super(contractService);
        this.contractService = contractService;
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ContractDto> modifyStatus(@PathVariable UUID id, @RequestParam boolean active) {
        return ResponseEntity.ok(contractService.modifyStatus(id, active));
    }
}
