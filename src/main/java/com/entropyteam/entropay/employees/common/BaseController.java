package com.entropyteam.entropay.employees.common;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

public abstract class BaseController<T, K> implements ReactAdminController<T, K> {

    private static final String X_TOTAL_COUNT = "X-Total-Count";
    private final CrudService<T, K> crudService;

    public BaseController(CrudService<T, K> crudService) {
        this.crudService = crudService;
    }

    @Override
    @GetMapping
    public ResponseEntity<List<T>> getList(Filter filter, Sort sort, Range range) {
        List<T> entities = crudService.findAllActive(filter, sort, range);
        return ResponseEntity.ok()
                .header(X_TOTAL_COUNT, String.valueOf(entities.size()))
                .body(entities);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<T> getOne(@PathVariable(value = "id") K id) {
        return crudService.findOne(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity not found"));
    }

    @Override
    @PostMapping
    public ResponseEntity<T> create(@RequestBody T entity) {
        return ResponseEntity.ok(crudService.create(entity));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<T> delete(@PathVariable(value = "id") K id) {
        return ResponseEntity.ok(crudService.delete(id));
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<T> update(@PathVariable(value = "id") K id, @RequestBody T entity) {
        return crudService.findOne(id)
                .map(e -> {
                    crudService.update(id, entity);
                    return ResponseEntity.ok(entity);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity not found"));
    }
}
