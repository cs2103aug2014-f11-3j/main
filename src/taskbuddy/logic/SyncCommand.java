//@author A0110649J
//andrew

package taskbuddy.logic;

import java.io.IOException;
import java.text.ParseException;

import taskbuddy.database.Database;

public class SyncCommand {

	public static AcknowledgeBundle SyncCalendars() throws ParseException, IOException{
		AcknowledgeBundle acks = new AcknowledgeBundle();
		CommandParser cp = CommandParser.getInstance();
		Database db = cp.getDatabase();
		db.sync();
		return acks;
	}
	
}
