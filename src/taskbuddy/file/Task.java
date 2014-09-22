package taskbuddy.file;

/**
 * Encapsulates task description, date and time into a single <code>Task</code>
 * object.
 * 
 * @author Soh Yong Sheng
 *
 */
public class Task {
    String description;

    /**
     * Constructor for a <code>Task</code>, which initialises the task
     * description. Initalisation of task description in here emphasies the fact
     * that you cannot have a task without its description.
     * 
     * @param inputDescription
     *            task description
     */
    public Task(String inputDescription) {
        this.description = inputDescription;
    }

    /**
     * Returns the task description as a string
     * 
     * @return the task description as a string
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the task.
     * 
     * @param inputDescription
     *            the task description
     */
    public void setDescription(String inputDescription) {
        this.description = inputDescription;
    }
}
