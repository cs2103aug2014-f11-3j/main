package taskbuddy.googlecal;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

import taskbuddy.logic.Task;


public class GoogleCalendarManagerTest {


	GoogleCalendarManager gooCal = new GoogleCalendarManager();
	Task task = new Task ("Event Summary");

	
	
	
	// This tests the ability to read the description of a task sent to it  
	@Test
	public void testShowSummary() {
		assertEquals("Faled because event summary is wrong", "Event Summary", gooCal.showSummary(task));		
	}
	
	// This test tests the ability for GoogleCalendarManager to successfully add an event to Google Calendar servers.
	/*
	@Test
	public void testAdd() {
		assertTrue("Failed because event was not successfully added to Google Calendar Server", gooCal.add(task));		
	}
	*/
	
	
	// This test tests the ability for GoogleCalendarManager to detect the online status of the user. 
	@Test
	public void testIsUserOnline()  throws UnknownHostException, IOException  {
			assertTrue("Failed because of the inability to detect correct online status", gooCal.isUserOnline());
	}

	
}
