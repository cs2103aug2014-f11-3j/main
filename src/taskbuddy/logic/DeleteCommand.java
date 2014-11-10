//@author A0110649J
//andrew

package taskbuddy.logic;
import java.io.IOException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;

import taskbuddy.database.Database;

public class DeleteCommand {

	public static AcknowledgeBundle deleteTask(UserInputBundle inputs, Database db) throws ParseException, IOException{
		int id = Integer.parseInt(inputs.getTaskID());
		AcknowledgeBundle ack = new AcknowledgeBundle();
		Task t = new Task();
		CommandParser cp = CommandParser.getInstance();
		try{
			t = db.read(id);
			db.delete(id);
			ack.putSuccess();
			ack.putMessage("task deleted successfully");
			ack.putTask(t);
			cp.initRedo();
			cp.pushUndo(inputs);
			cp.pushUndoTask(t);
		} catch (UnknownHostException e){
			ack.putFailure();
			ack.putMessage("Deleted locally");
			cp.initRedo();
			cp.pushUndo(inputs);
			cp.pushUndoTask(t);
		} catch (Exception e){
			ack.putFailure();
			ack.putMessage("Deleted locally");
		}
		return ack;
	}
	
}
