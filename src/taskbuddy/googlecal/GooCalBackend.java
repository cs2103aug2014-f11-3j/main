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

import taskbuddy.logic.Task;

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
    private static final String CALENDAR_ID = "i357fqqhffrf1fa9udcbn9sikc@group.calendar.google.com";
    private static final String USER_ID = "ipeech";
    
    
	public boolean isUserOnline() throws UnknownHostException, IOException {
		//System.out.println("Executing isUserOnline:"); // For debugging
		Socket socket = null;
		try {
		    socket = new Socket("www.google.com", 80);
		} catch (UnknownHostException e) {
			System.out.println("User is offline");
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
	
	public Calendar initializeCalendar() throws IOException {
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
	
	

	public String addEventToCalendar(Calendar service, String eventSummary, String calendarID, String eventStartDate, String eventStartTime, String eventEndDate, String eventEndTime) throws IOException {
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

	
	
	private static String generateNewToken() throws IOException {
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
			FileOutputStream fout = new FileOutputStream("GoogleCalAuthenticationToken.txt");
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

	
	private static void checkCalendar(Calendar calendar) throws IOException {
		//System.out.println("Executing checkCalendar:"); // For debugging
		
		
		Calendar.CalendarList.List listRequest = calendar.calendarList().list();
		CalendarList feed = listRequest.execute();
	}
}
