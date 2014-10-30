package taskbuddy.logic;
import java.util.ArrayList;

import taskbuddy.database.Database;

public class DeleteCommand {

	public static AcknowledgeBundle deleteTask(int id, Database db){
		AcknowledgeBundle ack = new AcknowledgeBundle();
		try{
			Task t = db.read(id);
			db.delete(id);
			ack.putSuccess();
			ack.putMessage("task deleted successfully");
			ack.putTask(t);
		} catch (Exception e){
			ack.putFailure();
			ack.putMessage("Unable to delete task");
		}
		return ack;
	}
	
}
