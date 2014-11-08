package taskbuddy.logic;

import taskbuddy.database.Database;

public class SyncCommand {

	public static AcknowledgeBundle SyncCalendars(Database db){
		AcknowledgeBundle acks = new AcknowledgeBundle();
		// TODO try db.sync();
		
		return acks;
	}
	
}
