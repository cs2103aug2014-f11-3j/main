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
//import java.util.List;
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
//import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
//import com.google.api.services.calendar.model.Events;



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
    
    
    
    
//	public static void main(String[] args) throws IOException {
//		Task task1 = new Task ("Task number 1");
//		Task task2 = new Task ("Task number 2");
//		Task task3 = new Task ("Task number 3");
//		Task task4 = new Task ("Task number 4");
//		Task task5 = new Task ("Task number 5");
//		Task task6 = new Task ("Task number 6");
//		
//		task1.setStartTime("padding value");
//		task1.setEndTime("padding value", "padding value");
//		
//		task2.setStartTime("padding value");
//		task2.setEndTime("padding value", "padding value");
//		
//		task3.setStartTime("padding value");
//		task3.setEndTime("padding value", "padding value");
//	
//		task4.setStartTime("padding value");
//		task4.setEndTime("padding value", "padding value");
//		
//		task5.setStartTime("padding value");
//		task5.setEndTime("padding value", "padding value");
//		
//		task6.setStartTime("padding value");
//		task6.setEndTime("padding value", "padding value");
//		
//		
//		
//		add(task1);
//		System.out.println("Executing add:"); // For debugging
//		add(task2);
//		System.out.println("Executing add:"); // For debugging
//		
//		add(task3);
//		System.out.println("Executing add:"); // For debugging
//		add(task4);
//		System.out.println("Executing add:"); // For debugging
//		
//		add(task5);
//		System.out.println("Executing add:"); // For debugging
//		add(task6);
//		System.out.println("Executing add:"); // For debugging
//		
//		delete(task1.getGID());
//		System.out.println("Executing delete:"); // For debugging
//		delete(task5.getGID());
//		System.out.println("Executing delete:"); // For debugging
//		
//		System.out.println("Completed");
//	}
	
	
	
	public Bundle add(Task task) throws IOException {
		// Adds task to Google Calendar
		// Returns true if task has successfully been added to Google Calendar
		// Returns false if task has not been successfully added to Google Calendar (Eg: When user is offline)
		Calendar service = null;
		String calendarID = CALENDAR_ID;   // CURRENTLY HARDCODED
		String gooCalEventID;
		GooCalBackend gooCalBackend = new GooCalBackend(); 
		
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
		if (!gooCalBackend.isUserOnline()) {
			return failureNoInternet;
		}
		else {
			// This try catch blocks checks if Google's servers can be read
			try {
				service = gooCalBackend.initializeCalendar();
			} catch (UnknownHostException connectionProblem) {
				//System.out.println("Unable to connect to Google");
				return failureUnableToConnectToGoogle;
			}

			String eventSummary = getSummary(task);
			String eventStartDate = getStartDate(task);
			String eventStartTime = getStartTime(task);
			String eventEndDate = getEndDate(task);
			String eventEndTime = getEndTime(task);
			
			gooCalEventID = gooCalBackend.addEventToCalendar(service, eventSummary, calendarID, eventStartDate, eventStartTime, eventEndDate, eventEndTime);
			System.out.println(gooCalEventID);
			task.setGID(gooCalEventID);
			if (gooCalEventID.equals("")) {
				return failureEventFailedToCreate;
			}
			
			
			System.out.println("Event added!");
			return success;
		}
	}
	
	
	

	
	
	
	public Bundle delete(String eventId) throws IOException {
		// Deletes task from Google Calendar
		// Returns true if task has successfully been deleted from Google Calendar
		// Returns false if task has not been successfully deleted from Google Calendar (Eg: When user is offline)
		GooCalBackend gooCalBackend = new GooCalBackend(); 
		
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
		if (!gooCalBackend.isUserOnline()) {
			return failureNoInternet;
		}
		else {
			// This try catch blocks checks if Google's servers can be read
			try {
				service = gooCalBackend.initializeCalendar();
			} catch (UnknownHostException connectionProblem) {
				//System.out.println("Unable to connect to Google");
				return failureUnableToConnectToGoogle;
			}
			service.events().delete(calendarId,eventId).execute();
			System.out.println("Event deleted!");
			return success;
		}
	}

	
	
	public String retrieve(String eventId) throws IOException {
		// This is a stub
		
		// Retrieves events from Google Calendar
		// Returns a task object corresponding to the retrieval of the event from Google Calendar
		
		GooCalBackend gooCalBackend = new GooCalBackend(); 
		
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
		
		if (!gooCalBackend.isUserOnline()) {
			return "failureNoInternet";
		}
		else {
			// This try catch blocks checks if Google's servers can be read
			try {
				service = gooCalBackend.initializeCalendar();
			} catch (UnknownHostException connectionProblem) {
				//System.out.println("Unable to connect to Google");
				return "failureUnableToConnectToGoogle";
			}
			
			
			Event event = service.events().get(calendarId, eventId).execute();
			System.out.println(event.getSummary());
			return event.getSummary();
		}
	}

	public Bundle update(String eventId, Task task) {
		// This is a stub
		Bundle success = new Bundle();
		Bundle failureNoInternet = new Bundle();
		Bundle failureUnableToConnectToGoogle = new Bundle();
		
		return failureUnableToConnectToGoogle;
	}
	

	
	private String getSummary(Task task) {
		//System.out.println("Executing getSummary:"); // For debugging
		return task.getTitle();
	}
	
	
	private String getStartDate(Task task) throws NullPointerException {
		//System.out.println("Executing getStartDate:"); // For debugging
		return task.displayStartDate();
	}
	
	private String getStartTime(Task task) throws NullPointerException {
		//System.out.println("Executing getStartTime:"); // For debugging
		return task.displayStartTime();
	}
	
	
	private String getEndDate(Task task) {
		//System.out.println("Executing getEndDate:"); // For debugging
		return task.displayEndDate();
	}
	
	
	private String getEndTime(Task task) {
		//System.out.println("Executing getEndTime:"); // For debugging
		return task.displayEndTime();
	}
	

}

// https://developers.google.com/resources/api-libraries/documentation/calendar/v3/java/latest/com/google/api/services/calendar/model/EventDateTime.html