package taskbuddy.file;

import static org.junit.Assert.*;

import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;




public class GoogleCalendarManagerTest {


	GoogleCalendarManager gooCal = new GoogleCalendarManager();
	Task task = new Task ("Task Description");
	Date date = new Date (2,10,2014);
	
	
	// This tests the ability to read the description of a task sent to it  
	@Test
	public void testDisplayTaskDescription() {
		assertEquals("If this test fails, the task description is not reading properly.", "Task Description", gooCal.displayTaskDescription(task));		
	}
	
	// This tests the ability to read the date of a task sent to it  
	@Test
	public void testDisplayDate() {
		task.setDate(date);
		assertEquals("If this test fails, the task date is not reading properly.", "2.10.2014", gooCal.displayTaskDate(task));
	}
}
