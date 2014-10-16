package taskbuddy.database;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedList;

import taskbuddy.googlecal.GoogleCalendarManager;
import taskbuddy.logic.Bundle;
import taskbuddy.logic.Task;

/**
 * Stores tasks and implements methods associated with adding/retrieval of
 * tasks. This class is primarily called by logic component and calls Google
 * Calendar manager for task synchronisation with Google Calendar.
 * 
 * @author Soh Yong Sheng
 *
 */
/**
 * @author Soh Yong Sheng
 *
 */
public class Database {

    static final String LOG_NAME = "log";

    private static final String ERR_MSG_SEARCH_STRING_EMPTY = "Search string cannot be empty.";

    private static final String STATUS = "Status";
    private static final String MESSAGE = "Message";
    private static final String EMPTY_STRING = "";

    private static final String SUCCESS = "Success";
    private static final String SUCCESS_ADD = "Task added successfully.";
    private static final String SUCCESS_DELETE = "Task removed successfully.";

    private static final String FAILURE = "Failure";
    private static final String FAIL_ADD = "Unable to add task.";
    private static final String FAIL_DELETE_INVALID_TITLE = "Invalid title.";
    private static final String FAIL_DELETE_NO_TASKS = "No tasks to delete.";
    private static final String FAIL_DELETE_TITLE_NOT_FOUND = "No such title.";

    ArrayList<Task> tasks;
    LinkedList<DbCommand> commands;
    TaskLogger taskLogger;
    GoogleCalendarManager googleCal;
    Bundle ack;
    String logName;

    /**
     * Constructor for this class. Initialises temporary and logged memory for
     * tasks and commands.
     * 
     * @throws IOException
     *             when log file cannot be read from, written to or created when
     *             user is offline and tasks cannot be synced to Google
     *             Calendar.
     * @throws ParseException
     *             when tasks cannot be parsed from existing log file
     */
    public Database() throws IOException, ParseException {
        // Future versions of software may allow for user to specify name of log
        // file
        logName = LOG_NAME;
        taskLogger = new TaskLogger();
        tasks = taskLogger.prepareLog(logName);

        commands = new LinkedList<DbCommand>();
        googleCal = new GoogleCalendarManager();
    }

    /**
     * Retrieves all stored tasks.
     * 
     * @return entire all stored tasks.
     */
    public ArrayList<Task> getTasks() {
        return this.tasks;
    }

    /**
     * Adds task to temporary and logged list of tasks, as well as syncing to
     * Google Calendar.
     * 
     * @param task
     *            task to be added
     * 
     * @throws IOException
     *             when user is offline and task cannot be synced to Google
     *             Calendar
     */

    public void addTask(Task task) throws IOException {
        // Always add to database first so that adding to database will execute
        // even if adding to Google Calendar fails.
        this.tasks.add(task);
        this.setTaskIds();
        try {
            GoogleCalendarManager.add(task);
        } catch (IOException e) {
            // TODO Have to wait for GoogleCalendarManager class to be
            // implemented to see what kind of exceptions are implemented.
            throw new IOException(
                    "Task added to database and log but not Google Calendar. "
                            + e.getMessage(), e);
            // TODO Add add command to command queue

        }
    }

    /**
     * Searches for and returns a task based on its title from an empty or
     * non-empty stored list of tasks. It is assumed that all task titles are
     * unique ignoring case.
     * 
     * @param title
     *            title of task to retrieve
     * @return task whose title matches search string, null if stored list of
     *         task is empty or if no title match found.
     */
    public Task read(String title) {
        if (this.getTasks().isEmpty()) {
            return null;
        } else {
            return search(title);
        }
    }

    /**
     * Searches for and deletes a task based on its title from an empty or
     * non-empty stored list of tasks. It is assumed that all task titles are
     * unique ignoring case. Deletion of tasks from Google Calendar is based on
     * Google Calendar ID, not title.
     *
     * @param title
     *            title of task to be deleted
     * @return true if task is deleted, false if list of tasks is empty or if no
     *         title match found
     * @throws IOException
     *             when user is offline
     */
    public Bundle delete(String title) throws IOException {
        if (this.getTasks().isEmpty()) {
            ack = this.ackFromDatabase(FAILURE, FAIL_DELETE_NO_TASKS);
        } else {
            Task task = new Task();
            if ((task = this.search(title)) == null) {
                ack = this
                        .ackFromDatabase(FAILURE, FAIL_DELETE_TITLE_NOT_FOUND);
            } else {
                this.tasks.remove(task);
                ack = this.ackFromDatabase(SUCCESS, SUCCESS_DELETE);
                this.taskLogger.writeToLogFile(tasks);
                // TODO Call and handle GoogleCalendarManager's delete task
                // Comment following line if running DatabaseTest
                googleCal.delete(task.getGID());
            }
        }
        return ack;
    }

    public void setTaskIds() {
        // TODO Auto-generated method stub
        for (Task aTask : this.getTasks()) {
            aTask.setTaskId(this.getTasks().indexOf(aTask));
        }
    }

    // TODO Pass its test
    /**
     * Searches for and returns a task based on its title from a non-empty
     * stored list of tasks. It is assumed that all task titles are unique
     * ignoring case.
     * 
     * @param title
     *            of task to be searched
     * @return task whose title matches search string, null if no match found
     */
    public Task search(String title) {
        if (title.equals(EMPTY_STRING)) {
            throw new IllegalArgumentException(ERR_MSG_SEARCH_STRING_EMPTY);
        }
        for (Task aTask : this.getTasks()) {
            if (aTask.getTitle().equalsIgnoreCase(title)) {
                return aTask;
            }
        }
        return null;
    }

}