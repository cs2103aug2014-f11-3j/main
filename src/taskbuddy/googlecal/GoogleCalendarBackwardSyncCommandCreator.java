package taskbuddy.googlecal;

import java.io.IOException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;

//import sun.security.jca.GetInstance;
import taskbuddy.database.Database;
import taskbuddy.logic.Task;


/**
 * This class is part of the BackwardSync capabilities of the Google Calendar Sync.
 * Following the implementation of the Command pattern taught in CS2103, this class acts
 * as the command creator class. It creates the command objects BackwardSyncAddCommand,
 * BackwardSyncDeleteCommand and BackwardSyncEditCommand. 
 * 
 * This class scans the tasks stored in the dabatase, and the tasks currently on Google Calendar.
 * It generates arraylists of task objects for the necessary required functionalities of backward sync,
 * including Add, Delete and Edit functionality.
 * 
 * @author Pee Choon Hian, A0108411W
 *
 */


public class GoogleCalendarBackwardSyncCommandCreator {
	GoogleCalRetrieveAll googleCalRetrieveAll = new GoogleCalRetrieveAll();
	ArrayList<Task> tasksFromDatabase = new ArrayList<Task>();
	ArrayList<Task> tasksFromGoogleCal = new ArrayList<Task>();
	ArrayList<Task> tasksToAddToDb = new ArrayList<Task>();
	ArrayList<Task> tasksToDeleteFromDb = new ArrayList<Task>();
	
	ArrayList<Task> tasksInDb_ScanForEdits = new ArrayList<Task>();
	ArrayList<Task> tasksInGCal_ScanForEdits = new ArrayList<Task>();
	
	ArrayList<Task> tasksToEdit = new ArrayList<Task>();
	
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
	
	public void getTasksFromGCal() throws UnknownHostException {
		
		googleCalRetrieveAll.getListFromGoogle();
		tasksFromGoogleCal = googleCalRetrieveAll.getTasks();		
	}
	

	
	public void generateArrayListToAddToDatabase() {
		boolean flagTaskAlreadyInDb;
		for (Task task: tasksFromGoogleCal) {
			flagTaskAlreadyInDb = false;
			for (int i = 0; i < tasksFromDatabase.size(); i++) {
				if ((tasksFromDatabase.get(i)).getGID().equals(task.getGID())) {
					flagTaskAlreadyInDb = true;
				}
			}
			if (!flagTaskAlreadyInDb) {
				tasksToAddToDb.add(task);
			}
			else {
				tasksInGCal_ScanForEdits.add(task);
			}
		}
	}
		
			
	
	public void generateArrayListToDeleteFromDatabase() {
		boolean flagTaskNeedToDelete;
		for (Task task: tasksFromDatabase) {
			flagTaskNeedToDelete = true;
			for (int i = 0; i < tasksFromGoogleCal.size(); i++) {
				if ((tasksFromGoogleCal.get(i)).getGID().equals(task.getGID())) {
					flagTaskNeedToDelete = false;
				}
			}
			if (flagTaskNeedToDelete) {
				tasksToDeleteFromDb.add(task);
			}
			else {
				tasksInDb_ScanForEdits.add(task);
			}
		}
	}
		
	public void generateArrayListOfEdits() {		
		boolean flagEditRequired;
		for (Task task: tasksInDb_ScanForEdits) {
			flagEditRequired = false;
			for (int i = 0; i < tasksInGCal_ScanForEdits.size(); i++) {
				if (task.getGID().equals(tasksInGCal_ScanForEdits.get(i).getGID())) {
					if (!(task.getTitle().equals(tasksInGCal_ScanForEdits.get(i).getTitle()))) {
						System.out.println("title");
						flagEditRequired = true;
					}		
					else if (!(task.getDescription().equals(tasksInGCal_ScanForEdits.get(i).getDescription()))) {
						System.out.println("description");
						flagEditRequired = true;
					}				
					else if (!(task.getStartTime().equals(tasksInGCal_ScanForEdits.get(i).getStartTime()))) {
						System.out.println("start");
						flagEditRequired = true;
					}

					else if (!(task.getEndTime().equals(tasksInGCal_ScanForEdits.get(i).getEndTime()))) {
						System.out.println("end");
						flagEditRequired = true;
					}
					else if (!((task.getCompletionStatus()) == (tasksInGCal_ScanForEdits.get(i).getCompletionStatus()))) {
						System.out.println("completion");
						System.out.println(task.getCompletionStatus());
						System.out.println(tasksInGCal_ScanForEdits.get(i).getCompletionStatus());
						flagEditRequired = true;
					}
					else if (task.getPriority() != tasksInGCal_ScanForEdits.get(i).getPriority()) {
						System.out.println("priority");
						flagEditRequired = true;
					}
					else {
						flagEditRequired = false;
					}
				}
				if (flagEditRequired) {
					tasksToEdit.add(tasksInGCal_ScanForEdits.get(i));
				}
			}
		}
	}
	

	
	public void printArrayListOfTasks(ArrayList<Task> tasks) {
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
	
	
	public void printArrayListOfTasks4() {
		System.out.println("Tasks to delete from db");
		for (Task task4 : tasksToDeleteFromDb) {
			System.out.println(task4.displayTask());
		}
	}
	
	public void printArrayListOfTasks5() {
		System.out.println("Scan edit list: Tasks in db");
		for (Task task4 : tasksInDb_ScanForEdits) {
			System.out.println(task4.displayTask());
		}
	}
	
	public void printArrayListOfTasks6() {
		System.out.println("Scan edit list: tasks in gcal");
		for (Task task4 : tasksInGCal_ScanForEdits) {
			System.out.println(task4.displayTask());
		}
	}
	
	
	public void printArrayListOfTasks7() {
		System.out.println("Tasks to edit");
		for (Task task4 : tasksToEdit) {
			System.out.println(task4.displayTask());
		}
	}
	
	
	public void executeCommandCreator() {
		BackwardSyncAddCommand backwardSyncAddCommand = new BackwardSyncAddCommand(tasksToAddToDb);
		BackwardSyncDeleteCommand backwardSyncDeleteCommand = new BackwardSyncDeleteCommand(tasksToDeleteFromDb);
		BackwardSyncEditCommand backwardSyncEditCommand = new BackwardSyncEditCommand(tasksToEdit);
		
		GoogleCalendarBackwardSyncCommandQueue commandQueueAdd = new GoogleCalendarBackwardSyncCommandQueue(backwardSyncAddCommand);
		commandQueueAdd.executeCommands();
		GoogleCalendarBackwardSyncCommandQueue commandQueueDelete = new GoogleCalendarBackwardSyncCommandQueue(backwardSyncDeleteCommand);
		commandQueueDelete.executeCommands();
		GoogleCalendarBackwardSyncCommandQueue commandQueueEdit = new GoogleCalendarBackwardSyncCommandQueue(backwardSyncEditCommand);
		commandQueueEdit.executeCommands();
	}
	
	public void executeBackwardSync() throws UnknownHostException {
		getTasksFromDatabase();
		getTasksFromGCal();
		generateArrayListToAddToDatabase();
		generateArrayListToDeleteFromDatabase();
		generateArrayListOfEdits();
		executeCommandCreator();
	}
}
