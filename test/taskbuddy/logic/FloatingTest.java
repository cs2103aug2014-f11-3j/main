package taskbuddy.logic;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import org.junit.After;
import org.junit.Test;

import taskbuddy.database.Database;

public class FloatingTest {

	private static String nullValue = "padding value";
	private CommandParser cp;

	@Test
	public void testAddFloating() throws ParseException, IOException {
		try {
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
			pr(uf.getCommand());
			pr(uf.getTitle());
			pr(uf.getDescription());
			bf = cp.parseUserInputs(uf);
			Database db = cp.getDatabase();
			db.printTasks();
			String status = bf.getStatus();
			assertEquals("Success", status);
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}

	public void pr(String s) {
		System.out.println(s);
	}


	@Test
	public void testAddNormal() throws ParseException, IOException {
		try {
			cp = new CommandParser();
			UserInputBundle uf = new UserInputBundle();
			AcknowledgeBundle bf = new AcknowledgeBundle();
			uf.putTitle("task normal");
			uf.putDescription("test task for 2 dates");
			uf.putEndDate("23/10/2014");
			uf.putEndTime("2300");
			uf.putStartDate("23/10/2014");
			uf.putStartTime("2100");
			uf.putCommand("add");
			pr(uf.getCommand());
			pr(uf.getTitle());
			pr(uf.getDescription());
			bf = cp.parseUserInputs(uf);
			Database db = cp.getDatabase();
			db.printTasks();
			String status = bf.getStatus();
			assertEquals("Success", status);
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}
	
	@Test
	public void testAddDeadline() throws ParseException, IOException {
		try {
			cp = new CommandParser();
			UserInputBundle uf = new UserInputBundle();
			AcknowledgeBundle bf = new AcknowledgeBundle();
			uf.putTitle("task deadline today");
			uf.putDescription("test task for deadline");
			uf.putEndDate(nullValue);
			uf.putEndTime("2000");
			uf.putStartDate(nullValue);
			uf.putStartTime("2000");
			uf.putCommand("add");
			pr(uf.getCommand());
			pr(uf.getTitle());
			pr(uf.getDescription());
			bf = cp.parseUserInputs(uf);
			Database db = cp.getDatabase();
		//	db.printTasks();
			String status = bf.getStatus();
			assertEquals("Success", status);
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}
	

	@After
	public void deleteLog() {
		File log = new File("log");
		if (log.isFile()) {
			log.delete();
		}
	}
}
