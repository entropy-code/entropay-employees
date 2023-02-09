package com.entropyteam.entropay.employees.services;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.common.BaseService;
import com.entropyteam.entropay.common.ReactAdminMapper;
import com.entropyteam.entropay.employees.dtos.TenantDto;
import com.entropyteam.entropay.employees.models.Tenant;
import com.entropyteam.entropay.employees.repositories.TenantRepository;

@Service
public class TenantService extends BaseService<Tenant, TenantDto, UUID> {

    private final TenantRepository tenantRepository;

    @Autowired
    public TenantService(TenantRepository tenantRepository, ReactAdminMapper reactAdminMapper) {
        super(Tenant.class, reactAdminMapper);
        this.tenantRepository = tenantRepository;
    }

    @Override
    protected BaseRepository<Tenant, UUID> getRepository() {
        return tenantRepository;
    }

    @Override
    protected TenantDto toDTO(Tenant entity) {
        return new TenantDto(entity);
    }

    @Override
    protected Tenant toEntity(TenantDto entity) {
        return new Tenant(entity);
    }
}
