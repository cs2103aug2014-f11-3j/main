package taskbuddy.googlecal;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

import taskbuddy.logic.Task;
import taskbuddy.logic.Bundle;
import taskbuddy.googlecal.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/*
Instructions for use:
- Go online and run test to pass all test cases.

- To test exceptions, go offline, and run the test. 
- We should observe the error "java.net.UnknownHostException: User is offline". 

- To test assertion (missing task id), comment out the first testAddRetrieve method, and uncomment the second testAddRetrieve method.
- We should observe the error "java.lang.AssertionError: Title is missing from this task object".


*/


public class GoogleCalendarManagerTest {
	Calendar calendarInstance = null;
	Calendar calendarSpecifiedStart = null;
	Calendar calendarSpecifiedEnd = null;
	
	GoogleCalendarManager goocal = new GoogleCalendarManager();
	GooCalRetriever gooCalRetriever = new GooCalRetriever();
	
	
	Task task1Instance = new Task ("Task 1 Instance");
	Task task2Instance = new Task ("Task 2 Instance");
	Task task3SpecifiedString = new Task ("Task 3 Specified String");
	Task task4SpecifiedCalendar = new Task ("Task 4 Specified Calendar");
	Task task5Delete = new Task ("Task 5 Delete");
	Task task6Delete = new Task ("Task 6 Delete");
	Task task7AddRetrieve = new Task ("Task 7 add and retrieve");
	Task task8UpdateSummary = new Task ("Task 8 update summary");
	Task task9UpdateStartEnd = new Task ("Task 9 update start end");
	Task task10AllDayEventOneDay = new Task ("Task 10 Single Day All Day Event");
	Task task11AllDayEventMultipleDays = new Task ("Task 11 Multiple Day All Day Event");
	Task task12UpdateDescription = new Task ("Task 12 Update Description");

	
	public void createInstanceCalendars() {
		calendarInstance = Calendar.getInstance();
	}
	
	
	public void createSpecifiedCalendars() {
		calendarSpecifiedStart = Calendar.getInstance();
		calendarSpecifiedStart.set(2014,9,22,18,30,00);
		calendarSpecifiedEnd = Calendar.getInstance();
		calendarSpecifiedEnd.set(2014,9,22,19,00,00);
	}
	
	
	// There is evidence of boundary value analysis here, as we are testing the different inputs which can be put into the task before the task is added.
	// Main boundary values are "padding value" and a string of the date "22/10/2014" and a string of the time "1830".
	// We also have some tasks havinga description value and some not having any field in description, to see if both types of tasks can be added successfully.
	
	public void setTaskAttributes() {
		createInstanceCalendars();
		createSpecifiedCalendars();
		
		task1Instance.setStartTime(calendarInstance);
		task1Instance.setEndTime("padding value", "padding value");
		task1Instance.setDescription("LALALALALLA DESCRIBE");
		
		task2Instance.setStartTime(calendarInstance);
		task2Instance.setEndTime("padding value", "padding value");
		
		// Task 3 start and end is set using strings 
		task3SpecifiedString.setStartTime("22/10/2014","1800");
		task3SpecifiedString.setEndTime("22/10/2014", "1830");

		// Task 4 start and end is set using Calendar objects
		task4SpecifiedCalendar.setStartTime(calendarSpecifiedStart);
		task4SpecifiedCalendar.setEndTime(calendarSpecifiedEnd);
		
		task5Delete.setStartTime(calendarInstance);
		task5Delete.setEndTime("padding value", "padding value");
		
		task6Delete.setStartTime(calendarSpecifiedStart);
		task6Delete.setEndTime(calendarSpecifiedStart);
		
		task7AddRetrieve.setStartTime("22/10/2014", "1900");
		task7AddRetrieve.setEndTime("22/10/2014", "2000");
		
		task8UpdateSummary.setStartTime(calendarInstance);
		task8UpdateSummary.setEndTime("padding value", "padding value");
		
		task9UpdateStartEnd.setStartTime(calendarInstance);
		task9UpdateStartEnd.setEndTime("padding value", "padding value");
		
		task10AllDayEventOneDay.setStartTime("22/10/2014", "0000");
		task10AllDayEventOneDay.setEndTime("22/10/2014", "2359");
		
		task11AllDayEventMultipleDays.setStartTime("22/10/2014", "0000");
		task11AllDayEventMultipleDays.setEndTime("29/10/2014", "2359");
		
		task12UpdateDescription.setStartTime(calendarInstance);
		task12UpdateDescription.setEndTime("padding value", "padding value");
		
	}
	

	// There is evidence of boundary value analysis here, as we are testing the different inputs which can be put into the task before the task is added.
	// Main boundary values are "padding value" and a string of the date "22/10/2014" and a string of the time "1830".
	// We also have some tasks havinga description value and some not having any field in description, to see if both types of tasks can be added successfully.
	
	
	// Use case testing: User uses add and delete function of taskbuddy
	@Test
	public void testAddDelete() throws UnknownHostException {
		setTaskAttributes();
	
		goocal.add(task1Instance);
		System.out.println("Executing add:"); // For debugging
		goocal.add(task2Instance);
		System.out.println("Executing add:"); // For debugging
		goocal.add(task3SpecifiedString);
		System.out.println("Executing add:"); // For debugging
		goocal.add(task4SpecifiedCalendar);
		System.out.println("Executing add:"); // For debugging
		goocal.add(task5Delete);
		System.out.println("Executing add:"); // For debugging
		goocal.add(task6Delete);
		System.out.println("Executing add:"); // For debugging
		
		goocal.delete(task5Delete.getGID());
		System.out.println("Executing delete:"); // For debugging
		goocal.delete(task6Delete.getGID());
		System.out.println("Executing delete:"); // For debugging
		
		goocal.add(task10AllDayEventOneDay);
		System.out.println("Executing add:");
		goocal.add(task11AllDayEventMultipleDays);
		System.out.println("Executing add:");

	}
	

	@Test
	public void testAddRetrieve() throws UnknownHostException {
		setTaskAttributes();
		String taskTitle = task7AddRetrieve.getTitle();		
		goocal.add(task7AddRetrieve);
		String GID = task7AddRetrieve.getGID();
		gooCalRetriever.retrieve(GID);
		String eventSummary = gooCalRetriever.getRetrievedSummary();
		assertEquals("Faled. Task title did not match event summary of the Google Calendar Event retrieved", taskTitle, eventSummary);	
	}
	
	// Use case testing: User uses edit function to edit title of task
	@Test
	public void testUpdateSummary() throws UnknownHostException {
		setTaskAttributes();
		//String taskTitle = task8UpdateSummary.getTitle();		
		goocal.add(task8UpdateSummary);
		String GID = task8UpdateSummary.getGID();
		//String eventSummary = goocal.retrieve(GID);

		// Now I modify the title
		task8UpdateSummary.setTitle("UPDATEDDDD! Task8");
		
		// I store the modified title
		String updatedTitle = task8UpdateSummary.getTitle();
		
		// I execute the update method of GoogleCalendarManager
		goocal.update(task8UpdateSummary);
		
		// I retrieve the updated event summary 
		gooCalRetriever.retrieve(GID);
		String updatedEventSummary = gooCalRetriever.getRetrievedSummary();
		
		// I assert to see whether update was successful
		assertEquals("Faled. Update of event summary failed", updatedTitle, updatedEventSummary);	
	}
	
	
	// Use case testing: User uses edit function to edit time and date of task
	@Test
	public void testUpdateStartEnd() throws UnknownHostException {
		// This test has not been asserted because the retrieve method is still being worked on
		
		setTaskAttributes();	
		goocal.add(task9UpdateStartEnd);
		
		// Now I modify the start date time
		task9UpdateStartEnd.setStartTime("22/10/2014", "2300");
		task9UpdateStartEnd.setEndTime("22/10/2014", "2359");
	
		// I execute the update method of GoogleCalendarManager
		goocal.update(task9UpdateStartEnd);
	}

	// Use case testing: User uses edit function to edit description of task
	@Test
	public void testUpdateDescription() throws UnknownHostException {
		setTaskAttributes();
		goocal.add(task12UpdateDescription);
		String GID = task12UpdateDescription.getGID();

		// Now I modify the description
		task12UpdateDescription.setDescription("My Updated Description for Task 12!");
		
		// I store the modified title
		String updatedDescription = task12UpdateDescription.getDescription();
		
		// I execute the update method of GoogleCalendarManager
		goocal.update(task12UpdateDescription);
		
		// I retrieve the updated event summary 
		
		gooCalRetriever.retrieve(GID);
		String updatedEventDescription = gooCalRetriever.getRetrievedDescription();
		
		
		// I assert to see whether update was successful
		assertEquals("Faled. Update of event summary failed", updatedDescription, updatedEventDescription);	
	}
	

	
	@Test
	public void testGetSummary() {
		setTaskAttributes();
		assertEquals("Faled. getSummary method did not return the right string", "Task 1 Instance", goocal.getSummary(task1Instance));
	}	
}
