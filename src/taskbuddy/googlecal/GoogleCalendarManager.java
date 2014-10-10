package taskbuddy.googlecal;
import taskbuddy.logic.Task;
import taskbuddy.logic.Bundle;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.UnknownHostException;
import java.net.Socket;
//import java.io.StringWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
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
//import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
//import com.google.api.client.json.JsonGenerator;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;



/**
 * Communicates with Google Calendar Servers
 * Creates a calendar object with the existing authentication token. If authentication token is invalid, generates new token.
 * Receives calls from database. Task object passed from database.
 * Parses task title, task start date, task start time, task end date, task end time
 * Add method: Posts these tasks as Google Calendar Events onto google calendar
 * Stores Google Calendar Event ID into task object
 * Delete method: Deletes event from Google Calendar based on event ID
 * 
 * @author Pee Choon Hian 
 *
 */

public class GoogleCalendarManager {

	// Strings which are currently hardcoded
    private static final String CALENDAR_ID = "i357fqqhffrf1fa9udcbn9sikc@group.calendar.google.com";
    private static final String USER_ID = "ipeech";
    
    
    
	public static void main(String[] args) throws IOException {
		Task task1 = new Task ("Task number 1");
		Task task2 = new Task ("Task number 2");
		Task task3 = new Task ("Task number 3");
		Task task4 = new Task ("Task number 4");
		Task task5 = new Task ("Task number 5");
		Task task6 = new Task ("Task number 6");
		
		task1.setStartTime("padding value");
		task1.setEndTime("padding value", "padding value");
		
		task2.setStartTime("padding value");
		task2.setEndTime("padding value", "padding value");
		
		task3.setStartTime("padding value");
		task3.setEndTime("padding value", "padding value");
	
		task4.setStartTime("padding value");
		task4.setEndTime("padding value", "padding value");
		
		task5.setStartTime("padding value");
		task5.setEndTime("padding value", "padding value");
		
		task6.setStartTime("padding value");
		task6.setEndTime("padding value", "padding value");
		
		
		
		add(task1);
		System.out.println("Executing add:"); // For debugging
		add(task2);
		System.out.println("Executing add:"); // For debugging
		
		add(task3);
		System.out.println("Executing add:"); // For debugging
		add(task4);
		System.out.println("Executing add:"); // For debugging
		
		add(task5);
		System.out.println("Executing add:"); // For debugging
		add(task6);
		System.out.println("Executing add:"); // For debugging
		
		delete(task1.getGID());
		System.out.println("Executing delete:"); // For debugging
		delete(task5.getGID());
		System.out.println("Executing delete:"); // For debugging
		
		System.out.println("Completed");
	}
	
	
	
	public static Bundle add(Task task) throws IOException {
		// Adds task to Google Calendar
		// Returns true if task has successfully been added to Google Calendar
		// Returns false if task has not been successfully added to Google Calendar (Eg: When user is offline)
		Calendar service = null;
		String calendarID = CALENDAR_ID;   // CURRENTLY HARDCODED
		String gooCalEventID;
		
		// Bundle Objects
		Bundle success = new Bundle();
		Bundle failureNoInternet = new Bundle();
		Bundle failureUnableToConnectToGoogle = new Bundle();
		Bundle failureEventFailedToCreate = new Bundle();
		success.putString("Success", "Event has been successfully added to Google Calendar.");
		failureNoInternet.putString("Failure", "User is offline.");
		failureUnableToConnectToGoogle.putString("Failure", "Unable to connect to Google.");
		failureEventFailedToCreate.putString("Failure", "Event failed to be created.");
		
		
		// First, check user online status.
		if (!isUserOnline()) {
			return failureNoInternet;
		}
		else {
			// This try catch blocks checks if Google's servers can be read
			try {
				service = initializeCalendar();
			} catch (UnknownHostException connectionProblem) {
				//System.out.println("Unable to connect to Google");
				return failureUnableToConnectToGoogle;
			}

			String eventSummary = getSummary(task);
			String eventStartDate = getStartDate(task);
			String eventStartTime = getStartTime(task);
			String eventEndDate = getEndDate(task);
			String eventEndTime = getEndTime(task);
			
			gooCalEventID = addEventToCalendar(service, eventSummary, calendarID, eventStartDate, eventStartTime, eventEndDate, eventEndTime);
			System.out.println(gooCalEventID);
			task.setGID(gooCalEventID);
			if (gooCalEventID.equals("")) {
				return failureEventFailedToCreate;
			}
			
			
			System.out.println("Event added!");
			return success;
		}
	}
	
	
	

	
	
	
	public static Bundle delete(String eventId) throws IOException {
		// Deletes task from Google Calendar
		// Returns true if task has successfully been deleted from Google Calendar
		// Returns false if task has not been successfully deleted from Google Calendar (Eg: When user is offline)
		
		
		// Bundle Objects
		Bundle success = new Bundle();
		Bundle failureNoInternet = new Bundle();
		Bundle failureUnableToConnectToGoogle = new Bundle();
		success.putString("Success", "Event has been successfully deleted from Google Calendar.");
		failureNoInternet.putString("Failure", "User is offline.");
		failureUnableToConnectToGoogle.putString("Failure", "Unable to connect to Google.");

		
		Calendar service = null;
		String calendarId = "i357fqqhffrf1fa9udcbn9sikc@group.calendar.google.com";
		//System.out.println(eventId);
		// First, check user online status.
		if (!isUserOnline()) {
			return failureNoInternet;
		}
		else {
			// This try catch blocks checks if Google's servers can be read
			try {
				service = initializeCalendar();
			} catch (UnknownHostException connectionProblem) {
				//System.out.println("Unable to connect to Google");
				return failureUnableToConnectToGoogle;
			}
			service.events().delete(calendarId,eventId).execute();
			System.out.println("Event deleted!");
			return success;
		}
	}

	
	
	public static boolean isUserOnline() throws UnknownHostException, IOException {
		//System.out.println("Executing isUserOnline:"); // For debugging
		
		
		Socket socket = null;
		boolean reachable = false;
		try {
		    socket = new Socket("www.google.com", 80);
		    reachable = true;
		} catch (UnknownHostException e) {
			//System.out.println("User is offline");
			return false;
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
	
	public static Calendar initializeCalendar() throws IOException {
		//System.out.println("Executing initializeCalendar:"); // For debugging
		
		
		Calendar service;

		//clearDb(); // For debugging
		//addToDb("abc"); // To purposely create an invalid code in the database for testing
				
		if (isTokenDbEmpty()) {			
			// Generate new Google Calendar Authentication
			addToDb(generateNewToken());
			service = createCalendar(readDb());		
		}
		
		else {
			try {
				// Check if authentication key is valid. If it is invalid, IOException unauthorized will be caught.
				service = createCalendar(readDb());
				try { 
					checkCalendar(createCalendar(readDb()));
					//getAllCalendarListSummary(createCalendar(readDb()));
				} catch (UnknownHostException connectionProblem) {
					//System.out.println("Unable to connect to Google 1");
					return null;
				}
			} catch (IOException unauthorized) {
				System.out.println("Invalid authentication code");
				clearDb();
				addToDb(generateNewToken());
				service = createCalendar(readDb());
			}
		}
		return service;
	}
	
	
	public static String getSummary(Task task) {
		//System.out.println("Executing getSummary:"); // For debugging
		return task.getTitle();
	}
	
	
	public static String getStartDate(Task task) throws NullPointerException {
		//System.out.println("Executing getStartDate:"); // For debugging
		return task.displayStartDate();
	}
	
	public static String getStartTime(Task task) throws NullPointerException {
		//System.out.println("Executing getStartTime:"); // For debugging
		return task.displayStartTime();
	}
	
	
	public static String getEndDate(Task task) {
		//System.out.println("Executing getEndDate:"); // For debugging
		return task.displayEndDate();
	}
	
	
	public static String getEndTime(Task task) {
		//System.out.println("Executing getEndTime:"); // For debugging
		return task.displayEndTime();
	}
	
	public static String addEventToCalendar(Calendar service, String eventSummary, String calendarID, String eventStartDate, String eventStartTime, String eventEndDate, String eventEndTime) throws IOException {
		//System.out.println("Executing addEventToCalendar:"); // For debugging
		
		Event event = new Event();

		event.setSummary(eventSummary);


		SimpleDateFormat simpleDateFormatAllDay = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

		if (eventStartTime.isEmpty() || eventEndTime.isEmpty()) {
			//System.out.println("CREATE AN ALL-DAY EVENT IF END TIMES ARE LEFT BLANK:");
			try {
				// To parse string into Date object
				Date dateFirst = simpleDateFormatAllDay.parse(eventStartDate);
				Date dateSecond = simpleDateFormatAllDay.parse(eventEndDate);

				// Creates string from date object, string must be in a
				// particular format to create a DateTime object with no
				// time element
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				String startDateStr = dateFormat.format(dateFirst);
				String endDateStr = dateFormat.format(dateSecond);

				// Out of the 6 methods for creating a DateTime object with
				// no time element, only the String version works
				DateTime startDateTime = new DateTime(startDateStr);
				DateTime endDateTime = new DateTime(endDateStr);

				// Must use the setDate() method for an all-day event
				// (setDateTime() is used for timed events)
				EventDateTime startEventDateTime = new EventDateTime().setDate(startDateTime);
				EventDateTime endEventDateTime = new EventDateTime().setDate(endDateTime);
				
				//System.out.println(endDateTime);

				// Set event parameters
				event.setStart(startEventDateTime);
				event.setEnd(endEventDateTime);
			} catch (ParseException ex) {
				System.out.println("Exception " + ex);
			}
		} else {
			//System.out.println("CREATE A NORMAL TIMED EVENT:");
			try {

				// To parse string into Date object
				Date dateFirst = simpleDateFormat.parse(eventStartDate + " " + eventStartTime);
				Date dateSecond = simpleDateFormat.parse(eventEndDate + " " + eventEndTime);

				// Formats Date object according to simpleDateFormat, print.
				//System.out.println("date : " + simpleDateFormat.format(dateFirst));
				//System.out.println("date : " + simpleDateFormat.format(dateSecond));

				// Create DateTime object to add to event object
				DateTime dateTime1 = new DateTime(dateFirst,TimeZone.getTimeZone("UTC"));
				event.setStart(new EventDateTime().setDateTime(dateTime1));
				DateTime dateTime2 = new DateTime(dateSecond,TimeZone.getTimeZone("UTC"));
				event.setEnd(new EventDateTime().setDateTime(dateTime2));
				
				//System.out.println(dateTime1);
				//System.out.println(dateTime2);
				
				
			} catch (ParseException ex) {
				System.out.println("Exception " + ex);
			}
		}
		// Create event object, execute the insertion of this event into the google calendar
		Event createdEvent = service.events().insert(calendarID, event).execute();
		return createdEvent.getId();
	}

	
	
	
	
	public static Calendar createCalendar(String token) throws IOException {
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

	
	
	public static String generateNewToken() throws IOException {
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

		// use the code that is returned as a url parameter
		// to request an authorization token
		// System.out.println("What is the 'code' url parameter?");
		String code = new Scanner(System.in).nextLine();

		AuthorizationCodeTokenRequest tokenRequest = codeFlow
				.newTokenRequest(code);

		tokenRequest.setRedirectUri(redirectUri);
		TokenResponse tokenResponse = tokenRequest.execute();
		System.out.println(tokenResponse.getAccessToken());
		addToDb(tokenResponse.getAccessToken());
		
		return tokenResponse.getAccessToken();
	}
	
	

	public static boolean isTokenDbEmpty() {
		//System.out.println("Executing isTokenDbEmpty:"); // For debugging
		
		
		// System.out.println("Inside the db: " + readDb());

		if (readDb().equals("")) {
			return true;
		} else {
			return false;
		}
	}

	public static void addToDb(String accessToken) {
		//System.out.println("Executing addToDb:"); // For debugging
		
		
		try {
			FileOutputStream fout = new FileOutputStream("GoogleCalAuthenticationToken.txt");
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(accessToken);
			oos.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void clearDb() {
		//System.out.println("Executing clearDb:"); // For debugging
		
		
		try {
			FileOutputStream fout = new FileOutputStream("GoogleCalAuthenticationToken.txt");
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
			FileInputStream fin = new FileInputStream("GoogleCalAuthenticationToken.txt");
			ObjectInputStream ois = new ObjectInputStream(fin);
			accessToken = (String) ois.readObject();
			ois.close();
			return accessToken;

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	
	public static void checkCalendar(Calendar calendar) throws IOException {
		//System.out.println("Executing checkCalendar:"); // For debugging
		
		
		Calendar.CalendarList.List listRequest = calendar.calendarList().list();
		CalendarList feed = listRequest.execute();
	}
}

// https://developers.google.com/resources/api-libraries/documentation/calendar/v3/java/latest/com/google/api/services/calendar/model/EventDateTime.html