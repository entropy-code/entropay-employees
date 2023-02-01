package com.entropyteam.entropay.employees.repositories;

import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.employees.models.PaymentSettlement;

import java.util.List;
import java.util.UUID;

public interface PaymentSettlementRepository extends BaseRepository<PaymentSettlement, UUID> {

    List<PaymentSettlement> findAllByContractIdAndDeletedIsFalse(UUID id);

}
