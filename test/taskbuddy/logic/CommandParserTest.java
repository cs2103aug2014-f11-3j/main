package taskbuddy.logic;

import java.util.ArrayList;

import org.junit.Test;

public class CommandParserTest {
	CommandParser cp = new CommandParser();
	
	Bundle addUserInputs = new Bundle();
	
	@Test
	public void testAdd() {
		cp.addTask(extras, db);
	}

}
