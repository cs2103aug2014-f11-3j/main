package taskbuddy.logic;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import org.junit.After;
import org.junit.Test;

import taskbuddy.database.Database;

public class LogicDatabaseIntegrate {

	private static String nullValue = "padding value";
	private CommandParser cp;
	
	@Test
	public void testAddFloating() throws ParseException, IOException{
		cp = new CommandParser();
		UserInputBundle uf = new UserInputBundle();
		AcknowledgeBundle bf = new AcknowledgeBundle();
		uf.putTitle("task floating");
		uf.putDescription("test task for integration");
		uf.putEndDate(nullValue);
		uf.putEndTime(nullValue);
		uf.putStartDate(nullValue);
		uf.putStartTime(nullValue);
		uf.putCommand("add");
		cp.parseUserInputs(uf);
		Database db = cp.getDatabase();
		db.printTasks();
		String status = bf.getStatus();
		assertEquals("Success", status);
	}
	/*
	@After
	public void deleteLog(){
		File log = new File("log");
		if (log.isFile()){
			log.delete();
		}
	}
	*/
	
}
