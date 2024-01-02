package com.entropyteam.entropay.employees.services;

import java.util.*;

import com.entropyteam.entropay.auth.AppRole;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.common.BaseService;
import com.entropyteam.entropay.employees.dtos.CountryDto;
import com.entropyteam.entropay.employees.models.Country;
import com.entropyteam.entropay.employees.repositories.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.entropyteam.entropay.common.ReactAdminMapper;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

@Service
public class CountryService extends BaseService<Country, CountryDto, UUID> {

    public static final String ALL_COUNTRY_NAME = "ALL";
    private final CountryRepository countryRepository;

    @Autowired
    public CountryService(CountryRepository countryRepository, ReactAdminMapper reactAdminMapper) {
        super(Country.class, reactAdminMapper);
        this.countryRepository = countryRepository;
    }

    @Override
    protected BaseRepository<Country, UUID> getRepository() {
        return countryRepository;
    }

    @Override
    protected CountryDto toDTO(Country entity) {
        return new CountryDto(entity);
    }

    @Override
    protected Country toEntity(CountryDto entity) {
        return new Country(entity);
    }

    @Override
    public Map<String, Object> getRestrictedFields(AppRole userRole) {
        Map<String, Object> restrictedFields = new HashMap<>();
        List<String> countries = countryRepository.findAllByDeletedIsFalseAndNameLikeIgnoreCase(ALL_COUNTRY_NAME).stream().map(Country::getName).toList();
        restrictedFields.put("name", countries);
        return restrictedFields;
    }
}