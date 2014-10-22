package taskbuddy.database;
import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import org.junit.After;
import org.junit.Test;

import taskbuddy.database.Database;
import taskbuddy.logic.Task;

public class IntegratedGoogleCalDatabaseTest {

	Task task;
	Database database;
	private Task firstTask;
	private Task secondTask;
	private Task thirdTask;

	public void setup() throws Exception {
		database = new Database();

		firstTask = createTask("First");
		secondTask = createTask("Second");
		thirdTask = createTask("Third");

		database.addTask(firstTask);
		database.addTask(secondTask);
		database.addTask(thirdTask);
	}

	public Task createTask(String title) {
		String description = "Description";
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		int priority = 1;
		boolean isComplete = true;
		boolean isFloating = false;

		Task task = new Task(title);
		task.setDescription(description);
		task.setStartTime(start);
		task.setEndTime(end);
		task.setPriority(priority);
		task.setCompletion(isComplete);
		task.setFloating(isFloating);

		return task;
	}

//	@Test
//	public void testAddDelete() throws Exception {
//		setup();
//		database.delete(0);
//	}

	@Test
	public void testSearchEdit() throws Exception {
		setup();
		
		String searchString = "DESCRIP";
		ArrayList<Task> searchResults = database.search(searchString);
		
		for (Task aTask : searchResults) {
			System.out.println(aTask.getTitle());
		}
		searchResults.get(1).setTitle("change title");
		for (Task aTask : searchResults) {
			System.out.println(aTask.getTaskId() + ": " + aTask.getTitle());
		}
	}

	/**
	 * Deletes existing log file after all tests have been run
	 */
	@After
	public void deleteLog() {
		File log = new File("log");

		if (log.isFile()) {
			log.delete();
		}
	}

}
