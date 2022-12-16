package com.entropyteam.entropay.employees.repositories;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.employees.models.PaymentInformation;
import com.entropyteam.entropay.employees.models.Role;

public interface PaymentInformationRepository extends BaseRepository<PaymentInformation, UUID>{
    // EmployeeId
    List<PaymentInformation> findAllByEmployeeIdAndDeletedIsFalse(UUID id);
}
