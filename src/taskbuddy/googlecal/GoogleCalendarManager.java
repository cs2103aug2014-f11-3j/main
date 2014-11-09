//@author A0108411W
package taskbuddy.googlecal;

import taskbuddy.logic.Task;
import java.net.UnknownHostException;
import com.google.api.services.calendar.Calendar;


/**
 * Acts as a frontend API to be called by classes of other packages - 
 * Mainly for Database to initiate communication with Google Calendar Forward Syncing.
 * 
 * Handles frontend calling of the CRUD (Create, Retrieve, Update and Delete) features 
 * when performing forward syncing  - from the local database
 * to the Google Calendar Servers. 
 * 
 * This class acts as the Facade class to the Backend class, GoogleCalBackend.java, and follows the 
 * implementations of the Facade pattern taught in CS2103. 
 *
 */

public class GoogleCalendarManager {

    private static final String USER_OFFLINE_ERROR = "User is offline";
    private static final String AUTHORIZATION_EXPIRED_ERROR = "Authorization has expired";
    private static final String MISSING_TITLE_ERROR = "Title is missing from this task object";
    private static final String MISSING_GOOGLE_ID_ERROR = "Google ID is missing from this task object";
    
	GooCalBackend gooCalBackend = new GooCalBackend(); 
	GoogleCalendarAuthorizerStatus googleCalendarAuthorizer = new GoogleCalendarAuthorizerStatus();
	GoogleCalendarBackwardSyncCommandCreator googleCalendarBackwardSyncCommandCreator = new GoogleCalendarBackwardSyncCommandCreator();
	
	public void add(Task task) throws UnknownHostException  {
		String gooCalEventID;
		// Assertion Tests
		assert (task.getTitle() != null): MISSING_TITLE_ERROR;

		// Check user online status, throws exception if offline
		if (!googleCalendarAuthorizer.isUserOnline()) {
			throw new UnknownHostException(USER_OFFLINE_ERROR); 
		}
		
		// Check calendar authorization status, throws exception if unauthorized/expired
		else if (!googleCalendarAuthorizer.isAuthenticationValid()) {
			throw new UnknownHostException(AUTHORIZATION_EXPIRED_ERROR);
		}
		
		// Google Calendar Services working
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
				
		GooCalBackend gooCalBackend = new GooCalBackend(); 
		
		// Check user online status, throws exception if offline
		if (!googleCalendarAuthorizer.isUserOnline()) {
			throw new UnknownHostException(USER_OFFLINE_ERROR); 
		}
		
		// Check calendar authorization status, throws exception if unauthorized/expired
		else if (!googleCalendarAuthorizer.isAuthenticationValid()) {
			throw new UnknownHostException(AUTHORIZATION_EXPIRED_ERROR);
		}
		
		// Google Calendar Services working
		else {
			Calendar service = googleCalendarAuthorizer.getCalendar();
			gooCalBackend.deleteEventFromCalendar(service, eventId);
		}
	}

	public String retrieve(String eventId) throws UnknownHostException {
		// Check user online status, throws exception if offline
		if (!googleCalendarAuthorizer.isUserOnline()) {
			throw new UnknownHostException(USER_OFFLINE_ERROR); 
		}
		
		// Check calendar authorization status, throws exception if unauthorized/expired
		else if (!googleCalendarAuthorizer.isAuthenticationValid()) {
			throw new UnknownHostException(AUTHORIZATION_EXPIRED_ERROR);
		}
		
		// Google Calendar Services working
		else {
			Calendar service = googleCalendarAuthorizer.getCalendar();
			return gooCalBackend.retrieveEvent(service, eventId);
		}
	}

	public void update(Task task) throws UnknownHostException  {
		// Assertion Tests
		assert (task.getGID() != null): MISSING_GOOGLE_ID_ERROR;
		assert (task.getTitle() != null): MISSING_TITLE_ERROR;
				
		GooCalBackend gooCalBackend = new GooCalBackend(); 

		// Check user online status, throws exception if offline
		if (!googleCalendarAuthorizer.isUserOnline()) {
			throw new UnknownHostException(USER_OFFLINE_ERROR); 
		}
		
		// Check calendar authorization status, throws exception if unauthorized/expired
		else if (!googleCalendarAuthorizer.isAuthenticationValid()) {
			throw new UnknownHostException(AUTHORIZATION_EXPIRED_ERROR);
		}
		
		// Google Calendar Services working
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
		}
	}
	
	public void executeBackwardSync() throws UnknownHostException {
		googleCalendarBackwardSyncCommandCreator.executeBackwardSync();
	}
	
	public String getSummary(Task task) {
		return task.getTitle();
	}
}