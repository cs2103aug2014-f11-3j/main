package taskbuddy.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import taskbuddy.logic.Task;

public class TaskViewController {
	
	@FXML private TableView<Task> taskTable;
	@FXML private TableColumn<Task, String> titleColumn;
	@FXML private TableColumn<Task, String> dueDateColumn;
	
	@FXML private Label taskIDLabel;
	@FXML private Label taskTitleLabel;
	@FXML private Label taskDesctipionLabel;
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
	
}
