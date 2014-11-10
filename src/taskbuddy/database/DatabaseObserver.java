//@author A0098745L

package taskbuddy.database;

/**
 * Interface that observes tasks stored in a <code>Database</code> object.
 * 
 */
public interface DatabaseObserver {
    /**
     * Gets updates on tasks by pulling data from a <code>Database</code>
     * object.
     */
    void update();
}
