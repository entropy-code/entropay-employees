package com.entropyteam.entropay.employees.repositories;

import java.util.List;
import java.util.UUID;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.employees.models.Client;
import com.entropyteam.entropay.employees.models.Company;

public interface CompanyRepository extends BaseRepository<Company, UUID> {
}
