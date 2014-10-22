package taskbuddy.googlecal;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Scanner;
import java.util.Set;
import java.util.TimeZone;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

public class GooCalRetriever {
	private static final String USER_ID = "ipeech";
	
	// Attributes
	private String retrievedSummary;
	private String retrievedDescription;
	private String retrievedStart;
	private String retrievedEnd;
	private java.util.Calendar retrievedStartCalendar;
	private java.util.Calendar retrievedEndCalendar;
	
	
	public void retrieve(Calendar service, String calendarId, String eventId) {
		Event event = null;
		try {
			event = service.events().get(calendarId, eventId).execute();
		} catch (IOException unableToRetrieve) {
			System.err.println("Unable to retrieve event");
		}
		setRetrievedSummary(event.getSummary());
		setRetrievedStart(event.getStart().toString());
		setRetrievedEnd(event.getEnd().toString());
		
	}
	
	// Mutators
	public void setRetrievedSummary(String retrievedSummary) {
		this.retrievedSummary = retrievedSummary;
	}
	
	public void setRetrievedStart(String retrievedStart) {
		this.retrievedStart = retrievedStart;
	}
	
	public void setRetrievedEnd(String retrievedEnd) {
		this.retrievedEnd = retrievedEnd;
	}

	
	// Accessors 
	public String getRetrievedSummary() {
		return retrievedSummary;
	}
	
	public String getRetrievedStart() {
		System.out.println("Retrieved Start: " + retrievedStart);
		return retrievedStart;
	}
	
	public String getRetrievedEnd() {
		System.out.println("Retrieved End: " + retrievedEnd);
		return retrievedEnd;
	}
}
