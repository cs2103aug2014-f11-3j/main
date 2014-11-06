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
    //private static final String CALENDAR_ID = "i357fqqhffrf1fa9udcbn9sikc@group.calendar.google.com";
    private static final String USER_OFFLINE_ERROR = "User is offline";
    private static final String AUTHORIZATION_EXPIRED_ERROR = "Authorization has expired";
    private static final String MISSING_TITLE_ERROR = "Title is missing from this task object";
    private static final String MISSING_GOOGLE_ID_ERROR = "Google ID is missing from this task object";
    private static final String MISSING_TASK_ID_ERROR = "Task ID is missing (equals to zero)";
    
	GooCalBackend gooCalBackend = new GooCalBackend(); 
	GoogleCalendarAuthorizer googleCalendarAuthorizer = new GoogleCalendarAuthorizer();
   
	public void add(Task task) throws UnknownHostException  {
		// Assertion Tests
		//assert (task.getGID() != null): MISSING_GOOGLE_ID_ERROR;
		assert (task.getTitle() != null): MISSING_TITLE_ERROR;
		// assert (task.getTaskId() != 0): MISSING_TASK_ID_ERROR;  // To be uncommented once database is correctly implemented
		
		
		// Adds task to Google Calendar
		// Returns true if task has successfully been added to Google Calendar
		// Returns false if task has not been successfully added to Google Calendar (Eg: When user is offline)
		
		//String calendarID = CALENDAR_ID;   // CURRENTLY HARDCODED
		String gooCalEventID;

		// First, check user online status.
		if (!googleCalendarAuthorizer.isUserOnline()) {
			throw new UnknownHostException(USER_OFFLINE_ERROR); 
		}
		
		else if (!googleCalendarAuthorizer.isAuthenticationValid()) {
			throw new UnknownHostException(AUTHORIZATION_EXPIRED_ERROR);
		}
		else {
			Calendar service = googleCalendarAuthorizer.getCalendar();
			String eventSummary = task.getTitle();
			String eventDescription = task.getDescription();
			String eventStartDate = task.displayStartDate();
			String eventStartTime = task.displayStartTime();
			String eventEndDate = task.displayEndDate();
			String eventEndTime = task.displayEndTime();
			int eventPriority = task.getPriority();
			boolean isComplete = task.getCompletionStatus();
			
			if (isComplete) {
				eventPriority = 4;
			}
			
			gooCalEventID = gooCalBackend.addEventToCalendar(service, eventSummary, eventDescription, eventStartDate, eventStartTime, eventEndDate, eventEndTime, eventPriority);
			
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
		
		
		//Calendar service = null;

		//System.out.println(eventId);
		// First, check user online status.
		if (!googleCalendarAuthorizer.isUserOnline()) {
			throw new UnknownHostException(USER_OFFLINE_ERROR); 
		}
		
		else if (!googleCalendarAuthorizer.isAuthenticationValid()) {
			throw new UnknownHostException(AUTHORIZATION_EXPIRED_ERROR);
		}
		else {
			Calendar service = googleCalendarAuthorizer.getCalendar();
		
			gooCalBackend.deleteEventFromCalendar(service,  eventId);
			

		}
	}

	public String retrieve(String eventId) throws UnknownHostException {

		if (!googleCalendarAuthorizer.isUserOnline()) {
			throw new UnknownHostException(USER_OFFLINE_ERROR); 
		}
		
		else if (!googleCalendarAuthorizer.isAuthenticationValid()) {
			throw new UnknownHostException(AUTHORIZATION_EXPIRED_ERROR);
		}
		else {
			Calendar service = googleCalendarAuthorizer.getCalendar();
			return gooCalBackend.retrieveEvent(service, eventId);
		}
	}

	public void update(Task task) throws UnknownHostException  {
		// Assertion Tests
		assert (task.getGID() != null): MISSING_GOOGLE_ID_ERROR;
		assert (task.getTitle() != null): MISSING_TITLE_ERROR;
		// assert (task.getTaskId() != 0): MISSING_TASK_ID_ERROR;  // To be uncommented once database is correctly implemented
				
				
		// Updates an event already present on Google Calendar
		// Returns true if task has successfully been added to Google Calendar
		// Returns false if task has not been successfully added to Google Calendar (Eg: When user is offline)
		//Calendar service = null;

		
		GooCalBackend gooCalBackend = new GooCalBackend(); 

		// First, check user online status.
		if (!googleCalendarAuthorizer.isUserOnline()) {
			throw new UnknownHostException(USER_OFFLINE_ERROR); 
		}
		
		else if (!googleCalendarAuthorizer.isAuthenticationValid()) {
			throw new UnknownHostException(AUTHORIZATION_EXPIRED_ERROR);
		}
		else {
			
			Calendar service = googleCalendarAuthorizer.getCalendar();
			String eventSummary = task.getTitle();
			String eventDescription = task.getDescription();
			String eventStartDate = task.displayStartDate();
			String eventStartTime = task.displayStartTime();
			String eventEndDate = task.displayEndDate();
			String eventEndTime = task.displayEndTime();
			String gooCalEventID = task.getGID();
			int eventPriority = task.getPriority();
			boolean isComplete = task.getCompletionStatus();

			if (isComplete) {
				eventPriority = 4;
			}
			
			
			gooCalBackend.updateEvent(service, eventSummary, eventDescription,  gooCalEventID, eventStartDate, eventStartTime, eventEndDate, eventEndTime, eventPriority);
			//System.out.println(gooCalEventID);
			//task.setGID(gooCalEventID);			
		}



	}
	
	public String getSummary(Task task) {
		//System.out.println("Executing getSummary:"); // For debugging
		return task.getTitle();
	}
	

}

// https://developers.google.com/resources/api-libraries/documentation/calendar/v3/java/latest/com/google/api/services/calendar/model/EventDateTime.html