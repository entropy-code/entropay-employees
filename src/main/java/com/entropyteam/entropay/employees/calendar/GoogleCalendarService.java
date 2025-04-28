package com.entropyteam.entropay.employees.calendar;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import com.entropyteam.entropay.employees.repositories.HolidayRepository;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.Calendar.Builder;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.annotations.VisibleForTesting;

@EnableConfigurationProperties(GoogleCredentialsProperties.class)
@Service
public class GoogleCalendarService implements CalendarService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GoogleCalendarService.class);
    private static final String ARGENTINA = "Argentina";
    private final GoogleCredentialsProperties googleCredentialsProperties;
    private final HolidayRepository holidayRepository;

    @Autowired
    public GoogleCalendarService(GoogleCredentialsProperties googleCredentialsProperties,
            HolidayRepository holidayRepository) {
        this.googleCredentialsProperties = googleCredentialsProperties;
        this.holidayRepository = holidayRepository;
    }

    @Override
    public void createBirthdayEvent(String employeeId, String firstName, String lastName, LocalDate birthdate) {
        LOGGER.info("Creating birthday event for employee {}: {}", employeeId, birthdate);
        Event event = buildBirthdayEvent(employeeId, firstName, lastName, birthdate);
        createGoogleCalendarEvent(event);

        // Check if a birthday falls on a non-working day and create an additional event for the next working day
        LocalDate birthdayThisYear = getBirthdayThisYear(birthdate);
        if (!isWorkingDay(birthdayThisYear)) {
            LocalDate nextWorkingDay = getNextWorkingDay(birthdayThisYear);
            LOGGER.info("Birthday falls on a non-working day, creating additional event for next working day: {}",
                    nextWorkingDay);
            Event workingDayEvent = buildBirthdayEvent(employeeId, firstName, lastName, nextWorkingDay);
            createGoogleCalendarEvent(workingDayEvent);
        }
    }

    @Override
    public void updateBirthdayEvent(String employeeId, String firstName, String lastName, LocalDate birthDate) {
        // Update the event on the actual birthday date
        Event event = buildBirthdayEvent(employeeId, firstName, lastName, birthDate);
        LOGGER.info("Updating birthday event for employee {}: {}", employeeId, event);
        updateGoogleCalendarEvent(event);

        // Check if a birthday falls on a non-working day and update or create the additional event for the next
        // working day
        LocalDate birthdayThisYear = getBirthdayThisYear(birthDate);
        if (!isWorkingDay(birthdayThisYear)) {
            LocalDate nextWorkingDay = getNextWorkingDay(birthdayThisYear);
            LOGGER.info("Birthday falls on a non-working day, updating additional event for next working day: {}",
                    nextWorkingDay);
            Event workingDayEvent = buildBirthdayEvent(employeeId, firstName, lastName, nextWorkingDay);
            updateGoogleCalendarEvent(workingDayEvent);
        } else {
            // If a birthday is on a working day, try to delete the additional event if it exists
            try {
                String mondayEventId = getReminderBirthdayEventId(employeeId);
                deleteGoogleCalendarEvent(mondayEventId);
                LOGGER.info("Deleted additional event as birthday is on a working day");
            } catch (Exception e) {
                // Ignore errors if the additional event doesn't exist
                LOGGER.debug("No additional event to delete or error deleting it", e);
            }
        }
    }

    @Override
    public void deleteBirthdayEvent(String employeeId) {
        // Delete the main birthday event
        String eventId = getBirthdayEventId(employeeId);
        deleteGoogleCalendarEvent(eventId);

        // Also try to delete the additional event for the next working day if it exists
        try {
            String additionalEventId = getReminderBirthdayEventId(employeeId);
            deleteGoogleCalendarEvent(additionalEventId);
            LOGGER.info("Deleted additional working day birthday event for employee {}", employeeId);
        } catch (Exception e) {
            // Ignore errors if the additional event doesn't exist
            LOGGER.debug("No additional working day event to delete or error deleting it", e);
        }
    }

    @Override
    public void createHolidayEvent(String holidayId, LocalDate holidayDate, String holidayName, String holidayCountry) {
        Event event = buildHolidayEvent(holidayId, holidayDate, holidayName, holidayCountry);
        createGoogleCalendarEvent(event);
    }

    @Override
    public void updateHolidayEvent(String holidayId, LocalDate holidayDate, String holidayName, String holidayCountry) {
        Event event = buildHolidayEvent(holidayId, holidayDate, holidayName, holidayCountry);
        updateGoogleCalendarEvent(event);
    }

    @Override
    public void deleteHolidayEvent(String holidayId, LocalDate holidayDate) {
        String eventId = getHolidayEventId(holidayId, holidayDate);
        deleteGoogleCalendarEvent(eventId);
    }

    @Override
    public void createLeaveEvent(String leaveId, String leaveType, String firstName, String lastName,
            LocalDate startDate, LocalDate endDate) {
        Event event = buildLeaveEvent(leaveId, leaveType, firstName, lastName, startDate, endDate);
        createGoogleCalendarEvent(event);
    }

    @Override
    public void updateLeaveEvent(String leaveId, String leaveType, String firstName, String lastName,
            LocalDate startDate, LocalDate endDate) {
        Event event = buildLeaveEvent(leaveId, leaveType, firstName, lastName, startDate, endDate);
        updateGoogleCalendarEvent(event);
    }

    @Override
    public void deleteLeaveEvent(String leaveId) {
        String eventId = getLeaveEventId(leaveId);
        deleteGoogleCalendarEvent(eventId);
    }

    private static Event buildBirthdayEvent(String employeeId, String firstName, String lastName, LocalDate birthDate) {
        LocalDate birthday = getBirthdayThisYear(birthDate);
        return new Event()
                .setId(getBirthdayEventId(employeeId))
                .setSummary("Birthday - %s %s".formatted(firstName, lastName))
                .setStart(toEventDateTime(birthday))
                .setEnd(toEventDateTime(birthday))
                .setColorId(EventColor.PALE_RED.getColorId());
    }

    private static String getBirthdayEventId(String employeeId) {
        return "%d%s".formatted(LocalDate.now().getYear(), employeeId.replace("-", ""));
    }

    private static String getReminderBirthdayEventId(String employeeId) {
        return "%d%s-monday".formatted(LocalDate.now().getYear(), employeeId.replace("-", ""));
    }

    private Event buildHolidayEvent(String holidayId, LocalDate holidayDate, String name,
            String country) {

        return new Event()
                .setId(getHolidayEventId(holidayId, holidayDate))
                .setSummary(country.equals("ALL") ? name : "%s - %s".formatted(country, name))
                .setStart(toEventDateTime(holidayDate))
                .setEnd(toEventDateTime(holidayDate.plusDays(1)))
                .setColorId(EventColor.PALE_BLUE.getColorId());
    }

    private static String getHolidayEventId(String holidayId, LocalDate holidayDate) {
        return "%d%s".formatted(holidayDate.getYear(), holidayId.replace("-", ""));
    }

    private Event buildLeaveEvent(String leaveId, String leaveType, String firstName, String lastName,
            LocalDate startDate, LocalDate endDate) {

        return new Event()
                .setId(getLeaveEventId(leaveId))
                .setSummary("%s - %s %s".formatted(leaveType, firstName, lastName))
                .setStart(toEventDateTime(startDate))
                .setEnd(toEventDateTime(endDate.plusDays(1)))
                .setColorId(EventColor.PALE_GREEN.getColorId());
    }

    private String getLeaveEventId(String leaveId) {
        return leaveId.replace("-", "");
    }

    private void createGoogleCalendarEvent(Event event) {
        try {
            String calendarId = googleCredentialsProperties.getCalendarId();

            Event executed = getCalendarService()
                    .events()
                    .insert(calendarId, event)
                    .setSendNotifications(true)
                    .execute();

            LOGGER.info("Event created {}", executed.getHtmlLink());
        } catch (Exception e) {
            LOGGER.error("Unable to create event", e);
        }
    }

    private void updateGoogleCalendarEvent(Event event) {
        try {
            String calendarId = googleCredentialsProperties.getCalendarId();

            Event executed = getCalendarService()
                    .events()
                    .update(calendarId, event.getId(), event)
                    .execute();

            LOGGER.info("Event updated {}", executed.getHtmlLink());
        } catch (GoogleJsonResponseException e) {
            if (e.getStatusCode() == 404) {
                createGoogleCalendarEvent(event);
            }
        } catch (Exception e) {
            LOGGER.error("Unable to update event", e);
        }
    }

    private void deleteGoogleCalendarEvent(String eventId) {
        try {
            String calendarId = googleCredentialsProperties.getCalendarId();

            getCalendarService()
                    .events()
                    .delete(calendarId, eventId)
                    .execute();

            LOGGER.info("Event deleted successfully");
        } catch (Exception e) {
            LOGGER.error("Unable to delete event", e);
        }
    }

    private static EventDateTime toEventDateTime(LocalDate date) {
        DateTime dateTime = new DateTime(date.toString());
        return new EventDateTime().setDate(dateTime);
    }

    private static LocalDate getBirthdayThisYear(LocalDate birthDate) {
        // Return the actual birthday date for this year without adjustments
        return LocalDate.of(LocalDate.now().getYear(), birthDate.getMonth(), birthDate.getDayOfMonth());
    }

    private boolean isWorkingDay(LocalDate date) {
        // Check if it's a weekend
        if (date.getDayOfWeek().getValue() >= 6) { // Saturday or Sunday
            return false;
        }

        // Check if it's a holiday in Argentina
        return holidayRepository.findHolidaysByCountryAndPeriod(ARGENTINA, date, date)
                .stream()
                .noneMatch(holiday -> holiday.getDate().equals(date));
    }

    private LocalDate getNextWorkingDay(LocalDate date) {
        LocalDate nextDay = date;

        // Keep incrementing the date until we find a working day
        while (!isWorkingDay(nextDay)) {
            nextDay = nextDay.plusDays(1);
        }

        return nextDay;
    }

    @VisibleForTesting
    GoogleCredentialsProperties getGoogleCredentialsProperties() {
        return googleCredentialsProperties;
    }

    @VisibleForTesting
    Calendar getCalendarService() {
        try {
            final NetHttpTransport netHttpTransport = GoogleNetHttpTransport.newTrustedTransport();
            return new Builder(netHttpTransport, GsonFactory.getDefaultInstance(),
                    new HttpCredentialsAdapter(getCredentialsServiceAccount()))
                    .setApplicationName("GoogleCalendar")
                    .build();
        } catch (GeneralSecurityException | IOException e) {
            LOGGER.error("Unable to create calendar service", e);
            throw new RuntimeException("Unable to create calendar service", e);
        }
    }

    private GoogleCredentials getCredentialsServiceAccount() throws IOException {
        return GoogleCredentials.fromStream(new ByteArrayInputStream(googleCredentialsProperties.getCredentials()))
                .createScoped(List.of(CalendarScopes.CALENDAR));
    }
}
