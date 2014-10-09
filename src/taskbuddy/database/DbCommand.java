package taskbuddy.database;

import taskbuddy.logic.Task;

/**
 * Represents a command to be executed by Google Calendar. Commands are stored
 * in a queue in <code>Database</code> when the user is offline and syncing to
 * Google Calendar is impossible. The reason for this class is for ease of
 * storage of commands as a general entity, as opposed to something like
 * <code>addCommand</code>, <code>deleteCommand</code> classes in the command
 * log of <code>Database</code> class.
 * 
 * @author Soh Yong Sheng
 *
 */
public class DbCommand {
    /**
     * The task to be manipulated by the command. For example, the task
     * description of "add this task" is "this task". Having a task stored in
     * this class also aids easy retrieval and emphasises the strong association
     * between the command, i.e. task manipulator, and the actual task itself.
     */
    Task task;

    /**
     * Constructor for the <code>Command</code> class that initialises the
     * command type. Initialisation of command type in the constructor
     * emphasises the fact that you cannot have a type-less command, that is you
     * need to specify what the command does.
     * 
     * @param commandType
     *            the command type
     */
    public DbCommand(Task task) {
        this.task = task;
    }

    /**
     * Retrieves and returns the task to be manipulated by the command.
     * 
     * @return the task to be manipulated by the command
     */
    public Task getTask() {
        return task;
    }

    public boolean execute() {
        // TODO Implement this through polymorphism
        return false;
    }

}