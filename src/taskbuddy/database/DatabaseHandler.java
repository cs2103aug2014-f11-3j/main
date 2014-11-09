package taskbuddy.database;

import java.io.IOException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import org.junit.Assert;

import taskbuddy.googlecal.GoogleCalendarManager;
import taskbuddy.logic.Task;

public class DatabaseHandler {
    static final String LOG_NAME = "log";
    private static final String EMPTY_STRING = "";

    // @formatter:off
    private static final String ERR_NOT_SYNCED_GOOGLE_CALENDAR = 
            "Changes made to database and task log but not Google Calendar. ";
    private static final String ERR_NO_TASKS = 
            "Cannot read from empty list of tasks.";
    private static final String ERR_NO_SUCH_TASK_ID = 
            "No such task ID";
    private static final String ERR_MSG_SEARCH_STRING_EMPTY = 
            "Search string cannot be empty.";
    // @formatter:on

    String logName;
    TaskLogger taskLogger;
    ArrayList<Task> tasks;
    GoogleCalendarManager googleCal;
    LinkedList<GoogleCalendarCommand> commandQueue;
    ArrayList<DatabaseObserver> observerList;

    /**
     * Constructor for this class. Initialises temporary and logged memory for
     * tasks and commands.
     * 
     * @throws IOException
     *             when log file cannot be read from, written to or created
     * @throws ParseException
     *             when tasks cannot be parsed from existing log file
     */
    DatabaseHandler() throws IOException, ParseException {
        this.logName = LOG_NAME;
        this.taskLogger = new TaskLogger();
        this.tasks = taskLogger.prepareLog(logName);
        this.googleCal = new GoogleCalendarManager();
        GoogleCalendarCommand.googleCal = this.googleCal;
        this.commandQueue = new LinkedList<GoogleCalendarCommand>();
        this.observerList = new ArrayList<DatabaseObserver>();
    }

    /**
     * Retrieves all stored tasks.
     * 
     * @return all tasks
     */
    ArrayList<Task> getTasks() {
        return this.tasks;
    }

    /**
     * Retrieves command queue for Google Calendar commands.
     * 
     * @return command queue for Google Calendar commands
     */
    public LinkedList<GoogleCalendarCommand> getCommandQueue() {
        return commandQueue;
    }

    /**
     * Set task IDs in order of their arraylist indices. This method is called
     * whenever a task manipulation affects the arraylist index of a task, thus
     * making task ID allocation dynamic.
     */
    void setTaskIds() {
        for (Task aTask : this.getTasks()) {
            int taskId = this.getTasks().indexOf(aTask) + 1;
            aTask.setTaskId(taskId);
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
    void addTask(Task task) throws IOException, UnknownHostException {
        this.tasks.add(task);
        try {
            googleCal.add(task);
        } catch (UnknownHostException e) {
            // TODO Add add command to command queue
            assert GoogleCalendarCommand.googleCal != null;
            this.commandQueue.add(new GoogleCalendarAdd(task));
            throw new UnknownHostException(ERR_NOT_SYNCED_GOOGLE_CALENDAR
                    + e.getMessage());
        } finally {
            this.setTaskIds();
            this.taskLogger.writeToLogFile(this.getTasks());
            this.notifyObservers();
        }
    }

    /**
     * Finds the task whose task ID matches a given task ID
     * 
     * @param taskId
     *            task ID to search
     * @return task with matching task ID
     */
    Task findMatchingTask(int taskId) {
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
    Task read(int taskId) throws IllegalAccessException, NoSuchElementException {
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
    void delete(int taskId) throws IllegalAccessException,
            NoSuchElementException, IOException {
        Task taskToDelete = this.read(taskId);
        String gCalIdToDelete = taskToDelete.getGID();

        assert !this.getTasks().isEmpty() && taskToDelete != null;
        this.tasks.remove(taskToDelete);
        try {
            googleCal.delete(gCalIdToDelete);
        } catch (UnknownHostException e) {
            // TODO Add delete command to command queue
            assert GoogleCalendarCommand.googleCal != null;
            this.commandQueue.add(new GoogleCalendarDelete(taskToDelete));
            throw new UnknownHostException(ERR_NOT_SYNCED_GOOGLE_CALENDAR
                    + e.getMessage());
        } finally {
            this.setTaskIds();
            this.taskLogger.writeToLogFile(this.getTasks());
            this.notifyObservers();
        }
    }

    /**
     * Checks if a string contains a given sub-string
     * 
     * @param container
     *            larger string that contains the given sub-string
     * @param contained
     *            sub-string
     * @return true if larger string contains given sub-string, false otherwise
     */
    boolean containsIgnoreCase(String container, String contained) {
        String containerLower = container.toLowerCase();
        String containedLower = contained.toLowerCase();

        return containerLower.contains(containedLower);
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
    ArrayList<Task> search(String searchString)
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
    ArrayList<Task> searchTitle(String searchString) {
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
    ArrayList<Task> searchDescription(String searchString) {
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
    void edit(Task newTask) throws IllegalAccessException,
            NoSuchElementException, IOException {
        int taskIdToEdit = newTask.getTaskId();
        Task oldTask = this.read(taskIdToEdit);
        int oldTaskIndex = this.getTasks().indexOf(oldTask);
        this.getTasks().set(oldTaskIndex, newTask);

        try {
            googleCal.update(newTask);
        } catch (UnknownHostException e) {
            // TODO Add edit command to command queue
            assert GoogleCalendarCommand.googleCal != null;
            this.commandQueue.add(new GoogleCalendarUpdate(newTask));
            throw new UnknownHostException(ERR_NOT_SYNCED_GOOGLE_CALENDAR
                    + e.getMessage());
        } finally {
            this.setTaskIds();
            this.taskLogger.writeToLogFile(this.getTasks());
            this.notifyObservers();
        }

    }

    /**
     * Adds observer to observe changes to tasks stored in this class.
     * 
     * @param databaseObserver
     *            observer subscribing for tasks updates in this class
     */
    void addObserver(DatabaseObserver databaseObserver) {
        this.observerList.add(databaseObserver);
    }

    /**
     * Notify observers of updates to tasks stored in this class
     */
    void notifyObservers() {
        for (DatabaseObserver anObserver : observerList) {
            anObserver.update();
        }
    }

    /**
     * Synchronises tasks that are manipulated in database but not in Google
     * Calendar when user is offline forward to Google Calendar Manager. This is
     * achieved by executing the commands in the command queue. The reversed
     * synchronisation from Google Calendar to database is called backward
     * synchronisation.
     * 
     * @throws UnknownHostException
     *             when user is still offline and synchronisation cannot be
     *             performed.
     */
    void forwardSync() throws UnknownHostException {
        while (!this.getCommandQueue().isEmpty()) {
            GoogleCalendarCommand nextCommand = this.getCommandQueue().peek();
            nextCommand.execute();
            // If execution is unsuccessful due to user still being offline, the
            // following code will not be executed and the command that has just
            // failed to be executed will remain in the command queue.
            this.getCommandQueue().remove();
        }
    }

    /**
     * Adds a task to database like the <code>addTask</code> method, except
     * without synchronisation to Google Calendar. This method is used when
     * Google Calendar manager needs to add a task to database to synchronise
     * tasks between Google Calendar manager and database, which is also known
     * as backward sync.
     * 
     * @param task
     *            task to be added
     * @throws IOException
     *             when there are problems writing to log file
     */
    public void addBackwardSync(Task task) throws IOException {
        this.tasks.add(task);
        this.setTaskIds();
        this.taskLogger.writeToLogFile(this.getTasks());
        this.notifyObservers();
    }
}
