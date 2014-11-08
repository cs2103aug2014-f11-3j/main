package taskbuddy.googlecal;

import static org.junit.Assert.*;

import org.junit.Test;

public class GoogleCalendarBackwardSyncCommandCreatorTest {
	GoogleCalendarBackwardSyncCommandCreator gcbsCommandCreator = new GoogleCalendarBackwardSyncCommandCreator();
	@Test
	public void test() {
		gcbsCommandCreator.getTasksFromDatabase();
		gcbsCommandCreator.getTasksFromGCal();
		gcbsCommandCreator.generateArrayListToAddToDatabase();
		gcbsCommandCreator.generateArrayListToDeleteFromDatabase();
		//googleCalendarBackwardSync.scanForEditsInGoogleCal();
		gcbsCommandCreator.generateArrayListOfEdits();
		gcbsCommandCreator.printArrayListOfTasks1();
		gcbsCommandCreator.printArrayListOfTasks2();
		gcbsCommandCreator.printArrayListOfTasks3();
		gcbsCommandCreator.printArrayListOfTasks4();
		gcbsCommandCreator.printArrayListOfTasks5();
		gcbsCommandCreator.printArrayListOfTasks6();
		gcbsCommandCreator.printArrayListOfTasks7();
		
		
		//fail("Not yet implemented");
	}

}
