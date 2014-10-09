package taskbuddy.logic;

import java.io.IOException;

import org.junit.Test;

import taskbuddy.database.Database;

public class CommandParserTest {
	CommandParser cp = new CommandParser();
	
	private String user_command = "command";
	private String user_description = "description";
	private String user_endDate = "endDate";
	private String user_start = "startTime";
	private String user_endTime = "endTime";
	private String user_title = "title";
	private Database db;
	
	@Test
	public void testAdd() {
		try {
			db = new Database();
		} catch (IOException e) {
			Bundle b = new Bundle();
			b.putString("status", "failure");
			b.putString("message", "DB IO exception");
			b.putObject("task", null);
			//assertEquals(expected, returnvalue);
		}
		Bundle addUserInputs = new Bundle();
		addUserInputs.putString(user_command, "add");
		addUserInputs.putString(user_description, "test description");
		addUserInputs.putString(user_endDate, "10 10 14");
		addUserInputs.putString(user_start, "padding value");
		addUserInputs.putString(user_endTime, "2359");
		addUserInputs.putString(user_title, "test title");
		Bundle d = cp.addTask(addUserInputs, db);
		//assertEquals(expected, d);
	}

	public void testDelete(){
		try {
			db = new Database();
		} catch (IOException e) {
			Bundle b = new Bundle();
			b.putString("status", "failure");
			b.putString("message", "DB IO exception");
			b.putObject("task", null);
			//assertEquals(expected, returnvalue);
		}
		Bundle addUserInputs = new Bundle();
		addUserInputs.putString(user_command, "delete");
		addUserInputs.putString(user_description, "test description");
		addUserInputs.putString(user_endDate, "10 10 14");
		addUserInputs.putString(user_start, "padding value");
		addUserInputs.putString(user_endTime, "2359");
		addUserInputs.putString(user_title, "test title");
		String title = (String) addUserInputs.getItem(user_title);
		Bundle d = cp.deleteTask(title, db);
		//cant test because db is empty
	}
}
