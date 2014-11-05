package taskbuddy.googlecal;

import static org.junit.Assert.*;

import java.net.UnknownHostException;
import java.util.ArrayList;

import taskbuddy.logic.Task;

import org.junit.Test;

public class GoogleCalBackwardSyncTestt {

	@Test
	public void test() {
		GoogleCalBackwardSync googleCalBackwardSync = new GoogleCalBackwardSync();
		ArrayList<Task> tasks = new ArrayList<Task>();
		try {
			googleCalBackwardSync.getListFromGoogle();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tasks = googleCalBackwardSync.getTasks();
		googleCalBackwardSync.printArrayListOfTasks(tasks);
		
		//fail("Not yet implemented");
	}
}
