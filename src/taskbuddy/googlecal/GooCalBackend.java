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

import org.hamcrest.core.IsAnything;

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

public class GooCalBackend {

	
	// Strings which are currently hardcoded

    private static final String USER_ID = "ipeech";
    
    private static EventDateTime startEventAllDay;
    private static EventDateTime endEventAllDay;
    private static EventDateTime startEventDateTime;
    private static EventDateTime endEventDateTime;
    
    static AuthorizationCodeFlow codeFlow;
    static String redirectUri;
    
    
  
	public boolean isUserOnline()  {
		Socket socket = null;
		try {
		    socket = new Socket("www.google.com", 80);
		} catch (UnknownHostException hostNotReached) {
			System.err.println("User is offline");
			return false;
		} catch (IOException socketError) {
			System.err.println("Socket cannot be created");
		}
		finally { 
			if (socket != null) {
		    	try { 
		    		socket.close(); 
		    	} catch(IOException e) {}		    	
		    }
		}
		return true;
	}
	
	public static boolean isAuthenticationValid() {
		Calendar service = null;
//		if (isTokenDbEmpty()) {			
//			// Generate new Google Calendar Authentication
//			addToDb(generateNewToken());
//			try {
//				service = createCalendar(readDb());
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		
//		else {
			try {
				// Check if authentication key is valid. If it is invalid, IOException unauthorized will be caught.
				service = createCalendar(readDb());
				checkCalendar(createCalendar(readDb()));
			} catch (IOException unauthorized) {
				// An IOException is thrown by the createCalendar method, if the calendar fails to be created due to an invalid authorization token.
				// The IOException is caught at this point. 
				// When the exception is caught, the original token database is cleared, and a new token is generated by the method generateNewToken.
				return false;
			}
		//}
		return true;
	}
	
	public Calendar initializeCalendar() throws IOException {
		//System.out.println("Executing initializeCalendar:"); // For debugging
		Calendar service = null;

		//clearDb(); // For debugging
		//addToDb("abc"); // To purposely create an invalid code in the database for testing
				
		if (isTokenDbEmpty()) {			
			// Generate new Google Calendar Authentication
			//addToDb(generateNewToken());
			//service = createCalendar(readDb());
		}
		
		else {
			try {
				// Check if authentication key is valid. If it is invalid, IOException unauthorized will be caught.
				service = createCalendar(readDb());
				checkCalendar(createCalendar(readDb()));
			} catch (IOException unauthorized) {
				// An IOException is thrown by the createCalendar method, if the calendar fails to be created due to an invalid authorization token.
				// The IOException is caught at this point. 
				// When the exception is caught, the original token database is cleared, and a new token is generated by the method generateNewToken.
				System.err.println("Invalid authentication code");
				clearDb();
				addToDb(generateNewToken()); 
				service = createCalendar(readDb());
			}
		}
		return service;
	}
	
	public boolean isAllDayEvent(String eventStartTime, String eventEndTime) {
		//System.out.println("check all day start: " + eventStartTime);
		//System.out.println("check all day end: " + eventEndTime);
		if (eventStartTime.equals("00:00") && eventEndTime.equals("23:59")) {
			return true;
		}
		else {
			return false;
		}
	}
	
	
	public void setEventNormal(String eventStartDate, String eventStartTime, String eventEndDate, String eventEndTime) {
		// This method modifies global variables
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			// To parse string into Date object
			Date dateFirst = simpleDateFormat.parse(eventStartDate + " " + eventStartTime);
			Date dateSecond = simpleDateFormat.parse(eventEndDate + " " + eventEndTime);

			// Create DateTime object to add to event object
			DateTime dateTime1 = new DateTime(dateFirst,TimeZone.getTimeZone("UTC"));
			startEventDateTime = new EventDateTime();
			startEventDateTime.setDateTime(dateTime1);
			
			DateTime dateTime2 = new DateTime(dateSecond,TimeZone.getTimeZone("UTC"));
			endEventDateTime = new EventDateTime();
			endEventDateTime.setDateTime(dateTime2);
		} catch (ParseException ex) {
			System.out.println("Exception " + ex);
		}
	}
	
	public void setEventAllDay(String eventStartDate, String eventEndDate) {
		SimpleDateFormat simpleDateFormatAllDay = new SimpleDateFormat("dd/MM/yyyy");
		
		
		
		
		try {
			// To parse string into Date object
			Date dateFirst = simpleDateFormatAllDay.parse(eventStartDate);
			Date dateSecond = simpleDateFormatAllDay.parse(eventEndDate);
			
			java.util.Calendar cal = java.util.Calendar.getInstance();
			cal.setTime(dateSecond);
			cal.add( java.util.Calendar.DATE, 1 );
			
			
			
			
			// Creates string from date object, string must be in a
			// particular format to create a DateTime object with no
			// time element
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String startDateStr = dateFormat.format(dateFirst);
			String endDateStr2 = dateFormat.format(cal.getTime());
			String endDateStr = dateFormat.format(dateSecond);

			// Out of the 6 methods for creating a DateTime object with
			// no time element, only the String version works
			DateTime startDateTime = new DateTime(startDateStr);
			DateTime endDateTime = new DateTime(endDateStr2);

			// Must use the setDate() method for an all-day event
			// (setDateTime() is used for timed events)
			startEventAllDay = new EventDateTime();
			startEventAllDay.setDate(startDateTime);
			
			endEventAllDay = new EventDateTime();
			endEventAllDay.setDate(endDateTime);
			
			//System.out.println("all day event start: " + startEventAllDay);
			//System.out.println("all day event end: " + endEventAllDay);
			
			
		} catch (ParseException ex) {
			System.out.println("Exception " + ex);
		}
	}

	public String addEventToCalendar(Calendar service, String eventSummary, String eventDescription, String calendarID, String eventStartDate, String eventStartTime, String eventEndDate, String eventEndTime)  {
		//System.out.println("Executing addEventToCalendar:"); // For debugging
		
		Event event = new Event();
		
		if (isAllDayEvent(eventStartTime, eventEndTime)) {
			setEventAllDay(eventStartDate,eventEndDate);
			event.setSummary(eventSummary);
			event.setDescription(eventDescription);
			event.setStart(startEventAllDay);
			event.setEnd(endEventAllDay);
			//System.out.println("all day event start: " + startEventAllDay);
			//System.out.println("all day event end: " + endEventAllDay);
		} 
		else {
			setEventNormal(eventStartDate, eventStartTime, eventEndDate, eventEndTime);
			event.setSummary(eventSummary);
			event.setDescription(eventDescription);
			event.setStart(startEventDateTime);
			event.setEnd(endEventDateTime);
		}
		// Create event object, execute the insertion of this event into the google calendar
		Event createdEvent = null;
		try {
			createdEvent = service.events().insert(calendarID, event).execute();
		} catch (IOException unableToCreateEvent) {
			System.err.println("Unable to create event in Google Calendar");
		}
		
		System.out.println("Added to Google Calendar: " + createdEvent.getId());
		System.out.println("Event added!");
		
		return createdEvent.getId();
	}

	
	
	

	public void deleteEventFromCalendar(Calendar service, String calendarId, String eventId)  {
		try {
			service.events().delete(calendarId,eventId).execute();
			System.out.println("Deleted from Google Calendar: " + eventId);
			System.out.println("Event deleted!");
		} catch (IOException unableToDeleteEvent) {
			System.err.println("Unable to delete event from Google Calendar");
		}
	}
	
	
	public String updateEvent(Calendar service, String eventSummary, String eventDescription, String calendarID, String gooCalEventID, String eventStartDate, String eventStartTime, String eventEndDate, String eventEndTime)  {
		//System.out.println("Executing addEventToCalendar:"); // For debugging
		
		Event event = new Event();
		
		if (isAllDayEvent(eventStartTime, eventEndTime)) {
			setEventAllDay(eventStartDate,eventEndDate);
			event.setSummary(eventSummary);
			event.setDescription(eventDescription);
			event.setStart(startEventAllDay);
			event.setEnd(endEventAllDay);
		} 
		else {
			setEventNormal(eventStartDate, eventStartTime, eventEndDate, eventEndTime);
			event.setSummary(eventSummary);
			event.setDescription(eventDescription);
			event.setStart(startEventDateTime);
			event.setEnd(endEventDateTime);
		}

		
		
		// Create event object, execute the insertion of this event into the google calendar
		Event updatedEvent = null;
		try {
			updatedEvent = service.events().update(calendarID, gooCalEventID, event).execute();
		} catch (IOException unableToCreateEvent) {
			System.err.println("Unable to update event in Google Calendar");
		}
		
		System.out.println("Updated event in Google Calendar: " + gooCalEventID);
		System.out.println("Event updated!");
		return updatedEvent.getId();
	}

	
	
	private static Calendar createCalendar(String token) throws IOException {
		//System.out.println("Executing createCalendar:"); // For debugging
	
		
		// Two globals that will be used in each step.
		HttpTransport httpTransport = new NetHttpTransport();
		JsonFactory jsonFactory = new JacksonFactory();

		// Create the authorization code flow manager
		Set<String> scope = Collections.singleton(CalendarScopes.CALENDAR);
		String clientId = "369332843116-gr8ct1guerlf1fudpgivfjv43h0oleip.apps.googleusercontent.com";
		String clientSecret = "ISvEBCzHT-jheksy-kO-oBvs";

		AuthorizationCodeFlow.Builder codeFlowBuilder = new GoogleAuthorizationCodeFlow.Builder(httpTransport, jsonFactory, clientId, clientSecret, scope);

		AuthorizationCodeFlow codeFlow = codeFlowBuilder.build();
		TokenResponse existingToken = new TokenResponse();

		// Read existing token from file
		existingToken.setAccessToken(token);

		existingToken.setExpiresInSeconds(null);
		String userId = USER_ID;  // CURRENTLY HARDCODED 
		Credential credential = codeFlow.createAndStoreCredential(existingToken, userId);

		// Credentials may be used to initialize http requests
		HttpRequestInitializer initializer = credential;

		// and thus are used to initialize the calendar service
		Calendar.Builder serviceBuilder = new Calendar.Builder(httpTransport,jsonFactory, initializer);
		serviceBuilder.setApplicationName("GooCal");
		Calendar calendar = serviceBuilder.build();
		
		
		return calendar;
		
		
	}

	
	
	
	public static String generateNewTokenStep1()  {

		//System.out.println("Executing generateNewToken:"); // For debugging
		
		// Two globals that will be used in each step.
		HttpTransport httpTransport = new NetHttpTransport();
		JsonFactory jsonFactory = new JacksonFactory();

		// Create the authorization code flow manager
		Set<String> scope = Collections.singleton(CalendarScopes.CALENDAR);
		String clientId = "369332843116-gr8ct1guerlf1fudpgivfjv43h0oleip.apps.googleusercontent.com";
		String clientSecret = "ISvEBCzHT-jheksy-kO-oBvs";

		// Use a factory pattern to create the code flow
		AuthorizationCodeFlow.Builder codeFlowBuilder = new GoogleAuthorizationCodeFlow.Builder(
				httpTransport, jsonFactory, clientId, clientSecret, scope);
		codeFlow = codeFlowBuilder.build();

		// set the code flow to use a dummy user
		// in a servlet, this could be the session id
		String userId = "ipeech";

		// "redirect" to the authentication url
		redirectUri = "urn:ietf:wg:oauth:2.0:oob";
		AuthorizationCodeRequestUrl authorizationUrl = codeFlow
				.newAuthorizationUrl();
		authorizationUrl.setRedirectUri(redirectUri);
		System.out.println("Error: Google Calendar Authorization Token has expired.");
		System.out.println("Generating new token.");
		System.out.println("Go to the following address, copy and paste the code into this program, and enter:");
		System.out.println(authorizationUrl);
		return authorizationUrl.toString();
		
	}
	
	public static String generateNewTokenStep2(String userInput)  {
	
		//new
		//AWTgui.setGoogleCalDisplay(authorizationUrl.toString());
		

		// use the code that is returned as a url parameter
		// to request an authorization token
		// System.out.println("What is the 'code' url parameter?");
		String code = userInput;
		//NEW
		//String code = input;

		AuthorizationCodeTokenRequest tokenRequest = codeFlow
				.newTokenRequest(code);

		tokenRequest.setRedirectUri(redirectUri);
		TokenResponse tokenResponse = null;
		try {
			tokenResponse = tokenRequest.execute();
		} catch (IOException tokenRequestFailed) {
			System.err.println("Token request failed");
		}
		System.out.println(tokenResponse.getAccessToken());
		addToDb(tokenResponse.getAccessToken());
		
		return tokenResponse.getAccessToken();
	}
	
	
	
	
	
	public static String generateNewToken()  {
		//System.out.println("Executing generateNewToken:"); // For debugging
		
		// Two globals that will be used in each step.
		HttpTransport httpTransport = new NetHttpTransport();
		JsonFactory jsonFactory = new JacksonFactory();

		// Create the authorization code flow manager
		Set<String> scope = Collections.singleton(CalendarScopes.CALENDAR);
		String clientId = "369332843116-gr8ct1guerlf1fudpgivfjv43h0oleip.apps.googleusercontent.com";
		String clientSecret = "ISvEBCzHT-jheksy-kO-oBvs";

		// Use a factory pattern to create the code flow
		AuthorizationCodeFlow.Builder codeFlowBuilder = new GoogleAuthorizationCodeFlow.Builder(
				httpTransport, jsonFactory, clientId, clientSecret, scope);
		AuthorizationCodeFlow codeFlow = codeFlowBuilder.build();

		// set the code flow to use a dummy user
		// in a servlet, this could be the session id
		String userId = "ipeech";

		// "redirect" to the authentication url
		String redirectUri = "urn:ietf:wg:oauth:2.0:oob";
		AuthorizationCodeRequestUrl authorizationUrl = codeFlow
				.newAuthorizationUrl();
		authorizationUrl.setRedirectUri(redirectUri);
		System.out.println("Error: Google Calendar Authorization Token has expired.");
		System.out.println("Generating new token.");
		System.out.println("Go to the following address, copy and paste the code into this program, and enter:");
		System.out.println(authorizationUrl);
		//new
		//AWTgui.setGoogleCalDisplay(authorizationUrl.toString());
		

		// use the code that is returned as a url parameter
		// to request an authorization token
		// System.out.println("What is the 'code' url parameter?");
		String code = new Scanner(System.in).nextLine();
		//NEW
		//String code = input;

		AuthorizationCodeTokenRequest tokenRequest = codeFlow
				.newTokenRequest(code);

		tokenRequest.setRedirectUri(redirectUri);
		TokenResponse tokenResponse = null;
		try {
			tokenResponse = tokenRequest.execute();
		} catch (IOException tokenRequestFailed) {
			System.err.println("Token request failed");
		}
		System.out.println(tokenResponse.getAccessToken());
		addToDb(tokenResponse.getAccessToken());
		
		return tokenResponse.getAccessToken();
	}
	
	

	private static boolean isTokenDbEmpty() {
		//System.out.println("Executing isTokenDbEmpty:"); // For debugging
		
		
		// System.out.println("Inside the db: " + readDb());

		if (readDb().equals("")) {
			return true;
		} else {
			return false;
		}
	}

	private static void addToDb(String accessToken) {
		//System.out.println("Executing addToDb:"); // For debugging
		
		
		try {
			FileOutputStream fout = new FileOutputStream("GoogleCalAuthenticationToken");
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(accessToken);
			oos.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static void clearDb() {
		//System.out.println("Executing clearDb:"); // For debugging
		
		
		try {
			FileOutputStream fout = new FileOutputStream("GoogleCalAuthenticationToken");
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject("");
			oos.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static String readDb() {
		//System.out.println("Executing readDb:"); // For debugging
		
		
		String accessToken;
		try {
			FileInputStream fin = new FileInputStream("GoogleCalAuthenticationToken");
			ObjectInputStream ois = new ObjectInputStream(fin);
			accessToken = (String) ois.readObject();
			ois.close();
			return accessToken;

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	
	private static void checkCalendar(Calendar calendar) throws IOException {
		//System.out.println("Executing checkCalendar:"); // For debugging
		
		
		Calendar.CalendarList.List listRequest = calendar.calendarList().list();
		CalendarList feed = listRequest.execute();
	}
}
