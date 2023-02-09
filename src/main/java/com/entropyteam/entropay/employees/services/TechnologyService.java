package com.entropyteam.entropay.employees.services;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.common.BaseService;
import com.entropyteam.entropay.common.ReactAdminMapper;
import com.entropyteam.entropay.employees.dtos.TechnologyDto;
import com.entropyteam.entropay.employees.models.Project;
import com.entropyteam.entropay.employees.models.Technology;
import com.entropyteam.entropay.employees.repositories.TechnologyRepository;

@Service
public class TechnologyService extends BaseService<Technology, TechnologyDto, UUID> {

    private final TechnologyRepository technologyRepository;

    @Autowired
    public TechnologyService(TechnologyRepository technologyRepository, ReactAdminMapper reactAdminMapper) {
        super(Technology.class, reactAdminMapper);
        this.technologyRepository = technologyRepository;
    }

    @Override
    protected BaseRepository<Technology, UUID> getRepository() {
        return technologyRepository;
    }

    @Override
    protected TechnologyDto toDTO(Technology entity) {
        return new TechnologyDto(entity);
    }

    @Override
    protected Technology toEntity(TechnologyDto entity) {
        return new Technology(entity);
    }
}