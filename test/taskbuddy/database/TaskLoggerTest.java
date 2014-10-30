package taskbuddy.database;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import taskbuddy.logic.Task;

public class TaskLoggerTest {

    private static final String ERR_CANNOT_OPEN_LOG = "Cannot open log file.";

    // @formatter:off
    private static final int POSITION_TASK_ID       = 0;
    private static final int POSITION_TITLE         = 1;
    private static final int POSITION_DESCRIPTION   = 2;
    private static final int POSITION_START         = 3;
    private static final int POSITION_END           = 4;
    private static final int POSITION_PRIORITY      = 5;
    private static final int POSITION_IS_COMPLETE   = 6;
    private static final int POSITION_IS_FLOATING   = 7;
    private static final int POSITION_GOOGLE_ID     = 8;
    // @formatter:on
    private static final String DUMMY_GOOGLE_ID = "1111";

    Task firstTask;
    Task secondTask;
    ArrayList<Task> tasks;
    String logName;
    TaskLogger taskLogger;
    String[] splitFields;

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

    public void addTasks() throws IOException, UnknownHostException {
        tasks.add(firstTask);
        tasks.add(secondTask);
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

        tasks = new ArrayList<Task>();
        taskLogger = new TaskLogger();
        logName = DatabaseHandler.LOG_NAME;
    }

    /**
     * Create a dummy log file with tasks added by <code>addTasks</code> method
     */
    public void createDummyLog() throws IOException {
        addTasks();
        taskLogger.writeToLogFile(tasks);
    }

    /**
     * Can only be used only after task is initialised
     * 
     * @param task
     *            TODO
     */
    public void splitFields(Task task) {
        splitFields = taskLogger.splitToFields(task.displayTask());
    }

    @Test
    public void testPrepareLog() throws Exception {
        setup();
        File log = new File(logName);
        assertFalse("Log file created when it's not supposed to exist.",
                log.isFile());

        // Create empty log file since log file hasn't existed.
        taskLogger.prepareLog(logName);
        createDummyLog();
        // prepareLog method should read tasks in from existing log file
        ArrayList<Task> readTasks = taskLogger.prepareLog(logName);

        // Test if tasks read in from task log match those of temporary memory,
        // i.e. ArrayList tasks.
        assertEquals("No tasks read in from log file", 2, readTasks.size());
        expected = readTasks.get(0).displayTask();
        actual = tasks.get(0).displayTask();
        assertTrue("First task not read properly when preparing from "
                + "existing log file.", expected.equals(actual));
        expected = readTasks.get(1).displayTask();
        actual = tasks.get(1).displayTask();
        assertTrue("Second task not read properly when preparing from "
                + "existing log file.", expected.equals(actual));

        deleteLog();

        // Test for non-existing log file
        taskLogger.prepareLog(logName);
        assertTrue("Log file object not initialised with prepareLog method.",
                taskLogger.log instanceof File);
        assertTrue("Log file doesn't exist even when it's supposed to have "
                + "been created.", taskLogger.getLog().exists());
    }

    @Test
    public void testTasksToString() throws Exception {
        setup();
        addTasks();
        assertEquals("There are no two tasks stored.", this.tasks.size(), 2);

        int numberOfTasks = tasks.size();
        String expected = Integer.toString(numberOfTasks) + " tasks:\n"
                + tasks.get(0).displayTask() + "\n"
                + tasks.get(1).displayTask() + "\n";
        String actual = taskLogger.tasksToString(tasks);
        assertTrue("List of tasks not converted to string properly.",
                actual.equals(expected));
    }

    @Test
    public void testSplitToFields() throws Exception {
        setup();
        splitFields(firstTask);

        assertTrue("Task ID field not extracted properly.",
                splitFields[POSITION_TASK_ID].equals(firstTask.displayTaskId()));
        assertTrue("Title field not extracted properly.",
                splitFields[POSITION_TITLE].equals(firstTask.displayTitle()));
        assertTrue("Description field not extracted properly.",
                splitFields[POSITION_DESCRIPTION].equals(firstTask
                        .displayDescription()));
        assertTrue("Start field not extracted properly.",
                splitFields[POSITION_START].equals(firstTask.displayStart()));
        assertTrue("End field not extracted properly.",
                splitFields[POSITION_END].equals(firstTask.displayEnd()));
        assertTrue("End field not extracted properly.",
                splitFields[POSITION_PRIORITY].equals(firstTask
                        .displayPriority()));
        assertTrue("End field not extracted properly.",
                splitFields[POSITION_IS_COMPLETE].equals(firstTask
                        .displayIsComplete()));
        assertTrue("End field not extracted properly.",
                splitFields[POSITION_IS_FLOATING].equals(firstTask
                        .displayIsFloating()));
        assertTrue("End field not extracted properly.",
                splitFields[POSITION_GOOGLE_ID].equals(firstTask
                        .displayGoogleId()));
    }

    @Test
    public void testExtractTaskId() throws Exception {
        setup();
        splitFields(firstTask);
        String displayTaskId = splitFields[POSITION_TASK_ID];
        int extractedTaskId = taskLogger.extractTaskId(displayTaskId);

        assertEquals("Title not extracted properly.", extractedTaskId,
                firstTask.getTaskId());
    }

    @Test
    public void testExtractTitle() throws Exception {
        setup();
        splitFields(firstTask);
        String displayTitle = splitFields[POSITION_TITLE];
        String extractedTitle = taskLogger.extractTitle(displayTitle);

        assertTrue("Title not extracted properly.",
                extractedTitle.equals(firstTask.getTitle()));
    }

    @Test
    public void testExtractDescription() throws Exception {
        setup();
        splitFields(firstTask);
        String displayDescription = splitFields[POSITION_DESCRIPTION];
        String extractedDescription = taskLogger
                .extractDescription(displayDescription);

        assertTrue("Description not extracted properly.",
                extractedDescription.equals(firstTask.getDescription()));
    }

    @Test
    public void testExtractStart() throws Exception {
        setup();
        splitFields(firstTask);
        String displayStart = splitFields[POSITION_START];
        Calendar extractedStart = taskLogger.extractStart(displayStart);

        // Have to convert to string because extractedStart is only accurate to
        // minutes and will be different from task.getStartTime() that is
        // accurate to seconds
        String actual = firstTask.displayDateTime(extractedStart);
        String expected = firstTask.displayDateTime(firstTask.getStartTime());
        assertTrue("Start time not extracted properly.",
                actual.equals(expected));
    }

    @Test
    public void testExtractEnd() throws Exception {
        setup();
        splitFields(firstTask);
        String displayEnd = splitFields[POSITION_END];
        Calendar extractedEnd = taskLogger.extractEnd(displayEnd);

        // Have to convert to string, same reason as that for extracting start
        // time
        String actual = firstTask.displayDateTime(extractedEnd);
        String expected = firstTask.displayDateTime(firstTask.getEndTime());
        assertTrue("End time not extracted properly.", actual.equals(expected));
    }

    @Test
    public void testExtractPriority() throws Exception {
        setup();
        splitFields(firstTask);
        String displayPriority = splitFields[POSITION_PRIORITY];
        int extractedPriority = taskLogger.extractPriority(displayPriority);

        assertEquals("Priority not extracted properly", extractedPriority,
                firstTask.getPriority());
    }

    @Test
    public void testExtractIsComplete() throws Exception {
        setup();
        splitFields(firstTask);
        String displayIsComplete = splitFields[POSITION_IS_COMPLETE];
        boolean extractedIsComplete = taskLogger
                .extractIsComplete(displayIsComplete);

        assertEquals("isComplete not extracted properly", extractedIsComplete,
                firstTask.getCompletionStatus());
    }

    @Ignore
    @Test
    public void testExtractIsFloating() throws Exception {
        setup();
        splitFields(firstTask);
        String displayIsFloating = splitFields[POSITION_IS_FLOATING];
        boolean extractedIsFloating = taskLogger
                .extractIsComplete(displayIsFloating);

        assertEquals("isFloating not extracted properly", extractedIsFloating,
                firstTask.isFloatingTask());
    }

    @Test
    public void testExtractGoogleId() throws Exception {
        setup();
        firstTask.setGID(DUMMY_GOOGLE_ID);
        splitFields(firstTask);
        String displayGoogleId = splitFields[POSITION_GOOGLE_ID];
        String extractedGoogleId = taskLogger.extractGoogleId(displayGoogleId);

        assertEquals("Google Calendar ID not extracted properly",
                extractedGoogleId, firstTask.getGID());
    }

    @Test
    public void testReadTask() throws Exception {
        setup();
        String taskString = firstTask.displayTask();
        Task actualTask = taskLogger.readTask(taskString);

        // Use string versions of actual and expected tasks for comparison for
        // the similar reason in testExtractStart method, i.e. start/end times
        // for actual task is accurate to seconds whereas that for read task is
        // only accurate to minutes.
        actual = actualTask.displayTask();
        expected = firstTask.displayTask();

        assertTrue("Task not read in properly from task string.",
                actual.equals(expected));
    }

    // TODO 
    @Test
    public void testReadTasks() throws Exception {
        setup();
        try {
            // prepareLog initialises log variable in taskLogger
            taskLogger.prepareLog(logName);
            // Delete log file intentionally to force reading of non-existent
            // log file to test for IOException
            deleteLog();

            taskLogger.readTasks();
            fail("Exception not thrown when trying to read"
                    + " non-existent log file");
        } catch (IOException e) {
            assertTrue("Wrong IOException message when non-existent"
                    + " log file cannot be read.",
                    e.getMessage().equals(ERR_CANNOT_OPEN_LOG));
        }

        taskLogger.prepareLog(logName);
        createDummyLog();

        ArrayList<Task> readTasks = taskLogger.readTasks();
        // Again use strings for comparison; same reason as that of testReadTask
        // method
        Task firstTask = readTasks.get(0);
        actual = firstTask.displayTask();
        expected = tasks.get(0).displayTask();
        assertTrue("First task read from log file not the same "
                + "as actual first task.", actual.equals(expected));
        Task secondTask = readTasks.get(1);
        actual = secondTask.displayTask();
        expected = tasks.get(1).displayTask();
        assertTrue("Second task read from log file not the same "
                + "as actual second task.", actual.equals(expected));
    }

}
