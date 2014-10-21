package taskbuddy.googlecal;

import taskbuddy.logic.Task;
import taskbuddy.logic.Bundle;

import java.io.IOException;
import java.net.UnknownHostException;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;


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
    private static final String USER_OFFLINE_ERROR = "User is offline";
    private static final String MISSING_TITLE_ERROR = "Title is missing from this task object";
    private static final String MISSING_GOOGLE_ID_ERROR = "Google ID is missing from this task object";
    private static final String MISSING_TASK_ID_ERROR = "Task ID is missing (equals to zero)";
    
   
	public void add(Task task) throws UnknownHostException  {
		// Assertion Tests
		//assert (task.getGID() != null): MISSING_GOOGLE_ID_ERROR;
		assert (task.getTitle() != null): MISSING_TITLE_ERROR;
		// assert (task.getTaskId() != 0): MISSING_TASK_ID_ERROR;  // To be uncommented once database is correctly implemented
		
		
		// Adds task to Google Calendar
		// Returns true if task has successfully been added to Google Calendar
		// Returns false if task has not been successfully added to Google Calendar (Eg: When user is offline)
		Calendar service = null;
		String calendarID = CALENDAR_ID;   // CURRENTLY HARDCODED
		String gooCalEventID;
		GooCalBackend gooCalBackend = new GooCalBackend(); 
		

		
		
		// First, check user online status.
		if (!gooCalBackend.isUserOnline()) {
			throw new UnknownHostException(USER_OFFLINE_ERROR); 
		}
		else {
			// This try catch blocks checks if Google's servers can be read
			try {
				service = gooCalBackend.initializeCalendar();
			} catch (IOException connectionProblem) {
				// This catach statement cataches a connetion problem
				// Exception is only caught when authentication code is valid, yet tbere is a failure in initializing the Google Calendar
				//return failureUnableToConnectToGoogle;
			}

			String eventSummary = getSummary(task);
			String eventStartDate = getStartDate(task);
			String eventStartTime = getStartTime(task);
			String eventEndDate = getEndDate(task);
			String eventEndTime = getEndTime(task);
			
			gooCalEventID = gooCalBackend.addEventToCalendar(service, eventSummary, calendarID, eventStartDate, eventStartTime, eventEndDate, eventEndTime);
			
			task.setGID(gooCalEventID);			
			
			

		}
	}
	
	
	

	
	
	
	public void delete(String eventId) throws UnknownHostException  {
		// Assert Tests
		assert (!eventId.equals("Google Calendar ID:")): MISSING_GOOGLE_ID_ERROR;
				
		
		// Deletes task from Google Calendar
		// Returns true if task has successfully been deleted from Google Calendar
		// Returns false if task has not been successfully deleted from Google Calendar (Eg: When user is offline)
		GooCalBackend gooCalBackend = new GooCalBackend(); 
		
		
		Calendar service = null;
		String calendarId = CALENDAR_ID;
		//System.out.println(eventId);
		// First, check user online status.
		if (!gooCalBackend.isUserOnline()) {
			throw new UnknownHostException(USER_OFFLINE_ERROR); 
		}
		else {
			// This try catch blocks checks if Google's servers can be read
			try {
				service = gooCalBackend.initializeCalendar();
			} catch (IOException connectionProblem) {
				// This catach statement cataches a connetion problem
				// Exception is only caught when authentication code is valid, yet tbere is a failure in initializing the Google Calendar
				//return failureUnableToConnectToGoogle;
			}
			
			gooCalBackend.deleteEventFromCalendar(service, calendarId, eventId);
			

		}
	}

	
	
	public String retrieve(String eventId) {
		// This is a stub
		
		// Retrieves events from Google Calendar
		// Returns a task object corresponding to the retrieval of the event from Google Calendar
		
		GooCalBackend gooCalBackend = new GooCalBackend(); 
		

		
		Calendar service = null;
		String calendarId = CALENDAR_ID;
		//System.out.println(eventId);
		// First, check user online status.
		
		if (!gooCalBackend.isUserOnline()) {
			return "failureNoInternet";
		}
		else {
			// This try catch blocks checks if Google's servers can be read
			try {
				service = gooCalBackend.initializeCalendar();
			} catch (IOException connectionProblem) {
				// This catach statement cataches a connetion problem
				// Exception is only caught when authentication code is valid, yet tbere is a failure in initializing the Google Calendar
			}
			
			
			Event event = null;
			try {
				event = service.events().get(calendarId, eventId).execute();
			} catch (IOException unableToRetrieve) {
				System.err.println("Unable to retrieve event");
			}
			System.out.println(event.getSummary());
			return event.getSummary();
		}
	}

	public void update(Task task) throws UnknownHostException {
		// Assertion Tests
		assert (task.getGID() != null): MISSING_GOOGLE_ID_ERROR;
		assert (task.getTitle() != null): MISSING_TITLE_ERROR;
		// assert (task.getTaskId() != 0): MISSING_TASK_ID_ERROR;  // To be uncommented once database is correctly implemented
				
				
		// Updates an event already present on Google Calendar
		// Returns true if task has successfully been added to Google Calendar
		// Returns false if task has not been successfully added to Google Calendar (Eg: When user is offline)
		Calendar service = null;
		String calendarID = CALENDAR_ID;   // CURRENTLY HARDCODED
		
		GooCalBackend gooCalBackend = new GooCalBackend(); 

		// First, check user online status.
		if (!gooCalBackend.isUserOnline()) {
			throw new UnknownHostException(USER_OFFLINE_ERROR); 
		}
		else {
			// This try catch blocks checks if Google's servers can be read
			try {
				service = gooCalBackend.initializeCalendar();
			} catch (IOException connectionProblem) {
				// This catach statement cataches a connetion problem
				// Exception is only caught when authentication code is valid, yet tbere is a failure in initializing the Google Calendar
				//return failureUnableToConnectToGoogle;
			}

			String eventSummary = getSummary(task);
			String eventStartDate = getStartDate(task);
			String eventStartTime = getStartTime(task);
			String eventEndDate = getEndDate(task);
			String eventEndTime = getEndTime(task);
			String gooCalEventID = getGooCalEventID(task);

			gooCalBackend.updateEvent(service, eventSummary, calendarID, gooCalEventID, eventStartDate, eventStartTime, eventEndDate, eventEndTime);
			//System.out.println(gooCalEventID);
			//task.setGID(gooCalEventID);			
			


		}



	}
	

	
	public String getSummary(Task task) {
		//System.out.println("Executing getSummary:"); // For debugging
		return task.getTitle();
	}
	
	
	public String getStartDate(Task task) throws NullPointerException {
		//System.out.println("Executing getStartDate:"); // For debugging
		//System.out.println(task.displayStartDate());
		return task.displayStartDate();
	}
	
	public String getStartTime(Task task) throws NullPointerException {
		//System.out.println("Executing getStartTime:"); // For debugging
		//System.out.println(task.displayStartTime());
		return task.displayStartTime();
	}
	
	
	public String getEndDate(Task task) {
		//System.out.println("Executing getEndDate:"); // For debugging
		//System.out.println(task.displayEndDate());
		return task.displayEndDate();
	}
	
	
	public String getEndTime(Task task) {
		//System.out.println("Executing getEndTime:"); // For debugging
		//System.out.println(task.displayEndTime());
		return task.displayEndTime();
		
	}
	
	public String getGooCalEventID(Task task) {
		//System.out.println("Executing getEndTime:"); // For debugging
		//System.out.println(task.displayEndTime());
		return task.getGID();
		
	}
	
	/*
	public static void main(String[] args) throws IOException {
		java.util.Calendar calendarInstance = java.util.Calendar.getInstance();
		GoogleCalendarManager goocal = new GoogleCalendarManager ();
		
		Task task1 = new Task ("Task number 1");
		Task task2 = new Task ("Task number 2");
		Task task3 = new Task ("Task number 3");
		Task task4 = new Task ("Task number 4");
		Task task5 = new Task ("Task number 5");
		Task task6 = new Task ("Task number 6");
		
		task1.setStartTime(calendarInstance);
		task1.setEndTime("padding value", "padding value");
		
		task2.setStartTime(calendarInstance);
		task2.setEndTime("padding value", "padding value");
		
		task3.setStartTime(calendarInstance);
		task3.setEndTime("padding value", "padding value");
	
		task4.setStartTime(calendarInstance);
		task4.setEndTime("padding value", "padding value");
		
		task5.setStartTime(calendarInstance);
		task5.setEndTime("padding value", "padding value");
		
		task6.setStartTime(calendarInstance);
		task6.setEndTime("padding value", "padding value");
		
		
		
		goocal.add(task1);
		System.out.println("Executing add:"); // For debugging
		goocal.add(task2);
		System.out.println("Executing add:"); // For debugging
		
		goocal.add(task3);
		System.out.println("Executing add:"); // For debugging
		goocal.add(task4);
		System.out.println("Executing add:"); // For debugging
		
		goocal.add(task5);
		System.out.println("Executing add:"); // For debugging
		goocal.add(task6);
		System.out.println("Executing add:"); // For debugging
		
		goocal.delete(task1.getGID());
		System.out.println("Executing delete:"); // For debugging
		goocal.delete(task5.getGID());
		System.out.println("Executing delete:"); // For debugging
		
		task4.setTitle("UPDATED!");
		goocal.update(task4);
		
		
		System.out.println("Completed");
	}
	*/
}

// https://developers.google.com/resources/api-libraries/documentation/calendar/v3/java/latest/com/google/api/services/calendar/model/EventDateTime.html