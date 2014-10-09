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

    private static final String SUCCESS = "Successful";
    private static final String SUCCESS_ADD = "Task added successfully.";
    private static final String SUCCESS_DELETE = "Task removed successfully.";

    private static final String FAIL = "Fail";
    private static final String FAIL_ADD = "Unable to add task.";
    private static final String FAIL_DELETE_NO_TASKS = "No tasks to delete.";
    private static final String FAIL_DELETE_TITLE_NOT_FOUND = "No such title.";

    private static final String FAILURE_GOOGLE_CAL = "Failure";
    private static final String SUCCESS_GOOGLE_CAL = "Success";

    ArrayList<Task> tasks;
    LinkedList<DbCommand> commands;
    TaskLogger taskLogger;
    GoogleCalendarManager googleCal;
    Bundle status;
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
        status = new Bundle();

        if (this.tasks.add(task)) {
            this.taskLogger.writeToLogFile(tasks);
            status.putString(SUCCESS, SUCCESS_ADD);
            // TODO Call and handle GoogleCalendarManager's add method
            // Comment following line if running DatabaseTest
            GoogleCalendarManager.add(task);
        } else {
            status.putString(FAIL, FAIL_ADD);
        }
        return status;
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
        status = new Bundle();

        if (this.getTasks().isEmpty()) {
            status.putString(FAIL, FAIL_DELETE_NO_TASKS);
        } else {
            Task task = new Task();
            if ((task = search(title)) == null) {
                status.putString(FAIL, FAIL_DELETE_TITLE_NOT_FOUND);
            } else {
                this.tasks.remove(task);
                status.putString(SUCCESS, SUCCESS_DELETE);
                this.taskLogger.writeToLogFile(tasks);
                // TODO Call and handle GoogleCalendarManager's delete task
                // Comment following line if running DatabaseTest
                GoogleCalendarManager.delete(task.getGID());
            }
        }
        return status;
    }

}
