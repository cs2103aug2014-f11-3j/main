package taskbuddy.googlecal;

import java.util.ArrayList;

import taskbuddy.logic.Task;

public class GoogleCalendarBackwardSyncCommandQueue {
	
	private BackwardSyncAddCommand addCommand = null;
	private BackwardSyncDeleteCommand deleteCommand = null;
	private BackwardSyncEditCommand editCommand = null;
	
		
	
	//Constructors 
	
	public GoogleCalendarBackwardSyncCommandQueue(BackwardSyncAddCommand backwardSyncAddCommand) {
		addCommand = backwardSyncAddCommand;
	}
	
	public GoogleCalendarBackwardSyncCommandQueue(BackwardSyncDeleteCommand backwardSyncDeleteCommand) {
		deleteCommand = backwardSyncDeleteCommand;
	}

	public GoogleCalendarBackwardSyncCommandQueue(BackwardSyncEditCommand backwardSyncEditCommand) {
		editCommand = backwardSyncEditCommand;
	}
	
	// Execute
	
	public void executeCommands() {
		if (addCommand != null) {
			addCommand.executeAdd();
//			addCommand.printTasks();
		}
		else if (deleteCommand != null) {
			deleteCommand.executeDelete();
		}
		else if (editCommand != null) {
			editCommand.executeEdit();
		}
	}
	

	
	
	
}
