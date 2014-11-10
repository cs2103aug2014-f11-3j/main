//@author A0098745L

package taskbuddy.database;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import taskbuddy.database.Database;
import taskbuddy.database.DatabaseHandler;
import taskbuddy.database.GoogleCalendarManagerStub;
import taskbuddy.logic.Task;

/**
 * Contains unit tests for <code>Database</code> class
 * 
 */
public class DatabaseMethodsTest {
    Task firstTask;
    Task secondTask;
    String firstGoogleId;
    String secondGoogleId;

    Database database;
    String taskLogName = DatabaseHandler.TASK_LOG_NAME;
    GoogleCalendarManagerStub googleCalendarManagerStub;
    DatabaseHandler myDatabaseHandler;

    String expected;
    String actual;

    DatabaseObserverStub databaseObserverStub;

    /**
     * Deletes existing log file before running tests
     */
    public void deleteLog() {
        File taskLog = new File(DatabaseHandler.TASK_LOG_NAME);
        if (taskLog.isFile()) {
            taskLog.delete();
        }
        File commandLog = new File(DatabaseHandler.COMMAND_LOG_NAME);
        if (commandLog.isFile()) {
            commandLog.delete();
        }
    }

    // These added tasks do not have set Google Calendar IDs.
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

    public void checkTaskLogCorrectness() throws IOException, ParseException {
        ArrayList<Task> readTasks = myDatabaseHandler.taskLogger.readTasks();
        assertEquals(
                "Number of tasks in task log is not the same as that in database.",
                readTasks.size(), database.getTasks().size());
        for (int i = 0; i < readTasks.size(); i++) {
            actual = readTasks.get(i).displayTask();
            expected = database.getTasks().get(i).displayTask();
            assertTrue("Task " + i + " not correctly logged.",
                    actual.equals(expected));
        }
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
        } catch (IllegalAccessException e) {
            assertTrue("Empty list exception not thrown.", e.getMessage()
                    .equals(DatabaseHandler.ERR_NO_TASKS));
        }
        try {
            database.read("1111");
            fail("Exception not thrown when trying to "
                    + "read from empty list of tasks.");
        } catch (IllegalAccessException e) {
            assertTrue("Empty list exception not thrown.", e.getMessage()
                    .equals(DatabaseHandler.ERR_NO_TASKS));
        }

        addTasks();
        setGoogleIds();
        assertTrue("First task not read correctly from given task ID.",
                database.read(1).equals(firstTask));
        assertTrue(
                "First task not read correctly from given Google Calendar ID.",
                database.read(firstGoogleId).equals(firstTask));
        assertTrue("Second task not read correctly from given task ID.",
                database.read(2).equals(secondTask));
        assertTrue(
                "Second task not read correctly from given Google Calendar ID.",
                database.read(secondGoogleId).equals(secondTask));

        try {
            database.read(3);
            fail("No exception thrown for invalid task ID.");
        } catch (NoSuchElementException e) {
            assertTrue("No such task ID exception not thrown.", e.getMessage()
                    .equals(DatabaseHandler.ERR_NO_SUCH_TASK_ID));
        }
        try {
            database.read("3333");
        } catch (NoSuchElementException e) {
            assertEquals("No such task ID exception not thrown.",
                    e.getMessage(), DatabaseHandler.ERR_NO_SUCH_GOOGLE_ID);
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
                    .equals(DatabaseHandler.ERR_NO_TASKS));
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

        checkTaskLogCorrectness();

        // Test for invalid task ID
        try {
            database.delete(3);
            fail("Should have thrown no such task ID exception.");
        } catch (Exception e) {
            assertTrue("No such task ID exception not thrown.", e.getMessage()
                    .equals(DatabaseHandler.ERR_NO_SUCH_TASK_ID));
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

    public void setGoogleIds() {
        firstGoogleId = "1111";
        firstTask.setGID(firstGoogleId);

        secondGoogleId = "2222";
        secondTask.setGID(secondGoogleId);

    }

    @Test
    public void testAddBackwardSync() throws Exception {
        setGoogleIds();
        database.addBackwardSync(firstTask);

        assertEquals("Number of tasks did not increase from 0 to 1 after task "
                + "addition", 1, database.getTasks().size());
        assertTrue("Task not added properly", database.getTasks().get(0)
                .equals(firstTask));
        checkTaskLogCorrectness();
        checkObservedTasksCorrectness();
    }

    @Test
    public void testDeleteBackwardSync() throws Exception {
        try {
            database.deleteBackwardSync("1111");
            fail("Exception not thrown when trying to "
                    + "read from empty list of tasks.");
        } catch (IllegalAccessException e) {
            assertTrue("Empty list exception not thrown.", e.getMessage()
                    .equals(DatabaseHandler.ERR_NO_TASKS));
        }

        addTasks();
        setGoogleIds();
        assertEquals("Number of tasks in temporary memory is not two.",
                database.getTasks().size(), 2);
        assertEquals("Google Calendar ID for first task not set.",
                firstGoogleId, database.getTasks().get(0).getGID());
        assertEquals("Google Calendar ID for first task not set.",
                secondGoogleId, database.getTasks().get(1).getGID());

        database.deleteBackwardSync(firstGoogleId);

        assertEquals("Number of tasks did not decrease to one.", database
                .getTasks().size(), 1);
        assertTrue("Remaining task after deletion is not correct.", database
                .getTasks().get(0).equals(secondTask));
        checkTaskLogCorrectness();
        checkObservedTasksCorrectness();

        try {
            database.deleteBackwardSync("3333");
        } catch (NoSuchElementException e) {
            assertEquals("No such task ID exception not thrown.",
                    e.getMessage(), DatabaseHandler.ERR_NO_SUCH_GOOGLE_ID);
        }
    }

    @Test
    public void testEditBackwardSync() throws Exception {
        addTasks();
        setGoogleIds();
        assertEquals("Number of tasks is not two", 2, database.getTasks()
                .size());
        Task newTask = createTask("New", "New description.");
        newTask.setGID("3333");

        database.editBackwardSync("1111", newTask);
        assertEquals("First task is not replaced properly by new task",
                newTask, database.getTasks().get(0));
        assertEquals("Number of tasks is not two", 2, database.getTasks()
                .size());
        assertEquals("Second task is wrongly edited.", secondTask, database
                .getTasks().get(1));
        checkTaskLogCorrectness();
        checkObservedTasksCorrectness();
    }

}