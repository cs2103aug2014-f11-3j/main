//@author A0110649J
//andrew

package taskbuddy.logic;

import taskbuddy.database.Database;

public class SyncCommand {

	public static AcknowledgeBundle SyncCalendars(){
		AcknowledgeBundle acks = new AcknowledgeBundle();
		CommandParser cp = CommandParser.getInstance();
		Database db = cp.getDatabase();
		
		return acks;
	}
	
}
