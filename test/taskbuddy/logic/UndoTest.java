package taskbuddy.logic;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import taskbuddy.database.Database;

public class UndoTest {

	@Test
	public void testAdd() throws Exception {
		try {
			CommandParser cp = CommandParser.getInstance();
			UserInputBundle uf = new UserInputBundle();
			AcknowledgeBundle bf = new AcknowledgeBundle();
			uf.putTitle("task normal");
			uf.putDescription("test task for 2 dates");
			uf.putEndDate("23/10/2014");
			uf.putEndTime("2300");
			uf.putStartDate("23/10/2014");
			uf.putStartTime("2100");
			uf.putCommand("add");
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
	public void testUndo() throws Exception {
		CommandParser cp = CommandParser.getInstance();
		UserInputBundle undo = new UserInputBundle();
		undo.putCommand("undo");
		AcknowledgeBundle b = new AcknowledgeBundle();
		Database db = cp.getDatabase();
		try {
			b = cp.parseUserInputs(undo);
			db.printTasks();
			assertEquals("Success", b.getStatus());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
