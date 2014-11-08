package taskbuddy.googlecal;




import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Set;

//import org.hamcrest.core.IsAnything;


import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
//import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
//import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
//import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow.Builder;
//import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
//import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
//import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
//import com.google.api.client.json.*;
//import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.CalendarList;
//import com.google.api.services.calendar.model.ColorDefinition;
//import com.google.api.services.calendar.model.Colors;
//import com.google.api.services.calendar.model.Event;
//import com.google.api.services.calendar.model.EventDateTime;


public class GoogleCalendarAuthorizer {
	
	GoogleCalendarPreferenceLogger googleCalendarPreferenceLogger = new GoogleCalendarPreferenceLogger();
	Calendar calendarGlobal = null;
	
    static AuthorizationCodeFlow codeFlow;
    static String redirectUri;
    
    
	public boolean isUserOnline()  {
		Socket socket = null;
		try {
		    socket = new Socket("www.google.com", 80);
		} catch (UnknownHostException hostNotReached) {
			System.err.println("User is offline");
			return false;
		} catch (IOException socketError) {
			System.err.println("Socket cannot be created");
		}
		finally { 
			if (socket != null) {
		    	try { 
		    		socket.close(); 
		    	} catch(IOException e) {}		    	
		    }
		}
		return true;
	}
	
	
	public boolean isAuthenticationValid() {
		Calendar service = null;
		
			File file = new File("GoogleCalAuthenticationToken_New");
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		if (googleCalendarPreferenceLogger.isTokenFileEmpty()) {			
			try {
				service = createCalendar(googleCalendarPreferenceLogger.readTokenFile(), googleCalendarPreferenceLogger.readUsernameFile());
				checkCalendar(createCalendar(googleCalendarPreferenceLogger.readTokenFile(), googleCalendarPreferenceLogger.readUsernameFile()));
			} catch (IOException unauthorized) {
				// TODO Auto-generated catch block
				System.out.println("invalid");
				return false;
			}
		}
		
		else {
			try {
				// Check if authentication key is valid. If it is invalid, IOException unauthorized will be caught.
				service = createCalendar(googleCalendarPreferenceLogger.readTokenFile(), googleCalendarPreferenceLogger.readUsernameFile());
				checkCalendar(createCalendar(googleCalendarPreferenceLogger.readTokenFile(), googleCalendarPreferenceLogger.readUsernameFile()));
			} catch (IOException unauthorized) {
				// An IOException is thrown by the createCalendar method, if the calendar fails to be created due to an invalid authorization token.
				// The IOException is caught at this point. 
				// When the exception is caught, the original token database is cleared, and a new token is generated by the method generateNewToken.
				System.out.println("invalid");
				return false;
			}
		}
		return true;
	}
	
	
	public Calendar createCalendar(String token, String username) throws IOException {
		//System.out.println("Executing createCalendar:"); // For debugging
	
		
		// Two globals that will be used in each step.
		HttpTransport httpTransport = new NetHttpTransport();
		JsonFactory jsonFactory = new JacksonFactory();

		// Create the authorization code flow manager
		Set<String> scope = Collections.singleton(CalendarScopes.CALENDAR);
		String clientId = "369332843116-gr8ct1guerlf1fudpgivfjv43h0oleip.apps.googleusercontent.com";
		String clientSecret = "ISvEBCzHT-jheksy-kO-oBvs";

		AuthorizationCodeFlow.Builder codeFlowBuilder = new GoogleAuthorizationCodeFlow.Builder(httpTransport, jsonFactory, clientId, clientSecret, scope);

		AuthorizationCodeFlow codeFlow = codeFlowBuilder.build();
		TokenResponse existingToken = new TokenResponse();

		// Read existing token from file
		existingToken.setAccessToken(token);

		existingToken.setExpiresInSeconds(null);
		String userId = username;  
		Credential credential = codeFlow.createAndStoreCredential(existingToken, userId);

		// Credentials may be used to initialize http requests
		HttpRequestInitializer initializer = credential;

		// and thus are used to initialize the calendar service
		Calendar.Builder serviceBuilder = new Calendar.Builder(httpTransport,jsonFactory, initializer);
		serviceBuilder.setApplicationName("GooCal");
		Calendar calendar = serviceBuilder.build();
		
		calendarGlobal = calendar;
		return calendar;
		
		
	}
	
	public Calendar getCalendar() {
		return calendarGlobal;
	}
	
	public void checkCalendar(Calendar calendar) throws IOException {
		//System.out.println("Executing checkCalendar:"); // For debugging
		
		
		Calendar.CalendarList.List listRequest = calendar.calendarList().list();
		CalendarList feed = listRequest.execute();
	}   
	
	

	public String generateNewTokenStep1()  {

		//System.out.println("Executing generateNewToken:"); // For debugging
		
		// Two globals that will be used in each step.
		HttpTransport httpTransport = new NetHttpTransport();
		JsonFactory jsonFactory = new JacksonFactory();

		// Create the authorization code flow manager
		Set<String> scope = Collections.singleton(CalendarScopes.CALENDAR);
		String clientId = "369332843116-gr8ct1guerlf1fudpgivfjv43h0oleip.apps.googleusercontent.com";
		String clientSecret = "ISvEBCzHT-jheksy-kO-oBvs";

		// Use a factory pattern to create the code flow
		AuthorizationCodeFlow.Builder codeFlowBuilder = new GoogleAuthorizationCodeFlow.Builder(
				httpTransport, jsonFactory, clientId, clientSecret, scope);
		codeFlow = codeFlowBuilder.build();

		// set the code flow to use a dummy user
		// in a servlet, this could be the session id
		//String userId = username;

		// "redirect" to the authentication url
		redirectUri = "urn:ietf:wg:oauth:2.0:oob";
		AuthorizationCodeRequestUrl authorizationUrl = codeFlow
				.newAuthorizationUrl();
		authorizationUrl.setRedirectUri(redirectUri);
		System.out.println("Error: Google Calendar Authorization Token has expired.");
		System.out.println("Generating new token.");
		System.out.println("Go to the following address, copy and paste the code into this program, and enter:");
		System.out.println(authorizationUrl);
		return authorizationUrl.toString();
		
	}
	
	public  String generateNewTokenStep2(String userInput)  {
		String code = userInput;


		AuthorizationCodeTokenRequest tokenRequest = codeFlow
				.newTokenRequest(code);

		tokenRequest.setRedirectUri(redirectUri);
		TokenResponse tokenResponse = null;
		try {
			tokenResponse = tokenRequest.execute();
		} catch (IOException tokenRequestFailed) {
			System.err.println("Token request failed");
		}
		System.out.println(tokenResponse.getAccessToken());
		//addToDb(tokenResponse.getAccessToken());
		googleCalendarPreferenceLogger.createAndAddToTokenFile(tokenResponse.getAccessToken());
		return tokenResponse.getAccessToken();
	}	
}
