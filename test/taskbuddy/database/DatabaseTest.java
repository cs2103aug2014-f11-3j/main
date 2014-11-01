package taskbuddy.database;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import taskbuddy.database.Database;
import taskbuddy.database.DatabaseHandler;
import taskbuddy.database.GoogleCalendarManagerStub;
import taskbuddy.database.TaskLogger;
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

    Task firstTask;
    Task secondTask;

    Database database;
    String logName = DatabaseHandler.LOG_NAME;
    GoogleCalendarManagerStub googleCalendarManagerStub;
    DatabaseHandler myDatabaseHandler;

    String expected;
    String actual;

    private DatabaseObserverStub databaseObserverStub;

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
        
        googleCalendarManagerStub = new GoogleCalendarManagerStub();
        myDatabaseHandler.googleCal = googleCalendarManagerStub;
        
        databaseObserverStub = new DatabaseObserverStub(database);
    }

    @Test
    public void testSetTaskIds() throws Exception {
        addTasks();

        myDatabaseHandler.setTaskIds();
        ArrayList<Task> tasks = database.getTasks();
        for (Task aTask : tasks) {
            int expectedTaskId = tasks.indexOf(aTask) + 1;
            assertEquals(expectedTaskId, aTask.getTaskId());
        }
    }

    @Test
    public void testAddTask() throws Exception {
        database.addTask(firstTask);
        assertEquals("Number of tasks did not increase from 0 to 1 after task "
                + "addition", 1, database.getTasks().size());
        assertTrue("Task not added properly", database.getTasks().get(0)
                .equals(firstTask));
        assertEquals("Google Calendar ID of task not set properly.", database
                .getTasks().get(0).getGID(),
                googleCalendarManagerStub.googleCalendarId);

        ArrayList<Task> readTasks = myDatabaseHandler.taskLogger.readTasks();
        assertEquals("Number of tasks in log did not increase from 0 to 1 ", 1,
                readTasks.size());
        actual = readTasks.get(0).displayTask();
        expected = database.getTasks().get(0).displayTask();
        assertTrue("Task not logged correctly in log file.",
                actual.equals(expected));
    }

    @Test
    public void testRead() throws Exception {
        try {
            database.read(1);
            fail("Exception not thrown when trying to "
                    + "read from empty list of tasks.");
        } catch (Exception e) {
            assertTrue("Empty list exception not thrown.", e.getMessage()
                    .equals(ERR_NO_TASKS));
        }

        database.addTask(firstTask);
        assertTrue("First task not read correctly from given task ID.",
                database.read(1).equals(firstTask));
        database.addTask(secondTask);
        assertTrue("Second task not read correctly from given task ID.",
                database.read(2).equals(secondTask));

        try {
            database.read(3);
        } catch (Exception e) {
            assertTrue("No such task ID exception not thrown.", e.getMessage()
                    .equals(ERR_NO_SUCH_TASK_ID));
        }

    }

    @Test
    public void testDelete() throws Exception {
        // Test for deletion from empty task list
        try {
            database.delete(0);
            fail("Exception not thrown when trying to delete "
                    + "from empty list of tasks.");
        } catch (Exception e) {
            assertTrue("Empty list exception not thrown.", e.getMessage()
                    .equals(ERR_NO_TASKS));
        }

        addTasks();
        assertEquals("Number of tasks in temporary memory is not two.",
                database.getTasks().size(), 2);

        int taskIdToDelete = 1;
        database.delete(taskIdToDelete);

        assertEquals("Number of tasks did not decrease to one.", database
                .getTasks().size(), 1);
        assertTrue("Remaining task after deletion is not correct.", database
                .getTasks().get(0).equals(secondTask));

        ArrayList<Task> readTasks = myDatabaseHandler.taskLogger.readTasks();
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
    public void testEdit() throws Exception {
        addTasks();
        int numberOfTasks = 2;
        assertEquals("Number of tasks is not two", numberOfTasks, database
                .getTasks().size());

        // Create task with task ID 1
        Task newTask = createTask("New", "New description.");
        int taskIdToEdit = 1;
        newTask.setTaskId(taskIdToEdit);

        database.edit(newTask);
        assertTrue("First task is not replaced properly",
                newTask.equals(database.read(taskIdToEdit)));
        int taskIdNotEdited = 2;
        assertTrue("Second task is wrongly edited.",
                secondTask.equals(database.read(taskIdNotEdited)));

        // Test for task edition to task log
        ArrayList<Task> readTasks = myDatabaseHandler.taskLogger.readTasks();
        for (int i = 0; i < numberOfTasks; i++) {
            actual = readTasks.get(i).displayTask();
            expected = database.getTasks().get(i).displayTask();
            assertTrue("Task " + i + " not logged correctly in log file.",
                    actual.equals(expected));
        }
    }

    @Test
    public void testDatabase() throws Exception {
        assertTrue("Database not constructed with arraylist of Task objects.",
                myDatabaseHandler.tasks instanceof ArrayList);
        assertTrue("Database not constructed with linkedlist of DbCommands"
                + "objects.", myDatabaseHandler.commands instanceof LinkedList);
        assertTrue("Database not constructed with an instance of TaskLogger.",
                myDatabaseHandler.taskLogger instanceof TaskLogger);
        assertTrue("Database not constructed with an instance of "
                + "GoogleCalendarManager.",
                myDatabaseHandler.googleCal instanceof GoogleCalendarManager);

        // Test for non-existing log file
        myDatabaseHandler.taskLogger.prepareLog(logName);
        assertTrue("Log file object not initialised with prepareLog method.",
                myDatabaseHandler.taskLogger.log instanceof File);
        assertTrue("Log file doesn't exist even when it's supposed to have "
                + "been created.", myDatabaseHandler.taskLogger.getLog()
                .exists());

        // Test for preparing from existing log file
        addTasks();
        // Construct database again and see if it reads in from log file.
        // Log file is read when database is constructed.
        database = new Database();
        myDatabaseHandler.googleCal = googleCalendarManagerStub;

        ArrayList<Task> readTasks = database.getTasks();
        assertEquals("No tasks read in from log file", 2, readTasks.size());
        expected = readTasks.get(0).displayTask();
        actual = database.getTasks().get(0).displayTask();
        assertTrue("First task not read properly when preparing from "
                + "existing log file.", expected.equals(actual));
        expected = readTasks.get(1).displayTask();
        actual = database.getTasks().get(1).displayTask();
        assertTrue("Second task not read properly when preparing from "
                + "existing log file.", expected.equals(actual));
    }

    // TODO
    @Ignore
    @Test
    public void testSearchWord() throws Exception {

    }


    public void checkObservedTasksCorrectness() {
        ArrayList<Task> databaseTasks = database.getTasks();
        ArrayList<Task> observedTasks = databaseObserverStub.getObservedTasks();
        int numberOfDatabaseTasks = databaseTasks.size();
        int numberOfObservedTasks = observedTasks.size();
        
        for (int i = 0; i < numberOfDatabaseTasks; i++) {
            Task aDatabaseTask = databaseTasks.get(i);
            Task anObservedTask = observedTasks.get(i);
            String aDatabaseTitle = aDatabaseTask.getTitle();
            String anObservedTitle = anObservedTask.getTitle();
            String aDatabaseDescription = aDatabaseTask.getDescription();
            String anObservedDescription = anObservedTask.getDescription();
            
            assertEquals("Number of tasks in observer stub is not "
                    + "the same as that stored in database.",
                    numberOfDatabaseTasks, numberOfObservedTasks);
            assertEquals("Observed task " + i
                    + "'s title doesn't match that of its"
                    + " database task counterpart.", aDatabaseTitle,
                    anObservedTitle);
            assertEquals("Observed task " + i
                    + "'s description doesn't match that of its"
                    + " database task counterpart.", aDatabaseDescription,
                    anObservedDescription);
        }
    }

    @Test
    public void testObserver() throws Exception {
        int numberOfObservers = 1;
        assertEquals(numberOfObservers, myDatabaseHandler.observerList.size());

        addTasks();
        checkObservedTasksCorrectness();
        
        int taskIdToDelete = 1;
        database.delete(taskIdToDelete);
        checkObservedTasksCorrectness();
        
        Task newTask = createTask("New Task", "New description.");
        int taskIdToEdit = 1;
        newTask.setTaskId(taskIdToEdit);
        database.edit(newTask);
        checkObservedTasksCorrectness();
    }
}