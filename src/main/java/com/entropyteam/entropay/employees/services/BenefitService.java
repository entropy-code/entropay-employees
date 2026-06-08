package com.entropyteam.entropay.employees.services;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.common.BaseService;
import com.entropyteam.entropay.common.ReactAdminMapper;
import com.entropyteam.entropay.employees.dtos.BenefitDto;
import com.entropyteam.entropay.employees.models.Benefit;
import com.entropyteam.entropay.employees.repositories.BenefitRepository;

@Service
public class BenefitService extends BaseService<Benefit, BenefitDto, UUID> {

    private final BenefitRepository benefitRepository;

    @Autowired
    public BenefitService(BenefitRepository benefitRepository, ReactAdminMapper reactAdminMapper) {
        super(Benefit.class, reactAdminMapper);
        this.benefitRepository = benefitRepository;
    }

    @Override
    protected BaseRepository<Benefit, UUID> getRepository() {
        return benefitRepository;
    }

    @Override
    protected BenefitDto toDTO(Benefit entity) {
        return new BenefitDto(entity);
    }

    @Override
    protected Benefit toEntity(BenefitDto entity) {
        return new Benefit(entity);
    }
}
