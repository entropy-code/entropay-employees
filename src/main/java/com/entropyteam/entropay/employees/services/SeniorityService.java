package com.entropyteam.entropay.employees.services;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.common.BaseService;
import com.entropyteam.entropay.employees.dtos.SeniorityDto;
import com.entropyteam.entropay.employees.models.Seniority;
import com.entropyteam.entropay.employees.repositories.SeniorityRepository;

@Service
public class SeniorityService extends BaseService<Seniority, SeniorityDto, UUID> {

    private final SeniorityRepository seniorityRepository;

    @Autowired
    public SeniorityService(SeniorityRepository seniorityRepository) {
        this.seniorityRepository = seniorityRepository;
    }

    @Override
    protected BaseRepository<Seniority, UUID> getRepository() {
        return seniorityRepository;
    }

    @Override
    protected SeniorityDto toDTO(Seniority entity) {
        return new SeniorityDto(entity);
    }

    @Override
    protected Seniority toEntity(SeniorityDto entity) {
        return new Seniority(entity);
    }
}
