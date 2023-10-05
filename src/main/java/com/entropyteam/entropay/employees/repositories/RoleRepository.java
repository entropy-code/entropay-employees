package com.entropyteam.entropay.employees.repositories;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.employees.models.Role;
import org.springframework.data.jpa.repository.Query;

public interface RoleRepository extends BaseRepository<Role, UUID> {

    List<Role> findAllByDeletedIsFalse();

    Set<Role> findAllByDeletedIsFalseAndIdIn(List<UUID> roleIds);

    List<Role> findAllByDeletedIsFalseAndNameLikeIgnoreCase(String roleName);
}
