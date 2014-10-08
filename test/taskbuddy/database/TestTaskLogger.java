package taskbuddy.database;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import org.junit.Test;

import taskbuddy.logic.Task;

public class TestTaskLogger {

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

    Task task;
    TaskLogger taskLogger;
    String logName;
    String[] splitFields;
    ArrayList<Task> tasks;

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
        createTask();
        tasks = new ArrayList<Task>();
        taskLogger = new TaskLogger();
        logName = "log";
        taskLogger.prepareLog(logName);
    }

    public void splitFields() {
        splitFields = taskLogger.splitToFields(task.displayTask());
    }

    /**
     * Physically check if log file is created; may have to delete existing log
     * file to test if new log file is created.
     * 
     * @throws IOException
     */
    @Test
    public void testPrepareLog() throws IOException {
        setup();
        assertTrue("Log file object not initialised with prepareLog method.",
                taskLogger.log instanceof File);
    }

    @Test
    public void testTasksToString() throws Exception {
        setup();
        tasks.add(task);
        tasks.add(task);
        assertEquals("There are no two tasks stored.", this.tasks.size(), 2);

        int numberOfTasks = tasks.size();
        String expected = Integer.toString(numberOfTasks) + " tasks:\n"
                + tasks.get(0).displayTask() + "\n"
                + tasks.get(1).displayTask() + "\n";
        String actual = taskLogger.tasksToString(tasks);
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
        // taskLogger.writeToLogFile(tasks);

        // Two tasks written to log file
        tasks.add(task);
        tasks.add(task);
        taskLogger.writeToLogFile(tasks);
    }

    @Test
    public void testSplitToFields() throws Exception {
        setup();
        String fields[] = taskLogger.splitToFields(task.displayTask());

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
        String extractedTitle = taskLogger.extractTitle(displayTitle);

        assertTrue("Title not extracted properly.",
                extractedTitle.equals(task.getTitle()));
    }

    @Test
    public void testExtractDescription() throws Exception {
        setup();
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
        splitFields();
        String displayPriority = splitFields[POSITION_PRIORITY];
        int extractedPriority = taskLogger.extractPriority(displayPriority);

        assertEquals("Priority not extracted properly", extractedPriority,
                task.getPriority());
    }

    @Test
    public void testExtractIsComplete() throws Exception {
        setup();
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
        splitFields();
        String displayGoogleId = splitFields[POSITION_GOOGLE_ID];
        String extractedGoogleId = taskLogger.extractGoogleId(displayGoogleId);

        assertEquals("Google Calendar ID not extracted properly",
                extractedGoogleId, task.getGID());
    }

}
