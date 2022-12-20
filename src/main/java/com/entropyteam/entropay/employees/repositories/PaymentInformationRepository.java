package com.entropyteam.entropay.employees.repositories;

import java.util.List;
import java.util.UUID;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.employees.models.PaymentInformation;

public interface PaymentInformationRepository extends BaseRepository<PaymentInformation, UUID>{
   
    List<PaymentInformation> findAllByEmployeeIdAndDeletedIsFalse(UUID id);
}
