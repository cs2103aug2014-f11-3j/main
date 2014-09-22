package taskbuddy.file;

/**
 * Encapsulates task description, date and time into a single <code>Task</code>
 * object.
 * 
 * @author Soh Yong Sheng
 *
 */
public class Task {
    // task description
    String description;

    // date and time describes the task's deadline
    Date date;
    Time time;

    /**
     * Constructor for a <code>Task</code>, which initialises the task
     * description. Initalisation of task description in here emphasises the
     * fact that you cannot have a task without its description.
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

    /**
     * Retrieves and returns the date of the task deadline as a
     * <code>Date</code> object.
     * 
     * @return date of task deadline to retrieve
     */
    public Date getDate() {
        return date;
    }

    /**
     * Takes in a <code>Date</code> object and sets it as the date of the task
     * deadline
     * 
     * @param date
     *            date of task deadline to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Retrieves and returns the time of the task deadline as a
     * <code>Time</code>object.
     * 
     * @return time of task deadline to retrieve
     */
    public Time getTime() {
        return time;
    }

    /**
     * Takes in a <code>Time</code> object and sets it as the time of the task
     * deadline
     * 
     * @param time
     *            time of task deadline to set
     */
    public void setTime(Time time) {
        this.time = time;
    }

}
