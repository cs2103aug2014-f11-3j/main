package taskbuddy.googlecal;

import static org.junit.Assert.*;

import java.net.UnknownHostException;
import java.util.ArrayList;

import taskbuddy.logic.Task;

import org.junit.Test;

public class GoogleCalRetrieveAllTest {

	@Test
	public void test() {
		GoogleCalRetrieveAll googleCalRetrieveAll = new GoogleCalRetrieveAll();
		ArrayList<Task> tasks = new ArrayList<Task>();
		try {
			googleCalRetrieveAll.getListFromGoogle();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tasks = googleCalRetrieveAll.getTasks();
		//googleCalRetrieveAll.printArrayListOfTasks(tasks);
		
		//fail("Not yet implemented");
	}
}
