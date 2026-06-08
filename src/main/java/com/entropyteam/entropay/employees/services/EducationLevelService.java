package com.entropyteam.entropay.employees.services;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.common.BaseService;
import com.entropyteam.entropay.common.ReactAdminMapper;
import com.entropyteam.entropay.employees.dtos.EducationLevelDto;
import com.entropyteam.entropay.employees.models.EducationLevel;
import com.entropyteam.entropay.employees.repositories.EducationLevelRepository;

@Service
public class EducationLevelService extends BaseService<EducationLevel, EducationLevelDto, UUID> {

    private final EducationLevelRepository educationLevelRepository;

    @Autowired
    public EducationLevelService(EducationLevelRepository educationLevelRepository,
            ReactAdminMapper reactAdminMapper) {
        super(EducationLevel.class, reactAdminMapper);
        this.educationLevelRepository = educationLevelRepository;
    }

    @Override
    protected BaseRepository<EducationLevel, UUID> getRepository() {
        return educationLevelRepository;
    }

    @Override
    protected EducationLevelDto toDTO(EducationLevel entity) {
        return new EducationLevelDto(entity);
    }

    @Override
    protected EducationLevel toEntity(EducationLevelDto dto) {
        return new EducationLevel(dto);
    }
}
