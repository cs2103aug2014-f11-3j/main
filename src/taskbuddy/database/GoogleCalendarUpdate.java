//@author A0098745L

package taskbuddy.database;

import java.net.UnknownHostException;

import taskbuddy.logic.Task;

/**
 * This class is the update variant of the <code>GoogleCalendarAdd</code> class.
 * 
 */
public class GoogleCalendarUpdate extends GoogleCalendarCommand {
    static final String DELIMITER = " | ";
    static final String COMMAND_TYPE = "Update";
    
    
    public GoogleCalendarUpdate(Task task) {
        super(task);
    }

    @Override
    public void execute() throws UnknownHostException {
        try {
            googleCal.update(task);
        } catch (UnknownHostException e) {
            throw new UnknownHostException(ERR_NOT_SYNCED_GOOGLE_CALENDAR);
        }
    }

    @Override
    public String displayCommand() {
        return COMMAND_TYPE + DELIMITER + this.getTask().displayTask();
    }
}
