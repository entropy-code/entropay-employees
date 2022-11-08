package com.entropyteam.entropay.common;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;

public abstract class BaseService<Entity extends BaseEntity, DTO, Key> implements CrudService<DTO, Key> {

    @Override
    @Transactional
    public Optional<DTO> findOne(Key id) {
        return getRepository().findById(id)
                .map(this::toDTO);
    }

    @Override
    @Transactional
    public List<DTO> findAllActive(Filter filter, Sort sort, Range range) {
        return getRepository().findAllByDeletedIsFalse()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DTO delete(Key id) {
        Entity entity = getRepository().findById(id).orElseThrow();
        entity.setDeleted(true);
        return toDTO(entity);
    }

    @Override
    @Transactional
    public DTO create(DTO entity) {
        Entity entityToCreate = toEntity(entity);
        Entity savedEntity = getRepository().save(entityToCreate);
        return toDTO(savedEntity);
    }

    @Override
    @Transactional
    public DTO update(Key id, DTO entity) {
        Entity entityToUpdate = toEntity(entity);
        entityToUpdate.setId((UUID) id);
        Entity savedEntity = getRepository().save(entityToUpdate);
        return toDTO(savedEntity);
    }

    protected abstract BaseRepository<Entity, Key> getRepository();

    protected abstract DTO toDTO(Entity entity);

    protected abstract Entity toEntity(DTO entity);
}
