package taskbuddy.database;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
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

    private static final String SUCCESS = "Successful";
    private static final String FAIL = "Fail";
    private static final String SUCCESS_ADD_TASK = "Task added successfully.";
    private static final String FAIL_ADD_TASK = "Unable to add task.";

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
        startTime = "padding value";
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
        startTime = "padding value";
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

    @Test
    public void testAddTask() throws Exception {
        setup();
        createTask();
        Bundle addTask = database.addTask(task);

        boolean isAdded = addTask.bundle.containsKey(SUCCESS);
        assertTrue(isAdded);
        assertTrue("Success message not returned by addTask method.", addTask
                .getItem(SUCCESS).equals(SUCCESS_ADD_TASK));
        boolean isNotAdded = addTask.bundle.containsKey(FAIL);
        assertFalse("Message returned by addTask method contains fail status.",
                isNotAdded);

        assertEquals("Number of tasks did not increase from 0 to 1 after task "
                + "addition", 1, database.getTasks().size());
        assertTrue("Task not added properly", database.getTasks().get(0)
                .equals(task));

        String expected;
        String actual;
        ArrayList<Task> readTasks = database.taskLogger.readTasks();
        actual = readTasks.get(0).displayTask();
        expected = database.getTasks().get(0).displayTask();
        assertTrue("Task not logged correctly in log file.",
                actual.equals(expected));

        // Remove log file after test
        database.taskLogger.getLog().delete();
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

        // Remove log file after test
        database.taskLogger.getLog().delete();
    }

    // TODO Pass this test
    @Test
    public void testDelete() throws Exception {
        Bundle deleteTask;
        boolean isDeleted;
        boolean isNotDeleted;
        ArrayList<Task> readTasks;  
        String expected;
        String actual;
        setup();
        createTask();

        deleteTask = database.delete(title);
        isDeleted = deleteTask.bundle.containsKey(SUCCESS);
        assertFalse("Success status for deletion even though "
                + "list of tasks is empty", isDeleted);
        isNotDeleted = deleteTask.bundle.containsKey(FAIL);
        assertTrue("Fail status not returned for deletion in empty "
                + "list of tasks.", isNotDeleted);

         // Number of tasks is now one.
         database.addTask(task);
        
         deleteTask = database.delete("Untitled");
         isNotDeleted = deleteTask.bundle.containsKey(FAIL);         
         assertTrue("Fail status not returned for deletion of task with invalid title.", isNotDeleted);
         isDeleted = deleteTask.bundle.containsKey(SUCCESS);
         assertFalse("Success status wrongly returned for deletion of task with invalid title.", isDeleted);         
         assertEquals("Number of tasks is not one even no task was deleted.", 1,
         database.getTasks().size());
         readTasks = database.taskLogger.readTasks();
         assertEquals("Number of tasks in log is not one.", 1, readTasks.size());
         actual = readTasks.get(0).displayTask();
         expected = database.getTasks().get(0).displayTask();
         assertTrue("Task in log is not the same as that of in arraylist.", actual.equals(expected));
        
         deleteTask = database.delete(title);
         isDeleted = deleteTask.bundle.containsKey(SUCCESS);
         assertTrue("Success status for deletion not returned for valid deletion.", isDeleted);
         isNotDeleted = deleteTask.bundle.containsKey(FAIL);
         assertFalse("Fail status wrongly returned for valid deletion.", isNotDeleted);
         assertTrue("List of tasks is not empty after deletion.", database
         .getTasks().isEmpty());
         readTasks = database.taskLogger.readTasks();
         assertEquals("Number of tasks in log is not zero.", 0, readTasks.size());

        // Remove log file after test
        database.taskLogger.getLog().delete();
    }
}
