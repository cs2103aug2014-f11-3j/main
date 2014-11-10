//@author A0098745L

package taskbuddy.database;

import java.net.UnknownHostException;

import taskbuddy.logic.Task;

/**
 * This class is used by <code>Database</code> to store an "add task" command to
 * Google Calendar when the user is offline.
 * 
 */
public class GoogleCalendarAdd extends GoogleCalendarCommand {
    static final String COMMAND_TYPE = "Add";
    static final String DELIMITER = " | ";

    public GoogleCalendarAdd(Task task) {
        super(task);
    }

    @Override
    public void execute() throws UnknownHostException {
        try {
            googleCal.add(task);
        } catch (UnknownHostException e) {
            throw new UnknownHostException(ERR_NOT_SYNCED_GOOGLE_CALENDAR);
        }
    }

    @Override
    public String displayCommand() {
        return COMMAND_TYPE + DELIMITER + this.getTask().displayTask();
    }

}
