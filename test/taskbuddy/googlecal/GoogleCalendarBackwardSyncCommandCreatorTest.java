package taskbuddy.googlecal;

import static org.junit.Assert.*;

import java.net.UnknownHostException;

import org.junit.Test;

public class GoogleCalendarBackwardSyncCommandCreatorTest {
	GoogleCalendarBackwardSyncCommandCreator gcbsCommandCreator = new GoogleCalendarBackwardSyncCommandCreator();
	@Test
	public void test() {
		System.out.println("REAL DEAL");
		try {
			gcbsCommandCreator.executeBackwardSync();
		} catch (UnknownHostException e) {
			System.err.println("User not online / authorization expired");
		}
	}

}
