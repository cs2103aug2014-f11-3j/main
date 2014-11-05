package taskbuddy.googlecalendarcontroller;

import static org.junit.Assert.*;

import java.util.Scanner;

import googlecalcontroller.GoogleCalendarController;

import org.junit.Test;

public class GoogleCalendarControllerTest {

	GoogleCalendarController googleCalendarController = new GoogleCalendarController();
	
	@Test
	public void test() {
		Scanner sc = new Scanner(System.in);
		
		googleCalendarController.setUserNameAndAddress();
		System.out.println(googleCalendarController.getDisplayStrings());
		
		System.out.println(googleCalendarController.isCalendarAuthenticated());
		
		if (!googleCalendarController.isCalendarAuthenticated()) {
			System.out.println(googleCalendarController.getAuthorizationUrl());
			googleCalendarController.authorize(sc.nextLine());
		}
	}

}
