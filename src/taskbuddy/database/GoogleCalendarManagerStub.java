package taskbuddy.database;

import java.net.UnknownHostException;

import taskbuddy.googlecal.GoogleCalendarManager;
import taskbuddy.logic.Task;

/**
 * This class is a stub for the GoogleCalendarManager class, for dependency
 * injection in testing the Database class
 * 
 * @author Soh Yong Sheng
 *
 */
public class GoogleCalendarManagerStub extends GoogleCalendarManager {
    public String googleCalendarId = "11111";

    public void add(Task task) throws UnknownHostException {
        task.setGID(googleCalendarId);
    }

    public void delete(String eventId) throws UnknownHostException {
    }

    public void update(Task newTask) throws UnknownHostException {
    }
}
