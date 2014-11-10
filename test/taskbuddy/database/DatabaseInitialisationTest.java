//@author A0098745L

package taskbuddy.database;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;

import org.junit.Test;

import taskbuddy.googlecal.GoogleCalendarManager;
import taskbuddy.logic.Task;

public class DatabaseInitialisationTest {

    Task firstTask;
    Task secondTask;

    Database database;
    String logName = DatabaseHandler.TASK_LOG_NAME;
    GoogleCalendarManagerStub googleCalendarManagerStub;
    DatabaseHandler myDatabaseHandler;

    String expected;
    String actual;
    File taskLog;
    File commandLog;

    /**
     * Deletes existing command and task log files.
     */
    public void deleteLogs() {
        taskLog = new File(DatabaseHandler.TASK_LOG_NAME);
        if (taskLog.isFile()) {
            taskLog.delete();
        }
        commandLog = new File(DatabaseHandler.COMMAND_LOG_NAME);
        if (commandLog.isFile()) {
            commandLog.delete();
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

    public void setup() throws Exception {
        firstTask = createTask("First", "First description.");
        secondTask = createTask("Second", "Second description.");

        database = new Database();
        myDatabaseHandler = database.databaseHandler;

        googleCalendarManagerStub = new GoogleCalendarManagerStub();
        myDatabaseHandler.googleCal = googleCalendarManagerStub;
    }

    @Test
    public void testDatabase() throws Exception {
        deleteLogs();
        database = new Database();
        assertEquals("Static variable googleCal of GoogleCalendarCommand "
                + "not initialised properly in Database's constructor.",
                database.databaseHandler.googleCal,
                GoogleCalendarCommand.googleCal);

        deleteLogs();
        setup();
        assertTrue("Database not constructed with an instance of TaskLogger.",
                myDatabaseHandler.taskLogger instanceof TaskLogger);
        assertTrue("Database not constructed with arraylist of Task objects.",
                myDatabaseHandler.tasks instanceof ArrayList);
        assertTrue("Database not constructed with an instance of "
                + "GoogleCalendarManager.",
                myDatabaseHandler.googleCal instanceof GoogleCalendarManager);

        assertTrue("Database not constructed with linkedlist "
                + "of GoogleCalendarCommand objects.",
                myDatabaseHandler.commandQueue instanceof LinkedList);
        assertTrue(
                "Database not constructed with arraylist of Observer objects.",
                myDatabaseHandler.observerList instanceof ArrayList);

        assertTrue(
                "Task log not initialised with Database's constructor.",
                myDatabaseHandler.taskLogger.log instanceof File);
        assertTrue("Task log file doesn't exist even when it's supposed to have "
                + "been created.", myDatabaseHandler.taskLogger.getLog()
                .exists());
        assertTrue(
                "Command log not initialised with Database's constructor.",
                myDatabaseHandler.commandLogger.log instanceof File);
        assertTrue("Command log file doesn't exist even when it's supposed to have "
                + "been created.", myDatabaseHandler.commandLogger.getLog()
                .exists());

        // Populate task log first in preparation for next test
        addTasks();    
        taskLog = new File(DatabaseHandler.TASK_LOG_NAME);
        assertTrue("Task log doesn't exist when it should.", taskLog.isFile());
        
        
        setup();
        ArrayList<Task> tasks = database.getTasks();
        assertEquals("No tasks read in from log file", 2, tasks.size());
        expected = tasks.get(0).displayTask();
        actual = database.getTasks().get(0).displayTask();
        assertTrue("First task not read properly when preparing from "
                + "existing log file.", expected.equals(actual));
        expected = tasks.get(1).displayTask();
        actual = database.getTasks().get(1).displayTask();
        assertTrue("Second task not read properly when preparing from "
                + "existing log file.", expected.equals(actual));
    }

    @Test
    public void testMultipleInstances() throws Exception {
        deleteLogs();
        assertFalse("Log file exists when it shouldn't.", taskLog.isFile());

        // Initialise database using getInstance
        database = Database.getInstance();
        myDatabaseHandler = database.databaseHandler;
        googleCalendarManagerStub = new GoogleCalendarManagerStub();
        myDatabaseHandler.googleCal = googleCalendarManagerStub;

        firstTask = createTask("First", "First description.");
        secondTask = createTask("Second", "Second description.");
        addTasks();
        ArrayList<Task> tasks = database.getTasks();

        Database newDatabase = Database.getInstance();
        ArrayList<Task> newTasks = newDatabase.getTasks();

        for (int i = 0; i < tasks.size(); i++) {
            assertEquals("Task " + i + " not equal to that "
                    + "of its new task counterpart, i.e. getInstance "
                    + "method not working properly.", tasks.get(i).getTitle(),
                    newTasks.get(i).getTitle());
        }
    }

}
