package taskbuddy.googlecal;

import java.io.IOException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;

import sun.security.jca.GetInstance;
import taskbuddy.database.Database;
import taskbuddy.logic.Task;

public class GoogleCalendarBackwardSync {
	GoogleCalRetrieveAll googleCalRetrieveAll = new GoogleCalRetrieveAll();
	ArrayList<Task> tasksFromDatabase = new ArrayList<Task>();
	ArrayList<Task> tasksFromGoogleCal = new ArrayList<Task>();
	ArrayList<Task> tasksToAddToDb = new ArrayList<Task>();
	ArrayList<Task> tasksToDeleteFromDb = new ArrayList<Task>();
	
	
	
	
//	ArrayList<String> tasksFromDatabase_GID = new ArrayList<String>();
//	ArrayList<String> tasksFromGoogleCal_GID = new ArrayList<String>();
//	ArrayList<String> tasksToAddToDb_GID = new ArrayList<String>();
	//Database db;

//	public void databaseGetInstance() {
//		try {
//			db = db.getInstance();
//		} catch (IOException | ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	public void getTasksFromDatabase() {
		Database db = null;
		try {
			db = db.getInstance();
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tasksFromDatabase = db.getTasks();
//		System.out.println("Tasks from db");
//		printArrayListOfTasks(tasksFromDatabase);
	}
	
	public void getTasksFromGCal() {
		try {
			googleCalRetrieveAll.getListFromGoogle();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		tasksFromGoogleCal = googleCalRetrieveAll.getTasks();
//		System.out.println("Tasks from gcal");
//		printArrayListOfTasks(tasksFromGoogleCal);
		
	}
	
//	public void removeFloatingTasks() {
//	// Floating tasks are removed because they are not synced to Google Calendar
//		for (Task task : tasksFromDatabase) {
//			if (task.isFloatingTask()) {
//				tasksFromDatabase.remove(task);
//			}
//		}
//	}
	
	public void setTaskIdsZero() {
		for (Task task: tasksFromDatabase) {
			task.setTaskId(0);
		}
	}
	
	public void generateArrayListToAddToDatabase() {
		for (Task task: tasksFromGoogleCal) {
			if (!tasksFromDatabase.contains(task)) {
				tasksToAddToDb.add(task);
			}
		
//		for (Task task: tasksFromGoogleCal) {
//			String GID = task.getGID();
//			for (String GID: tasksFromGoogleCal) {
//				
//			}
//		}
//		if (!tasksFromDatabase.contains(task)) {
//			tasksToAddToDb.add(task);
//		}

		}
	}
	
	public void generateArrayListToDeleteFromDatabase() {
		for (Task task: tasksFromDatabase) {
			if (!tasksFromGoogleCal.contains(task)) {
				tasksToDeleteFromDb.add(task);
			}
		}
	}
	
	public void printArrayListOfTasks(ArrayList<Task> tasks) {
		//System.out.println("Tasks from db");
		for (Task task: tasks) {
			System.out.println(task.displayTask());
		}
	}
	
	public void printArrayListOfTasks1() {
		System.out.println("Tasks from db");
		for (Task task1: tasksFromDatabase) {
			System.out.println(task1.displayTask());
		}
	}
	
	public void printArrayListOfTasks2() {
		System.out.println("Tasks from gcal");
		for (Task task2: tasksFromGoogleCal) {
			System.out.println(task2.displayTask());
		}
	}
	
	public void printArrayListOfTasks3() {
		System.out.println("Tasks to add to db");
		for (Task task3 : tasksToAddToDb) {
			System.out.println(task3.displayTask());
		}
	}
	
//	public void executeBackwardSync() {
//		db.addTask(task)
//	}
	
	
	

	
//	public void compareLists() {
//		for (Task task: tasksFromGoogleCal) {
//			if (!tasksFromDatabase.contains(task)) {
//				tasksToAddToDb.add(task);
//			}
//		}
//	}
	
}
