package com.entropyteam.entropay.security.repositories;

import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.security.models.EmailLeakHistory;

import java.util.UUID;

public interface EmailLeakHistoryRepository extends BaseRepository<EmailLeakHistory, UUID> {

}
