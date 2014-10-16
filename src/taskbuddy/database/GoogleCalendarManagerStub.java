package taskbuddy.database;

import java.net.UnknownHostException;

import taskbuddy.googlecal.GoogleCalendarManager;
import taskbuddy.logic.Task;

// This class is a stub for the GoogleCalendarManager class, for dependency 
// injection in testing the Database class
public class GoogleCalendarManagerStub extends GoogleCalendarManager {
    public void add(Task task) throws UnknownHostException {
        // Stub for adding tasks
    }
    
    public void delete(String eventId) throws UnknownHostException {
        // Stub for adding tasks
    }
}
