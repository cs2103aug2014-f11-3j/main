package taskbuddy.logic;
import java.util.ArrayList;
import java.util.Stack;


//Author: andrew
public class CommandParser {

	private Stack<ArrayList<String>> commandStack = new Stack<ArrayList<String>>();
	private Stack<ArrayList<String>> redoStack = new Stack<ArrayList<String>>();
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
		
	}
	
	void printTask(){
		
	}
	
	void readTask(String title){
		
	}
	
	void deleteTask(String title){
		
	}

	public void userInputs(ArrayList<String> userIn) {
		String commandType = userIn.get(0);
		if (commandType.equalsIgnoreCase("add")) {
			redoStack = new Stack<ArrayList<String>>();
			commandStack.add(userIn);
			Task newTask = addTask(userIn);
			//Database.add(newTask);
		}
		if (commandType.equalsIgnoreCase("display")) {
			
		}
		if (commandType.equalsIgnoreCase("edit")) {
			redoStack = new Stack<ArrayList<String>>();
			commandStack.add(userIn);
		}
		if (commandType.equalsIgnoreCase("delete")) {
			redoStack = new Stack<ArrayList<String>>();
			commandStack.add(userIn);
		}
		if (commandType.equalsIgnoreCase("undo")) {

		}
		if (commandType.equalsIgnoreCase("redo")) {
			
		}
	}
}