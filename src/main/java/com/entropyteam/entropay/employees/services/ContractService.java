package com.entropyteam.entropay.employees.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.entropyteam.entropay.employees.models.*;
import com.entropyteam.entropay.employees.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.entropyteam.entropay.auth.SecureObjectService;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.common.BaseService;
import com.entropyteam.entropay.employees.dtos.ContractDto;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ContractService extends BaseService<Contract, ContractDto, UUID> {

    private final ContractRepository contractRepository;
    private final CompanyRepository companyRepository;
    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final SeniorityRepository seniorityRepository;
    private final SecureObjectService secureObjectService;
    private final PaymentSettlementService paymentSettlementService;
    private final PaymentSettlementRepository paymentSettlementRepository;

    @Autowired
    public ContractService(ContractRepository contractRepository, CompanyRepository companyRepository,
            EmployeeRepository employeeRepository, RoleRepository roleRepository,
            SeniorityRepository seniorityRepository, SecureObjectService secureObjectService,
            PaymentSettlementService paymentSettlementService, PaymentSettlementRepository paymentSettlementRepository) {
        this.contractRepository = contractRepository;
        this.companyRepository = companyRepository;
        this.employeeRepository = employeeRepository;
        this.roleRepository = roleRepository;
        this.seniorityRepository = seniorityRepository;
        this.secureObjectService = secureObjectService;
        this.paymentSettlementService = paymentSettlementService;
        this.paymentSettlementRepository = paymentSettlementRepository;
    }

    @Transactional
    @Override
    public ContractDto create(ContractDto contractDto) {
        contractRepository.findContractByEmployeeIdAndActiveIsTrue(contractDto.employeeId())
                .ifPresent(existent -> {
                    existent.setActive(false);
                    existent.setModifiedAt(LocalDateTime.now());
                    existent.setEndDate(LocalDate.now());
                    contractRepository.saveAndFlush(existent);
                });
        Contract entityToCreate = toEntity(contractDto.withActive(true));
        Contract savedEntity = getRepository().save(entityToCreate);
        paymentSettlementService.create(contractDto.paymentSettlement(),savedEntity);
        return toDTO(savedEntity);
    }

    @Override
    @Transactional
    public ContractDto update(UUID contractId, ContractDto contractDto){
        Contract entityToUpdate = toEntity(contractDto);
        entityToUpdate.setId(contractId);
        Contract savedEntity = getRepository().save(entityToUpdate);
        paymentSettlementService.update(contractDto.paymentSettlement(),savedEntity);
        return toDTO(savedEntity);
    }

    @Transactional
    public ContractDto modifyStatus(UUID contractId, boolean setActive) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity not found"));

        if ((!setActive && !contract.isActive()) || (setActive && contract.isActive())) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT,
                    "Contract " + contract.getId() + " already " + (setActive ? "active" : "inactive")
                            + " for employee " + contract.getEmployee().getId());
        }

        if (setActive) {
            contractRepository.findContractByEmployeeIdAndActiveIsTrue(contract.getEmployee().getId())
                    .ifPresent(existent -> {
                        existent.setActive(false);
                        existent.setModifiedAt(LocalDateTime.now());
                        existent.setEndDate(LocalDate.now());
                        contractRepository.saveAndFlush(existent);
                    });
        }
        contract.setActive(setActive);
        contract.setModifiedAt(LocalDateTime.now());
        return toDTO(contractRepository.save(contract));
    }

    @Override
    protected BaseRepository<Contract, UUID> getRepository() {
        return contractRepository;
    }

    @Override
    public ContractDto toDTO(Contract entity) {
        Contract securedEntity = (Contract) secureObjectService.secureObjectByRole(entity, getUserRole());
        List<PaymentSettlement> paymentsSettlementList = paymentSettlementRepository.findAllByContractIdAndDeletedIsFalse(entity.getId());
        return new ContractDto(securedEntity, paymentsSettlementList);
    }

    @Override
    protected Contract toEntity(ContractDto entity) {
        Company company = companyRepository.findById(entity.companyId()).orElseThrow();
        Employee employee = employeeRepository.findById(entity.employeeId()).orElseThrow();
        Role role = roleRepository.findById(entity.roleId()).orElseThrow();
        Seniority seniority = seniorityRepository.findById(entity.seniorityId()).orElseThrow();

        Contract contract = new Contract(entity);
        contract.setCompany(company);
        contract.setEmployee(employee);
        contract.setRole(role);
        contract.setSeniority(seniority);

        return contract;
    }
}
