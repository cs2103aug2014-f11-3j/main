//@author A0110649J
//andrew

package taskbuddy.logic;

import taskbuddy.database.Database;

public class RedoCommand {

	public static AcknowledgeBundle redo() {

		AcknowledgeBundle ack = new AcknowledgeBundle();
		try {
			CommandParser cp = CommandParser.getInstance();
			Database db = cp.getDatabase();
			UserInputBundle commandToRedo = new UserInputBundle(); 
			commandToRedo = cp.getRedo();
			cp.pushUndo(commandToRedo);
			String commandType = commandToRedo.getCommand();
			try {
				if (commandType.equals("add")) {
					ack = AddCommand.addTask(commandToRedo, db);
					ack.putMessage("add redid");
				} else if (commandType.equals("delete")){
					ack = DeleteCommand.deleteTask(commandToRedo, db);
					ack.putMessage("delete redid");
				} else if (commandType.equals("edit")){
					Task afterEdit = cp.getRedoTask();
					Task beforeEdit = cp.getRedoTask();
					db.edit(afterEdit);
					cp.pushUndoTask(beforeEdit);
					cp.pushUndoTask(afterEdit);
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
