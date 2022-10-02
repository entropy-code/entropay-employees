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
import com.entropyteam.entropay.employees.models.Position;
import com.entropyteam.entropay.employees.models.Seniority;
import com.entropyteam.entropay.employees.repositories.CompanyRepository;
import com.entropyteam.entropay.employees.repositories.ContractRepository;
import com.entropyteam.entropay.employees.repositories.EmployeeRepository;
import com.entropyteam.entropay.employees.repositories.PositionRepository;
import com.entropyteam.entropay.employees.repositories.SeniorityRepository;

@Service
public class ContractService extends BaseService<Contract, ContractDto, UUID> {

    private final ContractRepository contractRepository;
    private final CompanyRepository companyRepository;
    private final EmployeeRepository employeeRepository;
    private final PositionRepository positionRepository;
    private final SeniorityRepository seniorityRepository;

    @Autowired
    public ContractService(ContractRepository contractRepository, CompanyRepository companyRepository,
            EmployeeRepository employeeRepository, PositionRepository positionRepository,
            SeniorityRepository seniorityRepository) {
        this.contractRepository = contractRepository;
        this.companyRepository = companyRepository;
        this.employeeRepository = employeeRepository;
        this.positionRepository = positionRepository;
        this.seniorityRepository = seniorityRepository;

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
        Position position = positionRepository.findById(entity.positionId()).orElseThrow();
        Seniority seniority = seniorityRepository.findById(entity.seniorityId()).orElseThrow();

        Contract contract = new Contract(entity);
        contract.setCompany(company);
        contract.setEmployee(employee);
        contract.setPosition(position);
        contract.setSeniority(seniority);

        return contract;
    }
}
