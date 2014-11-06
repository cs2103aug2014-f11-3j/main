package taskbuddy.gui;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import taskbuddy.database.Database;
import taskbuddy.database.DatabaseObserver;
import taskbuddy.logic.Task;

public class TaskViewController implements DatabaseObserver {
	
	Database database;
	ArrayList<Task> observedTasks;
	
	@FXML private TableView<Task> taskTable;
	@FXML private TableColumn<Task, String> titleColumn;
	@FXML private TableColumn<Task, String> dueDateColumn;
	
	@FXML private Label taskIDLabel;
	@FXML private Label taskTitleLabel;
	@FXML private Label taskDescriptionLabel;
	@FXML private Label taskTypeLabel;
	@FXML private Label taskStartTimeLabel;
	@FXML private Label taskDueTimeLabel;
	@FXML private Label taskPriorityLabel;
	@FXML private Label taskCompletionLabel;

	
	
	private MainApp mainApp;
	
	public TaskViewController(){}
	
	private ObservableList<Task> taskData = FXCollections.observableArrayList();
	
	public ObservableList<Task> getTaskData(){
		return taskData;
	}
	
	@FXML
	private void initializeTask() throws IOException{
		titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
		dueDateColumn.setCellValueFactory(cellData -> cellData.getValue().dueDateProperty());
		showTaskDetails(null);
		taskTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showTaskDetails(newValue));
		update();
		taskData = FXCollections.observableArrayList(observedTasks);
	}
	
	public void setMainApp(MainApp mainApp){
		this.mainApp = mainApp;
		taskTable.setItems(mainApp.getTaskData());
	}
	
	private void showTaskDetails(Task task){
		if (task!=null){
			taskIDLabel.setText(String.valueOf(task.getTaskId()));
			taskTitleLabel.setText(task.getTitle());
			taskDescriptionLabel.setText(task.getDescription());
			taskTypeLabel.setText(task.displayIsFloating());
			taskStartTimeLabel.setText(task.displayStart());
			taskDueTimeLabel.setText(task.displayEnd());
			taskPriorityLabel.setText(task.displayPriority());
			taskCompletionLabel.setText(task.displayIsComplete());
		} else {
			taskIDLabel.setText("-");
			taskTitleLabel.setText("-");
			taskDescriptionLabel.setText("-");
			taskTypeLabel.setText("-");
			taskStartTimeLabel.setText("-");
			taskDueTimeLabel.setText("-");
			taskPriorityLabel.setText("-");
			taskCompletionLabel.setText("-");
		}
	}

	@Override
	public void update() {
		observedTasks = database.getTasks();
	}
}
