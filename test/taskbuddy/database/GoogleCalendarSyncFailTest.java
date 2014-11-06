package taskbuddy.database;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;

import taskbuddy.database.GoogleCalendarCommand;
import taskbuddy.logic.Task;

public class GoogleCalendarSyncFailTest {
    // @formatter:off
    private static final String ERR_NOT_SYNCED_GOOGLE_CALENDAR = 
            "Changes made to database and task log but not Google Calendar. ";
    // @formatter:on

    Task firstTask;
    Task secondTask;

    Database database;
    DatabaseHandler myDatabaseHandler;

    OfflineGoogleCalendarManagerStub offlineGoogleCalendarManagerStub;

    /**
     * Deletes existing log file before running tests
     */
    public void deleteLog() {
        File log = new File(DatabaseHandler.LOG_NAME);
        if (log.isFile()) {
            log.delete();
        }
    }

    public void addTasks() throws IOException, UnknownHostException {
        database.addTask(firstTask);
        database.addTask(secondTask);
    }

    public Task createTask(String title, String description) {
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        int priority = 1;
        boolean isComplete = true;
        boolean isFloating = false;

        Task task = new Task(title);
        task.setDescription(description);
        task.setStartTime(start);
        task.setEndTime(end);
        task.setPriority(priority);
        task.setCompletion(isComplete);
        task.setFloating(isFloating);

        return task;
    }

    @Before
    public void setup() throws Exception {
        deleteLog();

        firstTask = createTask("First", "First description.");
        secondTask = createTask("Second", "Second description.");

        database = new Database();
        myDatabaseHandler = database.databaseHandler;

        // @formatter:off
        offlineGoogleCalendarManagerStub 
            = new OfflineGoogleCalendarManagerStub();
        // @formatter:on
        myDatabaseHandler.googleCal = offlineGoogleCalendarManagerStub;
        GoogleCalendarCommand.setGoogleCal(myDatabaseHandler.googleCal);
    }

    @Test
    public void testOfflineExceptionMessageHandling() throws Exception {
        try {
            database.addTask(firstTask);
            fail("No exception thrown when adding task "
                    + "to Google Calendar when user is offline.");
        } catch (UnknownHostException e) {
            String errorMessageFromDatabase = ERR_NOT_SYNCED_GOOGLE_CALENDAR;
            String errorMessageFromGoogleCalendarManager = offlineGoogleCalendarManagerStub
            assertEquals("Wrong exception message when syncing to "
                    + "Google Calendar when user is offline.",
                    ERR_NOT_SYNCED_GOOGLE_CALENDAR, e.getMessage() + off);
        }

    }
}