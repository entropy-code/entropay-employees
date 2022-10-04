package com.entropyteam.entropay.employees.repositories;

import java.util.List;
import java.util.UUID;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.employees.models.Contract;

public interface ContractRepository extends BaseRepository<Contract, UUID> {

    List<Contract> findAllByDeletedIsFalse();
}
