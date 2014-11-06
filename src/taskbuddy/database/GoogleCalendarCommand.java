package taskbuddy.database;

import java.net.UnknownHostException;

import taskbuddy.googlecal.GoogleCalendarManager;
import taskbuddy.logic.Task;

/**
 * Represents a command to be executed by Google Calendar. Commands are stored
 * in a queue in <code>Database</code> when the user is offline and syncing to
 * Google Calendar is impossible. This class implements the command pattern.
 * 
 * @author Soh Yong Sheng
 *
 */
abstract class GoogleCalendarCommand {
    // @formatter:off
    static final String ERR_NOT_SYNCED_GOOGLE_CALENDAR = 
            "Cannot synchronise tasks to Google Calendar.";
    // @formatter:on
    static GoogleCalendarManager googleCal;
    Task task;

    /**
     * Constructor for this class, which initialises the task to be manipulated
     * by the command, and the associated Google Calendar manager. For example,
     * the task field of an add command is the task to be added to Google
     * Calendar.
     * 
     * @param task
     *            task to be manipulated by the command
     */
    public GoogleCalendarCommand(Task task) {
        this.task = task;
    }

    /**
     * Retrieves and returns the task to be manipulated by the command.
     * 
     * @return the task to be manipulated by the command
     */
    public Task getTask() {
        return task;
    }

    /**
     * Executes the command, where the execution behaviour depends on the type
     * of subclass, that is an add command's execution will be different from a
     * delete command's execution/
     * 
     * @throws UnknownHostException
     *             when user is offline
     */
    public abstract void execute() throws UnknownHostException;

}