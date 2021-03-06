//@author A0108411W
package taskbuddy.googlecal;


import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;


/**
 * 
 * Performs all back-end functionality for 
 * communicating with Google Calendar Servers
 * Handles all the CRUD (Create, Update and Delete) features 
 * when performing forward syncing  - from the local database
 * to the Google Calendar Servers. 
 *
 */




public class GooCalBackend {
    // These are the "magic numbers" of the Google Calendar event colour code IDs
	private static final String RED_COLOR = "11";
    private static final String YELLOW_COLOR = "5";
    private static final String GREEN_COLOR = "10";
    private static final String GREY_COLOR = "8";
    
    
    private static EventDateTime startEventAllDay;
    private static EventDateTime endEventAllDay;
    private static EventDateTime startEventDateTime;
    private static EventDateTime endEventDateTime;
    
    static AuthorizationCodeFlow codeFlow;
    static String redirectUri;
    
    GoogleCalendarPreferenceLogger googleCalendarPreferenceLogger = new GoogleCalendarPreferenceLogger();
    
   
	public boolean isAllDayEvent(String eventStartTime, String eventEndTime) {
		if (eventStartTime.equals("00:00") && eventEndTime.equals("23:59")) {
			return true;
		}
		else {
			return false;
		}
	}
	
	
	public void setEventNormal(String eventStartDate, String eventStartTime, String eventEndDate, String eventEndTime) {
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

			DateTime startDateTime = new DateTime(startDateStr);
			DateTime endDateTime = new DateTime(endDateStr2);

			startEventAllDay = new EventDateTime();
			startEventAllDay.setDate(startDateTime);
			
			endEventAllDay = new EventDateTime();
			endEventAllDay.setDate(endDateTime);
			
		} catch (ParseException ex) {
			System.err.println("A parsing exception has occurred for an all-day event" + ex);
		}
	}

	
	public String addEventToCalendar(Calendar service, String eventSummary, String eventDescription, String eventStartDate, String eventStartTime, String eventEndDate, String eventEndTime, int eventPriority)  {
		String calendarID = googleCalendarPreferenceLogger.readAddressFile();
		Event event = new Event();
		event = setEventVariables(event, eventSummary, eventDescription, eventStartDate, eventStartTime, eventEndDate, eventEndTime, eventPriority);

		// Create event object, execute the insertion of this event into the google calendar
		Event createdEvent = null;
		try {
			createdEvent = service.events().insert(calendarID, event).execute();
		} catch (IOException unableToCreateEvent) {
			System.err.println("Unable to create event in Google Calendar");
		}
		return createdEvent.getId();
	}

	public String retrieveEvent(Calendar service, String eventId)  {
		String calendarId = googleCalendarPreferenceLogger.readAddressFile();
		Event event = null;
		try {
			event = service.events().get(calendarId, eventId).execute();
		} catch (IOException unableToRetrieve) {
			System.err.println("Unable to retrieve event");
		}
		return event.getSummary();
	}
	

	public void deleteEventFromCalendar(Calendar service, String eventId)  {
		String calendarId = googleCalendarPreferenceLogger.readAddressFile();
		try {
			service.events().delete(calendarId,eventId).execute();
			System.out.println("Deleted from Google Calendar: " + eventId);
			System.out.println("Event deleted!");
		} catch (IOException unableToDeleteEvent) {
			System.err.println("Unable to delete event from Google Calendar");
		}
	}
	
	public String updateEvent(Calendar service, String eventSummary, String eventDescription, String gooCalEventID, String eventStartDate, String eventStartTime, String eventEndDate, String eventEndTime, int eventPriority)  {
		//System.out.println("Executing addEventToCalendar:"); // For debugging
		String calendarID = googleCalendarPreferenceLogger.readAddressFile();
		Event event = new Event();
		
		event = setEventVariables(event, eventSummary, eventDescription, eventStartDate, eventStartTime, eventEndDate, eventEndTime, eventPriority);
		
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

	
	
	private String setEventColour(int eventPriority) {
		if (eventPriority == 1) {
			return RED_COLOR;
		}
		else if (eventPriority == 2) {
			return YELLOW_COLOR;
		}
		else if (eventPriority == 3) {
			return GREEN_COLOR;
		}
		else if (eventPriority == 4) {
			return GREY_COLOR;
		}
		return YELLOW_COLOR;
	}

	public Event setEventVariables(Event event, String eventSummary, String eventDescription, String eventStartDate, String eventStartTime, String eventEndDate, String eventEndTime, int eventPriority) {
		event.setColorId(setEventColour(eventPriority));
		
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
		return event;
	}
}
