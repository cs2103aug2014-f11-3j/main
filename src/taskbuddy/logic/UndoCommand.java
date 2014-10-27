package taskbuddy.logic;

import java.util.ArrayList;
import java.util.Stack;

import taskbuddy.database.Database;

public class UndoCommand {

	public static AcknowledgeBundle undo(Stack<UserInputBundle> undoStack,
			Stack<UserInputBundle> redoStack, Stack<Task> undoStackTask,
			Stack<Task> redoStackTask, Database db) {

		AcknowledgeBundle ack = new AcknowledgeBundle();
		try {
			UserInputBundle previousCommand = undoStack.pop();
			redoStack.push(previousCommand);
			String commandType = previousCommand.getCommand();
			try {
				if (commandType.equals("add")) {
					Task added = undoStackTask.pop();
					redoStackTask.push(added);
					ArrayList<Task> allTasks = db.getTasks();
					int size = allTasks.size();
					db.delete((size-1));
					ack.putSuccess();
					ack.putMessage("add reverted");
					ack.putTask(added);
					
				} else if (commandType.equals("delete")) {
					Task deleted = undoStackTask.pop();
					redoStackTask.push(deleted);
					db.addTask(deleted);
					ack.putSuccess();
					ack.putMessage("delete reverted");
					ack.putTask(deleted);

				} else if (commandType.equals("edit")) {
					Task afterEdit = undoStackTask.pop();
					Task beforeEdit = undoStackTask.pop();
					redoStackTask.push(beforeEdit);
					redoStackTask.push(afterEdit);
					db.edit(beforeEdit);
					ack.putSuccess();
					ack.putMessage("edit reverted");
					ack.putOldTask(afterEdit);
					ack.putTask(beforeEdit);
					
				}
			} catch (Exception e) {
				undoStackTask.push(redoStackTask.pop());
				ack.putFailure();
				ack.putMessage("undo failure");
			}

		} catch (Exception e) {
			ack.putFailure();
			ack.putMessage("undo stack empty");
		}
		return ack;
	}

}
