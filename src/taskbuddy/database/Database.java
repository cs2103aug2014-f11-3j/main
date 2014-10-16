package taskbuddy.database;

import java.io.IOException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.NoSuchElementException;

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

    private static final String ERR_NOT_SYNCED_GOOGLE_CALENDAR = "Changes made to database and task log but not Google Calendar. ";
    private static final String ERR_NO_TASKS = "Cannot read from empty list of tasks.";
    private static final String ERR_NO_SUCH_TASK_ID = "No such task ID";

    private static final String ERR_MSG_SEARCH_STRING_EMPTY = "Search string cannot be empty.";

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

    // For injecting GoogleCalendarManager stub
    public void setGoogleCal(GoogleCalendarManager googleCal) {
        this.googleCal = googleCal;
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
     * Set task IDs according to their arraylist index. This method is called
     * whenever a task manipulation affects the arraylist index of a task, thus
     * making task ID allocation dynamic.
     */
    public void setTaskIds() {
        for (Task aTask : this.getTasks()) {
            aTask.setTaskId(this.getTasks().indexOf(aTask));
        }
    }

    /**
     * Adds task to temporary and logged list of tasks, as well as syncing to
     * Google Calendar.
     * 
     * @param task
     *            task to be added
     * @throws IOException
     *             when there are problems writing to log file
     * @throws UnknownHostException
     *             when user is offline and task cannot be synced to Google
     *             Calendar
     */
    public void addTask(Task task) throws IOException, UnknownHostException {
        this.tasks.add(task);
        this.setTaskIds();
        this.taskLogger.writeToLogFile(this.getTasks());
        try {
            googleCal.add(task);
        } catch (UnknownHostException e) {
            throw new UnknownHostException(ERR_NOT_SYNCED_GOOGLE_CALENDAR
                    + e.getMessage());
            // TODO Add add command to command queue
        }
    }

    /**
     * Finds the task whose task ID matches a given task ID
     * 
     * @param taskId
     *            task ID to search
     * @return task with matching task ID
     */
    public Task findMatchingTask(int taskId) {
        Task result = null;
        for (Task aTask : this.getTasks()) {
            if (aTask.getTaskId() == taskId) {
                result = aTask;
            }
        }
        return result;
    }

    /**
     * Searches for and returns a task based on its task ID from an empty or
     * non-empty stored list of tasks.
     * 
     * @param taskId
     *            title of task to retrieve
     * @return task whose title matches search string, null if stored list of
     *         task is empty or if no title match found.
     * @throws IllegalAccessException
     *             when this method tries to read from an empty list of tasks
     * @throws NoSuchElementException
     *             when this method cannot find a matching task to the given
     *             task ID.
     * 
     */
    public Task read(int taskId) throws IllegalAccessException,
            NoSuchElementException {
        if (this.getTasks().isEmpty()) {
            throw new IllegalAccessException(ERR_NO_TASKS);
        }

        assert !this.getTasks().isEmpty();
        Task result = findMatchingTask(taskId);
        if (result == null) {
            throw new NoSuchElementException(ERR_NO_SUCH_TASK_ID);
        }

        assert result != null;
        return result;
    }

    /**
     * Deletes a task from temporary and logged memory, as well as Google
     * Calendar, based on its task ID from an empty or non-empty stored list of
     * tasks.
     *
     * @param taskId
     *            task ID of task to be deleted
     * @throws IllegalAccessException
     *             when list of tasks is empty and there is no task for this
     *             method to delete
     * @throws NoSuchElementException
     *             when no matching task to given task ID is found
     * @throws IOException
     */
    public void delete(int taskId) throws IllegalAccessException,
            NoSuchElementException, IOException {
        Task taskToDelete = this.read(taskId);

        assert !this.getTasks().isEmpty() && taskToDelete != null;
        this.tasks.remove(taskToDelete);
        this.setTaskIds();
        this.taskLogger.writeToLogFile(this.getTasks());
        try {
            googleCal.delete(taskToDelete.getGID());
        } catch (UnknownHostException e) {
            throw new UnknownHostException(ERR_NOT_SYNCED_GOOGLE_CALENDAR
                    + e.getMessage());
            // TODO Add delete command to command queue
        }

    }
    //
    // // TODO Pass its test
    // /**
    // * Searches for and returns a task based on its title from a non-empty
    // * stored list of tasks. It is assumed that all task titles are unique
    // * ignoring case.
    // *
    // * @param title
    // * of task to be searched
    // * @return task whose title matches search string, null if no match found
    // */
    // public Task search(String title) {
    // if (title.equals(EMPTY_STRING)) {
    // throw new IllegalArgumentException(ERR_MSG_SEARCH_STRING_EMPTY);
    // }
    // for (Task aTask : this.getTasks()) {
    // if (aTask.getTitle().equalsIgnoreCase(title)) {
    // return aTask;
    // }
    // }
    // return null;
    // }

}