package com.entropyteam.entropay.employees.services;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.common.BaseService;
import com.entropyteam.entropay.common.ReactAdminMapper;
import com.entropyteam.entropay.employees.dtos.RoleDto;
import com.entropyteam.entropay.employees.models.Project;
import com.entropyteam.entropay.employees.models.Role;
import com.entropyteam.entropay.employees.repositories.RoleRepository;

@Service
public class RoleService extends BaseService<Role, RoleDto, UUID> {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository, ReactAdminMapper reactAdminMapper) {
        super(Role.class, reactAdminMapper);
        this.roleRepository = roleRepository;
    }

    @Override
    protected BaseRepository<Role, UUID> getRepository() {
        return roleRepository;
    }

    @Override
    protected RoleDto toDTO(Role entity) {
        return new RoleDto(entity);
    }

    @Override
    protected Role toEntity(RoleDto entity) {
        return new Role(entity);
    }
}
