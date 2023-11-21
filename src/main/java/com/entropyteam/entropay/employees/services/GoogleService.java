package com.entropyteam.entropay.employees.services;

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

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

@EnableConfigurationProperties(GoogleCredentialsProperties.class)
@Service
public class GoogleService {

    private final GoogleCredentialsProperties googleCredentialsProperties;
    private static final Logger LOGGER = LogManager.getLogger();
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);

    @Autowired
    public GoogleService(GoogleCredentialsProperties googleCredentialsProperties) {
        this.googleCredentialsProperties = googleCredentialsProperties;
    }

    public GoogleCredentials getCredentialsServiceAccount() throws IOException {
        return GoogleCredentials.fromStream(new FileInputStream("src/main/resources/<jsonfilename>.json"))
                .createScoped(SCOPES);
    }

    private EventDateTime[] convertToLocalTimeZones(LocalDate startDate, LocalDate endDate) {
        long startMillis = startDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long endMillis = endDate.atStartOfDay(ZoneId.systemDefault()).plusDays(1).toInstant().toEpochMilli();

        EventDateTime startEventDateTime = new EventDateTime().setDateTime(new DateTime(startMillis)).setTimeZone("UTC");
        EventDateTime endEventDateTime = new EventDateTime().setDateTime(new DateTime(endMillis)).setTimeZone("UTC");

        return new EventDateTime[]{startEventDateTime, endEventDateTime};
    }

    public void createGoogleCalendarEvent(String eventName, LocalDate startDate, LocalDate endDate) {
        try {
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY,
                    new HttpCredentialsAdapter(getCredentialsServiceAccount()))
                    .setApplicationName("GoogleCalendar")
                    .build();

            Event event = new Event().setSummary(eventName);

            EventDateTime[] eventDateTimes = convertToLocalTimeZones(startDate, endDate);

            event.setStart(eventDateTimes[0]);
            event.setEnd(eventDateTimes[1]);

            String idCalendar = googleCredentialsProperties.getIdCalender();
            event = service.events().insert(idCalendar, event).setSendNotifications(true).execute();

            LOGGER.info("Event created " + event.getHtmlLink());
        } catch (IOException | GeneralSecurityException e) {
            LOGGER.error(e.getMessage());
        }
    }
}
