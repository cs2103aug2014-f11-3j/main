package taskbuddy.file;
import java.util.ArrayList;
import java.util.Stack;

public class CommandParser {

	private Stack<ArrayList<String>> commandStack = new Stack<ArrayList<String>>();
	private Stack<ArrayList<String>> redoStack = new Stack<ArrayList<String>>();
	// if new command is parsed, clear redo stack;

	static enum Command {
		CREATE(1), READ(2), UDPATE(3), DELETE(4), UNDO(5), REDO(6);

		private int code;

		private Command(int c) {
			code = c;
		}

		public int getCode() {
			return code;
		}

		@Override
		public String toString() {
			String s = super.toString();
			return s.substring(0, 1) + s.substring(1).toLowerCase();
		}
	}

	public enum Acknowledge {

	}

	public Task createTask() {
		Task task = new Task();

		return task;
	}

	public void putTask(Task task) {
		//Database.addTask(task);
	}
	
	public void readTask(String title){
		
	}
	
	public void deleteTask(String title){
		
	}

	public void userInputs(ArrayList<String> userIn) {
		String commandType = userIn.get(0);
		if (commandType.equalsIgnoreCase("add")) {
			redoStack = new Stack<ArrayList<String>>();
			commandStack.add(userIn);
			String desc = userIn.get(1);
			String endDate = userIn.get(2);
			String endTime = userIn.get(3);
			String title = userIn.get(4);
			
		}
		if (commandType.equalsIgnoreCase("display")) {
			String title = userIn.get(4);
			//Task newTask = readTask(title);
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
