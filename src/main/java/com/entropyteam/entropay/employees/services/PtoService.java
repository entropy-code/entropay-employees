package com.entropyteam.entropay.employees.services;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.common.BaseService;
import com.entropyteam.entropay.common.ReactAdminMapper;
import com.entropyteam.entropay.common.exceptions.InvalidRequestParametersException;
import com.entropyteam.entropay.employees.calendar.CalendarService;
import com.entropyteam.entropay.employees.dtos.PtoDto;
import com.entropyteam.entropay.employees.models.Employee;
import com.entropyteam.entropay.employees.models.Holiday;
import com.entropyteam.entropay.employees.models.LeaveType;
import com.entropyteam.entropay.employees.models.Pto;
import com.entropyteam.entropay.employees.models.Status;
import com.entropyteam.entropay.employees.repositories.EmployeeRepository;
import com.entropyteam.entropay.employees.repositories.HolidayRepository;
import com.entropyteam.entropay.employees.repositories.LeaveTypeRepository;
import com.entropyteam.entropay.employees.repositories.PtoRepository;

@Service
public class PtoService extends BaseService<Pto, PtoDto, UUID> {

    public static final String VACATION_TYPE = "vacation";
    public static final Double HALF_DAY_OFF = 0.5;
    private final static Logger LOGGER = LoggerFactory.getLogger(PtoService.class);
    private final PtoRepository ptoRepository;
    private final EmployeeRepository employeeRepository;
    private final LeaveTypeRepository leaveTypeRepository;
    private final HolidayRepository holidayRepository;
    private final VacationService vacationService;
    private final CalendarService calendarService;

    @Autowired
    public PtoService(ReactAdminMapper mapper, PtoRepository ptoRepository, EmployeeRepository employeeRepository,
            LeaveTypeRepository leaveTypeRepository, HolidayRepository holidayRepository,
            VacationService vacationService, CalendarService calendarService) {
        super(Pto.class, mapper);
        this.ptoRepository = ptoRepository;
        this.employeeRepository = employeeRepository;
        this.leaveTypeRepository = leaveTypeRepository;
        this.holidayRepository = holidayRepository;
        this.vacationService = vacationService;
        this.calendarService = calendarService;
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
        calendarService.deleteLeaveEvent(id.toString());
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
        calendarService.updateLeaveEvent(id.toString(), savedEntity.getLeaveType().getName(),
                savedEntity.getEmployee().getFirstName(), savedEntity.getEmployee().getLastName(),
                ptoDto.ptoStartDate(), ptoDto.ptoEndDate());
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

        calendarService.createLeaveEvent(savedEntity.getId().toString(), savedEntity.getLeaveType().getName(),
                savedEntity.getEmployee().getFirstName(), savedEntity.getEmployee().getLastName(),
                savedEntity.getStartDate(), savedEntity.getEndDate());

        return toDTO(savedEntity);
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

    private static boolean isVacationType(Pto pto) {
        return VACATION_TYPE.equalsIgnoreCase(pto.getLeaveType().getName());
    }

    @Transactional
    public PtoDto cancelPto(UUID id) {
        Pto pto = ptoRepository.findById(id).orElseThrow();
        pto.setStatus(Status.CANCELLED);
        if (isVacationType(pto)) {
            vacationService.discountVacationDebit(pto.getEmployee(), pto.getDaysAsInteger());
        }
        calendarService.deleteLeaveEvent(id.toString());
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
