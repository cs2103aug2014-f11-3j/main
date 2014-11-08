//@author A0108411W
package taskbuddy.googlecal;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;



/**
 * 
 * This class performs all read/write functions of user preferences onto Files stored in the user's computer.
 * User preferences include the user's username, the user's Google Calendar Address, as well as the authorization token
 * provided by Google Calendar.
 * 
 *
 */

public class GoogleCalendarPreferenceLogger {
	public void createAndAddToTokenFile(String accessToken) {
		try {
			FileOutputStream fout = new FileOutputStream("GoogleCalAuthenticationToken_New");
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(accessToken);
			oos.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public String readTokenFile() {
		String accessToken;
		try {
			FileInputStream fin = new FileInputStream("GoogleCalAuthenticationToken_New");
			ObjectInputStream ois = new ObjectInputStream(fin);
			accessToken = (String) ois.readObject();
			ois.close();
			return accessToken;
		} catch (Exception ex) {
			System.err.println("token not found");
			//ex.printStackTrace;
			return "";
		}
	}
	
	public void clearTokenFile() {
		try {
			FileOutputStream fout = new FileOutputStream("GoogleCalAuthenticationToken_New");
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject("");
			oos.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public boolean isTokenFileEmpty() {
		if (readTokenFile().equals("")) {
			return true;
		} else {
			return false;
		}
	}
	

	
	public void createAndAddToUsernameFile(String username) {
		try {
			FileOutputStream fout = new FileOutputStream("Username");
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(username);
			oos.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public String readUsernameFile() {
		String accessToken;
		try {
			FileInputStream fin = new FileInputStream("Username");
			ObjectInputStream ois = new ObjectInputStream(fin);
			accessToken = (String) ois.readObject();
			ois.close();
			return accessToken;
		} catch (Exception ex) {
			System.err.println("file not found");
			//ex.printStackTrace;
			return "";
		}
	}
	
	public void clearUsernameFile() {
		try {
			FileOutputStream fout = new FileOutputStream("Username");
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject("");
			oos.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public boolean isUsernameFileEmpty() {
		if (readUsernameFile().equals("")) {
			return true;
		} else {
			return false;
		}
	}
	
	
	
	
	public void createAndAddToAddressFile(String address) {
		try {
			FileOutputStream fout = new FileOutputStream("Address");
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(address);
			oos.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public String readAddressFile() {
		String accessToken;
		try {
			FileInputStream fin = new FileInputStream("Address");
			ObjectInputStream ois = new ObjectInputStream(fin);
			accessToken = (String) ois.readObject();
			ois.close();
			return accessToken;
		} catch (Exception ex) {
			System.err.println("file not found");
			//ex.printStackTrace;
			return "";
		}
	}
	
	public void clearAddressFile() {
		try {
			FileOutputStream fout = new FileOutputStream("Address");
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject("");
			oos.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public boolean isAddressFileEmpty() {
		if (readAddressFile().equals("")) {
			return true;
		} else {
			return false;
		}
	}
	
	
	
	
}
	
	


