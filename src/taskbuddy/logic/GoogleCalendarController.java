package taskbuddy.logic;

import taskbuddy.googlecal.GooCalBackend;

public class GoogleCalendarController {
	GooCalBackend goocalbackend = new GooCalBackend();
	
	public boolean isCalendarAuthenticated() {
		return GooCalBackend.isAuthenticationValid();
	}
	
	public String getAuthenticationUrl() {
		return GooCalBackend.generateNewTokenStep1();
	}
	
	public String authenticate(String pastedCode) {
		return GooCalBackend.generateNewTokenStep2(pastedCode);
	}
}
