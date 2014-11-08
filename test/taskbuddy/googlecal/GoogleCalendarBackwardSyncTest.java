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
		googleCalendarBackwardSync.generateArrayListToAddToDatabase();
//		googleCalendarBackwardSync.generateArrayListToDeleteFromDatabase();
		//googleCalendarBackwardSync.compareLists();
		googleCalendarBackwardSync.printArrayListOfTasks1();
		googleCalendarBackwardSync.printArrayListOfTasks2();
		googleCalendarBackwardSync.printArrayListOfTasks3();
		
		
		
		//fail("Not yet implemented");
	}

}
