package com.entropyteam.entropay.employees.repositories;

import java.util.List;
import java.util.UUID;
import com.entropyteam.entropay.auth.AppRole;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.employees.models.Config;

public interface ConfigRepository extends BaseRepository<Config, UUID> {

    List<Config> findAllByDeletedIsFalseAndRole(AppRole userRole);
}
