package taskbuddy.googlecal;

import java.util.ArrayList;

import sun.security.jca.GetInstance;
import taskbuddy.database.Database;
import taskbuddy.logic.Task;

public class GoogleCalendarBackwardSync {
	GoogleCalRetrieveAll googleCalRetrieveAll = new GoogleCalRetrieveAll();
	ArrayList<Task> tasksFromDatabase = new ArrayList<Task>();
	ArrayList<Task> tasksFromGoogleCal = new ArrayList<Task>();
	ArrayList<Task> tasksToAddToDb = new ArrayList<Task>();
	ArrayList<Task> tasksToRemoveFromDb = new ArrayList<Task>();
	Database db = Database.getInstance();

	

	public void getTasksFromDatabase() {
		tasksFromDatabase = db.getTasks();
	}
	
	public void getTasksFromGCal() {
		tasksFromGoogleCal = googleCalRetrieveAll.getTasks();
	}
	
	public void removeFloatingTasks() {
	// Floating tasks are removed because they are not synced to Google Calendar
		for (Task task : tasksFromDatabase) {
			if (task.isFloatingTask()) {
				tasksFromDatabase.remove(task);
			}
		}
	}
	
	public void setTaskIdsZero() {
		for (Task task: tasksFromDatabase) {
			task.setTaskId(0);
		}
	}
	
	public void compareLists() {
		for (Task task: tasksFromGoogleCal) {
			if (!tasksFromDatabase.contains(task)) {
				tasksToAddToDb.add(task);
			}
		}
	}
	
	public void printArrayListOfTasks() {
		
		for (Task task : tasksToAddToDb) {
			System.out.println(task.displayTask());
		}
	}
}
