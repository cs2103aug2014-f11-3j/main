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


public class GoogleCalendarManagerTest {


	GoogleCalendarManager gooCal = new GoogleCalendarManager();
	Task task = new Task ("Event Summary");

	
	
	

	
	
	
	// This tests the ability to read the description of a task sent to it  
	@Test
	public void testGetSummary() {
		assertEquals("Faled because event summary is wrong", "Event Summary", gooCal.getSummary(task));		
	}
	
	// This test tests the ability for GoogleCalendarManager to successfully add an event to Google Calendar servers.
	/*
	@Test
	public void testAdd() {
		assertTrue("Failed because event was not successfully added to Google Calendar Server", gooCal.add(task));		
	}
	*/
	
	
	// This tests the ability to read the start time of a task sent to it  
	@Test
	public void testGetStartDate() throws NullPointerException {
		task.setStartTime("PADDING_VALUE");
		assertEquals("Faled because start date is wrong", "9/10/2014", gooCal.getStartDate(task));		
	}
	
	// This tests the ability to read the start time of a task sent to it  
	@Test
	public void testGetStartTime() throws NullPointerException {
		task.setStartTime("PADDING_VALUE");
		assertEquals("Faled because start time is wrong", "01:25", gooCal.getStartTime(task));		
	}
	
	
	// This tests the ability to read the end time of a task sent to it  
	@Test
	public void testGetEndDate() throws NullPointerException {
		task.setStartTime("PADDING_VALUE");
		assertEquals("Faled because end date is wrong", "9/10/2014", gooCal.getEndDate(task));		
	}
	
	// This tests the ability to read the end time of a task sent to it  
	@Test
	public void testGetEndTime() throws NullPointerException {
		task.setStartTime("PADDING_VALUE");
		assertEquals("Faled because end time is wrong", "01:25", gooCal.getStartTime(task));		
	}
	
	
	// This test tests the ability for GoogleCalendarManager to detect the online status of the user. 
	@Test
	public void testIsUserOnline()  throws UnknownHostException, IOException  {
			assertTrue("Failed because of the inability to detect correct online status", gooCal.isUserOnline());
	}
	
	


	
}
