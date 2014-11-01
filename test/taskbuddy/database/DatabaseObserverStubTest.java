package taskbuddy.database;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;

import taskbuddy.logic.Task;

public class DatabaseObserverStubTest {
    Task firstTask;
    Task secondTask;
    Database database;
    DatabaseObserverStub databaseObserverStub;

    String expected;
    String actual;

    /**
     * Deletes existing log file before running tests
     */
    public void deleteLog() {
        File log = new File(DatabaseHandler.LOG_NAME);
        if (log.isFile()) {
            log.delete();
        }
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

    public void addTasks() throws IOException, UnknownHostException {
        database.addTask(firstTask);
        database.addTask(secondTask);
    }

    @Before
    public void setup() throws Exception {
        deleteLog();

        firstTask = createTask("First", "First description.");
        secondTask = createTask("Second", "Second description.");

        database = new Database();
        DatabaseHandler myDatabaseHandler = database.databaseHandler;
        
        GoogleCalendarManagerStub googleCalendarManagerStub = new GoogleCalendarManagerStub();
        myDatabaseHandler.googleCal = googleCalendarManagerStub;
        
        databaseObserverStub = new DatabaseObserverStub(database);
    }

    @Test
    public void testDatabaseObserverStub() throws Exception {
        assertTrue("Database observer stub not initialised with database.",
                databaseObserverStub.database instanceof Database);
    }

    @Test
    public void testUpdate() throws Exception {
        assertTrue(databaseObserverStub.getObservedTasks().isEmpty());

        addTasks();
        databaseObserverStub.update();
        ArrayList<Task> observedTasks = databaseObserverStub.getObservedTasks();
        int numberOfAddedTasks = 2;
        assertEquals("Number of observed tasks is not two.",
                numberOfAddedTasks, observedTasks.size());
        assertEquals("Observed tasks not the same as that in database.",
                database.getTasks(), observedTasks);
    }
}
