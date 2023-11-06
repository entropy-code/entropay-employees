package com.entropyteam.entropay.employees.services;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@EnableConfigurationProperties(GoogleCredentialsProperties.class)
@Service
public class GoogleService {
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

    public void createGoogleCalendarEvent(String eventName, Date eventDate) {
        try {
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials()).setApplicationName("GoogleCalendar").build();

            Event event = new Event().setSummary(eventName);

            DateTime startDateTime = new DateTime(eventDate, TimeZone.getTimeZone("UTC"));
            EventDateTime start = new EventDateTime().setDateTime(startDateTime);
            DateTime endDateTime = new DateTime(eventDate, TimeZone.getTimeZone("UTC"));
            EventDateTime end = new EventDateTime().setDateTime(endDateTime);

            event.setStart(start);
            event.setEnd(end);

            Event createdEvent = service.events().insert("primary", event).execute();

            System.out.println("Event create: " + createdEvent.getHtmlLink());
        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
        }
    }
}
