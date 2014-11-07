package taskbuddy.googlecal;

import static org.junit.Assert.*;

import org.junit.Test;

public class GoogleCalendarSetUsernameAddressTest {
	GoogleCalendarPreferenceLogger googleCalendarPreferenceLogger = new GoogleCalendarPreferenceLogger();
	@Test
	public void testSetUserName() {
		googleCalendarPreferenceLogger.createAndAddToUsernameFile("ipeech");
		assertEquals("Fail", "ipeech", googleCalendarPreferenceLogger.readUsernameFile());
		//fail("Not yet implemented");
	}

	@Test
	public void testSetCalAddress() {
		//googleCalendarPreferenceLogger.createAndAddToAddressFile("i357fqqhffrf1fa9udcbn9sikc@group.calendar.google.com");
		googleCalendarPreferenceLogger.createAndAddToAddressFile("o5nh8ih7pitouclqvil28mtfho@group.calendar.google.com");
		//fail("Not yet implemented");
		assertEquals("Fail", "i357fqqhffrf1fa9udcbn9sikc@group.calendar.google.com", googleCalendarPreferenceLogger.readAddressFile());
	}

	
	
}
