package com.entropyteam.entropay.employees.services;

import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.common.BaseService;
import com.entropyteam.entropay.common.ReactAdminMapper;
import com.entropyteam.entropay.employees.dtos.HolidayDto;
import com.entropyteam.entropay.employees.models.Country;
import com.entropyteam.entropay.employees.models.Holiday;
import com.entropyteam.entropay.employees.repositories.CountryRepository;
import com.entropyteam.entropay.employees.repositories.HolidayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class HolidayService extends BaseService<Holiday, HolidayDto,UUID> {

    private final HolidayRepository holidayRepository;
    private final CountryRepository countryRepository;

    @Autowired
    public HolidayService(HolidayRepository holidayRepository, CountryRepository countryRepository, ReactAdminMapper reactAdminMapper) {
        super(Holiday.class, reactAdminMapper);
        this.countryRepository = countryRepository;
        this.holidayRepository = holidayRepository;
    }

    @Override
    protected BaseRepository<Holiday, UUID> getRepository() {
        return holidayRepository;
    }

    @Override
    protected HolidayDto toDTO(Holiday entity) {
        return new HolidayDto(entity);
    }

    @Override
    protected Holiday toEntity(HolidayDto entity) {
        Country country = countryRepository.findById(entity.countryId()).orElseThrow();

        Holiday holiday = new Holiday(entity);
        holiday.setCountry(country);
        return holiday;
    }

    @Override
    public List<String> getColumnsForSearch() {
        return List.of("date");
    }

}