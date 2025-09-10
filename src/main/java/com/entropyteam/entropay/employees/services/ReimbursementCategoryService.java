package com.entropyteam.entropay.employees.services;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.common.BaseService;
import com.entropyteam.entropay.common.ReactAdminMapper;
import com.entropyteam.entropay.employees.dtos.ReimbursementCategoryDto;
import com.entropyteam.entropay.employees.models.ReimbursementCategory;
import com.entropyteam.entropay.employees.repositories.ReimbursementCategoryRepository;

@Service
public class ReimbursementCategoryService extends BaseService<ReimbursementCategory, ReimbursementCategoryDto, UUID> {

    private final ReimbursementCategoryRepository repository;

    @Autowired
    public ReimbursementCategoryService(ReimbursementCategoryRepository repository, ReactAdminMapper reactAdminMapper) {
        super(ReimbursementCategory.class, reactAdminMapper);
        this.repository = repository;
    }

    @Override
    protected BaseRepository<ReimbursementCategory, UUID> getRepository() {
        return repository;
    }

    @Override
    protected ReimbursementCategoryDto toDTO(ReimbursementCategory entity) {
        return new ReimbursementCategoryDto(entity);
    }

    @Override
    protected ReimbursementCategory toEntity(ReimbursementCategoryDto dto) {
        return new ReimbursementCategory(dto);
    }

    @Override
    protected List<String> getColumnsForSearch() {
        return List.of("name", "description");
    }
}