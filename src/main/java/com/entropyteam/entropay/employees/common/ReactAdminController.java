package com.entropyteam.entropay.employees.common;

import java.util.List;
import org.springframework.http.ResponseEntity;

public interface ReactAdminController<T, K> {

    ResponseEntity<List<T>> getList(Filter filter, Sort sort, Range range);

    ResponseEntity<T> getOne(K id);

    ResponseEntity<T> create(T entity);

    ResponseEntity<T> delete(K id);

    ResponseEntity<T> update(K id, T entity);
}
