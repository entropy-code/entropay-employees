package com.entropyteam.entropay.employees.services;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.common.BaseService;
import com.entropyteam.entropay.common.ReactAdminMapper;
import com.entropyteam.entropay.employees.dtos.ReimbursementDto;
import com.entropyteam.entropay.employees.models.Employee;
import com.entropyteam.entropay.employees.models.Reimbursement;
import com.entropyteam.entropay.employees.models.ReimbursementCategory;
import com.entropyteam.entropay.employees.repositories.EmployeeRepository;
import com.entropyteam.entropay.employees.repositories.ReimbursementCategoryRepository;
import com.entropyteam.entropay.employees.repositories.ReimbursementRepository;

@Service
public class ReimbursementService extends BaseService<Reimbursement, ReimbursementDto, UUID> {

    private final ReimbursementRepository repository;
    private final EmployeeRepository employeeRepository;
    private final ReimbursementCategoryRepository categoryRepository;

    @Autowired
    public ReimbursementService(ReimbursementRepository repository, 
                               EmployeeRepository employeeRepository,
                               ReimbursementCategoryRepository categoryRepository,
                               ReactAdminMapper reactAdminMapper) {
        super(Reimbursement.class, reactAdminMapper);
        this.repository = repository;
        this.employeeRepository = employeeRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    protected BaseRepository<Reimbursement, UUID> getRepository() {
        return repository;
    }

    @Override
    protected ReimbursementDto toDTO(Reimbursement entity) {
        return new ReimbursementDto(entity);
    }

    @Override
    protected Reimbursement toEntity(ReimbursementDto dto) {
        Employee employee = employeeRepository.findById(dto.employeeId()).orElseThrow();
        ReimbursementCategory category = categoryRepository.findById(dto.categoryId()).orElseThrow();
        
        Reimbursement reimbursement = new Reimbursement(dto);
        reimbursement.setEmployee(employee);
        reimbursement.setCategory(category);
        
        return reimbursement;
    }

    @Override
    protected List<String> getColumnsForSearch() {
        return List.of("comment");
    }

    @Override
    protected List<String> getDateColumnsForSearch() {
        return List.of("date");
    }

    @Override
    public Map<String, List<String>> getRelatedColumnsForSearch() {
        Map<String, List<String>> relatedColumns = new HashMap<>();
        relatedColumns.put("employee", Arrays.asList("firstName", "lastName"));
        relatedColumns.put("category", Arrays.asList("name"));
        return relatedColumns;
    }
}