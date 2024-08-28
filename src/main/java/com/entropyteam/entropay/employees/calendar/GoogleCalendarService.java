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
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
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

    private final static Logger LOGGER = LoggerFactory.getLogger(GoogleCalendarService.class);

    private final GoogleCredentialsProperties googleCredentialsProperties;

    @Autowired
    public GoogleCalendarService(GoogleCredentialsProperties googleCredentialsProperties) {
        this.googleCredentialsProperties = googleCredentialsProperties;
    }

    @Override
    public void createBirthdayEvent(String employeeId, String firstName, String lastName, LocalDate birthdate) {
        Event event = buildBirthdayEvent(employeeId, firstName, lastName, birthdate);
        createGoogleCalendarEvent(event);
    }

    @Override
    public void updateBirthdayEvent(String employeeId, String firstName, String lastName, LocalDate birthDate) {
        Event event = buildBirthdayEvent(employeeId, firstName, lastName, birthDate);
        LOGGER.info("Updating birthday event for employee {}: {}", employeeId, event);
        updateGoogleCalendarEvent(event);
    }

    @Override
    public void deleteBirthdayEvent(String employeeId) {
        String eventId = getBirthdayEventId(employeeId);
        deleteGoogleCalendarEvent(eventId);
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
        deleteGoogleCalendarEvent(leaveId);
    }

    private static Event buildBirthdayEvent(String employeeId, String firstName, String lastName, LocalDate birthDate) {
        LocalDate birthday = LocalDate.of(LocalDate.now().getYear(), birthDate.getMonth(), birthDate.getDayOfMonth());
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