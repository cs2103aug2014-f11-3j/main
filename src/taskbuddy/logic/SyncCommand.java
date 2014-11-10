//@author A0110649J
//andrew

package taskbuddy.logic;

import java.io.IOException;
import java.text.ParseException;

import taskbuddy.database.Database;

public class SyncCommand {

	public static AcknowledgeBundle SyncCalendars(){
		AcknowledgeBundle acks = new AcknowledgeBundle();
		CommandParser cp = CommandParser.getInstance();
		Database db = cp.getDatabase();
		try {
			db.sync();
			acks.putFailure();
			acks.putMessage("sync success");
		} catch (Exception e){
			acks.putFailure();
			acks.putMessage("sync fail");
		}
		return acks;
	}
	
}
