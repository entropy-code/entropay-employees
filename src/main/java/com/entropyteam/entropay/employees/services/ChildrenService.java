package com.entropyteam.entropay.employees.services;

import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.common.BaseService;
import com.entropyteam.entropay.common.ReactAdminMapper;
import com.entropyteam.entropay.employees.dtos.ChildrenDto;
import com.entropyteam.entropay.employees.models.Children;
import com.entropyteam.entropay.employees.models.Employee;
import com.entropyteam.entropay.employees.repositories.ChildrenRepository;
import com.entropyteam.entropay.employees.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ChildrenService extends BaseService<Children, ChildrenDto, UUID> {

    private final ChildrenRepository childrenRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public ChildrenService(ChildrenRepository childrenRepository,
                           EmployeeRepository employeeRepository,
                           ReactAdminMapper reactAdminMapper) {
        super(Children.class, reactAdminMapper);
        this.childrenRepository = childrenRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    protected BaseRepository<Children, UUID> getRepository() {
        return childrenRepository;
    }

    @Override
    protected ChildrenDto toDTO(Children entity) { return new ChildrenDto(entity); }

    @Override
    protected Children toEntity(ChildrenDto entity) {
        Employee employee = employeeRepository.findById(entity.employeeId()).orElseThrow();
        Children children = new Children(entity);
        children.setEmployee(employee);
        return children;
    }
}
