package taskbuddy.database;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;

import org.junit.After;
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

    private static final String EMPTY_STRING = "";

    // @formatter:off
    private static final String ERR_NO_TASKS = 
            "Cannot read from empty list of tasks.";
    private static final String ERR_NO_SUCH_TASK_ID = 
            "No such task ID";
    private static final String ERR_MSG_SEARCH_STRING_EMPTY = 
            "Search string cannot be empty.";
    // @formatter:on

    Database database;
    String logName = Database.LOG_NAME;
    Task task;
    GoogleCalendarManagerStub googleCalendarManagerStub;

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
        googleCalendarManagerStub = new GoogleCalendarManagerStub();
        database.setGoogleCal(googleCalendarManagerStub);
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

    /**
     * Deletes existing log file after all tests have been run
     */
    @After
    public void deleteLog() {
        File log = database.taskLogger.getLog();
        if (log.isFile()) {
            database.taskLogger.getLog().delete();
        }
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
    }

    @Test
    public void testAddTask() throws Exception {
        setup();
        createTask();

        // Test for task addition to database
        database.addTask(task);
        assertEquals("Number of tasks did not increase from 0 to 1 after task "
                + "addition", 1, database.getTasks().size());
        assertTrue("Task not added properly", database.getTasks().get(0)
                .equals(task));
        assertEquals("Google Calendar ID of task not set properly.", database
                .getTasks().get(0).getGID(),
                googleCalendarManagerStub.googleCalendarId);

        // Test for task addition to task log
        String expected;
        String actual;
        ArrayList<Task> readTasks = database.taskLogger.readTasks();
        assertEquals("Number of tasks in log did not increase from 0 to 1 ", 1,
                readTasks.size());
        actual = readTasks.get(0).displayTask();
        expected = database.getTasks().get(0).displayTask();
        assertTrue("Task not logged correctly in log file.",
                actual.equals(expected));

    }

    @Test
    public void testRead() throws Exception {
        setup();

        // Test for reading from empty task list
        try {
            database.read(0);
        } catch (Exception e) {
            assertTrue("Empty list exception not thrown.", e.getMessage()
                    .equals(ERR_NO_TASKS));
        }

        // Test if task is read correctly
        createTask();
        database.addTask(task);
        assertTrue("First task not read correctly from given task ID.",
                database.read(0).equals(task));
        createAnotherTask();
        database.addTask(task);
        assertTrue("Second task not read correctly from given task ID.",
                database.read(1).equals(task));

        // Test for invalid task ID
        try {
            database.read(3);
        } catch (Exception e) {
            assertTrue("No such task ID exception not thrown.", e.getMessage()
                    .equals(ERR_NO_SUCH_TASK_ID));
        }

    }

    @Test
    public void testDelete() throws Exception {
        setup();

        // Test for deletion from empty task list
        try {
            database.delete(0);
            fail("Should have thrown empty list exception.");
        } catch (Exception e) {
            assertTrue("Empty list exception not thrown.", e.getMessage()
                    .equals(ERR_NO_TASKS));
        }

        // Test if task is deleted from database
        addTasks();
        assertEquals(database.getTasks().size(), 2);
        database.delete(0);
        assertEquals(database.getTasks().size(), 1);
        task.setTaskId(0);
        assertTrue("Remaining task after deletion is not correct.", database
                .getTasks().get(0).equals(task));

        // Test if task is deleted from task log
        String expected;
        String actual;
        ArrayList<Task> readTasks = database.taskLogger.readTasks();
        assertEquals("Number of tasks in log did not decrease to one ", 1,
                readTasks.size());
        actual = readTasks.get(0).displayTask();
        expected = database.getTasks().get(0).displayTask();
        assertTrue("Task deletion not correctly logged.",
                actual.equals(expected));

        // Test for invalid task ID
        try {
            database.delete(3);
            fail("Should have thrown no such task ID exception.");
        } catch (Exception e) {
            assertTrue("No such task ID exception not thrown.", e.getMessage()
                    .equals(ERR_NO_SUCH_TASK_ID));
        }
    }

    @Test
    public void testSearch() throws Exception {
        String searchString;
        ArrayList<Task> searchResults;
        setup();

        // Test for empty search string
        try {
            database.search(EMPTY_STRING);
            fail("Should have thrown exception when search string is empty.");
        } catch (IllegalArgumentException e) {
            assertEquals(
                    "Wrong error message shown when search string is empty "
                            + "string.", e.getMessage(),
                    ERR_MSG_SEARCH_STRING_EMPTY);
        }

        // First alphabet 'T'/'t' left out deliberately
        searchString = "itle";
        // Test for empty task list
        try {
            database.search(searchString);
            fail("Should have thrown empty list exception.");
        } catch (Exception e) {
            assertEquals("Wrong error message for empty list exception.",
                    ERR_NO_TASKS, e.getMessage());
        }

        // Test for search in title/description fields
        addTasks();
        searchResults = database.search(searchString);
        assertTrue(
                "Search for 'itle' did not return both currently stored tasks.",
                database.getTasks().equals(searchResults));

        searchString = "Another";
        searchResults = database.search(searchString);
        assertEquals("More than one task is returned.", 1, searchResults.size());
        assertTrue("Search for 'Another' did not return second task.",
                searchResults.get(0).equals(database.getTasks().get(1)));

        searchString = "Another t";
        searchResults = database.search(searchString);
        assertEquals("More than one task is returned.", 1, searchResults.size());
        assertTrue("Search for 'Another' did not return second task.",
                searchResults.get(0).equals(database.getTasks().get(1)));
    }

    @Test
    public void testEdit() throws Exception {
        setup();
        int numberOfTasks = 3;
        // Add three tasks
        for (int i = 0; i < numberOfTasks; i++) {
            createTask();
            database.addTask(task);
        }
        assertEquals("Number of tasks is not three", numberOfTasks,
                database.getTasks().size());

        // Create task with task ID 1
        createAnotherTask();
        int taskIndexToEdit = 1;
        task.setTaskId(taskIndexToEdit);

        // Test edited task
        database.edit(task);
        assertTrue("Second task is not replaced properly",
                task.equals(database.getTasks().get(taskIndexToEdit)));

        // Check that other tasks are not edited
        createTask();
        for (int i = 0; i < numberOfTasks; i++) {
            if (i != taskIndexToEdit) {
                assertTrue(
                        "Other task titles got edited instead",
                        task.displayTitle().equals(
                                database.getTasks().get(i).displayTitle()));
                assertTrue(
                        "Other task descriptions got edited instead",
                        task.displayDescription()
                                .equals(database.getTasks().get(i)
                                        .displayDescription()));
            }
        }

        // Test for task edition to task log
        String expected;
        String actual;
        ArrayList<Task> readTasks = database.taskLogger.readTasks();
        for (int i = 0; i < numberOfTasks; i++) {
            actual = readTasks.get(i).displayTask();
            expected = database.getTasks().get(i).displayTask();
            assertTrue("Task " + i + " not logged correctly in log file.",
                    actual.equals(expected));
        }

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

    }

}