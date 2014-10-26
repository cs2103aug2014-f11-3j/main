package taskbuddy.gui;




import java.awt.*;        // Using AWT container and component classes
import java.awt.event.*;  // Using AWT event classes and listener interfaces



public class AWTgui extends Frame implements ActionListener, WindowListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8401665365520669252L;
	private Label commandLabel;    // Declare component Label
	private Label responseLabel;    // Declare component Label
	private TextField commandTextField; // Declare component TextField
	private TextField responseTextField; // Declare component TextField
	private Button sendButton;   // Declare component Button
	private Button btnCount2;   // Declare component Button
	private int count = 0;     // Counter's value
	private String stringIn = null;
	



	/** Constructor to setup GUI components and event handling */
	public AWTgui () {
		setLayout(new FlowLayout());
		// "super" Frame sets its layout to FlowLayout, which arranges the components
		//  from left-to-right, and flow to next row from top-to-bottom.

		commandLabel = new Label("Command:");  // construct Label
		add(commandLabel);                    // "super" Frame adds Label
		
		
		commandTextField = new TextField("0", 10); // construct TextField
		commandTextField.setEditable(true);       // set to read-only
		add(commandTextField);                     // "super" Frame adds tfCount
		
		
		
		responseLabel = new Label("Response:");  // construct Label
		add(responseLabel);                    // "super" Frame adds Label



		
		responseTextField = new TextField("0", 10); // construct TextField
		responseTextField.setEditable(false);       // set to read-only
		add(responseTextField);                     // "super" Frame adds tfCount
		
		
		sendButton = new Button("Send");   // construct Button
		add(sendButton);                    // "super" Frame adds Button

		sendButton.addActionListener(this);
		// Clicking Button source fires ActionEvent
		// btnCount registers this instance as ActionEvent listener
		
		
		commandTextField.addActionListener(this);
		// Hitting enter fires ActionEvent

		
		

		setTitle("TaskBuddy v0.3!");  // "super" Frame sets title
		setSize(250, 100);        // "super" Frame sets initial window size

		// System.out.println(this);
		// System.out.println(lblCount);
		// System.out.println(tfCount);
		// System.out.println(btnCount);

		setVisible(true);         // "super" Frame shows

		// System.out.println(this);
		// System.out.println(lblCount);
		// System.out.println(tfCount);
		// System.out.println(btnCount);
		
		addWindowListener(this);
		
	}

	/** The entry main() method */
	public static void main(String[] args) {
		// Invoke the constructor to setup the GUI, by allocating an instance
		AWTgui app = new AWTgui();
	}

	/** ActionEvent handler - Called back upon button-click. */
	@Override
	public void actionPerformed(ActionEvent evt) {
		stringIn = commandTextField.getText();
		responseTextField.setText(stringIn);
		
		
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




