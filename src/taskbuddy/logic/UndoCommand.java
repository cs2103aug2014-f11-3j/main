//@author A0110649J
//andrew

package taskbuddy.logic;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Stack;

import taskbuddy.database.Database;

public class UndoCommand {

	public static AcknowledgeBundle undo() throws ParseException, IOException {
		CommandParser cp = CommandParser.getInstance();
		Database db = cp.getDatabase();
		AcknowledgeBundle ack = new AcknowledgeBundle();
		try {
			UserInputBundle previousCommand = new UserInputBundle();
			previousCommand = cp.getUndo();
			try{
				System.err.println(previousCommand.getCommand() + "command type");
			} catch (Exception e){
				;
			}
			cp.pushRedo(previousCommand);
			String commandType = previousCommand.getCommand();
			try {
				if (commandType.equals("add")) {
					Task added = cp.getUndoTask();
					cp.pushRedoTask(added);
					ArrayList<Task> allTasks = db.getTasks();
					int size = allTasks.size();
					db.delete((size));
					ack.putSuccess();
					ack.putMessage("add reverted");
					ack.putTask(added);
					
				} else if (commandType.equals("delete")) {
					Task deleted = cp.getUndoTask();
					cp.pushRedoTask(deleted);;
					db.addTask(deleted);
					ack.putSuccess();
					ack.putMessage("delete reverted");
					ack.putTask(deleted);

				} else if (commandType.equals("edit")) {
					Task afterEdit = cp.getUndoTask();
					System.err.println(afterEdit.getTitle() + " after");
					Task beforeEdit = cp.getUndoTask();
					System.err.println(beforeEdit.getTitle() + " before");
					cp.pushRedoTask(beforeEdit);
					cp.pushRedoTask(afterEdit);
					db.edit(beforeEdit);
					ack.putSuccess();
					ack.putMessage("edit reverted");
					ack.putOldTask(afterEdit);
					ack.putTask(beforeEdit);
				} else {
					ack.putFailure();
					ack.putMessage(previousCommand.getCommand() + " not recognized");
				}
			} catch (Exception e) {
				Task t = cp.getRedoTask();
				cp.pushUndoTask(t);
				ack.putFailure();
				ack.putMessage("undo failure");
			}

		} catch (Exception e) {
			ack.putFailure();
			ack.putMessage("no previous commands found");
		}
		return ack;
	}

}
