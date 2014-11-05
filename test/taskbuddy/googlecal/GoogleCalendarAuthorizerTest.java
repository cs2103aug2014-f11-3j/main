package taskbuddy.googlecal;

import static org.junit.Assert.*;

import java.util.Scanner;

import org.junit.Test;

public class GoogleCalendarAuthorizerTest {
	GoogleCalendarAuthorizer googleCalendarAuthorizer = new GoogleCalendarAuthorizer();
	@Test
	public void testIsUserOnline() {
		assertTrue("Failed. User is offline", googleCalendarAuthorizer.isUserOnline());	
	}
	
	@Test
	public void testAuthorizationRun() {
		Scanner sc = new Scanner(System.in);
		
		
		if (googleCalendarAuthorizer.isAuthenticationValid()) {
			System.out.println("valid");
		}
		else {
			googleCalendarAuthorizer.generateNewTokenStep1();
			googleCalendarAuthorizer.generateNewTokenStep2(sc.nextLine());
		}
	}
	
//	@Test
//	public void testGenerate1() {
//		System.out.println(googleCalendarAuthorizer.generateNewTokenStep1());
//	}
//	
//	@Test
//	public void testGenerate2() {
//		Scanner sc = new Scanner(System.in);
//		googleCalendarAuthorizer.generateNewTokenStep2(sc.nextLine());
//		System.out.println(googleCalendarAuthorizer.isAuthenticationValid());
//	}
	

}
