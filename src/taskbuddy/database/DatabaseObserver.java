package taskbuddy.database;

/**
 * Interface that observes tasks stored in a <code>Database</code> object.
 * 
 * @author Soh Yong Sheng
 *
 */
public interface DatabaseObserver {
    /**
     * Gets updates on tasks by pulling data from a <code>Database</code>
     * object.
     */
    void update();
}
