//@author A0110649J
//andrew

package taskbuddy.gui;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import taskbuddy.database.Database;
import taskbuddy.database.DatabaseObserver;
import taskbuddy.googlecalcontroller.GoogleCalendarController;
import taskbuddy.logic.AcknowledgeBundle;
import taskbuddy.logic.Task;
import taskbuddy.parser.Parser;
import taskbuddy.parser.Parser2;

public class TaskBuddyViewController implements DatabaseObserver {

	protected Database database;
	protected ArrayList<Task> listTasks;
	private final static String commandSync = "sync";
	private final static String commandDone = "done";
	private final static String commandRevert = "revert";
	private final static String commandPriority = "priority";
	private final static String commandUndo = "undo";
	private final static String commandRedo = "redo";

	private ObservableList<Task> taskData = FXCollections.observableArrayList();
	@FXML
	private TableView<Task> taskTable;
	@FXML
	private TableColumn<Task, String> titleColumn;
	@FXML
	private TableColumn<Task, String> taskIDColumn;
	@FXML
	private TableColumn<Task, String> startDateColumn;
	@FXML
	private TableColumn<Task, String> endDateColumn;
	@FXML
	private Label taskIDLabel;
	@FXML
	private Label taskTitleLabel;
	@FXML
	private Label taskDescriptionLabel;
	@FXML
	private Label taskTypeLabel;
	@FXML
	private Label taskStartTimeLabel;
	@FXML
	private Label taskDueTimeLabel;
	@FXML
	private Label taskPriorityLabel;
	@FXML
	private Label taskCompletionLabel;
	@FXML
	private Label responseLabel;
	@FXML
	private Label usernameLabel;
	@FXML
	private Label gooCalLabel;
	@FXML
	private Label authLabel;
	@FXML
	private Label userAuthLabel;
	@FXML
	protected TextField userInputField = new TextField();
	@FXML
	protected TextField usernameField = new TextField();
	@FXML
	protected TextField goocalField = new TextField();
	@FXML
	protected TextField authField = new TextField();
	protected GoogleCalendarController gcCont = new GoogleCalendarController();
	final String fail = "Failure";

	public TaskBuddyViewController() {
	}

	public TaskBuddyViewController(Database database) throws IOException,
			ParseException {
		listTasks = new ArrayList<Task>();
		this.database = database;
		database.addObserver(this);
	}

	@FXML
	private void initialize() {
		checkUser();
		titleColumn.setCellValueFactory(cellData -> cellData.getValue()
				.titleProperty());
		taskIDColumn.setCellValueFactory(cellData -> StringProperty
				.stringExpression(cellData.getValue().idProperty()));
		startDateColumn.setCellValueFactory(cellData -> cellData.getValue()
				.startDateProperty());
		endDateColumn.setCellValueFactory(cellData -> cellData.getValue()
				.dueDateProperty());
		taskTable
				.getSelectionModel()
				.selectedItemProperty()
				.addListener(
						(observable, oldValue, newValue) -> showTaskDetails(newValue));
		update();
	}

	private void showTaskDetails(Task task) {
		if (task != null) {
			taskIDLabel.setText(String.valueOf(task.getTaskId()));
			taskTitleLabel.setText(task.getTitle());
			taskDescriptionLabel.setText(task.getDescription());
			Boolean floating = task.isFloatingTask();
			if (floating) {
				taskTypeLabel.setText("Floating");
				taskStartTimeLabel.setText("-");
				taskDueTimeLabel.setText("-");
			} else {
				taskTypeLabel.setText("Timed");
				String starT = task.displayStart();
				String enD = task.displayEnd();
				String start = starT.substring(7);
				String end = enD.substring(5);
				taskStartTimeLabel.setText(start);
				taskDueTimeLabel.setText(end);
			}
			Boolean complete = task.getCompletionStatus();
			if (complete) {
				taskCompletionLabel.setText("Completed");
			} else {
				taskCompletionLabel.setText("Incomplete");
			}
			taskPriorityLabel.setText(String.valueOf(task.getPriority()));
		} else {
			taskIDLabel.setText("-");
			taskTitleLabel.setText("-");
			taskDescriptionLabel.setText("-");
			taskTypeLabel.setText("-");
			taskStartTimeLabel.setText("-");
			taskDueTimeLabel.setText("-");
			taskCompletionLabel.setText("-");
			taskPriorityLabel.setText("-");
		}
	}

	@FXML
	protected void parseInputs() {
		String inputLine = userInputField.getText();
		System.err.println(inputLine);
		boolean useParser2 = checkParser(inputLine);
		if (inputLine.isEmpty()) {
			responseLabel.setText("Nothing entered");
		} else {
			AcknowledgeBundle a = new AcknowledgeBundle();
			try {
				if (useParser2) {
					a = Parser2.parseOtherCommands(inputLine);
				} else {
					a = Parser.userInput(inputLine);
				}
			} catch (ParseException e) {
				a.putFailure();
				a.putMessage("Parse error");
				e.printStackTrace();
			} catch (IOException e) {
				a.putFailure();
				a.putMessage("Input error");
				e.printStackTrace();
			}
			String status = a.getStatus();
			System.err.println(a.getStatus());
			System.err.println(a.getMessage());
			if (status.equals(fail)) {
				String response = status + ": " + a.getMessage();
				System.err.println(a.getMessage());
				responseLabel.setText(response);
			} else {
				responseLabel.setText(a.getMessage());
			}
			update();
		}
	}

	
	protected boolean checkParser(String s) {
		String[] tokens = s.split(" ");
		String word = tokens[0];
		switch (word){
			case commandSync:
			case commandDone:
			case commandRevert:
			case commandPriority:
			case commandUndo:
			case commandRedo:
				return true;
			default:
				return false;
		}
	}

	@FXML
	protected void parseUsername() {
		String username = usernameField.getText();
		System.err.println(username);
		gcCont.setUserName(username);
	}

	@FXML
	protected void parseGoocal() {
		String address = goocalField.getText();
		System.err.println(address);
		gcCont.setCalAddress(address);
	}

	@FXML
	protected void parseAuth() {
		// try {
		String authCode = authField.getText();
		System.err.println(authCode);
		gcCont.authorize(authCode);
		if (gcCont.isCalendarAuthenticated()) {
			String username = usernameField.getText();
			userAuthLabel.setText(username + "'s Google Calendar");
			update();
		} else {
			userAuthLabel.setText("Authorization error");
		}
		// } catch (Exception e) {
		// e.printStackTrace();
		// userAuthLabel.setText("Authorization Error");
		// }
	}

	protected void checkUser() {
		String userInfo = gcCont.getDisplayStrings();
		String none = "Username and Address empty";
		String first = "Address empty";
		String second = "Username empty";
		if (userInfo.equals(none)) {
			;
		} else if (userInfo.equals(first)) {
			goocalField.setText(userInfo);
		} else if (userInfo.endsWith(second)) {
			usernameField.setText(userInfo);
		} else {
			String[] userIn = userInfo.split(" ");
			usernameField.setText(userIn[0]);
			goocalField.setText(userIn[1]);
			if (!gcCont.isCalendarAuthenticated()) {
				String authURL = gcCont.getAuthorizationUrl();
				try {
					GoogleCalendarController gcc = new GoogleCalendarController();
					if (gcc.isUserOnline()){
						Desktop.getDesktop().browse(new URI(authURL));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				userAuthLabel.setText(userIn[0] + "'s Google Calendar");
				update();
			}
		}
	}

	@Override
	public void update() {
		try {
			database = Database.getInstance();
			listTasks = database.getTasks();
			System.err.println("copied db");
		} catch (Exception e) {
			listTasks = new ArrayList<Task>();
			System.err.println("copy db fail");
			Task dummy = new Task();
			dummy.setTitle("dummy value");
			dummy.setDescription("dummy desc");
			dummy.setFloating(true);
			dummy.setCompletion(false);
			dummy.setStartTime(Calendar.getInstance());
			dummy.setEndTime(Calendar.getInstance());
			dummy.setPriority(1);
			dummy.setTaskId(1);
			listTasks.add(dummy);
		}
		taskData.setAll(listTasks);
		taskTable.setItems(taskData);
	}

}
