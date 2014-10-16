package taskbuddy.database;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;

import org.junit.Test;

import taskbuddy.googlecal.GoogleCalendarManager;
import taskbuddy.logic.Bundle;
import taskbuddy.logic.Task;

/**
 * Contains unit tests for <code>Database</code> class
 * 
 * @author Soh Yong Sheng
 *
 */
public class DatabaseTest {

    private static final String EMPTY_STRING = "";

    private static final String ERR_MSG_SEARCH_STRING_EMPTY = "Search string cannot be empty.";

    private static final String STATUS = "Status";
    private static final String MESSAGE = "Message";

    private static final String SUCCESS = "Success";
    private static final String SUCCESS_ADD = "Task added successfully.";
    private static final String SUCCESS_DELETE = "Task removed successfully.";

    private static final String FAILURE = "Failure";
    private static final String FAIL_ADD = "Unable to add task.";
    private static final String FAIL_DELETE_NO_TASKS = "No tasks to delete.";
    private static final String FAIL_DELETE_TITLE_NOT_FOUND = "No such title.";

    Database database;
    Task task;
    String logName = Database.LOG_NAME;

    String title;
    String description;
    Calendar start;
    Calendar end;
    int priority;
    boolean isComplete;
    boolean isFloating;
    String googleCalendarId;

    public void createTask() {
        title = "Title";
        description = "Description";
        start = Calendar.getInstance();
        end = Calendar.getInstance();
        priority = 1;
        isComplete = true;
        isFloating = false;
        googleCalendarId = "11111";

        task = new Task(title);
        task.setDescription(description);
        task.setStartTime(start);
        task.setEndTime(end);
        task.setPriority(priority);
        task.setCompletion(isComplete);
        task.setFloating(isFloating);
        task.setGID(googleCalendarId);
    }

    public void createAnotherTask() {
        title = "Another title";
        description = "Another description";
        start = Calendar.getInstance();
        end = Calendar.getInstance();
        priority = 2;
        isComplete = false;
        isFloating = true;
        googleCalendarId = "22222";

        task = new Task(title);
        task.setDescription(description);
        task.setStartTime(start);
        task.setEndTime(end);
        task.setPriority(priority);
        task.setCompletion(isComplete);
        task.setFloating(isFloating);
        task.setGID(googleCalendarId);
    }

    public void setup() throws IOException, ParseException {
        database = new Database();
    }

    public void addTasks() throws IOException {
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
    
    public void deleteLog() {
        database.taskLogger.getLog().delete();
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

        // Construct database again and see if it reads in from log file.
        // Log file is read when database is constructed.
        setup();
        readTasks = database.getTasks();

        assertEquals("No tasks read in from log file", 2, readTasks.size());
        expected = readTasks.get(0).displayTask();
        actual = database.getTasks().get(0).displayTask();
        assertTrue("First task not read properly when preparing from "
                + "existing log file.", expected.equals(actual));
        expected = readTasks.get(1).displayTask();
        actual = database.getTasks().get(1).displayTask();
        assertTrue("Second task not read properly when preparing from "
                + "existing log file.", expected.equals(actual));
        deleteLog();

        // Test for non-existing log file
        database.taskLogger.prepareLog(logName);
        assertTrue("Log file object not initialised with prepareLog method.",
                database.taskLogger.log instanceof File);
        assertTrue("Log file doesn't exist even when it's supposed to have "
                + "been created.", database.taskLogger.getLog().exists());

        deleteLog();
    }

    @Test
    public void testAddTask() throws Exception {
        setup();
        createTask();

        // Check if task is added to database
        database.addTask(task);
        assertEquals("Number of tasks did not increase from 0 to 1 after task "
                + "addition", 1, database.getTasks().size());
        assertTrue("Task not added properly", database.getTasks().get(0)
                .equals(task));

        // Check if task is added to task log
        String expected;
        String actual;
        ArrayList<Task> readTasks = database.taskLogger.readTasks();
        actual = readTasks.get(0).displayTask();
        expected = database.getTasks().get(0).displayTask();
        assertTrue("Task not logged correctly in log file.",
                actual.equals(expected));

        deleteLog();
    }

    

    // TODO Complete and pass this test
    @Test
    public void testSearch() throws Exception {
        setup();
        addTasks();
        try {
            database.search(EMPTY_STRING);
            fail("Should have thrown exception when search string is empty.");
        } catch (IllegalArgumentException e) {
            assertEquals(
                    "Wrong error message shown when search string is empty "
                            + "string.", e.getMessage(),
                    ERR_MSG_SEARCH_STRING_EMPTY);
        }

        deleteLog();
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

        deleteLog();
    }

    @Test
    public void testDelete() throws Exception {
        Bundle ack;
        ArrayList<Task> readTasks;
        String expected;
        String actual;
        setup();
        createTask();

        ack = database.delete(title);
        assertTrue("Failure status for deletion not returned even though "
                + "there are no tasks to delete.",
                ack.getItem(STATUS).equals(FAILURE));
        assertTrue("Failure message for deletion not returned even though "
                + "there are no tasks to delete.",
                ack.getItem(MESSAGE).equals(FAIL_DELETE_NO_TASKS));

        // Number of tasks is now one.
        database.addTask(task);

        ack = database.delete("Untitled");
        assertTrue(
                "Failure status for deletion not returned for no title match.",
                ack.getItem(STATUS).equals(FAILURE));
        assertTrue(
                "Failure message for deletion not returned for no title match.",
                ack.getItem(MESSAGE).equals(FAIL_DELETE_TITLE_NOT_FOUND));
        assertEquals("Number of tasks is not one even no task was deleted.", 1,
                database.getTasks().size());
        readTasks = database.taskLogger.readTasks();
        assertEquals("Number of tasks in log is not one.", 1, readTasks.size());
        actual = readTasks.get(0).displayTask();
        expected = database.getTasks().get(0).displayTask();
        assertTrue("Task in log is not the same as that of in arraylist.",
                actual.equals(expected));

        ack = database.delete(title);
        assertTrue(
                "Success status for deletion not returned for valid deletion.",
                ack.getItem(STATUS).equals(SUCCESS));
        assertTrue(
                "Success message for deletion not returned for valid deletion.",
                ack.getItem(MESSAGE).equals(SUCCESS_DELETE));
        assertTrue("List of tasks is not empty after deletion.", database
                .getTasks().isEmpty());
        readTasks = database.taskLogger.readTasks();
        assertEquals("Number of tasks in log is not " + "zero.", 0,
                readTasks.size());

        deleteLog();
    }

    @Test
    public void testSetTaskIds() throws Exception {
        setup();
        addTasks();
        
        database.setTaskIds();
        ArrayList<Task> tasks = database.getTasks();
        for (Task aTask : tasks) {
            assertEquals(tasks.indexOf(aTask), aTask.getTaskId());
        }

        deleteLog();
    }

}