package com.entropyteam.entropay.employees.repositories;

import java.util.List;
import java.util.UUID;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.employees.models.Role;

public interface RoleRepository extends BaseRepository<Role, UUID> {

    List<Role> findAllByDeletedIsFalse();

    List<Role> findAllByDeletedIsFalseAndIdIn(List<UUID> roleIds);
}
