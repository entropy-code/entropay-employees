package com.entropyteam.entropay.employees.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.common.BaseService;
import com.entropyteam.entropay.common.ReactAdminMapper;
import com.entropyteam.entropay.employees.dtos.ChildrenDto;
import com.entropyteam.entropay.employees.models.Children;
import com.entropyteam.entropay.employees.models.Employee;
import com.entropyteam.entropay.employees.repositories.ChildrenRepository;

@Service
public class ChildrenService extends BaseService<Children, ChildrenDto, UUID> {

    private final ChildrenRepository childrenRepository;

    @Autowired
    public ChildrenService(ChildrenRepository childrenRepository, ReactAdminMapper reactAdminMapper) {
        super(Children.class, reactAdminMapper);
        this.childrenRepository = childrenRepository;
    }

    @Override
    protected BaseRepository<Children, UUID> getRepository() {
        return childrenRepository;
    }

    protected List<Children> findAllByParentIdAndDeletedIsFalse(UUID parentId) {
        return childrenRepository.findAll().stream()
                .filter(c -> c.getParents().stream().anyMatch(p -> p.getId().equals(parentId)) && !c.isDeleted())
                .collect(Collectors.toList());
    }

    @Override
    protected ChildrenDto toDTO(Children entity) {
        return new ChildrenDto(entity);
    }

    @Override
    protected Children toEntity(ChildrenDto entity) {
        return new Children(entity);
    }

    public Set<Children> createChildren(Set<Children> children, Employee savedEntity) {
        children.forEach(c -> c.setParent(savedEntity));
        return new HashSet<>(childrenRepository.saveAll(children));
    }

    public void updateChildren(List<ChildrenDto> childrenDtos, Employee employee) {
        List<Children> childrenList = findAllByParentIdAndDeletedIsFalse(employee.getId());
        List<Children> childrenRequest = childrenDtos.stream().map(this::toEntity).toList();
        List<Children> childrenToDelete = new ArrayList<>();

        for (Children children : childrenList) {
            if (!childrenRequest.contains(children)) {
                children.setDeleted(true);
                childrenToDelete.add(children);
            }
        }

        childrenRequest.forEach(c -> c.setParent(employee));
        childrenRepository.saveAll(childrenRequest);
        childrenRepository.saveAll(childrenToDelete);
    }
}
