package com.entropyteam.entropay.employees.controllers;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.entropyteam.entropay.common.BaseController;
import com.entropyteam.entropay.employees.dtos.ContractDto;
import com.entropyteam.entropay.employees.models.ContractType;
import com.entropyteam.entropay.employees.models.Currency;
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

    @GetMapping("/contract-types")
    public ResponseEntity<ContractType[]> getContractTypes() {
        return ResponseEntity.ok().header(BaseController.X_TOTAL_COUNT, String.valueOf(ContractType.values().length))
                .body(ContractType.values());

    }

    @GetMapping("/currencies")
    public ResponseEntity<Currency[]> getCurrencies() {
        return ResponseEntity.ok().header(BaseController.X_TOTAL_COUNT, String.valueOf(Currency.values().length))
                .body(Currency.values());

    }

}
