//@author A0110649J
//andrew

package taskbuddy.logic;
import java.util.ArrayList;

import taskbuddy.database.Database;

public class DeleteCommand {

	public static AcknowledgeBundle deleteTask(UserInputBundle inputs, Database db){
		int id = Integer.parseInt(inputs.getTaskID());
		AcknowledgeBundle ack = new AcknowledgeBundle();
		try{
			Task t = db.read(id);
			db.delete(id);
			CommandParser cp = CommandParser.getInstance();
			ack.putSuccess();
			ack.putMessage("task deleted successfully");
			ack.putTask(t);
			cp.initRedo();
			cp.pushUndo(inputs);
			cp.pushUndoTask(t);
		} catch (Exception e){
			ack.putFailure();
			ack.putMessage("Unable to delete task");
		}
		return ack;
	}
	
}
