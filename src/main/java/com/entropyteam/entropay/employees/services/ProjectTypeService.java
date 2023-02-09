package com.entropyteam.entropay.employees.services;

import java.util.Objects;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.common.BaseService;
import com.entropyteam.entropay.common.ReactAdminMapper;
import com.entropyteam.entropay.employees.dtos.ProjectTypeDto;
import com.entropyteam.entropay.employees.models.ProjectType;
import com.entropyteam.entropay.employees.repositories.ProjectTypeRepository;

@Service
public class ProjectTypeService extends BaseService<ProjectType, ProjectTypeDto, UUID> {

    private final ProjectTypeRepository projectTypeRepository;

    @Autowired
    public ProjectTypeService(ProjectTypeRepository projectTypeRepository, ReactAdminMapper reactAdminMapper) {
        super(ProjectType.class, reactAdminMapper);
        this.projectTypeRepository = Objects.requireNonNull(projectTypeRepository);
    }

    @Override
    protected BaseRepository<ProjectType, UUID> getRepository() {
        return projectTypeRepository;
    }

    @Override
    protected ProjectTypeDto toDTO(ProjectType entity) {
        return new ProjectTypeDto(entity);
    }

    @Override
    protected ProjectType toEntity(ProjectTypeDto entity) {
        return new ProjectType(entity);
    }
}
