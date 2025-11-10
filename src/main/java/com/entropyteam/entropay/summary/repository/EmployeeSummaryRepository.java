package com.entropyteam.entropay.summary.repository;

import java.util.List;
import java.util.UUID;

import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.employees.models.Employee;
import com.entropyteam.entropay.summary.model.EmployeeSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface EmployeeSummaryRepository extends JpaRepository<EmployeeSummary, UUID> {}

