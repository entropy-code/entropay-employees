package com.entropyteam.entropay.employees.services;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.common.BaseService;
import com.entropyteam.entropay.common.ReactAdminMapper;
import com.entropyteam.entropay.employees.calendar.CalendarEventDto;
import com.entropyteam.entropay.employees.calendar.CalendarService;
import com.entropyteam.entropay.employees.dtos.HolidayDto;
import com.entropyteam.entropay.employees.models.Country;
import com.entropyteam.entropay.employees.models.Holiday;
import com.entropyteam.entropay.employees.repositories.CountryRepository;
import com.entropyteam.entropay.employees.repositories.HolidayRepository;

@Service
public class HolidayService extends BaseService<Holiday, HolidayDto, UUID> {

    private final HolidayRepository holidayRepository;
    private final CountryRepository countryRepository;
    private final CalendarService calendarService;


    @Autowired
    public HolidayService(HolidayRepository holidayRepository, CountryRepository countryRepository,
            ReactAdminMapper reactAdminMapper, CalendarService calendarService) {
        super(Holiday.class, reactAdminMapper);
        this.countryRepository = countryRepository;
        this.holidayRepository = holidayRepository;
        this.calendarService = calendarService;
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
    public List<String> getDateColumnsForSearch() {
        return List.of("date");
    }

    public List<Map<String, Integer>> getHolidayYears() {
        List<Integer> years = holidayRepository.getHolidaysYears();

        return years.stream()
                .map(year -> {
                    Map<String, Integer> yearMap = new HashMap<>();
                    yearMap.put("id", year);
                    yearMap.put("year", year);
                    return yearMap;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public HolidayDto create(HolidayDto holidayDto) {
        Holiday entityToCreate = toEntity(holidayDto);
        Holiday savedEntity = getRepository().save(entityToCreate);
        calendarService.createHolidayEvent(entityToCreate.getId().toString(), holidayDto.date(),
                holidayDto.description(),
                entityToCreate.getCountry().getName());
        return toDTO(savedEntity);
    }

    @Override
    @Transactional
    public HolidayDto update(UUID holidayId, HolidayDto holidayDto) {
        Holiday entityToUpdate = toEntity(holidayDto);
        entityToUpdate.setId(holidayId);
        Holiday savedEntity = getRepository().save(entityToUpdate);
        calendarService.updateHolidayEvent(holidayId.toString(), holidayDto.date(), holidayDto.description(),
                entityToUpdate.getCountry().getName());
        return toDTO(savedEntity);
    }

    @Transactional
    @Override
    public HolidayDto delete(UUID id) {
        Holiday holiday = getRepository().findById(id).orElseThrow();
        holiday.setDeleted(true);
        calendarService.deleteHolidayEvent(holiday.getId().toString(), holiday.getDate());
        return toDTO(holiday);
    }

    public CalendarEventDto formatEventData(UUID holidayId, LocalDate date, String description, String country) {
        int currentYear = date.getYear();
        LocalDate endDate = date.plusDays(1);
        String eventId = currentYear + holidayId.toString();
        String eventName;
        if (country.equals("ALL")) {
            eventName = description;
        } else {
            eventName = country + " - " + description;
        }

        return new CalendarEventDto(eventId, eventName, date, endDate);
    }
}