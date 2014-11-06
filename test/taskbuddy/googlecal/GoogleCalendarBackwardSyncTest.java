package taskbuddy.googlecal;

import static org.junit.Assert.*;

import org.junit.Test;

public class GoogleCalendarBackwardSyncTest {
	GoogleCalendarBackwardSync googleCalendarBackwardSync = new GoogleCalendarBackwardSync();
	@Test
	public void test() {
		googleCalendarBackwardSync.getTasksFromDatabase();
		googleCalendarBackwardSync.getTasksFromGCal();
		googleCalendarBackwardSync.setTaskIdsZero();
		googleCalendarBackwardSync.compareLists();
		googleCalendarBackwardSync.printArrayListOfTasks();
		
		
		
		//fail("Not yet implemented");
	}

}
