package com.entropyteam.entropay.employees.controllers;

import static com.entropyteam.entropay.auth.AuthConstants.ROLE_ADMIN;
import static com.entropyteam.entropay.auth.AuthConstants.ROLE_DEVELOPMENT;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.entropyteam.entropay.common.BaseController;
import com.entropyteam.entropay.employees.dtos.ContractDto;
import com.entropyteam.entropay.employees.jobs.ContractJob;
import com.entropyteam.entropay.employees.models.ContractType;
import com.entropyteam.entropay.employees.models.Currency;
import com.entropyteam.entropay.employees.services.ContractService;

@RestController
@CrossOrigin
@RequestMapping(value = "/contracts", produces = MediaType.APPLICATION_JSON_VALUE)
public class ContractController extends BaseController<ContractDto, UUID> {

    private final ContractService contractService;
    private final ContractJob contractJob;

    @Autowired
    public ContractController(ContractService contractService, ContractJob contractJob) {
        super(contractService);
        this.contractService = contractService;
        this.contractJob = contractJob;
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

    @GetMapping("/update-contracts-status")
    @Secured({ROLE_ADMIN, ROLE_DEVELOPMENT})
    public ResponseEntity<String> updateContractsStatus() {
        contractJob.updateContractsStatus();
        return ResponseEntity.ok("Successful job execution: update contracts status");
    }
}
