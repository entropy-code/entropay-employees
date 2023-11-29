package com.entropyteam.entropay.employees.services;

import com.entropyteam.entropay.employees.dtos.CalendarEventDto;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@EnableConfigurationProperties(GoogleCredentialsProperties.class)
@Service
public class GoogleService {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);

    @Autowired
    private final GoogleCredentialsProperties googleCredentialsProperties;

    public GoogleService(GoogleCredentialsProperties googleCredentialsProperties) {
        this.googleCredentialsProperties = googleCredentialsProperties;
    }

    public GoogleCredentials getCredentialsServiceAccount() throws IOException {
        return GoogleCredentials.fromStream(new ByteArrayInputStream(googleCredentialsProperties.getCredentials()))
                .createScoped(SCOPES);
    }

    private EventDateTime convertToLocalTimeZones(LocalDate date) {
        Date utilDate = java.sql.Date.valueOf(date);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(utilDate);
        DateTime dateTime = new DateTime(formattedDate);
        return new EventDateTime().setDate(dateTime);
    }


    public void createGoogleCalendarEvent(CalendarEventDto calendarEventDto) {
        try {
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY,
                    new HttpCredentialsAdapter(getCredentialsServiceAccount()))
                    .setApplicationName("GoogleCalendar")
                    .build();
            Event event = new Event().setSummary(calendarEventDto.description());
            String formattedId = calendarEventDto.id().replace("-", "");
            EventDateTime eventDateStartTime = convertToLocalTimeZones(calendarEventDto.startDate());
            LOGGER.info("startDate" + eventDateStartTime);
            EventDateTime eventDateEndTimes = convertToLocalTimeZones(calendarEventDto.endDate());
            LOGGER.info("startEnd" + eventDateEndTimes);

            event.setId(formattedId);
            event.setStart(eventDateStartTime);
            event.setEnd(eventDateEndTimes);

            String idCalendar = googleCredentialsProperties.getCalendarId();

            event = service.events().insert(idCalendar, event).setSendNotifications(true).execute();
            LOGGER.info("Event created " + event.getHtmlLink());
        } catch (IOException | GeneralSecurityException e) {
            LOGGER.error(e.getMessage());
        }
    }
}