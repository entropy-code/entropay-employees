package com.entropyteam.entropay.common;

import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;

public interface ReactAdminController<DTO, Key> {

    // ResponseEntity<List<DTO>> getList(Filter filter, Sort sort, Range range);

    ResponseEntity<List<DTO>> getList(ReactAdminParams params);

    ResponseEntity<DTO> getOne(Key id);

    ResponseEntity<DTO> create(DTO entity);

    ResponseEntity<DTO> delete(Key id);

    ResponseEntity<DTO> update(Key id, DTO entity);
}
