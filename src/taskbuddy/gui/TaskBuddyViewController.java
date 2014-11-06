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
	protected static TextField userInputField;
	@FXML
	protected static TextField usernameField;
	@FXML
	protected static TextField goocalField;
	@FXML
	protected static TextField authField;

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
	protected void parseInputs() throws ParseException, IOException {
		String inputLine = userInputField.getText();
		AcknowledgeBundle a = Parser.userInput(inputLine);
		String status = a.getStatus();
		if (status.equals("failure")) {
			String response = status + ": " + a.getMessage();
			responseLabel.setText(response);
		} else {
			responseLabel.setText(a.getMessage());
		}
		update();
	}

	protected void parseUsername() {
		String username = usernameField.getText();
		// TODO get username method from goocal
	}

	protected void parseGoocal() {
		String username = goocalField.getText();
		// TODO get address method from goocal
	}

	protected void parseAuth() {
		String username = authField.getText();
		// TODO get authentication method from goocal
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
