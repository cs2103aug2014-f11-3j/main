package taskbuddy.logic;

import java.io.IOException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Stack;

import taskbuddy.database.Database;

//Author: andrew

public class CommandParser {
	
	private static CommandParser instance;
	
	protected static Database database;
	protected static Stack<UserInputBundle> undoStack;
	protected static Stack<Task> undoStackTask;
	protected static Stack<UserInputBundle> redoStack;
	protected static Stack<Task> redoStackTask;

	public AcknowledgeBundle parseUserInputs(UserInputBundle userIn) {
		AcknowledgeBundle ack = new AcknowledgeBundle();
		try {
		} catch (Exception e){ System.err.println("nothing");}
		try {
			String commandType = userIn.getCommand();
			if (commandType.equals("add")) {
				ack = AddCommand.addTask(userIn, database);
			} else if (commandType.equals("delete")) {
				ack = DeleteCommand.deleteTask(userIn, database);
				Task t = ack.getTask();
				this.pushUndoTask(t);
			} else if (commandType.equals("edit")) {
				ack = EditCommand.editTask(userIn, database);
				Task oldt = ack.getOldTask();
				Task newt = ack.getTask();
				this.pushUndoTask(oldt);
				this.pushUndoTask(newt);
			} else if (commandType.equals("display")) {
				ack = DisplayCommand.displayAllTasks(database);
			} else if (commandType.equals("search")) {
				ack = SearchCommand.searchForTasks(userIn, database);
			} else if (commandType.equals("undo")){
				ack = UndoCommand.undo();
			} else if (commandType.equals("redo")){
				ack = RedoCommand.redo();
			} else if (commandType.equals("sync")){
				ack = SyncCommand.SyncCalendars(database);
			} else {
				ack.putFailure();
				ack.putMessage("invalid command");
			}
		} catch (Exception e) {
			ack.putFailure();
			ack.putMessage(e.getMessage());
		}
		return ack;
	}

	//deprecated
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

	//deprecated
	protected CommandParser() throws ParseException, IOException {
		database = Database.getInstance();
		undoStack = new Stack<UserInputBundle>();
		undoStackTask = new Stack<Task>();
		redoStack = new Stack<UserInputBundle>();
		redoStackTask = new Stack<Task>();
	}

	protected void pushUndo(UserInputBundle command){
		undoStack.push(command);
	}
	
	protected void pushUndoTask(Task item){
		undoStackTask.push(item);
	}
	
	protected void pushRedo(UserInputBundle command){
		redoStack.push(command);
	}
	
	protected void pushRedoTask(Task item){
		redoStackTask.push(item);
	}
	
	protected void initRedo(){
		redoStack = new Stack<UserInputBundle>();
		redoStackTask = new Stack<Task>();
	}
	
	protected UserInputBundle getUndo(){
		return undoStack.pop();
	}
	
	protected UserInputBundle getRedo(){
		return redoStack.pop();
	}
	
	protected Task getUndoTask(){
		return undoStackTask.pop();
	}
	
	protected Task getRedoTask(){
		return redoStackTask.pop();
	}
	
	protected UserInputBundle peekUndo(){
		return undoStack.peek();
	}
	public static CommandParser getInstance() throws ParseException, IOException{
		if (instance == null){
			instance = new CommandParser();
			database = Database.getInstance();
			undoStack = new Stack<UserInputBundle>();
			undoStackTask = new Stack<Task>();
			redoStack = new Stack<UserInputBundle>();
			redoStackTask = new Stack<Task>();
		}
		return instance;
	}
	
	public Database getDatabase() {
		return database;
	}
}
