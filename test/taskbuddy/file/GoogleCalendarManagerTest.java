package taskbuddy.file;

import static org.junit.Assert.*;

import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;




public class GoogleCalendarManagerTest {


	GoogleCalendarManager gooCal = new GoogleCalendarManager();
	Task task = new Task ("Task Description");

	
	
	// This tests the ability to read the description of a task sent to it  
	@Test
	public void testExecuteRead() {
		assertEquals("testing executeRead", "Task Description", gooCal.displayTaskDescription(task));		
	}
}
