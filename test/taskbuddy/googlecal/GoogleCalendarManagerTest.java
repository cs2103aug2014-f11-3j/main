package taskbuddy.googlecal;

import static org.junit.Assert.*;

import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;




public class GoogleCalendarManagerTest {


	GoogleCalendarManager gooCal = new GoogleCalendarManager();
	Task task = new Task ("Task Description");
	Date date = new Date (2,10,2014);
	Time time = new Time (21,30);
	
	
	// This tests the ability to read the description of a task sent to it  
	@Test
	public void testDisplayTaskDescription() {
		assertEquals("If this test fails, the task description is not reading properly.", "Task Description", gooCal.displayTaskDescription(task));		
	}
	
	// This tests the ability to read the date of a task sent to it  
	@Test
	public void testDisplayTaskDate() {
		task.setDate(date);
		assertEquals("If this test fails, the task date is not reading properly.", "2.10.2014", gooCal.displayTaskDate(task));
	}
	
	// This tests the ability to read the time of a task sent to it  
	@Test
	public void testDisplayTaskTime() {
		task.setTime(time);
		assertEquals("If this test fails, the task time is not reading properly.", "21.30", gooCal.displayTaskTime(task));
	}
	
	
}
