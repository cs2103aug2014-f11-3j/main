package taskbuddy.logic;

import java.util.ArrayList;
import java.util.Stack;

import taskbuddy.database.Database;

public class RedoCommand {

	public static AcknowledgeBundle redo(Stack<UserInputBundle> undoStack,
			Stack<UserInputBundle> redoStack, Stack<Task> undoStackTask,
			Stack<Task> redoStackTask, Database db) {

		AcknowledgeBundle ack = new AcknowledgeBundle();
		try {
			UserInputBundle commandToRedo = redoStack.pop();
			undoStack.push(commandToRedo);
			String commandType = commandToRedo.getCommand();
			try {
				if (commandType.equals("add")) {
					ack = AddCommand.addTask(commandToRedo, db);
					ack.putMessage("add redid");
				} else if (commandType.equals("delete")){
					int id = Integer.parseInt(commandToRedo.getTaskID());
					ack = DeleteCommand.deleteTask(id, db);
					ack.putMessage("delete redid");
				} else if (commandType.equals("edit")){
					Task afterEdit = redoStackTask.pop();
					Task beforeEdit = redoStackTask.pop();
					db.edit(afterEdit);
					undoStackTask.push(beforeEdit);
					undoStackTask.push(afterEdit);
					ack.putSuccess();
					ack.putMessage("edit redid");
					ack.putOldTask(beforeEdit);
					ack.putTask(afterEdit);
				}
			} catch (Exception e) {
				ack.putFailure();
				ack.putMessage("redo failure");
			}
		} catch (Exception e) {
			ack.putFailure();
			ack.putMessage("redo stack empty");
		}
		return ack;
	}

}
