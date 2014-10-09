package taskbuddy.database;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedList;

import org.junit.Test;

import taskbuddy.googlecal.GoogleCalendarManager;
import taskbuddy.logic.Task;

/**
 * Contains unit tests for <code>Database</code> class
 * 
 * @author Soh Yong Sheng
 *
 */
public class DatabaseTest {

    Database database;
    Task task;
    String logName = Database.LOG_NAME;

    String title;
    String description;
    String startTime;
    String endDate;
    String endTime;
    int priority;
    boolean isComplete;
    boolean isFloating;
    String googleCalendarId;

    public void createTask() {
        title = "Title";
        description = "Description";
        startTime = "PADDING_VALUE";
        endDate = "01000001";
        endTime = "0101";
        priority = 1;
        isComplete = true;
        isFloating = false;
        googleCalendarId = "11111";

        task = new Task(title);
        task.setDescription(description);
        task.setStartTime(startTime);
        task.setEndTime(endDate, endTime);
        task.setPriority(priority);
        task.setCompletion(isComplete);
        task.setFloating(isFloating);
        task.setGID(googleCalendarId);
    }

    public void createAnotherTask() {
        title = "Another title";
        description = "Another description";
        startTime = "PADDING_VALUE";
        endDate = "02010002";
        endTime = "0202";
        priority = 2;
        isComplete = false;
        isFloating = true;
        googleCalendarId = "22222";

        task = new Task(title);
        task.setDescription(description);
        task.setStartTime(startTime);
        task.setEndTime(endDate, endTime);
        task.setPriority(priority);
        task.setCompletion(isComplete);
        task.setFloating(isFloating);
        task.setGID(googleCalendarId);
    }

    public void setup() throws IOException, ParseException {
        database = new Database();
    }

    public void addTasks() {
        createTask();
        database.addTask(task);
        createAnotherTask();
        database.addTask(task);
    }

    /**
     * Create a dummy log file with two tasks added by <code>addTasks</code>
     * method
     * 
     * @throws IOException
     */
    public void createDummyLog() throws IOException {
        addTasks();
        database.taskLogger.writeToLogFile(database.getTasks());
    }

    @Test
    public void testDatabase() throws Exception {
        File log = new File(logName);
        assertFalse("Log file created when it's not supposed to exist.",
                log.isFile());

        // Construct database
        setup();
        assertTrue("Database not constructed with arraylist of Task objects.",
                database.tasks instanceof ArrayList);
        assertTrue("Database not constructed with linkedlist of DbCommands"
                + "objects.", database.commands instanceof LinkedList);
        assertTrue("Database not constructed with an instance of TaskLogger.",
                database.taskLogger instanceof TaskLogger);
        assertTrue("Database not constructed with an instance of "
                + "GoogleCalendarManager.",
                database.googleCal instanceof GoogleCalendarManager);

        String expected;
        String actual;

        // Test for preparing from existing log file
        ArrayList<Task> readTasks;
        database.taskLogger.prepareLog(logName);
        createDummyLog();
        readTasks = database.taskLogger.prepareLog(logName);

        assertEquals("No tasks read in from log file", 2, readTasks.size());
        expected = readTasks.get(0).displayTask();
        actual = database.getTasks().get(0).displayTask();
        assertTrue("First task not read properly when preparing from "
                + "existing log file.", expected.equals(actual));
        expected = readTasks.get(1).displayTask();
        actual = database.getTasks().get(1).displayTask();
        assertTrue("Second task not read properly when preparing from "
                + "existing log file.", expected.equals(actual));
        // Remove dummy log
        database.taskLogger.getLog().delete();
        
        // Test for non-existing log file
        database.taskLogger.prepareLog(logName);
        assertTrue("Log file object not initialised with prepareLog method.",
                database.taskLogger.log instanceof File);
        assertTrue("Log file doesn't exist even when it's supposed to have "
                + "been created.", database.taskLogger.getLog().exists());

        // Remove log file after test
        database.taskLogger.getLog().delete();
    }

    /*
    @Test
    public void testAddTask() throws Exception {
        setup();
        createTask();

        assertTrue(database.addTask(task));
        assertEquals("Number of tasks did not increase from 0 to 1 after task "
                + "addition", 1, database.getTasks().size());
        assertTrue("Task not added properly", database.getTasks().get(0)
                .equals(task));
    }

    @Test
    public void testRead() throws Exception {
        setup();
        createTask();

        assertNull(database.read(title));

        // Test for normal task retrieval
        database.addTask(task);
        Task readTask = database.read(title);
        assertTrue("Task titled 'Title' not retrieved.", readTask.equals(task));

        // Confirm that task is passed by reference
        String description = "description";
        readTask.setDescription(description);
        assertTrue(database.read(title).getDescription().equals(description));

        // Test for retrieval of task with invalid title
        title = "Untitled";
        assertNull(database.read(title));

        task.setTitle(title);
        database.addTask(task);
        assertTrue("Task titled 'Untitled' not retrieved.", database
                .read(title).equals(task));
    }

    @Test
    public void testDelete() throws Exception {
        setup();
        createTask();

        assertFalse("Task is deleted even though list of tasks is empty",
                database.delete(title));

        // Number of tasks is now 1
        database.addTask(task);

        assertFalse("Delete method returns true even when there's no task "
                + "titled 'Untitled'.", database.delete("Untitled"));
        assertEquals("Number of tasks is not 1 even no task was deleted.", 1,
                database.getTasks().size());

        assertTrue("Delete method does not return true after deletion.",
                database.delete(title));
        assertTrue("List of tasks is not empty after deletion.", database
                .getTasks().isEmpty());

    }
    */
}
