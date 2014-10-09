package taskbuddy.logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

import taskbuddy.database.Database;

//Author: andrew
public class CommandParser {

	private Database database;
	private Stack<Bundle> undoStack = new Stack<Bundle>();
	private Stack<Bundle> redoStack = new Stack<Bundle>();
	private Stack<Bundle> editStack = new Stack<Bundle>();
	// if new command is parsed, clear redo stack;
	//user bundle strings
	private String user_command = "command";
	private String user_description = "description";
	private String user_endDate = "endDate";
	private String user_start = "startTime";
	private String user_endTime = "endTime";
	private String user_title = "title";
	// acknowledge bundle strings
	private String status = "Status";
	private String success = "Success";
	private String failure = "Failure";
	private String message = "Message";
	private String task = "Task";

	Bundle addTask(Bundle extras, Database db) {
		String desc = (String)extras.getItem(user_description);
		String endDate = (String)extras.getItem(user_endDate);
		String endTime = (String)extras.getItem(user_endTime);
		String title = (String)extras.getItem(user_title);
		Task newTask = new Task(title);
		newTask.setDescription(desc);
		newTask.setEndTime(endDate, endTime);
		boolean result = db.addTask(newTask);
		Bundle acknowledgement = new Bundle();
		if (result) {
			acknowledgement = ackFromLogic(success, null, newTask);
		} else {
			acknowledgement = ackFromLogic(failure, "Add failure", newTask);
		}
		return acknowledgement;
	}

	Bundle deleteTask(String title, Database db) {
		Bundle ack = new Bundle();
		boolean result = db.delete(title);
		if (result) {
			ack = ackFromLogic(success, null, null);
		} else {
			ack = ackFromLogic(failure, "Nonexistent task", null);
		}
		return ack;
	}

	Bundle editTask(Bundle extras, Database db) {
		String title = (String)extras.getItem(user_title);
		Bundle ack = new Bundle();
		Bundle foundTask = deleteTask(title, db);
		if (foundTask.getItem(status).equals(success)) {
			ack = addTask(extras, db);
		} else {
			ack = ackFromLogic(failure, "Nonexistent task", null);
		}
		return ack;
	}

	void displayTasks(Database db) {
		ArrayList<Task> tempCache = db.getTasks();
		for (Task task : tempCache) {
			printTask(task);
		}
	}

	ArrayList<String> printTask(Task task) {
		ArrayList<String> taskInfo = new ArrayList<String>();
		taskInfo = task.getTaskInfo();
		return taskInfo;
	}

	void undo(Database db) {
		Bundle prevCommand = undoStack.pop();
		redoStack.push(prevCommand);
		String commandType = (String) prevCommand.getItem(user_command);
		if (commandType.equalsIgnoreCase("add")) {
			String delete_description = (String)prevCommand.getItem(user_description);
			deleteTask(delete_description, db);
		}
		if (commandType.equalsIgnoreCase("delete")) {
			addTask(prevCommand, db);
		}
		if (commandType.equalsIgnoreCase("edit")) {
			// todo stub
		}
	}

	void redo() {
		Bundle prevCommand = redoStack.pop();
		undoStack.push(prevCommand);
		parseUserInputs(prevCommand);
	}

	public Bundle ackFromLogic(String statusIn, String messageIn, Task taskToAck) {
		Bundle ackBundle = new Bundle();
		ackBundle.putString(status, statusIn);
		ackBundle.putString(message, messageIn);
		ackBundle.putObject(task, taskToAck);
		return ackBundle;
	}

	public Bundle parseUserInputs(Bundle userIn) {
		try {
			database = new Database();
		} catch (IOException e) {
			Bundle b = new Bundle();
			b.putString(status, failure);
			b.putString(message, "DB IO exception");
			b.putObject(task, null);
			return b;
		}
		String commandType = (String)userIn.getItem(user_command);
		Bundle status = new Bundle();
		if (commandType.equalsIgnoreCase("add")) {
			redoStack = new Stack<Bundle>();
			undoStack.push(userIn);
			status = addTask(userIn, database);
			return status;
		} else if (commandType.equalsIgnoreCase("display")) {
			displayTasks(database);
			status = ackFromLogic(null, null, null);
			return status;
		} else if (commandType.equalsIgnoreCase("edit")) {
			redoStack = new Stack<Bundle>();
			undoStack.push(userIn);
			editTask(userIn, database);
			return status;
		} else if (commandType.equalsIgnoreCase("delete")) {
			redoStack = new Stack<Bundle>();
			undoStack.push(userIn);
			String taskToDelete = (String)userIn.getItem(user_title);
			status = deleteTask(taskToDelete, database);
			return status;
		} else if (commandType.equalsIgnoreCase("undo")) {
			return status;
		} else if (commandType.equalsIgnoreCase("redo")) {
			return status;
		} else {
			return status;
		}
	}
}