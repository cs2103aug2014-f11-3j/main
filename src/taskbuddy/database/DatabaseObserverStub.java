//@author A0098745L

package taskbuddy.database;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import taskbuddy.logic.Task;

/**
 * Stub that observes tasks stored in a <code>Database</code> object.
 * 
 */
public class DatabaseObserverStub implements DatabaseObserver {
    Database database;
    ArrayList<Task> observedTasks;

    /**
     * Constructor that takes in and specifies the database to be observed for
     * this class
     * 
     * @param database
     *            database to be observed
     * @throws IOException
     *             when task log cannot be read from, written to or created when
     *             constructing database
     * @throws ParseException
     *             when tasks cannot be parsed from existing log file when
     *             constructing database
     */
    public DatabaseObserverStub(Database database) throws IOException,
            ParseException {
        observedTasks = new ArrayList<Task>();
        this.database = database;
        database.addObserver(this);
    }

    @Override
    public void update() {
        observedTasks = database.getTasks();
    }

    /**
     * @return list of tasks in this class that gets updates from
     *         <code>Database</code>'s tasks
     */
    public ArrayList<Task> getObservedTasks() {
        return observedTasks;
    }

}
