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
public class Database {

    static final String LOG_NAME = "log";

    private static final String STATUS = "Status";
    private static final String MESSAGE = "Message";

    private static final String SUCCESS = "Success";
    private static final String SUCCESS_ADD = "Task added successfully.";
    private static final String SUCCESS_DELETE = "Task removed successfully.";

    private static final String FAILURE = "Failure";
    private static final String FAIL_ADD = "Unable to add task.";
    private static final String FAIL_DELETE_NO_TASKS = "No tasks to delete.";
    private static final String FAIL_DELETE_TITLE_NOT_FOUND = "No such title.";

    ArrayList<Task> tasks;
    LinkedList<DbCommand> commands;
    public TaskLogger taskLogger;
    GoogleCalendarManager googleCal;
    Bundle ack;
    String logName;

    /**
     * Constructor for this class. Initialises temporary and logged memory for
     * tasks and commands.
     * 
     * @throws IOException
     *             when log file cannot be loaded (if existing) or created.
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
     * Returns a message indicating success/failure of a method.
     * 
     * @param statusIn
     *            success/failure
     * @param messageIn
     *            success/failure message
     * @return
     */
    public Bundle ackFromDatabase(String statusIn, String messageIn) {
        Bundle ackBundle = new Bundle();
        ackBundle.putString(STATUS, statusIn);
        ackBundle.putString(MESSAGE, messageIn);
        return ackBundle;
    }

    /**
     * Adds task to temporary and logged list of tasks, as well as syncing to
     * Google Calendar.
     * 
     * @param task
     *            task to be added
     * @return Bundle containing "Successful" key if task is added successfully,
     *         "Fail" key otherwise
     * @throws IOException
     *             when user is offline
     */
    public Bundle addTask(Task task) throws IOException {
        ack = new Bundle();

        if (this.tasks.add(task)) {
            ack = this.ackFromDatabase(SUCCESS, SUCCESS_ADD);
            // TODO Call and handle GoogleCalendarManager's add method
            // Comment following line if running DatabaseTest
            GoogleCalendarManager.add(task);
            this.taskLogger.writeToLogFile(tasks);
        } else {
            ack = this.ackFromDatabase(FAILURE, FAIL_ADD);
        }
        return ack;
    }

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
        for (Task aTask : this.getTasks()) {
            if (aTask.getTitle().equalsIgnoreCase(title)) {
                return aTask;
            }
        }
        return null;
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
            if ((task = search(title)) == null) {
                ack = this.ackFromDatabase(FAILURE, FAIL_DELETE_TITLE_NOT_FOUND);
            } else {
                this.tasks.remove(task);
                ack = this.ackFromDatabase(SUCCESS, SUCCESS_DELETE);
                this.taskLogger.writeToLogFile(tasks);
                // TODO Call and handle GoogleCalendarManager's delete task
                // Comment following line if running DatabaseTest
                GoogleCalendarManager.delete(task.getGID());
            }
        }
        return ack;
    }

}