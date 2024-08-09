package com.entropyteam.entropay.employees.services;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.common.BaseService;
import com.entropyteam.entropay.common.ReactAdminMapper;
import com.entropyteam.entropay.employees.dtos.EndReasonDto;
import com.entropyteam.entropay.employees.models.EndReason;
import com.entropyteam.entropay.employees.repositories.EndReasonRepository;

@Service
public class EndReasonService extends BaseService<EndReason, EndReasonDto, UUID> {

    private final EndReasonRepository endReasonRepository;

    @Autowired
    public EndReasonService(EndReasonRepository endReasonRepository, ReactAdminMapper reactAdminMapper) {
        super(EndReason.class, reactAdminMapper);
        this.endReasonRepository = endReasonRepository;
    }

    @Override
    protected BaseRepository<EndReason, UUID> getRepository() {
        return endReasonRepository;
    }

    @Override
    protected EndReasonDto toDTO(EndReason entity) {
        return new EndReasonDto(entity);
    }

    @Override
    protected EndReason toEntity(EndReasonDto entity) {
        return new EndReason(entity);
    }

}
