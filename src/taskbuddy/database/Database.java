package taskbuddy.database;

import java.util.ArrayList;
import java.util.LinkedList;

import taskbuddy.logic.Task;
import taskbuddy.logic.Bundle;

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
     * Returns message indicating success of task addition
     * 
     * @return message indicating success of task addition
     */
    public Bundle resultTaskAdded() {
        Bundle result = new Bundle();
        result.putString("Success", "Task added.");
        return result;
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
    public Task searchTask(String title) {
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
     *         task is empty or if no match found.
     */
    public Task getTask(String title) {
        if (this.getTasks().isEmpty()) {
            return null;
        } else {
            return searchTask(title);
        }
    }
}
