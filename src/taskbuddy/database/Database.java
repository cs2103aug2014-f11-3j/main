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
    private static final String FAIL_ADD_TASK = "Unable to add task.";

    private static final String FAIL = "Fail";

    private static final String FAILURE_GOOGLE_CAL = "Failure";

    private static final String SUCCESS_GOOGLE_CAL = "Success";

    private static final String SUCCESS_ADD_TASK = "Task added successfully.";

    private static final String SUCCESS = "Successful";

    static final String LOG_NAME = "log";

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
     * Adds task to the current storage of tasks.
     * 
     * @param task
     *            to be added
     * @return true if task is added successfully, false otherwise
     * @throws IOException
     *             user is offline
     */
    public Bundle addTask(Task task) throws IOException {
        
        status = new Bundle();
        if (!this.tasks.add(task)
                || GoogleCalendarManager.add(task).bundle
                        .containsKey(FAILURE_GOOGLE_CAL)) {
            status.putString(FAIL, FAIL_ADD_TASK);
        } else {
            this.taskLogger.writeToLogFile(tasks);
            status.putString(SUCCESS, SUCCESS_ADD_TASK);
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
     * unique ignoring case.
     * 
     * @param title
     *            title of task to be deleted
     * @return true if task is deleted, false if list of tasks is empty or if no
     *         title match found
     * @throws IOException 
     */
    public Bundle delete(String title) throws IOException {
        status = new Bundle();
        if (!this.getTasks().isEmpty()) {
            Task task = new Task();
            if ((task = search(title)) != null) {
                this.tasks.remove(task);
                if (GoogleCalendarManager.delete(task.getGID()).bundle.containsKey(SUCCESS_GOOGLE_CAL)) {
                    status.putString(SUCCESS, "Task deleted successfully.");
                    return status;
                }
            }
        }
        status.putString(FAIL, "Task deletion unsuccessful.");
        return status;
    }

}
