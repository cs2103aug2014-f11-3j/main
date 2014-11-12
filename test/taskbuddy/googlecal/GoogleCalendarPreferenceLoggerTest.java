//@author A0108411W
package taskbuddy.googlecal;

import static org.junit.Assert.*;

import org.junit.Test;

public class GoogleCalendarPreferenceLoggerTest {
	
	GoogleCalendarPreferenceLogger googleCalendarAuthorizer = new GoogleCalendarPreferenceLogger();
	

	
	
	
	@Test
	public void testCreateAndAddToTokenFile() {
		googleCalendarAuthorizer.createAndAddToTokenFile("lalalalal");
		assertEquals("Failed.", "lalalalal", googleCalendarAuthorizer.readTokenFile());	
	}
	
	@Test
	public void testClearTokenFile() {
		googleCalendarAuthorizer.clearTokenFile();
		assertEquals("Failed.", "", googleCalendarAuthorizer.readTokenFile());	
	}

	
	@Test
	public void testIsTokenFileEmpty() {
		googleCalendarAuthorizer.clearTokenFile();
		assertTrue("Failed", googleCalendarAuthorizer.isTokenFileEmpty());
	}
	
	
	
	@Test
	public void testCreateAndAddToUsernameFile() {
		googleCalendarAuthorizer.createAndAddToUsernameFile("lalalalal");
		assertEquals("Failed.", "lalalalal", googleCalendarAuthorizer.readUsernameFile());	
	}
	
	@Test
	public void testClearUsernameFile() {
		googleCalendarAuthorizer.clearUsernameFile();
		assertEquals("Failed.", "", googleCalendarAuthorizer.readUsernameFile());	
	}

	@Test
	public void testIsUsernameFileEmpty() {
		googleCalendarAuthorizer.clearUsernameFile();
		assertTrue("Failed", googleCalendarAuthorizer.isUsernameFileEmpty());
	}
	
	
	
	
	@Test
	public void testCreateAndAddToAddressFile() {
		googleCalendarAuthorizer.createAndAddToAddressFile("lalalalal");
		assertEquals("Failed.", "lalalalal", googleCalendarAuthorizer.readAddressFile());	
	}
	
	@Test
	public void testClearAddressFile() {
		googleCalendarAuthorizer.clearAddressFile();
		assertEquals("Failed.", "", googleCalendarAuthorizer.readAddressFile());	
	}

	@Test
	public void testIsAddressFileEmpty() {
		googleCalendarAuthorizer.clearAddressFile();
		assertTrue("Failed", googleCalendarAuthorizer.isAddressFileEmpty());
	}
}
