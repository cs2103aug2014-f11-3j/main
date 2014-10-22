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
    private static final String EMPTY_STRING = "";

    // @formatter:off
	private static final String ERR_NOT_SYNCED_GOOGLE_CALENDAR = "Changes made to database and task log but not Google Calendar. ";
	private static final String ERR_NO_TASKS = "Cannot read from empty list of tasks.";
	private static final String ERR_NO_SUCH_TASK_ID = "No such task ID";
	private static final String ERR_MSG_SEARCH_STRING_EMPTY = "Search string cannot be empty.";
	// @formatter:on

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
     * For debugging
     */
    public void printTasks() {
        System.out.println(this.taskLogger.tasksToString(tasks));
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
        try {
            googleCal.add(task);
        } catch (UnknownHostException e) {
            throw new UnknownHostException(ERR_NOT_SYNCED_GOOGLE_CALENDAR
                    + e.getMessage());
            // TODO Add add command to command queue
        } finally {
            this.setTaskIds();
            this.taskLogger.writeToLogFile(this.getTasks());
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
     *             when there are problems writing to log file
     */
    public void delete(int taskId) throws IllegalAccessException,
            NoSuchElementException, IOException {
        Task taskToDelete = this.read(taskId);
        String gCalIdToDelete = taskToDelete.getGID();

        assert !this.getTasks().isEmpty() && taskToDelete != null;
        this.tasks.remove(taskToDelete);
        try {
            googleCal.delete(gCalIdToDelete);
        } catch (UnknownHostException e) {
            throw new UnknownHostException(ERR_NOT_SYNCED_GOOGLE_CALENDAR
                    + e.getMessage());
            // TODO Add delete command to command queue
        } finally {
            this.setTaskIds();
            this.taskLogger.writeToLogFile(this.getTasks());
        }
    }

    public boolean containsIgnoreCase(String a, String b) {
        String aLower = a.toLowerCase();
        String bLower = b.toLowerCase();

        return aLower.contains(bLower);
    }

    /**
     * Searches for and returns a list of tasks whose descriptions and/or titles
     * match the search string.
     *
     * @param searchString
     *            search string
     * @return task whose title and/or description matches search string; empty
     *         list if no matches found
     * @throws IllegalArgumentException
     *             when search string is empty
     * @throws IllegalAccessException
     *             when task list is empty and there is nothing to search for
     */
    public ArrayList<Task> search(String searchString)
            throws IllegalArgumentException, IllegalAccessException {
        if (searchString.equals(EMPTY_STRING)) {
            throw new IllegalArgumentException(ERR_MSG_SEARCH_STRING_EMPTY);
        }
        if (this.getTasks().isEmpty()) {
            throw new IllegalAccessException(ERR_NO_TASKS);
        }

        assert !searchString.equals(EMPTY_STRING) && !this.getTasks().isEmpty();
        ArrayList<Task> results = new ArrayList<Task>();
        for (Task aTask : this.getTasks()) {
            if (this.containsIgnoreCase(aTask.getTitle(), searchString)
                    || this.containsIgnoreCase(aTask.getDescription(),
                            searchString)) {
                results.add(aTask);
            }
        }

        return results;
    }

    /**
     * Similar to <code>search</code> method but searches only in the title
     * fields of tasks.
     *
     * @param searchString
     *            search string
     * @return list of tasks whose titles contain search string; empty list if
     *         no title matches found
     */
    public ArrayList<Task> searchTitle(String searchString) {
        ArrayList<Task> results = new ArrayList<Task>();
        for (Task aTask : this.getTasks()) {
            if (aTask.getTitle().contains(searchString)) {
                results.add(aTask);
            }
        }
        return results;
    }

    /**
     * Similar to <code>search</code> method but searches only in the
     * description fields of tasks.
     *
     * @param searchString
     *            search string
     * @return list of tasks whose descriptions contain search string; empty
     *         list if no description matches found
     */
    public ArrayList<Task> searchDescription(String searchString) {
        ArrayList<Task> results = new ArrayList<Task>();
        for (Task aTask : this.getTasks()) {
            if (aTask.getDescription().contains(searchString)) {
                results.add(aTask);
            }
        }
        return results;
    }

    /**
     * Edits a task with task ID of argument <code>newTask</code> by replacing
     * the old task with the new task from the argument of this method.
     * 
     * @param newTask
     *            new task, which has the same task ID as that of old task, to
     *            replace old task
     * @throws IllegalAccessException
     *             when there are no tasks stored currently and hence there is
     *             no task to edit
     * @throws NoSuchElementException
     *             when this method cannot find a matching task to task ID in
     *             the given task argument.
     * @throws IOException
     *             when there are problems writing to log file
     * 
     */
    public void edit(Task newTask) throws IllegalAccessException,
            NoSuchElementException, IOException {
        int taskIdToEdit = newTask.getTaskId();
        Task oldTask = this.read(taskIdToEdit);
        int oldTaskIndex = this.getTasks().indexOf(oldTask);
        this.getTasks().set(oldTaskIndex, newTask);

        try {
            googleCal.update(newTask);
        } catch (UnknownHostException e) {
            throw new UnknownHostException(ERR_NOT_SYNCED_GOOGLE_CALENDAR
                    + e.getMessage());
            // TODO Add edit command to command queue
        } finally {
            this.setTaskIds();
            this.taskLogger.writeToLogFile(this.getTasks());
        }

    }
}