package com.entropyteam.entropay.employees.repositories;

import java.util.List;
import java.util.UUID;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.employees.models.EndReason;

public interface EndReasonRepository extends BaseRepository<EndReason, UUID> {

    List<EndReason> findAllByDeletedIsFalse();

}
