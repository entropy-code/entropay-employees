package com.entropyteam.entropay.summary.dtos;

import java.util.List;
import java.util.UUID;

public record SummaryRequest(UUID employeeId, List<String> feedbacks) {}