package taskbuddy.database;

import java.net.UnknownHostException;

import taskbuddy.googlecal.GoogleCalendarManager;
import taskbuddy.logic.Task;

/**
 * This class is a stub for the offline <code>GoogleCalendarManager</code>
 * class, for dependency injection into <code>Database</code> class to test its
 * exception handling when user is offline.
 * 
 * @author Soh Yong Sheng
 *
 */
public class OfflineGoogleCalendarManagerStub extends GoogleCalendarManager {
    private static final String ERROR_USER_OFFLINE = "User is offline";

    public void add(Task task) throws UnknownHostException {
        throw new UnknownHostException(ERROR_USER_OFFLINE);
    }

    public void delete(String eventId) throws UnknownHostException {
        throw new UnknownHostException(ERROR_USER_OFFLINE);
    }

    public void update(Task newTask) throws UnknownHostException {
        throw new UnknownHostException(ERROR_USER_OFFLINE);
    }
}