package com.entropyteam.entropay.common;

import java.util.Optional;
import org.springframework.data.domain.Page;

public interface CrudService<DTO, Key> {

    Optional<DTO> findOne(Key id);

    Page<DTO> findAllActive(ReactAdminParams params);

    DTO delete(Key id);

    DTO create(DTO entity);

    DTO update(Key id, DTO entity);

    Page<DTO> getList(ReactAdminParams params);
}
