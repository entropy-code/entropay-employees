package com.entropyteam.entropay.employees.services;

import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.common.BaseService;
import com.entropyteam.entropay.common.ReactAdminMapper;
import com.entropyteam.entropay.employees.dtos.LeaveTypeDto;
import com.entropyteam.entropay.employees.models.LeaveType;
import com.entropyteam.entropay.employees.repositories.LeaveTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
public class LeaveTypeService extends BaseService<LeaveType, LeaveTypeDto,UUID> {

    private final LeaveTypeRepository leaveTypeRepository;

    @Autowired
    public LeaveTypeService(LeaveTypeRepository leaveTypeRepository, ReactAdminMapper reactAdminMapper) {
        super(LeaveType.class, reactAdminMapper);
        this.leaveTypeRepository = Objects.requireNonNull(leaveTypeRepository);
    }

    @Override
    protected BaseRepository<LeaveType, UUID> getRepository() {
        return leaveTypeRepository;
    }

    @Override
    protected LeaveTypeDto toDTO(LeaveType entity) {
        return new LeaveTypeDto(entity);
    }

    @Override
    protected LeaveType toEntity(LeaveTypeDto entity) {
        return new LeaveType(entity);
    }

}
