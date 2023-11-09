package com.entropyteam.entropay.employees.services;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.CalendarListEntry;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@EnableConfigurationProperties(GoogleCredentialsProperties.class)
@Service
public class GoogleService {

    private static final Logger LOGGER = LogManager.getLogger();
    private final GoogleCredentialsProperties googleCredentialsProperties;
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);

    @Autowired
    public GoogleService(GoogleCredentialsProperties googleCredentialsProperties) {
        this.googleCredentialsProperties = googleCredentialsProperties;
    }

    public Credential getCredentials() throws IOException, GeneralSecurityException {
        String clientId = googleCredentialsProperties.getIdClient();
        String clientSecret = googleCredentialsProperties.getSecretClient();
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, clientId, clientSecret, SCOPES).setAccessType("offline").build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8100).build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
        return credential;
    }

    public void initGoogle() {
        try {
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials()).setApplicationName("GoogleCalender").build();
            DateTime now = new DateTime(System.currentTimeMillis());
            List<Event> items = service.events().list("primary").setMaxResults(10).setTimeMin(now).setOrderBy("startTime").setSingleEvents(true).execute().getItems();

            if (items.isEmpty()) {
                System.out.println("No upcoming events found.");
            } else {
                System.out.println("Upcoming events:");
                for (Event event : items) {
                    DateTime start = event.getStart().getDateTime();
                    if (start == null) {
                        start = event.getStart().getDate();
                    }
                    System.out.printf("%s (%s)\n", event.getSummary(), start);
                }
            }
        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
        }
    }

    public void createGoogleCalendarEvent(String eventName, LocalDate startDate, LocalDate endDate) {
        try {
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials())
                    .setApplicationName("GoogleCalendar").build();

            Event event = new Event().setSummary(eventName);

            Date startDateUtil = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date endDateUtil = Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String startDateStr = dateFormat.format(startDateUtil);
            String endDateStr = dateFormat.format(endDateUtil);

            DateTime startDateTime = new DateTime(startDateStr);
            DateTime endDateTime = new DateTime(endDateStr);

            EventDateTime startEventDateTime = new EventDateTime().setDate(startDateTime);
            EventDateTime endEventDateTime = new EventDateTime().setDate(endDateTime);

            event.setStart(startEventDateTime);
            event.setEnd(endEventDateTime);

            Event createdEvent = service.events().insert("primary", event).execute();

            System.out.println("Event created: " + createdEvent.getHtmlLink());

        } catch (GoogleJsonResponseException e) {
            System.err.println("Google Calendar API error:");
            System.err.println("HTTP code: " + e.getStatusCode());
            System.err.println("Error message: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
        }
    }


    public GoogleCredentials getCredentialsServiceAccount() throws IOException {
        return GoogleCredentials.fromStream(new FileInputStream("src/main/resources/optical-weft-403114-bb52e7bc537e.json"))
                .createScoped(SCOPES);
    }

    public void initGoogleForServiceAccount() {
        try {
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            Calendar service =
                    new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpCredentialsAdapter(getCredentialsServiceAccount()))
                            .setApplicationName("GoogleCalendar")
                            .build();
            DateTime now = new DateTime(System.currentTimeMillis());


            List<CalendarListEntry> calendarList = service.calendarList().list().execute().getItems();
            calendarList.forEach(c -> LOGGER.info("Calendar Id: {} Summary: {}", c.getId(), c.getSummary()));
            List<Event> items = service.events()
                    .list("<calendar id>")
                    .setMaxResults(10)
                    .setTimeMin(now)
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute().getItems();

            if (items.isEmpty()) {
                System.out.println("No upcoming events found.");
            } else {
                System.out.println("Upcoming events:");
                for (Event event : items) {
                    DateTime start = event.getStart().getDateTime();
                    if (start == null) {
                        start = event.getStart().getDate();
                    }
                    System.out.printf("%s (%s)\n", event.getSummary(), start);
                }
            }
        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
        }
    }


    /**
     * Invoke to accept shared calendars one time
     */
    public void insertCalendar() throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Calendar service =
                new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpCredentialsAdapter(getCredentialsServiceAccount()))
                        .setApplicationName("GoogleCalendar")
                        .build();
        CalendarListEntry calendarListEntry = new CalendarListEntry();
        calendarListEntry.setId("<Set calendar ID here>");
        CalendarListEntry updatedCalendarList = service.calendarList().insert(calendarListEntry).execute();
        LOGGER.info("Calendar Summary: {}", updatedCalendarList.getSummary());
    }
}
