package taskbuddy.googlecal;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import taskbuddy.database.Database;
import taskbuddy.logic.Task;

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
			//try {
				// TO MODIFY
				//db.deleteBackwardSync(task.getGID(), task);
				
				//db.addTask(task);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}		
	}
}
