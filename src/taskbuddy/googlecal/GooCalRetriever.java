package taskbuddy.googlecal;

import java.io.IOException;
import java.net.UnknownHostException;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;

/**
 * 
 * Performs back-end functionality for 
 * communicating with Google Calendar Servers
 * Handles retrieve features. 
 * 
 * @author Pee Choon Hian, A0108411W
 *
 */


public class GooCalRetriever {
  
    private static final String USER_OFFLINE_ERROR = "User is offline";
    private static final String AUTHORIZATION_EXPIRED_ERROR = "Authorization has expired";
    
	private String retrievedSummary;
	private String retrievedDescription;
	private String retrievedStart;
	private String retrievedEnd;
	
	GooCalBackend gooCalBackend = new GooCalBackend();
	GoogleCalendarAuthorizerStatus googleCalendarAuthorizer = new GoogleCalendarAuthorizerStatus();
	GoogleCalendarPreferenceLogger googleCalendarPreferenceLogger = new GoogleCalendarPreferenceLogger();
	
	public void retrieve(String eventId) throws UnknownHostException{
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
			retriever(service, eventId);
		}	
	}
	
	
	public void retriever(Calendar service,  String eventId) {
		Event event = null;
		String calendarId = googleCalendarPreferenceLogger.readAddressFile();
		
		try {
			event = service.events().get(calendarId, eventId).execute();
		} catch (IOException unableToRetrieve) {
			System.err.println("Unable to retrieve event");
		}
		setRetrievedSummary(event.getSummary());
		setRetrievedStart(event.getStart().toString());
		setRetrievedEnd(event.getEnd().toString());
		setRetrievedDescription(event.getDescription());
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
	
	
	private void setRetrievedDescription(String retrievedDescription) {
		this.retrievedDescription = retrievedDescription;
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
	
	public String getRetrievedDescription() {
		System.out.println("Retrieved Description: " + retrievedDescription);
		return retrievedDescription;
	}
}