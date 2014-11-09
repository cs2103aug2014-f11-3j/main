package taskbuddy.database;

import java.io.IOException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import taskbuddy.logic.Task;

/**
 * 
 * Facade class for <code>DatabaseHandler</code> that stores tasks and
 * implements methods associated with adding/retrieval of tasks. This class
 * provides the API for database component. It is primarily called by logic
 * component and effectively communicates with Google Calendar manager for task
 * synchronisation with Google Calendar.
 * 
 * @author Soh Yong Sheng
 *
 */
public class Database {
    private static Database instance = null;
    DatabaseHandler databaseHandler;

    /**
     * TODO Delete this before sending to production For debugging
     */
    public void printTasks() {
        ArrayList<Task> tasks = databaseHandler.getTasks();
        TaskLogger myTaskLogger = databaseHandler.taskLogger;
        System.out.println(myTaskLogger.tasksToString(tasks));
    }

    /**
     * @return an instance of this class.
     * @throws IOException
     *             when the same circumstances in this class' private
     *             constructor apply
     * @throws ParseException
     *             when the same circumstances in this class' private
     *             constructor apply
     */
    public static Database getInstance() throws IOException, ParseException {
        if (Database.instance == null) {
            Database.instance = new Database();
        }
        return Database.instance;
    }

    /**
     * Private constructor for this class that defeats instantiation by other
     * classes. Initialises temporary and logged memory for tasks and commands
     * through the database handler.
     * 
     * @throws IOException
     *             when log file cannot be read from, written to or created when
     *             user is offline and tasks cannot be synced to Google
     *             Calendar.
     * @throws ParseException
     *             when tasks cannot be parsed from existing log file
     */
    Database() throws IOException, ParseException {
        databaseHandler = new DatabaseHandler();
    }

    /**
     * Retrieves all stored tasks.
     * 
     * @return all tasks
     */
    public ArrayList<Task> getTasks() {
        return databaseHandler.getTasks();
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
        databaseHandler.addTask(task);
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
        return databaseHandler.read(taskId);
    }

    /**
     * Searches for and returns a task based on its Google Calendar ID from an
     * empty or non-empty stored list of tasks.
     * 
     * @param googleId
     *            Google Calendar ID of task to retrieve
     * @return task whose Google Calendar ID matches given Google Calendar ID,
     *         null if stored list of task is empty or if no Google Calendar ID
     *         match is found.
     * @throws IllegalAccessException
     *             when this method tries to read from an empty list of tasks
     * @throws NoSuchElementException
     *             when this method cannot find a matching task to the given
     *             Google Calendar ID.
     * 
     */
    public Task read(String googleId) throws IllegalAccessException,
            NoSuchElementException {
        return databaseHandler.read(googleId);
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
        databaseHandler.delete(taskId);
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
        return databaseHandler.search(searchString);
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
        databaseHandler.edit(newTask);
    }

    /**
     * Adds observer to observe changes to tasks stored in this class.
     * 
     * @param databaseObserver
     *            observer subscribing for tasks updates in this class
     */
    public void addObserver(DatabaseObserver databaseObserver) {
        databaseHandler.addObserver(databaseObserver);
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
    public void forwardSync() throws UnknownHostException {
        databaseHandler.forwardSync();
    }

    /**
     * Adds a task to database like the <code>addTask</code> method, except
     * without adding the this task to Google Calendar. This method is used when
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
        databaseHandler.addBackwardSync(task);
    }

    /**
     * Deletes a task from database like the <code>delete</code> method, except
     * without deleting this task from Google Calendar. This method is the
     * delete variant of the <code>addBackwardSync</code> method.
     *
     * @param googleId
     *            Google Calendar ID of task to be deleted
     * @throws IllegalAccessException
     *             when list of tasks is empty and there is no task for this
     *             method to delete
     * @throws NoSuchElementException
     *             when no matching task to given Google Calendar ID is found
     * @throws IOException
     *             when there are problems writing to log file
     */
    public void deleteBackwardSync(String googleId)
            throws IllegalAccessException, NoSuchElementException, IOException {
        databaseHandler.deleteBackwardSync(googleId);
    }

    /**
     * Edits a task from database like the <code>edit</code> method, except
     * without updating this task to Google Calendar. This method is the edit
     * variant of the <code>addBackwardSync</code> method.
     * 
     * @param googleIdToEdit
     *            Google Calendar ID of task to edit
     * @param newTask
     *            new task to replace task to be edited
     */
    public void editBackwardSync(String googleIdToEdit, Task newTask)
            throws IllegalAccessException, NoSuchElementException, IOException {
        // TODO Auto-generated method stub
        databaseHandler.editBackwardSync(googleIdToEdit, newTask);

    }

}