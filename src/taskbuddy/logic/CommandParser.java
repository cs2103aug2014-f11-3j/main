package taskbuddy.logic;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Stack;

import taskbuddy.database.Database;

//Author: andrew
public class CommandParser {

	private Database database;
	private Stack<UserInputBundle> undoStack = new Stack<UserInputBundle>();
	private Stack<UserInputBundle> redoStack = new Stack<UserInputBundle>();
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

	/*AcknowledgeBundle searchTask(UserInputBundle extras, Database db) {
		AcknowledgeBundle ack = new AcknowledgeBundle();
		String title = extras.getTitle();
		String desc = extras.getDescription();
		ArrayList<Task> searchResults = new ArrayList<Task>();
		try {
			if (!title.equals(nullValue)) {
				searchResults = db.search(title);
			} else if (!desc.equals(nullValue)) {
				searchResults = db.search(desc);
			}
		} catch (IllegalAccessException e) {
			ack.putFailure();
			ack.putMessage("Reading tasks from null");
		}
		ArrayList<String> showToUser = concatTaskList(searchResults);
		ack.putSuccess();
		ack.putMessage("search success");
		ack.putList(showToUser);
		return ack;
	}

	ArrayList<String> concatTaskList(ArrayList<Task> listT) {
		ArrayList<String> toReturn = new ArrayList<String>();
		int listLength = listT.size();
		if (listLength == 0) {
			toReturn.add("No results found");
		} else {
			for (int i = 0; i < listLength; i++) {
				Task current = listT.get(i);
				String title = current.getTitle();
				int ID = current.getTaskId();
				String toAdd = "Task " + title + " with ID: " + ID;
				toReturn.add(toAdd);
			}
		}
		return toReturn;
	}*/

	AcknowledgeBundle addTask(UserInputBundle extras, Database db)
			throws IOException {
		
		String desc = extras.getDescription();
		String endDate = extras.getEndDate();
		String endTime = extras.getEndTime();
		String title = extras.getTitle();
		Task newTask = new Task(title);
		String startTime = extras.getStartTime();
		String startDate = extras.getStartDate();
		newTask.setDescription(desc);
		newTask.setStartTime(startDate, startTime);
		newTask.setEndTime(endDate, endTime);
		newTask.setGID(nullValue);
		assert newTask != null;
		AcknowledgeBundle ack = new AcknowledgeBundle();
		try {
			db.addTask(newTask);
			ack.putSuccess();
			ack.putMessage("added successfully");
			ack.putTask(newTask);
		} catch (IOException e) {
			ack.putFailure();
			ack.putMessage("add command failure");
			ack.putTask(newTask);
		}
		assert ack != null;
		return ack;
	}

	AcknowledgeBundle deleteTask(int ID, Database db) throws IOException,
			IllegalAccessException, NoSuchElementException {
		AcknowledgeBundle ack = new AcknowledgeBundle();
		try {
			db.delete(ID);
			ack.putSuccess();
			ack.putMessage("deletion success");
		} catch (NoSuchElementException e) {
			ack.putFailure();
			ack.putMessage("no such element in db");
		}
		assert ack != null;
		return ack;
	}

	AcknowledgeBundle editTask(UserInputBundle extras, Database db)
			throws IOException, NoSuchElementException, IllegalAccessException {
		String title = (String) extras.getItem(user_title);
		AcknowledgeBundle ack = new AcknowledgeBundle();
		int ID = Integer.parseInt(extras.getTaskID());
		try {
			Task toEdit = new Task();
			toEdit = db.read(ID);
			Bundle editInfo = toEdit.getTaskInfo();
			editStack.push(editInfo);
			String newTitle = extras.getTitle();
			String newDesc = extras.getDescription();
			String newEndDate = extras.getEndDate();
			String newEndTime = extras.getEndTime();
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
			ack.putSuccess();
			ack.putMessage("editted task successfully");
		} catch (NoSuchElementException e) {
			ack.putFailure();
			ack.putMessage("no such element in db");
		}
		assert ack != null;
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
				// TODO STUB
			} else {
				Bundle acks = ackFromLogic(failure,
						"fatal error: invalid undo", null);
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
			} else if (commandType.equalsIgnoreCase("delete")) {
				parseUserInputs(prevCommand);
			} else if (commandType.equalsIgnoreCase("edit")) {
				// todo edit stub
			} else {
				Bundle acks = ackFromLogic(failure,
						"fatal error: invalid redo", null);
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

	public AcknowledgeBundle parseUserInputs(Bundle userIn)
			throws ParseException, IOException {
		try {
			database = new Database();
		} catch (IOException e) {
			AcknowledgeBundle b = new AcknowledgeBundle();
			b.putFailure();
			b.putMessage("DB IO exception");
			assert b != null;
			return b;
		}
		String commandType = (String) userIn.getItem(user_command);
		AcknowledgeBundle status = new AcknowledgeBundle();
		if (commandType.equalsIgnoreCase("add")) {
			redoStack = new Stack<Bundle>();
			undoStack.push(userIn);
			status = addTask(userIn, database);
			assert status != null;
			return status;
		} else if (commandType.equalsIgnoreCase("display")) {
			try {
				displayTasks(database);
				status.putSuccess();
			} catch (IOException e) {
				status.putFailure();
			}
			assert status.getStatus() != null;
			return status;
		} else if (commandType.equalsIgnoreCase("edit")) {
			redoStack = new Stack<Bundle>();
			undoStack.push(userIn);
			try {
				editTask(userIn, database);
				status.putSuccess();
			} catch (IOException e) {
				status.putFailure();
			}
			assert status.getStatus() != null;
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