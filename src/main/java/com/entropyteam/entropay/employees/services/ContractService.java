package com.entropyteam.entropay.employees.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.common.BaseService;
import com.entropyteam.entropay.employees.dtos.ContractDto;
import com.entropyteam.entropay.employees.models.Company;
import com.entropyteam.entropay.employees.models.Contract;
import com.entropyteam.entropay.employees.models.Employee;
import com.entropyteam.entropay.employees.models.Role;
import com.entropyteam.entropay.employees.models.Seniority;
import com.entropyteam.entropay.employees.repositories.CompanyRepository;
import com.entropyteam.entropay.employees.repositories.ContractRepository;
import com.entropyteam.entropay.employees.repositories.EmployeeRepository;
import com.entropyteam.entropay.employees.repositories.RoleRepository;
import com.entropyteam.entropay.employees.repositories.SeniorityRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ContractService extends BaseService<Contract, ContractDto, UUID> {

    private final ContractRepository contractRepository;
    private final CompanyRepository companyRepository;
    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final SeniorityRepository seniorityRepository;

    @Autowired
    public ContractService(ContractRepository contractRepository, CompanyRepository companyRepository,
                           EmployeeRepository employeeRepository, RoleRepository roleRepository,
                           SeniorityRepository seniorityRepository) {
        this.contractRepository = contractRepository;
        this.companyRepository = companyRepository;
        this.employeeRepository = employeeRepository;
        this.roleRepository = roleRepository;
        this.seniorityRepository = seniorityRepository;
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
        return super.create(contractDto.withActive(true));
    }

    @Transactional
    public ContractDto modifyStatus(UUID contractId, boolean setActive) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity not found"));

        if ((!setActive && !contract.isActive()) || (setActive && contract.isActive()))
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Contract " + contract.getId() + " already " + (setActive ? "active" : "inactive")
                    + " for employee " + contract.getEmployee().getId());

        if (setActive) {
            contractRepository.findContractByEmployeeIdAndActiveIsTrue(contract.getEmployee().getId()).ifPresent(existent -> {
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
        return new ContractDto(entity);
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
