package taskbuddy.googlecal;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import taskbuddy.database.Database;
import taskbuddy.logic.Task;

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
