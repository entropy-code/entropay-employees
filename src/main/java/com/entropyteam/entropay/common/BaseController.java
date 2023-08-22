package com.entropyteam.entropay.common;

import java.util.List;
import java.util.Objects;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

import static com.entropyteam.entropay.auth.AuthConstants.*;

public abstract class BaseController<T, K> implements ReactAdminController<T, K> {

    public static final String X_TOTAL_COUNT = "X-Total-Count";
    private final CrudService<T, K> crudService;

    public BaseController(CrudService<T, K> crudService) {
        this.crudService = Objects.requireNonNull(crudService);
    }

    @Override
    @GetMapping
    @Secured({ROLE_ADMIN, ROLE_MANAGER_HR, ROLE_ANALYST, ROLE_DEVELOPMENT, ROLE_DIRECTOR_HR})
    public ResponseEntity<List<T>> getList(ReactAdminParams params) {
        Page<T> response = crudService.findAllActive(params);
        return ResponseEntity.ok()
                .header(X_TOTAL_COUNT, String.valueOf(response.getTotalElements()))
                .body(response.getContent());
    }

    @Override
    @GetMapping("/{id}")
    @Secured({ROLE_ADMIN, ROLE_MANAGER_HR, ROLE_ANALYST, ROLE_DEVELOPMENT, ROLE_DIRECTOR_HR})
    public ResponseEntity<T> getOne(@PathVariable(value = "id") K id) {
        return crudService.findOne(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity not found"));
    }

    @Override
    @PostMapping
    @Secured({ROLE_ADMIN, ROLE_MANAGER_HR, ROLE_DEVELOPMENT, ROLE_DIRECTOR_HR})
    public ResponseEntity<T> create(@Valid @RequestBody T entity) {
        return ResponseEntity.ok(crudService.create(entity));
    }

    @Override
    @DeleteMapping("/{id}")
    @Secured({ROLE_ADMIN, ROLE_MANAGER_HR, ROLE_DEVELOPMENT, ROLE_DIRECTOR_HR})
    public ResponseEntity<T> delete(@PathVariable(value = "id") K id) {
        return ResponseEntity.ok(crudService.delete(id));
    }

    @Override
    @PutMapping("/{id}")
    @Secured({ROLE_ADMIN, ROLE_MANAGER_HR, ROLE_DEVELOPMENT, ROLE_DIRECTOR_HR})
    public ResponseEntity<T> update(@PathVariable(value = "id") K id, @Valid @RequestBody T entity) {
        return crudService.findOne(id)
                .map(e -> {
                    crudService.update(id, entity);
                    return ResponseEntity.ok(entity);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity not found"));
    }
}
