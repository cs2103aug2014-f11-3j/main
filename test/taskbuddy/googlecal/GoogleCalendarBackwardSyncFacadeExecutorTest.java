package taskbuddy.googlecal;

import static org.junit.Assert.*;

import java.net.UnknownHostException;

import org.junit.Test;

public class GoogleCalendarBackwardSyncFacadeExecutorTest {
	GoogleCalendarManager googleCalendarManager = new GoogleCalendarManager();
	
	@Test
	public void test() {
		try {
			googleCalendarManager.executeBackwardSync();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
