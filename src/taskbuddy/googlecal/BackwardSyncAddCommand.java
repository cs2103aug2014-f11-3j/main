//@author A0108411W
package taskbuddy.googlecal;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import taskbuddy.database.Database;
import taskbuddy.logic.Task;

/**
 * This class is part of the BackwardSync capabilities of the Google Calendar Sync.
 * Following the implementation of the Command pattern taught in CS2103, 
 * this class is the command class for the add command.
 * It calls the database, and performs adding of tasks to the database. 
 * 
 *
 */

public class BackwardSyncAddCommand {
	//private Task taskToAdd = new Task();
	private ArrayList<Task> tasksToAdd = new ArrayList<Task>();
	Database db;
	GoogleCalendarBackwardSyncCommandCreator googleCalendarBackwardSyncCommandCreator = new GoogleCalendarBackwardSyncCommandCreator();
	
	
	// Constructor 
	public BackwardSyncAddCommand(ArrayList<Task> tasks) {
		tasksToAdd = tasks;
	}
	
	public void executeAdd() {
		System.out.println("Running from command queue: to add");
		printTasks();

		try {
			db = db.getInstance();
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (Task task : tasksToAdd) {
			try {
				db.addBackwardSync(task);
				googleCalendarBackwardSyncCommandCreator.clearAllArrayLists();
				//System.out.println(task);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}
	
	public void printTasks() {
		System.out.println("Tasks to add: running from object");
		for (Task task : tasksToAdd) {
			System.out.println(task.displayTask());
		}
	}
}
