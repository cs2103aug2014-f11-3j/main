package taskbuddy.logic;
import java.util.ArrayList;
import java.util.Stack;


//Author: andrew
public class CommandParser {

	private Stack<ArrayList<String>> undoStack = new Stack<ArrayList<String>>();
	private Stack<ArrayList<String>> redoStack = new Stack<ArrayList<String>>();
	private Stack<ArrayList<String>> editStack = new Stack<ArrayList<String>>();
	// if new command is parsed, clear redo stack;

	Task addTask(ArrayList<String> extras) {
		String desc = extras.get(1);
		String endDate = extras.get(2);
		String endTime = extras.get(3);
		String title = extras.get(4);
		Task newTask = new Task(title);
		newTask.setDescription(desc);
		newTask.setEndTime(endDate, endTime);
		return newTask;
	}
	
	void displayTasks(){
		/*
		Stack<Task> tempCache = Database.getAllTasks();
		while(!tempCache.isEmpty()){
			Task current = tempCache.pop();
			printTask(current);
		}
		*/
	}
	
	void printTask(Task task){
		//GUI.print(task);
	}
	
	void editTask(String title){
		
	}
	
	void deleteTask(String title){
		
	}
	
	void undo(){
		ArrayList<String> prevCommand = undoStack.pop();
		redoStack.push(prevCommand);
		String commandType = prevCommand.get(0);
		if (commandType.equalsIgnoreCase("add")){
			deleteTask(prevCommand.get(1));
		} if (commandType.equalsIgnoreCase("delete")){
			addTask(prevCommand);
		} if (commandType.equalsIgnoreCase("edit")){
			
		}
	}
	
	void redo(){
		ArrayList<String> prevCommand = redoStack.pop();
		undoStack.push(prevCommand);
		userInputs(prevCommand);
	}

	public void userInputs(ArrayList<String> userIn) {
		String commandType = userIn.get(0);
		if (commandType.equalsIgnoreCase("add")) {
			redoStack = new Stack<ArrayList<String>>();
			undoStack.push(userIn);
			Task newTask = addTask(userIn);
			//Database.add(newTask);
		}
		if (commandType.equalsIgnoreCase("display")) {
			
		}
		if (commandType.equalsIgnoreCase("edit")) {
			redoStack = new Stack<ArrayList<String>>();
			undoStack.push(userIn);
		}
		if (commandType.equalsIgnoreCase("delete")) {
			redoStack = new Stack<ArrayList<String>>();
			undoStack.push(userIn);
		}
		if (commandType.equalsIgnoreCase("undo")) {

		}
		if (commandType.equalsIgnoreCase("redo")) {
			
		}
	}
}