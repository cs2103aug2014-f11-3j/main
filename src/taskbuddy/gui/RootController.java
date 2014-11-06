package taskbuddy.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class RootController {

	@FXML private Label responseLabel;
	@FXML private TextField usernameField;
	@FXML private TextField gooCalField;
	@FXML private TextField authorizationField;
	@FXML private TextField userInputField;
	
	private MainApp mainApp;
	
	private void takeUserInputs(){
		userInputField.getText();
		//TODO STUB
	}
	
}
