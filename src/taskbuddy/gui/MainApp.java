package taskbuddy.gui;

import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApp extends Application {

	private Stage primaryStage;
	private BorderPane taskBuddyView;

	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("TaskBuddy!");
		initView();
	}

	public void initView() {
		try {
			FXMLLoader loader = new FXMLLoader();
			URL location = this.getClass().getResource("TaskBuddyView.fxml");
			loader.setLocation(location);
			taskBuddyView = loader.load();
			Scene scene = new Scene(taskBuddyView);
			primaryStage.setScene(scene);
			primaryStage.show();
			System.err.println("opening gui");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void main(String[] args) {
		launch(args);
	}
	/*
	private void checkGoogleAuth() {

		GooCalBackend goocalbackend = new GooCalBackend();
		GoogleCalendarController googlecalendarcontroller = new GoogleCalendarController();
		
		if (googlecalendarcontroller.isCalendarAuthenticated()) {
			String auth = "Calendar authorized";
			TaskBuddyViewController.goocalField.setText(auth);
		} else {
			String url = googlecalendarcontroller.getAuthenticationUrl();
			gooCalDisplay.setText(url);
			try {
				try {
					Desktop.getDesktop().browse(new URI(url));
				} catch (URISyntaxException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				// } catch (IOException | URISyntaxException e1) {
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		// TODO else case
		googlecalendarcontroller.authenticate(stringGooCalUserInput);
	}
	 */
}
