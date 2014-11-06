package taskbuddy.gui;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

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

public class TaskBuddyViewController implements DatabaseObserver {

	Database database;
	ArrayList<Task> observedTasks;
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
		observedTasks = new ArrayList<Task>();
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
			taskTypeLabel.setText(task.displayIsFloating());
			taskStartTimeLabel.setText(task.displayStart());
			taskDueTimeLabel.setText(task.displayEnd());
			taskCompletionLabel.setText(String.valueOf(task
					.getCompletionStatus()));
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
		if (inputLine.isEmpty()) {
			responseLabel.setText("Nothing entered");
		} else {
			AcknowledgeBundle a = new AcknowledgeBundle();
			try {
				a = Parser.userInput(inputLine);
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
			if (status.equals(fail)) {
				String response = status + ": " + a.getMessage();
				responseLabel.setText(response);
			} else {
				responseLabel.setText(a.getMessage());
			}
			update();
		}
	}

	@FXML
	protected void parseUsername() {
		String username = usernameField.getText();
		gcCont.setUserName(username);
	}

	@FXML
	protected void parseGoocal() {
		String address = goocalField.getText();
		gcCont.setCalAddress(address);
	}

	@FXML
	protected void parseAuth() {
		String authCode = authField.getText();
		// TODO get authentication method from goocal
	}

	protected void checkUser(){
		String userInfo = gcCont.getDisplayStrings();
		String none = "Username and Address empty";
		String first = "Address empty";
		String second = "Username empty";
		if (userInfo.equals(none)){
			;
		} else if (userInfo.equals(first)){
			goocalField.setText(userInfo);
		} else if (userInfo.endsWith(second)){
			usernameField.setText(userInfo);
		} else {
			String[] userIn = userInfo.split(" ");
			usernameField.setText(userIn[0]);
			goocalField.setText(userIn[1]);
		}
	}
	
	@Override
	public void update() {
		try {
			observedTasks = database.getTasks();
		} catch (Exception e) {
			observedTasks = new ArrayList<Task>();
		}
		taskData.setAll(observedTasks);
		taskTable.setItems(taskData);
	}

}
