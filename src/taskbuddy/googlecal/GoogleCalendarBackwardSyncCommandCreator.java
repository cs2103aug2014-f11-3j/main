package taskbuddy.googlecal;

import java.io.IOException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;

//import sun.security.jca.GetInstance;
import taskbuddy.database.Database;
import taskbuddy.logic.Task;

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
	
	public void getTasksFromGCal() {
		try {
			googleCalRetrieveAll.getListFromGoogle();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		tasksFromGoogleCal = googleCalRetrieveAll.getTasks();		
	}
	

	
//	public void setTaskIdsZero() {
//		for (Task task: tasksFromDatabase) {
//			task.setTaskId(0);
//		}
//	}
//	
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
//						System.out.println("title is:" + task.getTitle());
//						System.out.println("title is:" + tasksInGCal_ScanForEdits.get(i).getTitle());
//						System.out.println(task.getTitle().equals(tasksInGCal_ScanForEdits.get(i).getTitle()));
						flagEditRequired = true;
					}		
					else if (!(task.getDescription().equals(tasksInGCal_ScanForEdits.get(i).getDescription()))) {
						System.out.println("description");
//						System.out.println("description is:" + task.getDescription());
//						System.out.println("description is:" + tasksInGCal_ScanForEdits.get(i).getDescription());
//						System.out.println(task.getDescription().equals(tasksInGCal_ScanForEdits.get(i).getCompletionStatus()));
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
//	public void executeBackwardSync() {
//		db.addTask(task)
//	}
	
	
	public void executeCommandCreator() {
//		System.out.println("tasks to add");
		//printArrayListOfTasks(tasksToAddToDb);
		BackwardSyncAddCommand backwardSyncAddCommand = new BackwardSyncAddCommand(tasksToAddToDb);
//		backwardSyncAddCommand.printTasks();
		
//		System.out.println("tasks to delete");
//		printArrayListOfTasks(tasksToDeleteFromDb);
		BackwardSyncDeleteCommand backwardSyncDeleteCommand = new BackwardSyncDeleteCommand(tasksToDeleteFromDb);
//		backwardSyncDeleteCommand.printTasks();
		
//		System.out.println("tasks to edit");
//		printArrayListOfTasks(tasksToEdit);
		BackwardSyncEditCommand backwardSyncEditCommand = new BackwardSyncEditCommand(tasksToEdit);
//		backwardSyncEditCommand.printTasks();
		
		GoogleCalendarBackwardSyncCommandQueue commandQueueAdd = new GoogleCalendarBackwardSyncCommandQueue(backwardSyncAddCommand);
		commandQueueAdd.executeCommands();
		GoogleCalendarBackwardSyncCommandQueue commandQueueDelete = new GoogleCalendarBackwardSyncCommandQueue(backwardSyncDeleteCommand);
		commandQueueDelete.executeCommands();
		GoogleCalendarBackwardSyncCommandQueue commandQueueEdit = new GoogleCalendarBackwardSyncCommandQueue(backwardSyncEditCommand);
		commandQueueEdit.executeCommands();
	}
	
	public void executeBackwardSync() {
		getTasksFromDatabase();
		getTasksFromGCal();
		generateArrayListToAddToDatabase();
		generateArrayListToDeleteFromDatabase();
		generateArrayListOfEdits();
		executeCommandCreator();
	}
}
