package taskbuddy.database;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.LinkedList;

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
    private static final String TASKS = " tasks:";
    File log;
    ArrayList<Task> tasks;
    LinkedList<DbCommand> commands;
    BufferedWriter writer;

    /**
     * Constructor for this class. Initialises temporary and logged memory for
     * tasks and commands.
     * 
     * @throws IOException
     *             when log file cannot be loaded (if existing) or created.
     */
    public Database() throws IOException {
        tasks = new ArrayList<Task>();
        commands = new LinkedList<DbCommand>();

        String logName = "log";
        this.prepareLog(logName);
    }

    /**
     * Returns the <code>File</code> object representing the log file. Used
     * primarily for <code>File</code> and <code>Path</code> manipulation.
     * 
     * @return the <code>File</code> object representing the log file
     */
    public File getLog() {
        return log;
    }

    /**
     * Retrieves entire arraylist of stored tasks.
     * 
     * @return entire arraylist of stored tasks.
     */
    public ArrayList<Task> getTasks() {
        return tasks;
    }

    /**
     * Converts list of tasks to a string for writing to task log.
     * 
     * @param tasks
     *            list of tasks to be converted to string
     * @return string containing all tasks and associated information
     */
    public String tasksToString(ArrayList<Task> tasks) {
        int numberOfTasks = this.getTasks().size();
        String result = Integer.toString(numberOfTasks) + TASKS + "\n";

        for (Task aTask : tasks) {
            result = result + aTask.displayTask() + "\n";
        }
        return result;
    }

    /**
     * Writes all existing tasks represented as a <code>String</code> into the
     * log file. The <code>prepareLog</code> method must be called first before
     * calling this.
     * 
     * @param tasks
     *            all currently existing tasks in TextBuddy's task manager
     *            represented as a <code>String</code>
     * @throws IOException
     *             if there are write problems to the log file
     */
    public void writeToLogFile(ArrayList<Task> tasks) throws IOException {
        Path logFilePath = this.getLog().toPath();
        try {
            writer = Files.newBufferedWriter(logFilePath,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.TRUNCATE_EXISTING);
            writer.write(this.tasksToString(this.getTasks()));
        } finally {
            writer.close();
        }
    }

    /**
     * Prepares the log file to be read/written from/to. If the log file exists,
     * this method assumes that the log file came from a previous session. Thus
     * to use an existing log file, the text in the log file must be properly
     * formatted. If the log file does not exist, this method creates it and
     * logs all tasks into newly created log file.
     * 
     * @param tbTaskManager
     *            the task manager of TextBuddy handling all task operations
     * @return status message describing status of preparation
     * @throws IOException
     *             if there are read/write problems from/to the log file
     */
    public void prepareLog(String logName) throws IOException {

        log = new File(logName);
        if (log.isFile() && log.canRead() && log.canWrite()) {
            // load tasks and use existing log file for writing
        } else {
            log.createNewFile();
        }
    }

    /**
     * Adds task to the current storage of tasks.
     * 
     * @param task
     *            to be added
     * @return true if task is added successfully, false otherwise
     */
    public boolean addTask(Task task) {
        return this.tasks.add(task);
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
     */
    public boolean delete(String title) {
        if (!this.getTasks().isEmpty()) {
            Task task = new Task();
            if ((task = search(title)) != null) {
                this.tasks.remove(task);
                return true;
            }
        }
        return false;
    }

}
