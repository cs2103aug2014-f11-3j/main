package taskbuddy.database;

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
    ArrayList<Task> tasks;
    LinkedList<DbCommand> commands;

    /**
     * Constructor for this class.
     */
    public Database() {
        tasks = new ArrayList<Task>();
        commands = new LinkedList<DbCommand>();
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
     * Adds task to the current storage of tasks.
     * 
     * @param task
     */
    public void addTask(Task task) {
        this.tasks.add(task);
    }

}
