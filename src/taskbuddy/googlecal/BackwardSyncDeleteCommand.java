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
 * this class is the command class for the delete command.
 * It calls the database, and performs deleting of tasks to the database. 
 * 
 *
 */


public class BackwardSyncDeleteCommand {
	//private Task taskToAdd = new Task();
	private ArrayList<Task> tasksToDelete = new ArrayList<Task>();
	Database db;
	
	
	// Constructor 
	public BackwardSyncDeleteCommand(ArrayList<Task> tasks) {
		tasksToDelete = tasks;
	}
	
	
	public void printTasks() {
		System.out.println("Tasks to delete: running from object");
		for (Task task : tasksToDelete) {
			System.out.println(task.displayTask());
		}
	}
	

	public void executeDelete() {
		System.out.println("Running from command queue: to delete");
		printTasks();
		
		try {
			db = db.getInstance();
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (Task task : tasksToDelete) {

				try {
					db.deleteBackwardSync(task.getGID());
				} catch (IllegalAccessException | NoSuchElementException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				

		}		
	}
}
