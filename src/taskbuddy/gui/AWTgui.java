package taskbuddy.gui;




//import java.awt.*;        // Using AWT container and component classes
import java.awt.event.*;  // Using AWT event classes and listener interfaces
import java.awt.Button;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.TextField;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.text.DefaultCaret;

import taskbuddy.googlecal.GooCalBackend;
import taskbuddy.logic.CommandParser;
import taskbuddy.logic.GoogleCalendarController;
import taskbuddy.parser.Parser;

public class AWTgui extends Frame implements ActionListener, WindowListener{
	private static final long serialVersionUID = -8401665365520669252L;
	
	// Command
	private Label commandLabel;    // Declare component Label
	private TextField commandTextField; // Declare component TextField
	
	// Response
	private Label responseLabel;    // Declare component Label
	private TextField responseTextField; // Declare component TextField
	private static String stringResponse = null;

	// Display
	private Label displayLabel;    // Declare component Label
	private TextArea displayTextArea;
	private static String display = "";
	
	// Button
	private Button sendButton;   // Declare component Button
	
	// Google Cal
	private Label gooCalLabel;    
	private static TextField gooCalDisplay;
	private static String stringGooCalDisplay = null;
	private TextField gooCalUserInput;
	private static TextField gooCalAuthenticationStatus;
	//private Button authenticateCal;
	
	


	/** Constructor to setup GUI components and event handling */
	public AWTgui () {
		setLayout(new FlowLayout());

		GooCalBackend goocalbackend = new GooCalBackend();
		GoogleCalendarController googlecalendarcontroller = new GoogleCalendarController();
		
		
		
		//COMMAND
		commandLabel = new Label("Command:");  
		add(commandLabel);                    
		commandTextField = new TextField("", 50); 
		commandTextField.setEditable(true);       
		add(commandTextField);                     
		
		//RESPONSE
		responseLabel = new Label("Response:");
		add(responseLabel);
		responseTextField = new TextField(stringResponse, 50); 
		responseTextField.setEditable(false); 
		add(responseTextField);
		
		//DISPLAY
		displayLabel = new Label("Display:");  
		add(displayLabel);      
		displayTextArea = new TextArea(stringResponse, 30, 50, 1);
		displayTextArea.setEditable(false);
		
		add(displayTextArea);
		
		//BUTTON
		sendButton = new Button("TaskBuddy!"); 
		add(sendButton);
		
		//ACTIONEVENT FIRING
		sendButton.addActionListener(this);
		// Clicking Button source fires ActionEvent
		commandTextField.addActionListener(this);
		// Hitting enter fires ActionEvent


		//ACTIVATE GOOGLE CAL
		JButton gooCalButton = new JButton("GooCal");
//		
//				new AbstractAction("GooCal") {
//	        @Override
//	        public void actionPerformed( ActionEvent e ) {
//	            System.out.println("KALALLA");
//	        	String stringGooCalUserInput = gooCalUserInput.getText();
//	        	GooCalBackend.generateNewToken(stringGooCalUserInput);
//	        	gooCalDisplay.setText(stringGooCalDisplay);
//	        }
//	    });
		JButton gooCalCheckStatus = new JButton("GooCalCheckStatus");
		
		gooCalLabel = new Label("Google Calendar:");  
		add(gooCalLabel);      
		
//		gooCalAuthenticationStatus = new TextField("Cal Status", 50);
//		add(gooCalAuthenticationStatus);
		
		gooCalDisplay = new TextField("", 50); 
		gooCalDisplay.setEditable(false);       
		
		if(googlecalendarcontroller.isCalendarAuthenticated()) {
			gooCalDisplay.setText("Google Calendar Already Authenticated!");
		}
		else {
			String url = googlecalendarcontroller.getAuthenticationUrl();
			gooCalDisplay.setText(url);
			try {
				Desktop.getDesktop().browse(new URI(url));
			} catch (IOException | URISyntaxException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
//		if (goocalbackend.isUserOnline()) {
//			gooCalDisplay.setText("User is online");
//		}
//		else {
//			gooCalDisplay.setText("User is offline");
//		}
		
		
		add(gooCalDisplay);
		gooCalUserInput = new TextField("", 50);
		gooCalUserInput.setEditable(true);
		add(gooCalUserInput);
		
//		gooCalCheckStatus = new JButton ("Check Cal Status");
//		add(gooCalCheckStatus);
//		gooCalCheckStatus.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				if(GooCalBackend.isAuthenticationValid()) {
//					gooCalAuthenticationStatus.setText("Authenticated.");
//				}
//				else {
//					gooCalAuthenticationStatus.setText("Not authenticated. Please copy URL, paste code, click Autheticate.");
//				}
//			}
//		});
		
		gooCalButton = new JButton ("Authenticate");
		add(gooCalButton);
		gooCalButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
	            System.out.println("KALALLA");
	        	String stringGooCalUserInput = gooCalUserInput.getText();
	        	googlecalendarcontroller.authenticate(stringGooCalUserInput);
	        	gooCalDisplay.setText("Generated");
			}
		});
		
		
		
				

		setTitle("TaskBuddy v0.3!");
		setSize(440, 850);       
		setVisible(true); 
		addWindowListener(this);
		
	}

	/** The entry main() method */
	public static void main(String[] args) {
		// Invoke the constructor to setup the GUI, by allocating an instance
		AWTgui app = new AWTgui();
	}

	
	public static void appendToDisplay(String toAppend) {
		display = display + System.getProperty("line.separator") + toAppend + System.getProperty("line.separator");
	}
	
	public static void clearDisplayString() {
		display = "";
	}
	
	public void clearCommandTextField() {
		commandTextField.setText("");
	}
	
	public static void setResponseString(String response) {
		stringResponse = response;
	}
	
	public static void setGoogleCalDisplay(String googleCalDisplayToSet) {
		stringGooCalDisplay = googleCalDisplayToSet;
	}
	
	
	/** ActionEvent handler - Called back upon button-click. */
	@Override
	public void actionPerformed(ActionEvent evt) {
		String stringCommand = commandTextField.getText();
		try {
			Parser.userInput(stringCommand);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
//		try {
//			Parser.userInput(stringResponse);
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		DisplayObserver displayObserver = new DisplayObserver();
		displayObserver.generateDisplayString();
//		
		responseTextField.setText(stringResponse);
		//displayTextArea.append(display);
		displayTextArea.setText(display);
		displayTextArea.setCaretPosition(display.length()); // Used to move cart (scrolling) of displayTextArea to the bottom
		clearDisplayString();
		clearCommandTextField();
		
		
		//Integer.parseInt(tfInput.getText());
		
		//++count; // increase the counter value
		// Display the counter value on the TextField tfCount
		//responseTextField.setText(count + ""); // convert int to String
	}
	
	@Override
	public void windowClosing(WindowEvent e) {
	   System.exit(0);  // Terminate the program
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}




