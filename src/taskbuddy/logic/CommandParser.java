package taskbuddy.logic;

import java.util.ArrayList;
import java.util.Stack;

import taskbuddy.database.Database;

//Author: andrew
public class CommandParser {

	private Database database = new Database();
	private Stack<ArrayList<String>> undoStack = new Stack<ArrayList<String>>();
	private Stack<ArrayList<String>> redoStack = new Stack<ArrayList<String>>();
	private Stack<ArrayList<String>> editStack = new Stack<ArrayList<String>>();

	// if new command is parsed, clear redo stack;

	Bundle addTask(ArrayList<String> extras) {
		String desc = extras.get(1);
		String endDate = extras.get(2);
		String endTime = extras.get(3);
		String title = extras.get(4);
		Task newTask = new Task(title);
		newTask.setDescription(desc);
		newTask.setEndTime(endDate, endTime);
		boolean result = database.addTask(newTask);
		Bundle acknowledgement = new Bundle();
		if (result) {
			acknowledgement = ackFromLogic("Success", null, newTask);
		} else {
			acknowledgement = ackFromLogic("Failure", "Add failure", newTask);
		}
		return acknowledgement;
	}

	void displayTasks() {
		ArrayList<Task> tempCache = database.getTasks();
		for (Task task : tempCache) {
			printTask(task);
		}
	}

	ArrayList<String> printTask(Task task) {
		ArrayList<String> taskInfo = new ArrayList<String>();
		taskInfo = task.getTaskInfo();
		return taskInfo;
	}

	Bundle editTask(ArrayList<String> extras) {
		String title = extras.get(4);
		Bundle ack = new Bundle();
		Bundle foundTask = deleteTask(title);
		if (foundTask.getItem("status").equals("Success")) {
			ack = addTask(extras);
		} else {
			ack = ackFromLogic("Failed", "Nonexistent task", null);
		}
		return ack;
	}

	Bundle deleteTask(String title) {
		Bundle ack = new Bundle();
		boolean result = database.delete(title);
		if (result) {
			ack = ackFromLogic("Success", null, null);
		} else {
			ack = ackFromLogic("Failure", "Nonexistent task", null);
		}
		return ack;
	}

	void undo() {
		ArrayList<String> prevCommand = undoStack.pop();
		redoStack.push(prevCommand);
		String commandType = prevCommand.get(0);
		if (commandType.equalsIgnoreCase("add")) {
			deleteTask(prevCommand.get(1));
		}
		if (commandType.equalsIgnoreCase("delete")) {
			addTask(prevCommand);
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

	void sendAck(Bundle b) {
		// send to gui
	}

	public Bundle userInputs(ArrayList<String> userIn) {
		String commandType = userIn.get(0);
		Bundle status = new Bundle();
		if (commandType.equalsIgnoreCase("add")) {
			redoStack = new Stack<ArrayList<String>>();
			undoStack.push(userIn);
			status = addTask(userIn);
			return status;
		} else if (commandType.equalsIgnoreCase("display")) {
			displayTasks();
			status = ackFromLogic(null, null, null);
			return status;
		} else if (commandType.equalsIgnoreCase("edit")) {
			redoStack = new Stack<ArrayList<String>>();
			undoStack.push(userIn);
			editTask(userIn);
			return status;
		} else if (commandType.equalsIgnoreCase("delete")) {
			redoStack = new Stack<ArrayList<String>>();
			undoStack.push(userIn);
			String taskToDelete = userIn.get(4);
			status = deleteTask(taskToDelete);
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