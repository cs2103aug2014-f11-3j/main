package taskbuddy.database;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
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

    // @formatter:off
    private static final int POSITION_TITLE         = 0;
    private static final int POSITION_DESCRIPTION   = 1;
    private static final int POSITION_START         = 2;
    private static final int POSITION_END           = 3;
    private static final int POSITION_PRIORITY      = 4;
    private static final int POSITION_IS_COMPLETE   = 5;
    private static final int POSITION_IS_FLOATING   = 6;
    private static final int POSITION_GOOGLE_ID     = 7;
    // @formatter:on

    Database database;
    Task task;

    String title;
    String description;
    String startTime;
    String endDate;
    String endTime;
    int priority;
    boolean isComplete;
    boolean isFloating;
    String googleCalendarId;

    String[] splitFields;

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

        task = new Task("Title");
        task.setDescription(description);
        task.setStartTime(startTime);
        task.setEndTime(endDate, endTime);
        task.setPriority(priority);
        task.setCompletion(true);
        task.setFloating(isFloating);
        task.setGID(googleCalendarId);
    }

    public void setup() throws IOException {
        database = new Database();
        createTask();
    }

    public void splitFields() {
        splitFields = database.splitToFields(task.displayTask());
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

        assertTrue("Title field not extracted properly.",
                fields[POSITION_TITLE].equals(task.displayTitle()));
        assertTrue("Description field not extracted properly.",
                fields[POSITION_DESCRIPTION].equals(task.displayDescription()));
        assertTrue("Start field not extracted properly.",
                fields[POSITION_START].equals(task.displayStart()));
        assertTrue("End field not extracted properly.",
                fields[POSITION_END].equals(task.displayEnd()));
        assertTrue("End field not extracted properly.",
                fields[POSITION_PRIORITY].equals(task.displayPriority()));
        assertTrue("End field not extracted properly.",
                fields[POSITION_IS_COMPLETE].equals(task.displayIsComplete()));
        assertTrue("End field not extracted properly.",
                fields[POSITION_IS_FLOATING].equals(task.displayIsFloating()));
        assertTrue("End field not extracted properly.",
                fields[POSITION_GOOGLE_ID].equals(task.displayGoogleId()));
    }

    @Test
    public void testExtractTitle() throws Exception {
        setup();
        splitFields();
        String displayTitle = splitFields[POSITION_TITLE];
        String extractedTitle = database.extractTitle(displayTitle);

        assertTrue("Title not extracted properly.",
                extractedTitle.equals(task.getTitle()));
    }

    @Test
    public void testExtractDescription() throws Exception {
        setup();
        splitFields();
        String displayDescription = splitFields[POSITION_DESCRIPTION];
        String extractedDescription = database
                .extractDescription(displayDescription);

        assertTrue("Description not extracted properly.",
                extractedDescription.equals(task.getDescription()));
    }

    @Test
    public void testExtractStart() throws Exception {
        setup();
        splitFields();
        String displayStart = splitFields[POSITION_START];
        Calendar extractedStart = database.extractStart(displayStart);

        // Have to convert to string because extractedStart is only accurate to
        // minutes and will be different from task.getStartTime() that is
        // accurate to seconds
        String actual = task.displayDateTime(extractedStart);
        String expected = task.displayDateTime(task.getStartTime());
        assertTrue("Start time not extracted properly.",
                actual.equals(expected));
    }

    @Test
    public void testExtractEnd() throws Exception {
        setup();
        splitFields();
        String displayEnd = splitFields[POSITION_END];
        Calendar extractedEnd = database.extractEnd(displayEnd);

        // Have to convert to string, same reason as that for extracting start
        // time
        String actual = task.displayDateTime(extractedEnd);
        String expected = task.displayDateTime(task.getEndTime());
        assertTrue("End time not extracted properly.", actual.equals(expected));
    }

    @Test
    public void testExtractPriority() throws Exception {
        setup();
        splitFields();
        String displayPriority = splitFields[POSITION_PRIORITY];
        int extractedPriority = database.extractPriority(displayPriority);

        assertEquals("Priority not extracted properly", extractedPriority,
                task.getPriority());
    }

    @Test
    public void testExtractIsComplete() throws Exception {
        setup();
        splitFields();
        String displayIsComplete = splitFields[POSITION_IS_COMPLETE];
        boolean extractedIsComplete = database
                .extractIsComplete(displayIsComplete);

        assertEquals("isComplete not extracted properly", extractedIsComplete,
                task.getCompletionStatus());
    }

    @Test
    public void testExtractIsFloating() throws Exception {
        setup();
        splitFields();
        String displayIsFloating = splitFields[POSITION_IS_FLOATING];
        boolean extractedIsFloating = database
                .extractIsComplete(displayIsFloating);

        assertEquals("isFloating not extracted properly", extractedIsFloating,
                task.isFloatingTask());
    }

    @Test
    public void testExtractGoogleId() throws Exception {
        setup();
        splitFields();
        String displayGoogleId = splitFields[POSITION_GOOGLE_ID];
        String extractedGoogleId = database.extractGoogleId(displayGoogleId);

        assertEquals("Google Calendar ID not extracted properly",
                extractedGoogleId, task.getGID());
    }

    // TODO more test methods here for extraction methods from log file

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
