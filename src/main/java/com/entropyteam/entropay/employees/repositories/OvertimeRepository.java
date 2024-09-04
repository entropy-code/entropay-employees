package com.entropyteam.entropay.employees.repositories;

import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.employees.models.Assignment;
import com.entropyteam.entropay.employees.models.Overtime;
import com.entropyteam.entropay.employees.models.PaymentInformation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OvertimeRepository extends BaseRepository<Overtime, UUID> {

}
