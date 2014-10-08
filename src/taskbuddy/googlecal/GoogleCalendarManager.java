//Author: Pee Choon Hian (A0108411W)

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






public class GoogleCalendarManager {

	public static void main(String[] args) throws IOException {
		Task task1 = new Task ("Task number 1");
		Task task2 = new Task ("Task number 2");
		add(task1);
		add(task2);
		delete(task1.getGID());
	}
	
	
	
	public static Bundle add(Task task) throws IOException {
		// Adds task to Google Calendar
		// Returns true if task has successfully been added to Google Calendar
		// Returns false if task has not been successfully added to Google Calendar (Eg: When user is offline)
		Calendar service = null;
		String calendarID = "i357fqqhffrf1fa9udcbn9sikc@group.calendar.google.com";
		String gooCalEventID;
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
			
			if (gooCalEventID.equals("")) {
				return failureEventFailedToCreate;
			}
			task.setGID(gooCalEventID);
			return success;
		}
	}
	
	
	

	
	
	
	public static boolean delete(String eventId) throws IOException {
		// Deletes task from Google Calendar
		// Returns true if task has successfully been deleted from Google Calendar
		// Returns false if task has not been successfully deleted from Google Calendar (Eg: When user is offline)
		
		Calendar service = null;
		String calendarId = "i357fqqhffrf1fa9udcbn9sikc@group.calendar.google.com";
		System.out.println(eventId);
		// First, check user online status.
		if (!isUserOnline()) {
			return false;
		}
		else {
			// This try catch blocks checks if Google's servers can be read
			try {
				service = initializeCalendar();
			} catch (UnknownHostException connectionProblem) {
				System.out.println("Unable to connect to Google");
				return false;
			}
			System.out.println(service.events().delete(calendarId,eventId).execute());
			return true;
		}
	}

	
	
	
	public static boolean isUserOnline() throws UnknownHostException, IOException {
		Socket socket = null;
		boolean reachable = false;
		try {
		    socket = new Socket("www.google.com", 80);
		    reachable = true;
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
	
	public static Calendar initializeCalendar() throws IOException {
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
					System.out.println("Unable to connect to Google 1");
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
		return task.getTitle();
	}
	
	
	public static String getStartDate(Task task) throws NullPointerException {
	// This is a stub
		String startDate = "9/10/2014";
		return startDate;
		//return task.toString(task.getStartTime());
	}
	
	public static String getStartTime(Task task) throws NullPointerException {
	// This is a stub
		String startTime = "10:30";
		return startTime;
		//return task.toString(task.getStartTime());
	}
	
	
	public static String getEndDate(Task task) {
		// This is a stub
		String endDate = "9/10/2014";
		return endDate;
	}
	
	
	public static String getEndTime(Task task) {
		// This is a stub
		String endTime = "13:30";
		return endTime;
	}
	
	public static String addEventToCalendar(Calendar service, String eventSummary, String calendarID, String eventStartDate, String eventStartTime, String eventEndDate, String eventEndTime) throws IOException {
		Event event = new Event();

		event.setSummary(eventSummary);


		SimpleDateFormat simpleDateFormatAllDay = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

		if (eventStartTime.isEmpty() || eventEndTime.isEmpty()) {
			System.out.println("CREATE AN ALL-DAY EVENT IF END TIMES ARE LEFT BLANK:");
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

				// Set event parameters
				event.setStart(startEventDateTime);
				event.setEnd(endEventDateTime);
			} catch (ParseException ex) {
				System.out.println("Exception " + ex);
			}
		} else {
			System.out.println("CREATE A NORMAL TIMED EVENT:");
			try {

				// To parse string into Date object
				Date dateFirst = simpleDateFormat.parse(eventStartDate + " " + eventStartTime);
				Date dateSecond = simpleDateFormat.parse(eventEndDate + " " + eventEndTime);

				// Formats Date object according to simpleDateFormat, print.
				System.out.println("date : " + simpleDateFormat.format(dateFirst));
				System.out.println("date : " + simpleDateFormat.format(dateSecond));

				// Create DateTime object to add to event object
				DateTime dateTime1 = new DateTime(dateFirst,TimeZone.getTimeZone("UTC"));
				event.setStart(new EventDateTime().setDateTime(dateTime1));
				DateTime dateTime2 = new DateTime(dateSecond,TimeZone.getTimeZone("UTC"));
				event.setEnd(new EventDateTime().setDateTime(dateTime2));
			} catch (ParseException ex) {
				System.out.println("Exception " + ex);
			}
		}
		// Create event object, execute the insertion of this event into the google calendar
		Event createdEvent = service.events().insert(calendarID, event).execute();
		return createdEvent.getId();
	}

	
	
	
	

	
	
	public static Calendar createCalendar(String token) throws IOException {
		System.out.println("creating calendar");
		
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
		String userId = "ipeech";
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
		// System.out.println("Go to the following address:");
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
	
	

	
	
	
	enum CommandType {
		GET_CALENDARLIST_SUMMARY, GET_ALL_CALENDARLIST_SUMMARY, GET_CALENDAR_SUMMARY, LIST_EVENTS, ADD_EVENT, EXIT, INVALID
	};

	


	
	public static void executeCalendarTasks(Calendar calendar)
			throws IOException {
		while (true) {
			showToUser("Enter a choice: ");
			showToUser("1. GET_CALENDARLIST_SUMMARY");
			showToUser("2. GET_ALL_CALENDARLIST_SUMMARY");
			showToUser("3. GET_CALENDAR_SUMMARY");
			showToUser("4. LIST_EVENTS");
			showToUser("5. ADD_EVENT");
			showToUser("6. EXIT");

			String calendarID = "i357fqqhffrf1fa9udcbn9sikc@group.calendar.google.com";
			Scanner sc = new Scanner(System.in);
			String command = getCommand(sc);
			CommandType commandToExecute = identifyCommand(command, sc);
			executeCommand(commandToExecute, calendarID, calendar);

			/*
			 * getCalendarListSummary(calendarID,calendar);
			 * getAllCalendarListSummary(calendar);
			 * getCalendarSummary(calendarID,calendar); listEvents(calendarID,
			 * calendar); addEventCurrentDateTime(calendarID, calendar);
			 * addEvent(calendarID, calendar);
			 */
		}
	}

	private static String getCommand(Scanner sc) {
		String command = sc.next();
		return command;
	}

	private static CommandType identifyCommand(String command, Scanner sc) {
		switch (command) {
		case "1":
			return CommandType.GET_CALENDARLIST_SUMMARY;
		case "2":
			return CommandType.GET_ALL_CALENDARLIST_SUMMARY;
		case "3":
			return CommandType.GET_CALENDAR_SUMMARY;
		case "4":
			return CommandType.LIST_EVENTS;
		case "5":
			return CommandType.ADD_EVENT;
		case "6":
			return CommandType.EXIT;
		default:
			return CommandType.INVALID;
		}
	}

	private static void executeCommand(CommandType commandToExecute,
			String calendarID, Calendar calendar) throws IOException {
		switch (commandToExecute) {
		case GET_CALENDARLIST_SUMMARY:
			getCalendarListSummary(calendarID, calendar);
			break;
		case GET_ALL_CALENDARLIST_SUMMARY:
			getAllCalendarListSummary(calendar);
			break;
		case GET_CALENDAR_SUMMARY:
			getCalendarSummary(calendarID, calendar);
			break;
		case LIST_EVENTS:
			listEvents(calendarID, calendar);
			break;
		case ADD_EVENT:
			addEvent(calendarID, calendar);
			break;
		case EXIT:
			exitProgram();
		case INVALID:
			showToUser(String.format("Invalid!"));
		}
	}

	public static boolean isTokenDbEmpty() {
		// System.out.println("Inside the db: " + readDb());

		if (readDb().equals("")) {
			return true;
		} else {
			return false;
		}
	}

	public static void addToDb(String accessToken) {
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



/*	public static Calendar authorizeCal() throws IOException {
		
		HttpTransport httpTransport = new NetHttpTransport();
		JsonFactory jsonFactory = new JacksonFactory();

		
		Set<String> scope = Collections.singleton(CalendarScopes.CALENDAR);
		String clientId = "369332843116-gr8ct1guerlf1fudpgivfjv43h0oleip.apps.googleusercontent.com";
		String clientSecret = "ISvEBCzHT-jheksy-kO-oBvs";

		
		AuthorizationCodeFlow.Builder codeFlowBuilder = new GoogleAuthorizationCodeFlow.Builder(
				httpTransport, jsonFactory, clientId, clientSecret, scope);
		AuthorizationCodeFlow codeFlow = codeFlowBuilder.build();

	
		String userId = "ipeech";

	
		String redirectUri = "urn:ietf:wg:oauth:2.0:oob";
		AuthorizationCodeRequestUrl authorizationUrl = codeFlow
				.newAuthorizationUrl();
		authorizationUrl.setRedirectUri(redirectUri);
	
		System.out.println(authorizationUrl);


		String code = new Scanner(System.in).nextLine();

		AuthorizationCodeTokenRequest tokenRequest = codeFlow
				.newTokenRequest(code);

		tokenRequest.setRedirectUri(redirectUri);
		TokenResponse tokenResponse = tokenRequest.execute();
		System.out.println(tokenResponse.getAccessToken());
		addToDb(tokenResponse.getAccessToken());

	
		Credential credential = codeFlow.createAndStoreCredential(
				tokenResponse, userId);


		HttpRequestInitializer initializer = credential;

		
		Calendar.Builder serviceBuilder = new Calendar.Builder(httpTransport,
				jsonFactory, initializer);
		serviceBuilder.setApplicationName("GooCal");
		Calendar calendar = serviceBuilder.build();
		return calendar;
	}
*/
	
	
	/*
	public static Calendar initializeCalWithExistingToken() throws IOException {
		
		HttpTransport httpTransport = new NetHttpTransport();
		JsonFactory jsonFactory = new JacksonFactory();

		
		Set<String> scope = Collections.singleton(CalendarScopes.CALENDAR);
		String clientId = "369332843116-gr8ct1guerlf1fudpgivfjv43h0oleip.apps.googleusercontent.com";
		String clientSecret = "ISvEBCzHT-jheksy-kO-oBvs";

		AuthorizationCodeFlow.Builder codeFlowBuilder = new GoogleAuthorizationCodeFlow.Builder(
				httpTransport, jsonFactory, clientId, clientSecret, scope);

		AuthorizationCodeFlow codeFlow = codeFlowBuilder.build();
		TokenResponse existingToken = new TokenResponse();


		existingToken.setAccessToken(readDb());

		existingToken.setExpiresInSeconds(null);
		String userId = "ipeech";
		Credential credential = codeFlow.createAndStoreCredential(
				existingToken, userId);

	
		HttpRequestInitializer initializer = credential;

	
		Calendar.Builder serviceBuilder = new Calendar.Builder(httpTransport,
				jsonFactory, initializer);
		serviceBuilder.setApplicationName("GooCal");
		Calendar calendar = serviceBuilder.build();

		return calendar;
	}
*/
	
	
	public static void getCalendarListSummary(String calendarID,
			Calendar calendar) throws IOException {
		showToUser("Running getCalendarListSummary");
		showToUser("Calendar ID used: " + calendarID);
		CalendarListEntry calendarListEntry = calendar.calendarList()
				.get(calendarID).execute();
		showToUser(calendarListEntry.getSummary());
		newLine();
		newLine();
	}

	public static void getAllCalendarListSummary(Calendar calendar)
			throws IOException {
		showToUser("Running getAllCalendarListSummary");
		Calendar.CalendarList.List listRequest = calendar.calendarList().list();
		CalendarList feed = listRequest.execute();
		for (CalendarListEntry entry : feed.getItems()) {
			System.out.println("ID: " + entry.getId());
			System.out.println("Summary: " + entry.getSummary());
		}
		newLine();
		newLine();
	}
	
	
	public static void checkCalendar(Calendar calendar) throws IOException {
		Calendar.CalendarList.List listRequest = calendar.calendarList().list();
		CalendarList feed = listRequest.execute();
	}

	public static void getCalendarSummary(String calendarID, Calendar service)
			throws IOException {
		showToUser("Running getCalendarSummary");
		// Calendar calendar = service.calendars().get("primary").execute();

		// Generates primary calendar
		com.google.api.services.calendar.model.Calendar calendar = service
				.calendars().get("primary").execute();
		showToUser("Primary calendar summary:");
		System.out.println(calendar.getSummary());

		// Generates calendar based on calendar ID supplied
		com.google.api.services.calendar.model.Calendar calendar2 = service
				.calendars().get(calendarID).execute();
		showToUser("Calendar ID used: " + calendarID);
		System.out.println(calendar2.getSummary());

		newLine();
		newLine();
	}

	public static void listEvents(String calendarID, Calendar service)
			throws IOException {
		showToUser("Running listEvents:");

		String pageToken = null;
		do {
			Events events = service.events().list(calendarID)
					.setPageToken(pageToken).execute();

			// Generates a list of items obtained from Google Calendar
			List<Event> items = events.getItems();

			// Accesses this list and get information about each event
			for (Event event : items) {
				// Show event summary (title)
				showToUser(event.getSummary());

				// Show event date/time details
				if (!(event.getStart().getDate() == null)) {
					showToUser("This is an all-day event");
					showToUserSameLine("Start date: ");
					System.out.println(event.getStart().getDate());
					showToUserSameLine("End date: ");
					System.out.println(event.getEnd().getDate());
				} else {
					showToUserSameLine("Start date/time: ");
					System.out.println(event.getStart().getDateTime());
					showToUserSameLine("End date/time: ");
					System.out.println(event.getEnd().getDateTime());
				}

				// Shows event description only if description is not null
				if (!(event.getDescription() == null)) {
					showToUser(event.getDescription());
				}

				newLine();
			}
			pageToken = events.getNextPageToken();
		} while (pageToken != null);

		newLine();
		newLine();
	}

	public static void addEventCurrentDateTime(String calendarID,
			Calendar service) throws IOException {
		Event event = new Event();
		event.setSummary("Appointment");
		Date startDate = new Date();
		Date endDate = new Date(startDate.getTime() + 3600000);
		DateTime start = new DateTime(startDate, TimeZone.getTimeZone("UTC"));
		event.setStart(new EventDateTime().setDateTime(start));
		DateTime end = new DateTime(endDate, TimeZone.getTimeZone("UTC"));
		event.setEnd(new EventDateTime().setDateTime(end));
		Event createdEvent = service.events().insert(calendarID, event)
				.execute();
		System.out.println(createdEvent.getId());
	}

	
	
	
	
	public static void addEvent(String calendarID, Calendar service)
			throws IOException {
		while (true) {
			Scanner sc = new Scanner(System.in);
			Event event = new Event();
			showToUserSameLine("Enter title: ");
			event.setSummary(sc.nextLine());
			showToUserSameLine("Enter start date (Format: DD/MM/YYYY): ");
			String s1_date = sc.nextLine();
			showToUserSameLine("Enter start time (Format: HH:MM) (Leave blank of all-day event): ");
			String s1_time = sc.nextLine();
			showToUserSameLine("Enter end date (Format: DD/MM/YYYY): ");
			String s2_date = sc.nextLine();
			showToUserSameLine("Enter end time (Format: HH:MM) (Leave blank of all-day event): ");
			String s2_time = sc.nextLine();

			SimpleDateFormat simpleDateFormatAllDay = new SimpleDateFormat(
					"dd/MM/yyyy");
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"dd/MM/yyyy HH:mm");

			if (s1_time.isEmpty() || s2_time.isEmpty()) {
				System.out
						.println("CREATE AN ALL-DAY EVENT IF END TIMES ARE LEFT BLANK:");
				try {
					// To parse string into Date object
					Date dateFirst = simpleDateFormatAllDay.parse(s1_date);
					Date dateSecond = simpleDateFormatAllDay.parse(s2_date);

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
					EventDateTime startEventDateTime = new EventDateTime()
							.setDate(startDateTime);
					EventDateTime endEventDateTime = new EventDateTime()
							.setDate(endDateTime);

					// Set event parameters
					event.setStart(startEventDateTime);
					event.setEnd(endEventDateTime);
				} catch (ParseException ex) {
					System.out.println("Exception " + ex);
				}
			} else {
				System.out.println("CREATE A NORMAL TIMED EVENT:");
				try {

					// To parse string into Date object
					Date dateFirst = simpleDateFormat.parse(s1_date + " "
							+ s1_time);
					Date dateSecond = simpleDateFormat.parse(s2_date + " "
							+ s2_time);

					// Formats Date object according to simpleDateFormat, print.
					System.out.println("date : "
							+ simpleDateFormat.format(dateFirst));
					System.out.println("date : "
							+ simpleDateFormat.format(dateSecond));

					// Create DateTime object to add to event object
					DateTime dateTime1 = new DateTime(dateFirst,
							TimeZone.getTimeZone("UTC"));
					event.setStart(new EventDateTime().setDateTime(dateTime1));
					DateTime dateTime2 = new DateTime(dateSecond,
							TimeZone.getTimeZone("UTC"));
					event.setEnd(new EventDateTime().setDateTime(dateTime2));
				} catch (ParseException ex) {
					System.out.println("Exception " + ex);
				}
			}
			// Create event object, execute the insertion of this event into the
			// google calendar
			Event createdEvent = service.events().insert(calendarID, event)
					.execute();
			System.out.println("Event ID: " + createdEvent.getId());

			showToUserSameLine("Create another event? Y/N: ");
			String response = sc.next();
			if (response.equalsIgnoreCase("N")) {
				break;
			}
		}
	}

	public static void showToUser(String message) {
		System.out.println(message);
	}

	public static void showToUserSameLine(String message) {
		System.out.print(message);
	}

	public static void newLine() {
		System.out.println();
	}
	
	public static void exitProgram() {
		System.exit(0);
	}
}

// https://developers.google.com/resources/api-libraries/documentation/calendar/v3/java/latest/com/google/api/services/calendar/model/EventDateTime.html