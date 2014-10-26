package taskbuddy.gui;




import java.awt.*;        // Using AWT container and component classes
import java.awt.event.*;  // Using AWT event classes and listener interfaces
import java.awt.KeyboardFocusManager;
import java.io.IOException;
import java.text.ParseException;

import taskbuddy.logic.CommandParser;
import taskbuddy.parser.Parser;

public class AWTgui extends Frame implements ActionListener, WindowListener{
	private static final long serialVersionUID = -8401665365520669252L;
	private Label displayLabel;    // Declare component Label
	private Label commandLabel;    // Declare component Label
	private Label responseLabel;    // Declare component Label
	private TextField commandTextField; // Declare component TextField
	private TextField responseTextField; // Declare component TextField
	private TextArea displayTextArea;
	
	private Button sendButton;   // Declare component Button
	//private Button btnCount2;   // Declare component Button
	//private int count = 0;     // Counter's value
	private static String stringResponse = null;
	private static String display = "";



	/** Constructor to setup GUI components and event handling */
	public AWTgui () {
		setLayout(new FlowLayout());


		
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


		

		setTitle("TaskBuddy v0.3!");
		setSize(440, 700);       
		setVisible(true); 
		addWindowListener(this);
		
	}

	/** The entry main() method */
	public static void main(String[] args) {
		// Invoke the constructor to setup the GUI, by allocating an instance
		AWTgui app = new AWTgui();
	}

	
	public static void appendToDisplay(String toAppend) {
		display = display + System.getProperty("line.separator") + toAppend;
	
		
	}
	
	public static void setResponseString(String response) {
		stringResponse = response;
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
//		
		responseTextField.setText(stringResponse);
		displayTextArea.append(display);
		
		
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




