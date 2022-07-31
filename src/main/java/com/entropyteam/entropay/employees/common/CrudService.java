package com.entropyteam.entropay.employees.common;

import java.util.List;
import java.util.Optional;

public interface CrudService<DTO, Key> {

    Optional<DTO> findOne(Key id);

    List<DTO> findAllActive(Filter filter, Sort sort, Range range);

    DTO delete(Key id);

    DTO create(DTO entity);

    DTO update(Key id, DTO entity);
}
