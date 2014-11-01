package taskbuddy.googlecal;

import static org.junit.Assert.*;

import java.util.ArrayList;
import taskbuddy.logic.Task;

import org.junit.Test;

public class GoogleCalBackwardSyncTestt {

	@Test
	public void test() {
		GoogleCalBackwardSync googleCalBackwardSync = new GoogleCalBackwardSync();
		ArrayList<Task> tasks = new ArrayList<Task>();
		googleCalBackwardSync.getListFromGoogle();
		tasks = googleCalBackwardSync.getTasks();
		googleCalBackwardSync.printArrayListOfTasks(tasks);
		
		//fail("Not yet implemented");
	}
}
