package com.entropyteam.entropay.employees.services;


import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.common.BaseService;
import com.entropyteam.entropay.common.ReactAdminMapper;
import com.entropyteam.entropay.employees.dtos.CompanyDto;
import com.entropyteam.entropay.employees.models.Company;
import com.entropyteam.entropay.employees.models.Tenant;
import com.entropyteam.entropay.employees.repositories.CompanyRepository;
import com.entropyteam.entropay.employees.repositories.TenantRepository;

@Service
public class CompanyService extends BaseService<Company, CompanyDto, UUID> {

    private final CompanyRepository companyRepository;
    private final TenantRepository tenantRepository;

    @Autowired
    public CompanyService(CompanyRepository companyRepository, TenantRepository tenantRepository,
            ReactAdminMapper reactAdminMapper) {
        super(Company.class, reactAdminMapper);
        this.companyRepository = companyRepository;
        this.tenantRepository = tenantRepository;
    }

    @Override
    protected BaseRepository<Company, UUID> getRepository() {
        return companyRepository;
    }

    @Override
    protected CompanyDto toDTO(Company entity) {
        return new CompanyDto(entity);
    }

    @Override
    protected Company toEntity(CompanyDto entity) {
        Tenant tenant = tenantRepository.findById(entity.tenantId()).orElseThrow();
        Company company = new Company(entity);
        company.setTenant(tenant);
        return company;
    }
}
