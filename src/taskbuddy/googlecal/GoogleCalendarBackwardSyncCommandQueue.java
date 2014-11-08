//@author A0108411W
package taskbuddy.googlecal;

/**
 * This class is part of the BackwardSync capabilities of the Google Calendar Sync.
 * Following the implementation of the Command pattern taught in CS2103, this class acts
 * as the command queue class. It executes the commands, without knowing what the functionalities
 * of the commands are.  
 * 
 *
 */


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
	
	// Execution 
	public void executeCommands() {
		if (addCommand != null) {
			addCommand.executeAdd();
		}
		else if (deleteCommand != null) {
			deleteCommand.executeDelete();
		}
		else if (editCommand != null) {
			editCommand.executeEdit();
		}
	}
}
