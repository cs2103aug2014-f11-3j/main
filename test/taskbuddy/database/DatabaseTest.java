package taskbuddy.database;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import org.junit.Test;

import taskbuddy.logic.Task;

/**
 * Contains unit tests for <code>Database</code> class
 * 
 * @author Soh Yong Sheng
 *
 */
public class DatabaseTest {
    
    private static final int POSITION_TITLE = 0;
    
    private static final String DELIMITER = " | ";
    private static final String TITLE = "Title: ";
    private static final String DESCRIPTION = "Description: ";
    private static final String START = "Start: ";
    private static final String END = "End: ";
    private static final String PRIORITY = "Priority: ";
    private static final String IS_COMPLETE = "Completed: ";
    private static final String IS_FLOATING = "Floating task: ";
    private static final String GOOGLE_CALENDAR_ID = "Google Calendar ID: ";

    Database database;
    Task task;

    String title;
    String description;
    String start;
    String endDate;
    String endTime;
    int priority;
    boolean isComplete;
    boolean isFloating;
    String googleCalendarId = "11111";

    public void createTask() {
        title = "Title";
        description = "Description";
        start = "";
        endDate = "31122014";
        endTime = "2359";
        priority = 1;
        isComplete = true;
        isFloating = false;

        task = new Task("Title");
        task.setCompletion(true);
        task.setDescription(description);
        task.setStartTime(start);
        task.setEndTime(endDate, endTime);
        task.setFloating(isFloating);
        task.setPriority(priority);
    }

    public void setup() throws IOException {
        database = new Database();
        createTask();
    }

    @Test
    public void testDatabase() throws Exception {
        setup();
        assertTrue("Database not constructed with arraylist of Task objects.",
                database.tasks instanceof ArrayList);
        assertTrue("Database not constructed with linkedlist of DbCommand "
                + "objects.", database.commands instanceof LinkedList);
        assertTrue("Database not constructed with log file object.",
                database.log instanceof File);
    }

    @Test
    public void testTasksToString() throws Exception {
        setup();
        database.addTask(task);
        database.addTask(task);
        assertEquals("There are no two tasks stored.", database.getTasks()
                .size(), 2);

        ArrayList<Task> tasks = database.getTasks();
        int numberOfTasks = tasks.size();
        String expected = Integer.toString(numberOfTasks) + " tasks:\n"
                + tasks.get(0).displayTask() + "\n"
                + tasks.get(1).displayTask() + "\n";
        String actual = database.tasksToString(tasks);
        assertTrue("List of tasks not converted to string properly.",
                actual.equals(expected));
    }

    /**
     * Physically check log file to see if tasks are written to the former.
     * 
     * @throws Exception
     */
    @Test
    public void testWrite() throws Exception {
        setup();
        
        // No tasks written to log file
        // database.writeToLogFile(database.getTasks());
        
        // Two tasks written to log file
        database.addTask(task);
        database.addTask(task);
        database.writeToLogFile(database.getTasks());
    }
    
    @Test
    public void testSplitToFields() throws Exception {
        setup();
        String fields[] = database.splitToFields(task.displayTask());
        
        assertTrue("Title field not extracted properly.", fields[POSITION_TITLE].equals(TITLE + title));
        
        
    }

    @Test
    public void testAddTask() throws Exception {
        setup();
        assertTrue(database.addTask(task));
        assertEquals("Number of tasks did not increase from 0 to 1 after task "
                + "addition", 1, database.getTasks().size());
        assertTrue("Task not added properly", database.getTasks().get(0)
                .equals(task));
    }

    @Test
    public void testRead() throws Exception {
        setup();
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
}
