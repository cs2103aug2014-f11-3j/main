package taskbuddy.logic;
import java.util.ArrayList;

import taskbuddy.database.Database;

public class DeleteCommand {

	public static AcknowledgeBundle deleteTask(int id, Database db){
		AcknowledgeBundle ack = new AcknowledgeBundle();
		try{
			db.delete(id);
			ack.putSuccess();
			ack.putMessage("task deleted successfully");
		} catch (Exception e){
			ack.putFailure();
			ack.putMessage("Unable to delete task");
		}
		return ack;
	}
	
}
