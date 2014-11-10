//@author A0108411W
package taskbuddy.googlecalcontroller;

import taskbuddy.googlecal.GooCalBackend;
import taskbuddy.googlecal.GoogleCalendarAuthorizerStatus;
import taskbuddy.googlecal.GoogleCalendarPreferenceLogger;

/**
 * This class sits in the package which GUI interfaces directly with. 
 * Its main aim is to facilitate authorization of the google calendar, and also
 * to obtain the user's Username and Google Calendar Address.
 *  
 */


public class GoogleCalendarController {

	GoogleCalendarAuthorizerStatus googleCalendarAuthorizer = new GoogleCalendarAuthorizerStatus();
	GoogleCalendarPreferenceLogger googleCalendarPreferenceLogger = new GoogleCalendarPreferenceLogger();



	// To be called by GUI
	public String getDisplayStrings() {
		if (isUserNameEmpty()  && isAddressEmpty()) {
			return "Username and Address empty";
		}
		else if (isUserNameEmpty() && !isAddressEmpty()) {
			return "Username empty";
		}
		else if (isAddressEmpty() && !isUserNameEmpty()) {
			return "Address empty";
		}
		else  {
			return googleCalendarPreferenceLogger.readUsernameFile() + " " +  googleCalendarPreferenceLogger.readAddressFile();
		}
	}

	// To be called by GUI
	public void setUserName(String set) {
		googleCalendarPreferenceLogger.createAndAddToUsernameFile(set);
	}

	// To be called by GUI
	public void setCalAddress(String set) {
		googleCalendarPreferenceLogger.createAndAddToAddressFile(set);
	}


	// To be called by GUI
	public boolean isCalendarAuthenticated() {
		return googleCalendarAuthorizer.isAuthenticationValid();
	}
	
	// To be called by GUI
	public boolean isUserOnline() {
		return googleCalendarAuthorizer.isUserOnline();
	}

	// To be called by GUI
	public String getAuthorizationUrl() {
		return googleCalendarAuthorizer.generateNewTokenStep1();
	}

	// To be called by GUI
	public String authorize(String pastedCode) {
		return googleCalendarAuthorizer.generateNewTokenStep2(pastedCode);
	}

	public boolean isUserNameEmpty() {
		return googleCalendarPreferenceLogger.isUsernameFileEmpty();
	}

	public boolean isAddressEmpty() {
		return googleCalendarPreferenceLogger.isAddressFileEmpty();
	}
}
