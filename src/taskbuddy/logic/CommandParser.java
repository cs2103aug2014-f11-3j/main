package taskbuddy.logic;

import java.io.IOException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Stack;

import taskbuddy.database.Database;

//Author: andrew

public class CommandParser {
	private Database database;
	private Stack<UserInputBundle> undoStack;
	private Stack<Task> undoStackTask;
	private Stack<UserInputBundle> redoStack;
	private Stack<Task> redoStackTask;

	public AcknowledgeBundle parseUserInputs(UserInputBundle userIn) {
		AcknowledgeBundle ack = new AcknowledgeBundle();
		try {
			String commandType = userIn.getCommand();
			if (commandType.equals("add")) {
				preProcess(userIn);
				ack = AddCommand.addTask(userIn, database);
				Task t = ack.getTask();
				undoStackTask.push(t);
			} else if (commandType.equals("delete")) {
				preProcess(userIn);
				int id = Integer.parseInt(userIn.getTaskID());
				ack = DeleteCommand.deleteTask(id, database);
				Task t = ack.getTask();
				undoStackTask.push(t);
			} else if (commandType.equals("edit")) {
				preProcess(userIn);
				ack = EditCommand.editTask(userIn, database);
				Task oldt = ack.getOldTask();
				Task newt = ack.getTask();
				undoStackTask.push(oldt);
				undoStackTask.push(newt);
			} else if (commandType.equals("display")) {
				ack = DisplayCommand.displayAllTasks(database);
			} else if (commandType.equals("search")) {
				ack = SearchCommand.searchForTasks(userIn, database);
			} else if (commandType.equals("undo")){
				ack = UndoCommand.undo(undoStack, redoStack, undoStackTask, redoStackTask, database);
			} else if (commandType.equals("redo")){
				ack = RedoCommand.redo(undoStack, redoStack, undoStackTask, redoStackTask, database);
			} else if (commandType.equals("sync")){
				ack = SyncCommand.SyncCalendars(database);
			} else {
				ack.putFailure();
				ack.putMessage("invalid command");
			}
		} catch (Exception e) {
			ack.putFailure();
			ack.putMessage(e.getCause().getMessage());
		}
		return ack;
	}

	private void preProcess(UserInputBundle u) {
		redoStack = new Stack<UserInputBundle>();
		redoStackTask = new Stack<Task>();
		undoStack.push(u);
	}
	
	private String parseDate(Calendar cal){
		String s = cal.getTime().toString();
		String[] calInfo = s.split(" ");
		String time = calInfo[3].substring(0,5);
		String toReturn = calInfo[0] + " " + calInfo[1] + " " + calInfo[2] + ", " + time;
		return toReturn;
	}

	public CommandParser() throws ParseException, IOException {
		database = Database.getInstance();
		undoStack = new Stack<UserInputBundle>();
		undoStackTask = new Stack<Task>();
		redoStack = new Stack<UserInputBundle>();
		redoStackTask = new Stack<Task>();
	}

	public Database getDatabase() {
		return database;
	}
}
