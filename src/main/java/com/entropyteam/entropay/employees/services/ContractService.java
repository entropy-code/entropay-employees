package com.entropyteam.entropay.employees.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.entropyteam.entropay.auth.AppRole;
import com.entropyteam.entropay.employees.models.Company;
import com.entropyteam.entropay.employees.models.Contract;
import com.entropyteam.entropay.employees.models.Employee;
import com.entropyteam.entropay.employees.models.PaymentSettlement;
import com.entropyteam.entropay.employees.models.Role;
import com.entropyteam.entropay.employees.models.Seniority;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.entropyteam.entropay.auth.SecureObjectService;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.common.BaseService;
import com.entropyteam.entropay.common.ReactAdminMapper;
import com.entropyteam.entropay.employees.dtos.ContractDto;
import com.entropyteam.entropay.employees.repositories.CompanyRepository;
import com.entropyteam.entropay.employees.repositories.ContractRepository;
import com.entropyteam.entropay.employees.repositories.EmployeeRepository;
import com.entropyteam.entropay.employees.repositories.PaymentSettlementRepository;
import com.entropyteam.entropay.employees.repositories.RoleRepository;
import com.entropyteam.entropay.employees.repositories.SeniorityRepository;

@Service
public class ContractService extends BaseService<Contract, ContractDto, UUID> {

    public static final String CONTRACT_ROLE_HHRR = "hhrr";
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
                           PaymentSettlementService paymentSettlementService, PaymentSettlementRepository paymentSettlementRepository,
                           ReactAdminMapper reactAdminMapper) {
        super(Contract.class, reactAdminMapper);
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
        Contract entityToCreate = toEntity(contractDto);
        Contract savedEntity = getRepository().save(checkActiveContract(entityToCreate));
        paymentSettlementService.createPaymentsSettlement(savedEntity.getPaymentsSettlement(), savedEntity);
        return toDTO(savedEntity);
    }

    @Override
    @Transactional
    public ContractDto update(UUID contractId, ContractDto contractDto) {
        Contract entityToUpdate = toEntity(contractDto);
        entityToUpdate.setId(contractId);
        Contract savedEntity = getRepository().save(checkActiveContract(entityToUpdate));
        paymentSettlementService.updatePaymentsSettlement(contractDto.paymentSettlement(), savedEntity);
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
            contractRepository.findContractByEmployeeIdAndActiveIsTrueAndDeletedIsFalse(contract.getEmployee().getId())
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
        List<PaymentSettlement> paymentsSettlementList =
                paymentSettlementRepository.findAllByContractIdAndDeletedIsFalse(entity.getId());
        List<PaymentSettlement> securedPaymentSettlementList = paymentsSettlementList.stream()
                .map(p -> (PaymentSettlement) secureObjectService.secureObjectByRole(p, getUserRole()))
                .collect(Collectors.toList());
        return new ContractDto(securedEntity, securedPaymentSettlementList);
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
        contract.setPaymentsSettlement(entity.paymentSettlement() == null ?  Collections.emptySet() : entity.paymentSettlement().stream().map(PaymentSettlement::new).collect(Collectors.toSet()));
        return contract;
    }

    public Contract checkActiveContract(Contract contractToCheck) {
        Optional<Contract> activeContract = contractRepository.findContractByEmployeeIdAndActiveIsTrueAndDeletedIsFalse(contractToCheck.getEmployee().getId());
        if ((contractToCheck.getEndDate() == null || contractToCheck.getEndDate().isAfter(LocalDate.now())) && (contractToCheck.getStartDate().isBefore(LocalDate.now())) || contractToCheck.getStartDate().isEqual(LocalDate.now())) {
            contractToCheck.setActive(true);
            activeContract.ifPresent(contract -> {
                contract.setActive(false);
                if (contract.getEndDate() == null) {
                    contract.setEndDate(LocalDate.now());
                }
                contractRepository.saveAndFlush(contract);
            });
        } else {
            contractToCheck.setActive(false);
        }
        return contractToCheck;
    }
    @Override
    public Map<String, Object> getRestrictedFields(AppRole userRole){
        Map<String,Object> restrictedFields = new HashMap<>();
                if(AppRole.ROLE_MANAGER_HR.equals(userRole)){
                    
                    Optional<Role> role = roleRepository.findByDeletedIsFalseAndName(CONTRACT_ROLE_HHRR);
                    if(role.isPresent()){
                        restrictedFields.put("role", role.get());
                    }
                }
                return  restrictedFields;
    }

}
