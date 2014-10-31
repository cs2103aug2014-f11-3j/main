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
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TimeZone;

import taskbuddy.gui.AWTgui;

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
import com.google.api.services.calendar.model.Events;



public class GoogleCalBackwardSync {
	private static final String CALENDAR_ID = "i357fqqhffrf1fa9udcbn9sikc@group.calendar.google.com";
	
	public static void main(String[] args) {
		getListFromGoogle();
	}


	public static void getListFromGoogle() {
		GooCalBackend gooCalBackend = new GooCalBackend();
		GooCalRetriever gooCalRetriever = new GooCalRetriever();
		
		Calendar service = null;
		String calendarId = CALENDAR_ID;
		
		if (!gooCalBackend.isUserOnline()) {
			//return "failureNoInternet";
		}
		
		else {
			// This try catch blocks checks if Google's servers can be read
			try {
				service = gooCalBackend.initializeCalendar();
			} catch (IOException connectionProblem) {
				// This catach statement cataches a connetion problem
				// Exception is only caught when authentication code is valid, yet tbere is a failure in initializing the Google Calendar
			}
			
			String pageToken = null;
			do {
			  Events events = null;
			try {
				events = service.events().list(CALENDAR_ID).setPageToken(pageToken).execute();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			  List<Event> items = events.getItems();
			  for (Event event : items) {
			    System.out.println(event.getSummary());
			    
			  }
			  pageToken = events.getNextPageToken();
			} while (pageToken != null);
			
		}
			
	}
	

}
