package taskbuddy.file;

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
     * The command type. For example, the command type of "add this task" is
     * add. For a general Command object, a field is needed to identify the
     * precise command type. For example, "add something" is a command and can
     * be represented as a Command object. However, to execute this add command,
     * one needs to know that it is an add command. Having the commandType field
     * thus achieves the above mentioned.
     */
    CommandType commandType;

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
    public DbCommand(CommandType commandType) {
        this.commandType = commandType;
    }

    /**
     * Retrieves and returns the command type. Useful for execution, for
     * example, the execution of "add this task" is opposite to that of
     * "delete this task". The behaviour of execution depends on the command
     * type retrieved by this method.
     * 
     * @return the command type of this object
     */
    public CommandType getCommandType() {
        return commandType;
    }

    /**
     * Sets the command type of this object.
     * 
     * @param command
     *            the command type of this object.
     */
    public void setCommandType(CommandType command) {
        this.commandType = command;
    }

    /**
     * Retrieves and returns the task to be manipulated by the command.
     * 
     * @return the task to be manipulated by the command
     */
    public Task getTask() {
        return task;
    }

    /**
     * Sets the task to be manipulated by the command.
     * 
     * @param task
     *            task to be manipulated by the command.
     */
    public void setTask(Task task) {
        this.task = task;
    }

    public boolean execute() {
        // TODO Implement this
        // can implement this through polymorphism if you like
        return false;
    }

}