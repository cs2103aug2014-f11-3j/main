package taskbuddy.database;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import org.junit.After;
import org.junit.Test;

import taskbuddy.logic.Task;

public class TaskLoggerTest {

    private static final String ERR_CANNOT_PARSE = "Cannot parse task";
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

    Task task;
    TaskLogger taskLogger;
    String logName;
    String[] splitFields;
    ArrayList<Task> tasks;

    int taskId;
    String title;
    String description;
    Calendar start;
    Calendar end;
    int priority;
    boolean isComplete;
    boolean isFloating;
    String googleCalendarId;

    public void createTask() {
        taskId = 0;
        title = "Title";
        description = "Description";
        start = Calendar.getInstance();
        end = Calendar.getInstance();
        priority = 1;
        isComplete = true;
        isFloating = false;
        googleCalendarId = "11111";

        task = new Task(title);
        task.setTaskId(taskId);
        task.setDescription(description);
        task.setStartTime(start);
        task.setEndTime(end);
        task.setPriority(priority);
        task.setCompletion(isComplete);
        task.setFloating(isFloating);
        task.setGID(googleCalendarId);
    }

    public void createAnotherTask() {
        taskId = 1;
        title = "Another title";
        description = "Another description";
        start = Calendar.getInstance();
        end = Calendar.getInstance();
        priority = 2;
        isComplete = false;
        isFloating = true;
        googleCalendarId = "22222";

        task = new Task(title);
        task.setTaskId(taskId);
        task.setDescription(description);
        task.setStartTime(start);
        task.setEndTime(end);
        task.setPriority(priority);
        task.setCompletion(isComplete);
        task.setFloating(isFloating);
        task.setGID(googleCalendarId);
    }

    public void setup() throws IOException {
        tasks = new ArrayList<Task>();
        taskLogger = new TaskLogger();
        logName = "log";
    }

    public void addTasks() {
        createTask();
        tasks.add(task);
        createAnotherTask();
        tasks.add(task);
    }

    /**
     * Create a dummy log file with two tasks added by <code>addTasks</code>
     * method
     * 
     * @throws IOException
     */
    public void createDummyLog() throws IOException {
        addTasks();
        taskLogger.writeToLogFile(tasks);
    }

    /**
     * Can only be used only after task is initialised
     */
    public void splitFields() {
        splitFields = taskLogger.splitToFields(task.displayTask());
    }

    /**
     * Deletes existing log file after all tests have been run
     */
    @After
    public void deleteLog() {
        File log = taskLogger.getLog();
        if (log != null && log.isFile()) {
            taskLogger.getLog().delete();
        }
    }

    @Test
    public void testPrepareLog() throws Exception {
        String expected;
        String actual;
        setup();
        File log = new File(logName);
        assertFalse("Log file created when it's not supposed to exist.",
                log.isFile());

        // Test for preparing from existing log file
        ArrayList<Task> readTasks;
        taskLogger.prepareLog(logName);
        createDummyLog();
        readTasks = taskLogger.prepareLog(logName);

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
        createTask();
        String fields[] = taskLogger.splitToFields(task.displayTask());

        assertTrue("Task ID field not extracted properly.",
                fields[POSITION_TASK_ID].equals(task.displayTaskId()));
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
    public void testExtractTaskId() throws Exception {
        setup();
        createTask();
        splitFields();
        String displayTaskId = splitFields[POSITION_TASK_ID];
        int extractedTaskId = taskLogger.extractTaskId(displayTaskId);

        assertEquals("Title not extracted properly.", extractedTaskId,
                task.getTaskId());
    }

    @Test
    public void testExtractTitle() throws Exception {
        setup();
        createTask();
        splitFields();
        String displayTitle = splitFields[POSITION_TITLE];
        String extractedTitle = taskLogger.extractTitle(displayTitle);

        assertTrue("Title not extracted properly.",
                extractedTitle.equals(task.getTitle()));
    }

    @Test
    public void testExtractDescription() throws Exception {
        setup();
        createTask();
        splitFields();
        String displayDescription = splitFields[POSITION_DESCRIPTION];
        String extractedDescription = taskLogger
                .extractDescription(displayDescription);

        assertTrue("Description not extracted properly.",
                extractedDescription.equals(task.getDescription()));
    }

    @Test
    public void testExtractStart() throws Exception {
        setup();
        createTask();
        splitFields();
        String displayStart = splitFields[POSITION_START];
        Calendar extractedStart = taskLogger.extractStart(displayStart);

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
        createTask();
        splitFields();
        String displayEnd = splitFields[POSITION_END];
        Calendar extractedEnd = taskLogger.extractEnd(displayEnd);

        // Have to convert to string, same reason as that for extracting start
        // time
        String actual = task.displayDateTime(extractedEnd);
        String expected = task.displayDateTime(task.getEndTime());
        assertTrue("End time not extracted properly.", actual.equals(expected));
    }

    @Test
    public void testExtractPriority() throws Exception {
        setup();
        createTask();
        splitFields();
        String displayPriority = splitFields[POSITION_PRIORITY];
        int extractedPriority = taskLogger.extractPriority(displayPriority);

        assertEquals("Priority not extracted properly", extractedPriority,
                task.getPriority());
    }

    @Test
    public void testExtractIsComplete() throws Exception {
        setup();
        createTask();
        splitFields();
        String displayIsComplete = splitFields[POSITION_IS_COMPLETE];
        boolean extractedIsComplete = taskLogger
                .extractIsComplete(displayIsComplete);

        assertEquals("isComplete not extracted properly", extractedIsComplete,
                task.getCompletionStatus());
    }

    @Test
    public void testExtractIsFloating() throws Exception {
        setup();
        createTask();
        splitFields();
        String displayIsFloating = splitFields[POSITION_IS_FLOATING];
        boolean extractedIsFloating = taskLogger
                .extractIsComplete(displayIsFloating);

        assertEquals("isFloating not extracted properly", extractedIsFloating,
                task.isFloatingTask());
    }

    @Test
    public void testExtractGoogleId() throws Exception {
        setup();
        createTask();
        splitFields();
        String displayGoogleId = splitFields[POSITION_GOOGLE_ID];
        String extractedGoogleId = taskLogger.extractGoogleId(displayGoogleId);

        assertEquals("Google Calendar ID not extracted properly",
                extractedGoogleId, task.getGID());
    }

    @Test
    public void testReadTask() throws Exception {
        setup();
        createTask();
        String taskString = task.displayTask();
        Task actualTask = taskLogger.readTask(taskString);

        // Use string versions of actual and expected tasks for comparison for
        // the similar reason in testExtractStart method, i.e. start/end times
        // for actual task is accurate to seconds whereas that for read task is
        // only accurate to minutes.
        String actual = actualTask.displayTask();
        String expected = task.displayTask();

        assertTrue("Task not read in properly from task string.",
                actual.equals(expected));

    }

    @Test
    public void testReadTasks() throws Exception {
        String actual;
        String expected;

        setup();

        try {
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
        // Add two different tasks and write to log file. This method also tests
        // if tasks are written correctly, since they won't be read properly if
        // they were written wrongly at first.
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
