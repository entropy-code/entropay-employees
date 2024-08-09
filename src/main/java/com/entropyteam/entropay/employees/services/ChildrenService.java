package com.entropyteam.entropay.employees.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.common.BaseService;
import com.entropyteam.entropay.common.ReactAdminMapper;
import com.entropyteam.entropay.employees.dtos.ChildrenDto;
import com.entropyteam.entropay.employees.models.Children;
import com.entropyteam.entropay.employees.models.Employee;
import com.entropyteam.entropay.employees.repositories.ChildrenRepository;
import com.entropyteam.entropay.employees.repositories.EmployeeRepository;

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

    protected List<Children> findAllByEmployeeIdAndDeletedIsFalse(UUID employeeId) {
        return childrenRepository.findAllByEmployeeIdAndDeletedIsFalse(employeeId);
    }

    @Override
    protected ChildrenDto toDTO(Children entity) {
        return new ChildrenDto(entity);
    }

    @Override
    protected Children toEntity(ChildrenDto entity) {
        Employee employee = employeeRepository.findById(entity.employeeId()).orElseThrow();
        Children children = new Children(entity);
        children.setEmployee(employee);
        return children;
    }

    public Set<Children> createChildren(Set<Children> children, Employee savedEntity) {
        children.forEach(c -> c.setEmployee(savedEntity));
        return new HashSet<>(childrenRepository.saveAll(children));
    }

    public void updateChildren(List<ChildrenDto> childrenDtos, Employee employee) {
        List<Children> childrenList = childrenRepository.findAllByEmployeeIdAndDeletedIsFalse(employee.getId());
        List<Children> childrenRequest = childrenDtos.stream().map(this::toEntity).toList();
        List<Children> childrenToDelete = new ArrayList<>();

        for (Children children : childrenList) {
            if (!childrenRequest.contains(children)) {
                children.setDeleted(true);
                childrenToDelete.add(children);
            }
        }

        childrenRequest.forEach(c -> c.setEmployee(employee));
        childrenRepository.saveAll(childrenRequest);
        childrenRepository.saveAll(childrenToDelete);
    }
}
