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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TimeZone;

import sun.security.jca.GetInstance;
import taskbuddy.gui.AWTgui;
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
import com.google.api.services.calendar.model.Events;

/**
 * 
 * This class allows for the conversion of all existing events on Google Calendar into Task objects used in the local database.
 * 
 * @author Pee Choon Hian, A0108411W
 *
 */



public class GoogleCalRetrieveAll {
	private static final String USER_OFFLINE_ERROR = "User is offline";
	private static final String AUTHORIZATION_EXPIRED_ERROR = "Authorization has expired";
	
    private static final String RED_COLOR = "11";
    private static final String YELLOW_COLOR = "5";
    private static final String GREEN_COLOR = "10";
    private static final String GREY_COLOR = "8";
	
	
    private static final ArrayList<Task> tasks = new ArrayList<Task>();
    
	GooCalBackend gooCalBackend = new GooCalBackend(); 
	GoogleCalendarAuthorizerStatus googleCalendarAuthorizer = new GoogleCalendarAuthorizerStatus();
	GoogleCalendarPreferenceLogger googleCalendarPreferenceLogger = new GoogleCalendarPreferenceLogger();
	
	

	public void getListFromGoogle() throws UnknownHostException {
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
			String pageToken = null;
			do {
				Events events = null;
				try {
					events = service.events().list(googleCalendarPreferenceLogger.readAddressFile()).setPageToken(pageToken).execute();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				List<Event> items = events.getItems();
				for (Event event : items) {
					createTaskObject(event);
				}
				pageToken = events.getNextPageToken();
			} while (pageToken != null);	
		}
	}
		
	public void createTaskObject(Event event) {
		Task newTask = new Task();

		EventDateTime eventStart = null;
		EventDateTime eventEnd = null;
		String eventTitle = null;
		String eventDescription = null;
		String eventGCalId = null;
		
		
		eventStart = event.getStart();
		eventEnd = event.getEnd();
		eventTitle = event.getSummary();
		eventDescription = event.getDescription();
		eventGCalId = event.getId();
		

		newTask.setTitle(eventTitle);
		newTask.setGID(eventGCalId);
		newTask.setPriority(getTaskPriorityInteger(event));
		if (getTaskPriorityInteger(event) == 4) {
			newTask.setCompletion(true);
		}
		else if (getTaskPriorityInteger(event) == 2) {
			newTask.setPriority(0);
		}

		if (eventDescription == null ) {
			eventDescription = "padding value";
		}
		newTask.setDescription(eventDescription);
		
		
		
		if (isEventAllDay(eventStart, eventEnd)) {
			Date eventStartDateObject = formatDateAllDay(eventStart.getDate());
			java.util.Calendar eventStartCalendarObject = java.util.Calendar.getInstance();
			eventStartCalendarObject.setTime(eventStartDateObject);
			int yearStart = eventStartCalendarObject.get(java.util.Calendar.YEAR);
			int monthStart = eventStartCalendarObject.get(java.util.Calendar.MONTH);
			int dateStart = eventStartCalendarObject.get(java.util.Calendar.DAY_OF_MONTH);
			eventStartCalendarObject.set(yearStart, monthStart, dateStart, 0, 0, 0);

			Date eventEndDateObject = formatDateAllDay(eventEnd.getDate());
			java.util.Calendar eventEndCalendarObject = java.util.Calendar.getInstance();
			eventEndCalendarObject.setTime(eventEndDateObject);
			int yearEnd = eventEndCalendarObject.get(java.util.Calendar.YEAR);
			int monthEnd = eventEndCalendarObject.get(java.util.Calendar.MONTH);
			int dateEnd = eventEndCalendarObject.get(java.util.Calendar.DAY_OF_MONTH) - 1;
			eventEndCalendarObject.set(yearEnd, monthEnd, dateEnd, 23, 59, 0);

			newTask.setStartTime(eventStartCalendarObject);
			newTask.setEndTime(eventEndCalendarObject);	
	
				newTask.setFloating(false);
			
			
			addTaskToArraylist(newTask);
		}
		else {
			// For normal timed tasks 
			Date eventStartDateObject = formatDateNormal(eventStart.getDateTime());
			java.util.Calendar eventStartCalendarObject = java.util.Calendar.getInstance();
			eventStartCalendarObject.setTime(eventStartDateObject);

			Date eventEndDateObject = formatDateNormal(eventEnd.getDateTime());
			java.util.Calendar eventEndCalendarObject = java.util.Calendar.getInstance();
			eventEndCalendarObject.setTime(eventEndDateObject);

			newTask.setStartTime(eventStartCalendarObject);
			newTask.setEndTime(eventEndCalendarObject);		
			
			if (isEventFloating(eventStart, eventEnd)) {
				newTask.setFloating(true);
			}
			else {
				newTask.setFloating(false);
			}
			
			addTaskToArraylist(newTask);
		}
	}

	public Date formatDateNormal(DateTime date) {
		SimpleDateFormat formatDateNormal = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		try {
			return formatDateNormal.parse(date.toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public Date formatDateAllDay(DateTime date) {
		Date formattedDate = new Date();
		SimpleDateFormat formatDateAllDaySDF = new SimpleDateFormat("yyyy-MM-dd");
		try {
			String dateString = date.toString();
			formattedDate = formatDateAllDaySDF.parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return formattedDate;
	}
	
	public boolean isEventAllDay(EventDateTime eventStart, EventDateTime eventEnd) {
		if (eventStart.getDateTime() == null && eventEnd.getDateTime() == null) {
			return true;
		}
		else if (eventStart.getDate() == null && eventEnd.getDate() == null) {
			return false;
		}
		System.out.println("neither");
		return false;
	}
	
	public boolean isEventFloating(EventDateTime eventStart, EventDateTime eventEnd) {
		if (eventStart.getDateTime().toString().equals(eventEnd.getDateTime().toString())) {
			return true;
		}
		else return false;
	}
	
	public int getTaskPriorityInteger (Event event) {
		String eventColorId = event.getColorId();
		if (!(eventColorId == null)) {
			switch (eventColorId) {
			case RED_COLOR: 
				return 1;
			case YELLOW_COLOR:
				return 2;
			case GREEN_COLOR:
				return 3;
			case GREY_COLOR:
				return 4;
			default: 
				return 2;
			}
		}
		return 2;
	}
		
	public ArrayList<Task> addTaskToArraylist(Task task) {	
		tasks.add(task);
		return tasks;
	}
	
	public void printArrayListOfTasks(ArrayList<Task> tasks) {
		for (Task task : tasks) {
			System.out.println(task.displayTask());
		}
	}
	
	public ArrayList<Task> getTasks() {
		
		return tasks;
	}
}