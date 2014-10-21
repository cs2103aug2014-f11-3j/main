package taskbuddy.googlecal;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

import taskbuddy.logic.Task;
import taskbuddy.logic.Bundle;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;

/*
Instructions for use:
- Go online and run test to pass all test cases.

- To test exceptions, go offline, and run the test. 
- We should observe the error "java.net.UnknownHostException: User is offline". 

- To test assertion (missing task id), comment out the first testAddRetrieve method, and uncomment the second testAddRetrieve method.
- We should observe the error "java.lang.AssertionError: Title is missing from this task object".


*/


public class GoogleCalendarManagerTest {
	java.util.Calendar calendarInstance = java.util.Calendar.getInstance();
	GoogleCalendarManager goocal = new GoogleCalendarManager ();
	
	
	Task task1 = new Task ("Task number 1");
	Task task2 = new Task ("Task number 2");
	Task task3 = new Task ("Task number 3");
	Task task4 = new Task ("Task number 4");
	Task task5 = new Task ("Task number 5");
	Task task6 = new Task ("Task number 6");
	Task taskAddRetrieve = new Task ("Task add and retrieve");
	
	
	public void setTaskAttributes() {
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
		
		taskAddRetrieve.setStartTime(calendarInstance);
		taskAddRetrieve.setEndTime("padding value", "padding value");
		
	}
	

	@Test
	public void testAddDelete() throws UnknownHostException {
		setTaskAttributes();
	
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

	}
	

	@Test
	public void testAddRetrieve() throws UnknownHostException {
		setTaskAttributes();
		String taskTitle = task1.getTitle();		
		goocal.add(task1);
		String GID = task1.getGID();
		String eventSummary = goocal.retrieve(GID);
		assertEquals("Faled. Task title did not match event summary of the Google Calendar Event retrieved", taskTitle, eventSummary);	
	}
	
	@Test
	public void testUpdateSummary() throws UnknownHostException {
		setTaskAttributes();
		String taskTitle = task1.getTitle();		
		goocal.add(task1);
		String GID = task1.getGID();
		String eventSummary = goocal.retrieve(GID);
		//assertEquals("Faled. Task title did not match event summary of the Google Calendar Event retrieved", taskTitle, eventSummary);	
		
		// Now I modify the title
		task1.setTitle("UPDATEDDDD!");
		
		// I store the modified title
		String updatedTitle = task1.getTitle();
		
		// I execute the update method of GoogleCalendarManager
		goocal.update(task1);
		
		// I retrieve the updated event summary 
		String updatedEventSummary = goocal.retrieve(GID);
		
		// I assert to see whether update was successful
		assertEquals("Faled. Update of event summary failed", updatedTitle, updatedEventSummary);	
	}

	
	// Use this test method to test the assertion: Title is missing from this task object
	/*
	@Test
	public void testAddRetrieve() throws UnknownHostException {
		setTaskAttributes();
		String taskTitle = task1.getTitle();
		task1.setTitle(null); //Removes the element in question on purpose 
		goocal.add(task1);
		String GID = task1.getGID();
		String eventSummary = goocal.retrieve(GID);
		assertEquals("Faled. Task title did not match event summary of the Google Calendar Event retrieved", taskTitle, eventSummary);	
	}
	*/
	
	@Test
	public void testGetSummary() {
		setTaskAttributes();
		assertEquals("Faled. getSummary method did not return the right string", "Task number 1", goocal.getSummary(task1));
	}	
}
