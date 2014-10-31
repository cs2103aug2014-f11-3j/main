package taskbuddy.parser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.ParseException;

import org.junit.Test;

import taskbuddy.parser.*;
import taskbuddy.logic.*;

public class UserInputTest {

	private static final String NULL_VALUE = "padding value";
	
	@Test
	public void specificTaskTest() {
		String command = "";
		command = "add lunch with family at 2pm tomorrow #important fellowship";
		try {
			Parser.userInput(command);
			UserInputBundle bundle = Parser.getUserInput();
			assertEquals("Incorrect Command Type for case 1", "add", bundle.getCommand());
			assertEquals("Incorrect Description for case 1", "important fellowship", bundle.getDescription());
			assertEquals("Incorrect Title for case 1", "lunch with family", bundle.getTitle());
			assertEquals("Incorrect Start Time for case 1", "1400", bundle.getStartTime());
			assertEquals("Incorrect End Time for case 1", "1400", bundle.getEndTime());
			assertEquals("Incorrect Start Date for case 1", "tomorrow", bundle.getStartDate());
			assertEquals("Incorrect End Date for case 1", "tomorrow", bundle.getEndDate());
			
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		command = "add go to Hong Kong from 02/12/2014 5pm to 05/12/2014 10am";
		try {
			Parser.userInput(command);
			UserInputBundle bundle = Parser.getUserInput();
			assertEquals("Incorrect Command Type for case 2", "add", bundle.getCommand());
			assertEquals("Incorrect Description for case 2", NULL_VALUE, bundle.getDescription());
			assertEquals("Incorrect Title for case 2", "go to Hong Kong", bundle.getTitle());
			assertEquals("Incorrect Start Time for case 2", "1700", bundle.getStartTime());
			assertEquals("Incorrect End Time for case 2", "1000", bundle.getEndTime());
			assertEquals("Incorrect Start Date for case 2", "02/12/2014", bundle.getStartDate());
			assertEquals("Incorrect End Date for case 2", "05/12/2014", bundle.getEndDate());
			
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		command = "edit 6 go to Japan";
		try {
			Parser.userInput(command);
			UserInputBundle bundle = Parser.getUserInput();
			assertEquals("Incorrect Command Type for case 3", "edit", bundle.getCommand());
			assertEquals("Incorrect ID for case 3", "6", bundle.getTaskID());
			assertEquals("Incorrect Description for case 3", NULL_VALUE, bundle.getDescription());
			assertEquals("Incorrect Title for case 3", "go to Japan", bundle.getTitle());
			assertEquals("Incorrect Start Time for case 3", NULL_VALUE, bundle.getStartTime());
			assertEquals("Incorrect End Time for case 3", NULL_VALUE, bundle.getEndTime());
			assertEquals("Incorrect Start Date for case 3", NULL_VALUE, bundle.getStartDate());
			assertEquals("Incorrect End Date for case 3", NULL_VALUE, bundle.getEndDate());
			
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
