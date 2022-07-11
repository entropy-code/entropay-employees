package com.entropyteam.entropay.employees.common;

import java.util.List;
import java.util.Optional;

public interface CrudService<T, K> {

    Optional<T> findOne(K id);

    List<T> findAllActive(Filter filter, Sort sort, Range range);

    T delete(K id);

    T create(T entity);

    T update(K id, T entity);
}
