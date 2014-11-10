//@author A0098745L

package taskbuddy.database;

import java.net.UnknownHostException;

import taskbuddy.logic.Task;

/**
 * This class is the delete variant of the <code>GoogleCalendarAdd</code> class.
 * 
 */
public class GoogleCalendarDelete extends GoogleCalendarCommand {
    static final String COMMAND_TYPE = "Delete";
    static final String DELIMITER = " | ";

    public GoogleCalendarDelete(Task task) {
        super(task);
    }

    @Override
    public void execute() throws UnknownHostException {
        try {
            googleCal.delete(task.getGID());
        } catch (UnknownHostException e) {
            throw new UnknownHostException(ERR_NOT_SYNCED_GOOGLE_CALENDAR);
        }
    }

    @Override
    public String displayCommand() {
        return COMMAND_TYPE + DELIMITER + this.getTask().displayTask();
    }
}
