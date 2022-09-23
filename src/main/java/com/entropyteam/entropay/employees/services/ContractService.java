package com.entropyteam.entropay.employees.services;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.common.BaseService;
import com.entropyteam.entropay.employees.dtos.ContractDto;
import com.entropyteam.entropay.employees.models.Company;
import com.entropyteam.entropay.employees.models.Contract;
import com.entropyteam.entropay.employees.models.Employee;
import com.entropyteam.entropay.employees.repositories.CompanyRepository;
import com.entropyteam.entropay.employees.repositories.ContractRepository;
import com.entropyteam.entropay.employees.repositories.EmployeeRepository;

@Service
public class ContractService extends BaseService<Contract, ContractDto, UUID> {

    private final ContractRepository contractRepository;
    private final CompanyRepository companyRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public ContractService(ContractRepository contractRepository, CompanyRepository companyRepository,
            EmployeeRepository employeeRepository) {
        this.contractRepository = contractRepository;
        this.companyRepository = companyRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    protected BaseRepository<Contract, UUID> getRepository() {
        return contractRepository;
    }

    @Override
    protected ContractDto toDTO(Contract entity) {
        return new ContractDto(entity);
    }

    @Override
    protected Contract toEntity(ContractDto entity) {
        Company company = companyRepository.findById(entity.companyId()).orElseThrow();
        Employee employee = employeeRepository.findById(entity.employeeId()).orElseThrow();

        Contract contract = new Contract(entity);
        contract.setCompany(company);
        contract.setEmployee(employee);

        return contract;
    }
}
