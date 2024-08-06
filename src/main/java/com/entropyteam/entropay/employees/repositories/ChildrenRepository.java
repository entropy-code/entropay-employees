package com.entropyteam.entropay.employees.repositories;

import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.employees.models.Children;

import java.util.List;
import java.util.UUID;

public interface ChildrenRepository extends BaseRepository<Children, UUID> {

    List<Children> findAllByEmployeeIdAndDeletedIsFalse(UUID id);

}
