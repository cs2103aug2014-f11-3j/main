package taskbuddy.googlecal;

import static org.junit.Assert.*;

import org.junit.Test;

public class GoogleCalendarBackwardSyncCommandCreatorTest {
	GoogleCalendarBackwardSyncCommandCreator gcbsCommandCreator = new GoogleCalendarBackwardSyncCommandCreator();
	@Test
	public void test() {
		System.out.println("REAL DEAL");
		gcbsCommandCreator.executeBackwardSync();
	}

}
