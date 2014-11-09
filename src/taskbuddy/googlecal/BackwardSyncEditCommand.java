//@author A0108411W
package taskbuddy.googlecal;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import taskbuddy.database.Database;
import taskbuddy.logic.Task;

/**
 * This class is part of the BackwardSync capabilities of the Google Calendar Sync.
 * Following the implementation of the Command pattern taught in CS2103, 
 * this class is the command class for the edit command.
 * It calls the database, and performs editing of tasks in the database. 
 * 
 *
 */


public class BackwardSyncEditCommand {
	//private Task taskToAdd = new Task();
	private ArrayList<Task> tasksToEdit = new ArrayList<Task>();
	Database db;
	
	// Constructor 
	public BackwardSyncEditCommand(ArrayList<Task> tasks) {
		tasksToEdit = tasks;
	}
	
	
	public void printTasks() {
		System.out.println("Tasks to edit: running from object");
		for (Task task : tasksToEdit) {
			System.out.println(task.displayTask());
		}
	}

	public void executeEdit() {
		System.out.println("Running from command queue: to delete");
		printTasks();
		
		try {
			db = db.getInstance();
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (Task task : tasksToEdit) {
			try {
				db.editBackwardSync(task.getGID(), task);
			} catch (IOException | IllegalAccessException | NoSuchElementException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}
}
