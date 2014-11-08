package taskbuddy.googlecal;



//import java.io.EOFException;
//import java.io.File;
import java.io.FileInputStream;
//import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
//import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
//import java.security.GeneralSecurityException;
//import java.text.DateFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Collections;
//import java.util.Date;
//import java.util.Map;
//import java.util.Scanner;
//import java.util.Set;
//import java.util.TimeZone;
//
//import org.hamcrest.core.IsAnything;

//import taskbuddy.gui.AWTgui;






//import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
//import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
//import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest;
//import com.google.api.client.auth.oauth2.Credential;
//import com.google.api.client.auth.oauth2.TokenResponse;
//import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
//import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
//import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
//import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow.Builder;
//import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
//import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
//import com.google.api.client.http.HttpRequestInitializer;
//import com.google.api.client.http.HttpTransport;
//import com.google.api.client.http.javanet.NetHttpTransport;
//import com.google.api.client.json.JsonFactory;
//import com.google.api.client.json.jackson.JacksonFactory;
//import com.google.api.client.json.jackson2.JacksonFactory;
//import com.google.api.client.json.*;
//import com.google.api.client.util.DateTime;
//import com.google.api.services.calendar.Calendar;
//import com.google.api.services.calendar.CalendarScopes;
//import com.google.api.services.calendar.model.CalendarList;
//import com.google.api.services.calendar.model.ColorDefinition;
//import com.google.api.services.calendar.model.Colors;
//import com.google.api.services.calendar.model.Event;
//import com.google.api.services.calendar.model.EventDateTime;



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
	
	


