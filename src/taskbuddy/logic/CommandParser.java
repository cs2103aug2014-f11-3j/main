package taskbuddy.logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

import taskbuddy.database.Database;

//Author: andrew
public class CommandParser {
	
	private Database database;
	private Stack<ArrayList<String>> undoStack = new Stack<ArrayList<String>>();
	private Stack<ArrayList<String>> redoStack = new Stack<ArrayList<String>>();
	private Stack<ArrayList<String>> editStack = new Stack<ArrayList<String>>();

	// if new command is parsed, clear redo stack;

	Bundle addTask(ArrayList<String> extras, Database db) {
		String desc = extras.get(1);
		String endDate = extras.get(2);
		String endTime = extras.get(3);
		String title = extras.get(4);
		Task newTask = new Task(title);
		newTask.setDescription(desc);
		newTask.setEndTime(endDate, endTime);
		boolean result = db.addTask(newTask);
		Bundle acknowledgement = new Bundle();
		if (result) {
			acknowledgement = ackFromLogic("Success", null, newTask);
		} else {
			acknowledgement = ackFromLogic("Failure", "Add failure", newTask);
		}
		return acknowledgement;
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

	Bundle editTask(ArrayList<String> extras, Database db) {
		String title = extras.get(4);
		Bundle ack = new Bundle();
		Bundle foundTask = deleteTask(title, db);
		if (foundTask.getItem("status").equals("Success")) {
			ack = addTask(extras, db);
		} else {
			ack = ackFromLogic("Failed", "Nonexistent task", null);
		}
		return ack;
	}

	Bundle deleteTask(String title, Database db) {
		Bundle ack = new Bundle();
		boolean result = db.delete(title);
		if (result) {
			ack = ackFromLogic("Success", null, null);
		} else {
			ack = ackFromLogic("Failure", "Nonexistent task", null);
		}
		return ack;
	}

	void undo(Database db) {
		ArrayList<String> prevCommand = undoStack.pop();
		redoStack.push(prevCommand);
		String commandType = prevCommand.get(0);
		if (commandType.equalsIgnoreCase("add")) {
			deleteTask(prevCommand.get(1), db);
		}
		if (commandType.equalsIgnoreCase("delete")) {
			addTask(prevCommand, db);
		}
		if (commandType.equalsIgnoreCase("edit")) {
			// todo stub
		}
	}

	void redo() {
		ArrayList<String> prevCommand = redoStack.pop();
		undoStack.push(prevCommand);
		userInputs(prevCommand);
	}

	public Bundle ackFromLogic(String status, String message, Task taskToAck) {
		Bundle ackBundle = new Bundle();
		ackBundle.putString("status", status);
		ackBundle.putString("message", message);
		ackBundle.putObject("task", taskToAck);
		return ackBundle;
	}

	public Bundle userInputs(ArrayList<String> userIn) {
		try{
			database = new Database();
		} catch (IOException e){
			Bundle b = new Bundle();
			b.putString("status", "failure");
			b.putString("message", "DB IO exception");
			b.putObject("task", null);
			return b;
		}
		String commandType = userIn.get(0);
		Bundle status = new Bundle();
		if (commandType.equalsIgnoreCase("add")) {
			redoStack = new Stack<ArrayList<String>>();
			undoStack.push(userIn);
			status = addTask(userIn, database);
			return status;
		} else if (commandType.equalsIgnoreCase("display")) {
			displayTasks(database);
			status = ackFromLogic(null, null, null);
			return status;
		} else if (commandType.equalsIgnoreCase("edit")) {
			redoStack = new Stack<ArrayList<String>>();
			undoStack.push(userIn);
			editTask(userIn, database);
			return status;
		} else if (commandType.equalsIgnoreCase("delete")) {
			redoStack = new Stack<ArrayList<String>>();
			undoStack.push(userIn);
			String taskToDelete = userIn.get(4);
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