package taskbuddy.logic;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Stack;

import taskbuddy.database.Database;

//Author: andrew
public class CommandParser {

	private Database database;
	private Stack<Bundle> undoStack = new Stack<Bundle>();
	private Stack<Bundle> redoStack = new Stack<Bundle>();
	private Stack<Bundle> editStack = new Stack<Bundle>();
	private static String nullValue = "padding value";
	// if new command is parsed, clear redo stack;
	// user bundle strings
	private String user_command = "command";
	private String user_description = "description";
	private String user_endDate = "endDate";
	private String user_start = "startTime";
	private String user_endTime = "endTime";
	private String user_title = "title";
	private String user_flag = "flag";
	private String user_priority = "priority";
	private String user_googleID = "GoogleID";
	// acknowledge bundle strings
	private String status = "Status";
	private String success = "Success";
	private String failure = "Failure";
	private String message = "Message";
	private String task = "Task";

	Bundle addTask(Bundle extras, Database db) throws IOException {
		String desc = (String) extras.getItem(user_description);
		String endDate = (String) extras.getItem(user_endDate);
		String endTime = (String) extras.getItem(user_endTime);
		String title = (String) extras.getItem(user_title);
		Task newTask = new Task(title);
		String startTime = (String) extras.getItem(user_start);
		newTask.setDescription(desc);
		newTask.setStartTime(endDate, startTime);
		newTask.setEndTime(endDate, endTime);
		newTask.setGID(nullValue);
		Bundle response = db.addTask(newTask);
		Bundle acknowledgement = new Bundle();
		String result = (String) response.getItem(status);
		if (result.equals(success)) {
			acknowledgement = ackFromLogic(success, null, newTask);
		} else {
			acknowledgement = ackFromLogic(failure, "Add failure", newTask);
		}
		return acknowledgement;
	}

	Bundle deleteTask(String title, Database db) throws IOException {
		Bundle ack = new Bundle();
		Bundle response = db.delete(title);
		String result = (String) response.getItem(status);
		if (result.equals(success)) {
			ack = ackFromLogic(success, null, null);
		} else {
			ack = ackFromLogic(failure, "Nonexistent task", null);
		}
		return ack;
	}

	Bundle editTask(Bundle extras, Database db) throws IOException {
		String title = (String) extras.getItem(user_title);
		Bundle ack = new Bundle();
		Task toEdit = db.search(title);
		if (toEdit != null) {
			Bundle editInfo = toEdit.getTaskInfo();
			editStack.push(editInfo);
			String newDesc = (String) extras.getItem(user_description);
			String newEndDate = (String) extras.getItem(user_endDate);
			String newEndTime = (String) extras.getItem(user_endTime);
			Task toAdd = new Task();
			toAdd.setTitle(title);
			if (!newDesc.equals(nullValue)) {
				toAdd.setDescription(newDesc);
			} else {
				String oldDesc = (String) editInfo.getItem(user_description);
				toAdd.setDescription(oldDesc);
			}
			if (!newEndDate.equals(nullValue)) {
				toAdd.setEndTime(newEndDate, newEndTime);
			} else {
				String oldDate = (String) editInfo.getItem(user_endDate);
				String oldTime = (String) editInfo.getItem(user_endTime);
				toAdd.setEndTime(oldDate, oldTime);
			}
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

	Bundle printTask(Task task) {
		Bundle taskInfo = new Bundle();
		taskInfo = task.getTaskInfo();
		return taskInfo;
	}

	Bundle undo(Database db) throws IOException {
		if (!undoStack.isEmpty()) {
			Bundle prevCommand = undoStack.pop();
			redoStack.push(prevCommand);
			String commandType = (String) prevCommand.getItem(user_command);
			if (commandType.equalsIgnoreCase("add")) {
				String delete_description = (String) prevCommand
						.getItem(user_description);
				deleteTask(delete_description, db);
			} else if (commandType.equalsIgnoreCase("delete")) {
				addTask(prevCommand, db);
			} else if (commandType.equalsIgnoreCase("edit")) {
				// todo stub
			} else {
				Bundle acks = ackFromLogic(failure, "fatal error: invalid undo", null);
				return acks;
			}
			Bundle acks = ackFromLogic(success, "Undone", null);
			return acks;
		} else {
			Bundle acks = ackFromLogic(failure, "Undo stack empty", null);
			return acks;
		}
	}

	Bundle redo() throws ParseException, IOException {
		if (!redoStack.isEmpty()) {
			Bundle prevCommand = redoStack.pop();
			undoStack.push(prevCommand);
			parseUserInputs(prevCommand);
			String commandType = (String) prevCommand.getItem(user_command);
			if (commandType.equalsIgnoreCase("add")) {
				parseUserInputs(prevCommand);
			} else if (commandType.equalsIgnoreCase("delete")){
				parseUserInputs(prevCommand);
			} else if (commandType.equalsIgnoreCase("edit")){
				//todo edit stub
			} else {
				Bundle acks = ackFromLogic(failure, "fatal error: invalid redo", null);
				return acks;
			}
			Bundle acks = ackFromLogic(success, "Redid", null);
			return acks;
		} else {
			Bundle acks = ackFromLogic(failure, "Redo stack empty", null);
			return acks;
		}
	}

	Bundle ackFromLogic(String statusIn, String messageIn, Task taskToAck) {
		Bundle ackBundle = new Bundle();
		ackBundle.putString(status, statusIn);
		ackBundle.putString(message, messageIn);
		ackBundle.putObject(task, taskToAck);
		return ackBundle;
	}

	public Bundle parseUserInputs(Bundle userIn) throws ParseException, IOException {
		try {
			database = new Database();
		} catch (IOException e) {
			Bundle b = new Bundle();
			b.putString(status, failure);
			b.putString(message, "DB IO exception");
			b.putObject(task, null);
			return b;
		}
		String commandType = (String) userIn.getItem(user_command);
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
			String taskToDelete = (String) userIn.getItem(user_title);
			status = deleteTask(taskToDelete, database);
			return status;
		} else if (commandType.equalsIgnoreCase("undo")) {
			undo(database);
			return status;
		} else if (commandType.equalsIgnoreCase("redo")) {
			return status;
		} else {
			return status;
		}
	}
}