package com.entropyteam.entropay.employees.services;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.HashMap;
import java.util.stream.Collectors;


import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.common.BaseService;
import com.entropyteam.entropay.common.ReactAdminMapper;
import com.entropyteam.entropay.common.exceptions.InvalidRequestParametersException;
import com.entropyteam.entropay.employees.dtos.CalendarEventDto;
import com.entropyteam.entropay.employees.dtos.PtoDto;
import com.entropyteam.entropay.employees.repositories.PtoRepository;
import com.entropyteam.entropay.employees.repositories.EmployeeRepository;
import com.entropyteam.entropay.employees.repositories.LeaveTypeRepository;
import com.entropyteam.entropay.employees.repositories.HolidayRepository;
import com.entropyteam.entropay.employees.repositories.VacationRepository;
import com.entropyteam.entropay.employees.models.Employee;
import com.entropyteam.entropay.employees.models.Holiday;
import com.entropyteam.entropay.employees.models.LeaveType;
import com.entropyteam.entropay.employees.models.Pto;
import com.entropyteam.entropay.employees.models.Status;

@Service
public class PtoService extends BaseService<Pto, PtoDto, UUID> {

    public static final String VACATION_TYPE = "vacation";
    public static final Double HALF_DAY_OFF = 0.5;
    private static final Logger LOGGER = LogManager.getLogger();
    private PtoRepository ptoRepository;
    private EmployeeRepository employeeRepository;
    private LeaveTypeRepository leaveTypeRepository;
    private VacationRepository vacationRepository;
    private HolidayRepository holidayRepository;
    private VacationService vacationService;
    private GoogleService googleService;

    @Autowired
    public PtoService(ReactAdminMapper mapper, PtoRepository ptoRepository, EmployeeRepository employeeRepository,
                      LeaveTypeRepository leaveTypeRepository, VacationRepository vacationRepository,
                      HolidayRepository holidayRepository, VacationService vacationService, GoogleService googleService) {
        super(Pto.class, mapper);
        this.ptoRepository = ptoRepository;
        this.employeeRepository = employeeRepository;
        this.leaveTypeRepository = leaveTypeRepository;
        this.vacationRepository = vacationRepository;
        this.holidayRepository = holidayRepository;
        this.vacationService = vacationService;
        this.googleService = googleService;
    }

    @Transactional
    @Override
    public PtoDto delete(UUID id) {
        Pto pto = getRepository().findById(id).orElseThrow();
        pto.setDeleted(true);
        if (isVacationType(pto)) {
            vacationService.discountVacationDebit(pto.getEmployee(), pto.getDaysAsInteger());
            LOGGER.info("Pto of type vacation deleted, employeeId: {}, amount of days: {}",
                    pto.getEmployee().getId(), pto.getDays());
        }
        googleService.deleteGoogleCalendarEvent(id.toString());
        return toDTO(pto);
    }

    @Transactional
    @Override
    public PtoDto update(UUID id, PtoDto ptoDto) {
        Pto oldEntity = ptoRepository.findById(id).orElseThrow();
        Pto entityToUpdate = toEntity(ptoDto);
        LOGGER.info("Started update of pto of type: {}, update to type: {}, amount of days: {} ",
                oldEntity.getLeaveType().getName(), entityToUpdate.getLeaveType().getName(), entityToUpdate.getDays());
        if (isVacationType(oldEntity) && isVacationType(entityToUpdate)
                && oldEntity.getDays().compareTo(entityToUpdate.getDays()) != 0) {
            vacationService.discountVacationDebit(oldEntity.getEmployee(), oldEntity.getDaysAsInteger());
            vacationService.addVacationDebit(entityToUpdate.getEmployee(), entityToUpdate.getDaysAsInteger());
        } else if (isVacationType(oldEntity) && !isVacationType(entityToUpdate)) {
            vacationService.discountVacationDebit(oldEntity.getEmployee(), oldEntity.getDaysAsInteger());
        } else if (!isVacationType(oldEntity) && isVacationType(entityToUpdate)) {
            vacationService.addVacationDebit(entityToUpdate.getEmployee(), entityToUpdate.getDaysAsInteger());
        }
        entityToUpdate.setId(id);
        entityToUpdate.setStatus(Status.APPROVED); // For now all approved
        Pto savedEntity = getRepository().save(entityToUpdate);
        CalendarEventDto calendarEventDto = createCalendarEventDto(savedEntity);
        googleService.updateGoogleCalendarEvent(calendarEventDto);
        return toDTO(savedEntity);
    }

    @Transactional
    @Override
    public PtoDto create(PtoDto ptoDto) {
        Pto entityToCreate = toEntity(ptoDto);
        entityToCreate.setStatus(Status.APPROVED); // For now all approved
        if (isVacationType(entityToCreate)) {
            LOGGER.info("PTO of type vacation started creation employeeId: {}, amount of days: {}",
                    entityToCreate.getEmployee().getId(), entityToCreate.getDays());
            Double totalDaysAsDouble = entityToCreate.getDays();
            if (Objects.equals(totalDaysAsDouble, HALF_DAY_OFF)) {
                throw new InvalidRequestParametersException("Can't take half a day off on vacations");
            }

            Integer totalDays = entityToCreate.getDaysAsInteger();
            if (totalDays == 0) {
                throw new InvalidRequestParametersException("Vacation days must be greater than 0");
            }

            vacationService.addVacationDebit(entityToCreate.getEmployee(), totalDays);
        }

        Pto savedEntity = getRepository().save(entityToCreate);
        LOGGER.info("PTO of type {} created employeeId: {}, amount of days: {}",
                savedEntity.getLeaveType().getName(), savedEntity.getEmployee().getId(), savedEntity.getDays());
        ptoDto = toDTO(savedEntity);
        CalendarEventDto calendarEventDto = createCalendarEventDto(savedEntity);
        googleService.createGoogleCalendarEvent(calendarEventDto);
        return ptoDto;
    }

    @Override
    protected BaseRepository<Pto, UUID> getRepository() {
        return this.ptoRepository;
    }

    @Override
    protected PtoDto toDTO(Pto entity) {
        return new PtoDto(entity);
    }

    @Override
    protected Pto toEntity(PtoDto dto) {
        Employee employee = employeeRepository.findById(dto.employeeId()).orElse(null);
        LeaveType leaveType = leaveTypeRepository.findById(dto.leaveTypeId()).orElse(null);
        Status statusType = dto.status();
        Pto pto = new Pto(dto);
        setTimeAmount(pto, dto.isHalfDay(), employee.getCountry().getId());
        pto.setEmployee(employee);
        pto.setLeaveType(leaveType);
        pto.setStatus(statusType);
        return pto;
    }

    public void setTimeAmount(Pto entity, boolean isHalfDay, UUID employeesCountryId) {
        Long days = ChronoUnit.DAYS.between(entity.getStartDate(), entity.getEndDate());
        if (days.compareTo(0L) == 0) {
            if (isHalfDay) {
                entity.setDays(HALF_DAY_OFF);
                entity.setLabourHours(0);
            } else {
                entity.setLabourHours(0);
                entity.setDays(1.0);
            }
        } else {
            Double labourDays = 0.0;
            LocalDate currentDate = entity.getStartDate();
            LocalDate endDate = entity.getEndDate();
            List<Holiday> holidaysInPeriod = holidayRepository.
                    findHolidaysByCountryAndPeriod(employeesCountryId, currentDate, endDate);
            while (!currentDate.isAfter(endDate)) {
                LocalDate finalCurrentDate = currentDate;
                if (currentDate.getDayOfWeek() != DayOfWeek.SATURDAY &&
                        currentDate.getDayOfWeek() != DayOfWeek.SUNDAY &&
                        holidaysInPeriod.stream().noneMatch(holiday -> holiday.getDate().equals(finalCurrentDate))) {
                    labourDays++;
                }

                currentDate = currentDate.plusDays(1);
            }
            entity.setLabourHours(0);
            entity.setDays(labourDays);
        }
    }

    private static boolean isVacationType(Pto oldEntity) {
        return StringUtils.equalsIgnoreCase(oldEntity.getLeaveType().getName(), VACATION_TYPE);
    }

    private CalendarEventDto createCalendarEventDto(Pto pto) {
        String eventId = pto.getId().toString();
        LocalDate startDate = pto.getStartDate();
        LocalDate endDate = pto.getEndDate().plusDays(1);
        Employee employee = pto.getEmployee();
        LeaveType leaveType = pto.getLeaveType();
        String eventName = employee.getFirstName() + " " + employee.getLastName() + " " + leaveType.getName();
        return new CalendarEventDto(eventId, eventName, startDate, endDate);
    }

    @Transactional
    public PtoDto cancelPto(UUID id) {
        Pto pto = ptoRepository.findById(id).orElseThrow();
        pto.setStatus(Status.CANCELLED);
        if (isVacationType(pto)) {
            vacationService.discountVacationDebit(pto.getEmployee(), pto.getDaysAsInteger());
        }
        googleService.deleteGoogleCalendarEvent(id.toString());
        Pto savedEntity = getRepository().save(pto);
        return toDTO(savedEntity);
    }

    public List<Map<String, Integer>> getPtosYears() {
        List<Integer> years = ptoRepository.getPtosYears();

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
    public List<String> getDateColumnsForSearch() {
        return List.of("startDate", "endDate");
    }
}
